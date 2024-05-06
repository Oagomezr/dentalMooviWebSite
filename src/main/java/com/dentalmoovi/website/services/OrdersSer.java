package com.dentalmoovi.website.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.cart.CartResponse;
import com.dentalmoovi.website.models.cart.OrderFormat;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Orders;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class OrdersSer {
    private final OrdersRep ordersRep;
    private final UserRep userRep;
    private final ProductsSer productsSer;
    private final AddressesRep addressesRep;
    private final SpringTemplateEngine ste;
    private final DepartamentsRep departamentsRep;
    private final MunicipalyRep municipalyRep;

    public OrdersSer(OrdersRep ordersRep, UserRep userRep, ProductsSer productsSer, AddressesRep addressesRep,
            SpringTemplateEngine ste, DepartamentsRep departamentsRep, MunicipalyRep municipalyRep) {
        this.ordersRep = ordersRep;
        this.userRep = userRep;
        this.productsSer = productsSer;
        this.addressesRep = addressesRep;
        this.ste = ste;
        this.departamentsRep = departamentsRep;
        this.municipalyRep = municipalyRep;
    }

    private boolean admin = false;

    Orders order;
    Users user;

    public void downloadOrder(CartRequest req, long idAddress, boolean admin, HttpServletResponse response){
        this.admin = admin;
        try{
            File pdf = generateOrder(req, idAddress);
            Path file = Paths.get(pdf.getAbsolutePath());
            if (Files.exists(file)){
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename="+ file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
                Files.delete(pdf.toPath());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public File generateOrder(CartRequest req, long idAddress) throws Exception{
        File pdfFile = generatePdf(req, idAddress);
        byte[] pdfContent = Files.readAllBytes(pdfFile.toPath());
        order = ordersRep.save(
            new Orders(
                order.id(), pdfContent, order.status(), order.idUser(), 
                order.idAddress(), order.products()));

        return pdfFile;
    }

    private File generatePdf(CartRequest req, long idAddress) throws Exception{
        Context context = getOrderContext(req, idAddress);
        String html = loadAndFillTemplate(context);
        return renderPdf(html);
    }

    private Context getOrderContext(CartRequest req, long idAddress) throws Exception{

        user = admin ? getUser(req.idUser()) : getUser();
        
        order = Utils.setOrder(StatusOrderList.PENDING, user.id(), idAddress, req, ordersRep);

        Addresses address = addressesRep.findById(idAddress)
            .orElseThrow(() -> new RuntimeException("Address not found"));

        MunicipalyCity municipaly = municipalyRep.findById(address.idMunicipalyCity())
                .orElseThrow(() -> new RuntimeException("Municipaly not found"));

        Departaments departament = departamentsRep.findById(municipaly.id_departament())
            .orElseThrow(() -> new RuntimeException("Departament not found"));

        CartResponse cartResponse = productsSer.getShoppingCartProducts(req, admin, true);
        
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String hour = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

        OrderFormat orderData = 
            new OrderFormat(
                order.id(), user.firstName(), user.lastName(), address.phone(), date, hour, departament.name(), 
                municipaly.name(), address.address(), null, cartResponse.data(), 
                String.format("%,.2f", cartResponse.total()));

        Context context = new Context();
        context.setVariable("order", orderData);
        return context;
    }

    private Users getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String userName = userDetails.getUsername();
        return Utils.getUserByEmail(userName, userRep);
    }

    private Users getUser(long id){
        return userRep.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String loadAndFillTemplate(Context context) {
        return ste.process("order", context);
    }

    private File renderPdf(String html) throws Exception {
        File file = File.createTempFile("order", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }
}
