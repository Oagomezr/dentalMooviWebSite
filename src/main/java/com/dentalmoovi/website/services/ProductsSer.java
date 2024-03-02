package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.awt.image.BufferedImage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dentalmoovi.website.models.cart.CartDtoRequest;
import com.dentalmoovi.website.models.cart.CartDtoRespose;
import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.cart.CartResponse;
import com.dentalmoovi.website.models.dtos.ImagesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ImgRep;
import com.dentalmoovi.website.repositories.ProductsRep;

@Service
public class ProductsSer {
    
    private final ProductsRep productsRep;
    private final CategoriesRep categoriesRep;
    private final ImgRep imagesRep;

    public ProductsSer(ProductsRep productsRep, CategoriesRep categoriesRep, ImgRep imagesRep) {
        this.productsRep = productsRep;
        this.categoriesRep = categoriesRep;
        this.imagesRep = imagesRep;
    }

    private String categoryNotFound = "Category not found";
    private String productNotFound = "Product not found";

    @Cacheable(cacheNames = "productsByCategory")
    public ProductsResponse getProductsByCategory(String parentCategoryName, int currentPage, int productsPerPage, boolean all){
        class GetProductsByCategory{
            ProductsResponse getProductsByCategory() {

                //Search category inside database
                Categories parentCategory = categoriesRep.findByName(parentCategoryName)
                        .orElseThrow(() -> new RuntimeException(categoryNotFound));
            
                //prepare the list of products
                List<Products> allProducts = new ArrayList<>();

                //We need to have all category's products, so we need to have all category's subcategories
                List<String> listAllSubCategories = getNamesCategories(parentCategory);
                listAllSubCategories.add(parentCategoryName);
            
                /*We have products in each subcategory, 
                so we loop all subcategories and recovery the products in a list of products*/
                listAllSubCategories.stream().forEach(categoryName ->
                    allProducts.addAll(productsRep.findByCategoryName(categoryName))
                );
                
                //We have all products but unorganized, we organize them alphabetical
                Collections.sort(allProducts, (product1, product2) -> product1.name().compareTo(product2.name()));

                /*We cannot show the costumer N amount of products if N is a high number,
                so we need to do pagination*/
                int startIndex = (currentPage - 1) * productsPerPage;
                int endIndex = Math.min(startIndex + productsPerPage, allProducts.size());
                List<Products> currentPageProducts = allProducts.subList(startIndex, endIndex);
                
                //Classic DTO
                List<ProductsDTO> productsDTO = convertToProductsDTOList(currentPageProducts, all);

                /*One of the best practices in programming is not send List or Arrays as response,
                instead Objects as response*/
                return new ProductsResponse(allProducts.size(), productsDTO.size(), productsDTO);
            }

            //Get all subcategories
            private List<String> getNamesCategories(Categories parentCategory) {
                List<Categories> subCategories = categoriesRep.findByParentCategory(parentCategory.id());
                List<String> subcategoriesNames = new ArrayList<>();
                subCategories.stream().forEach(subCategory ->{
                    subcategoriesNames.add(subCategory.name());
                    subcategoriesNames.addAll(getNamesCategories(subCategory));
                });

                return subcategoriesNames;
            }
        }
        GetProductsByCategory innerClass = new GetProductsByCategory();
        return innerClass.getProductsByCategory();
    }

    @Cacheable(cacheNames = "getProduct")
    public ProductsDTO getProduct(String name, boolean admin){

        class GetProduct{
            ProductsDTO getProduct(){

                //Search product
                Products product = productsRep.findByName(name)
                    .orElseThrow(() -> new RuntimeException(productNotFound));

                if (!admin && !product.openToPublic())
                    throw new RuntimeException("Something wrong");
                
                //Get product's category
                @SuppressWarnings("null")
                Categories category = categoriesRep.findById(product.idCategory())
                    .orElseThrow(() -> new RuntimeException(categoryNotFound));

                //Get location producto since parent category until its subcategory
                List<String> location = getLocationProduct(category);

                //Get Product images
                List<ImagesDTO> productImagesDTO = getProductImages(product, true);

                String hidden = null;

                if (admin && !product.openToPublic()) hidden = "yes";

                return new ProductsDTO(product.id() , name, product.unitPrice(), product.description(), 
                    product.shortDescription(), product.stock(), productImagesDTO, location, hidden);
            }

            //It's a function with the aim of find the location products inside the categories
            @SuppressWarnings("null")
            private List<String> getLocationProduct(Categories category){
                List<String> location = new ArrayList<>(List.of(category.name()));
                if(category.idParentCategory() != null) 
                    location.addAll(getLocationProduct(
                        categoriesRep.findById(category.idParentCategory())
                        .orElseThrow(() -> new RuntimeException(categoryNotFound))
                    )
                );
                return location;
            }
        }

        GetProduct innerClass = new GetProduct();
        return innerClass.getProduct();
    }

    @Cacheable(cacheNames = "getProducsByContaining")
    public ProductsResponse getProductsByContaining(String name, boolean limit, int currentPage, int productsPerPage, boolean all){
        List<Products> productsFound;

        //if the user wants to obtain only the 7 first query results or all results
        if(limit){

            //Get the first 7 query results from database
            productsFound = productsRep.findByNameContaining(name,7,0);

            //Convert those results in DTOs
            List<ProductsDTO> productsDTO = convertToProductsDTOList(productsFound, all);

            //Put the DTOs inside the Object
            return new ProductsResponse(productsDTO.size(), productsDTO.size(), productsDTO);
        }

        //Get all query results
        List<Products> allProductsFound = productsRep.findByNameContaining(name, productsPerPage, ((currentPage-1)*productsPerPage));

        //Convert those results in DTOs
        List<ProductsDTO> productsDTO = convertToProductsDTOList(allProductsFound, all);

        //Put the DTOs inside the Object
        return new ProductsResponse(productsRep.countProductsByContaining(name), productsDTO.size(), productsDTO);
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public MessageDTO updateMainImage(long idImage, String productName){
        Products product = productsRep.findByName(productName)
            .orElseThrow(() -> new RuntimeException(productNotFound));
        productsRep.save(new Products(product.id(), product.name(), product.description(), product.shortDescription(), product.unitPrice(), product.stock(), product.openToPublic(), idImage, product.idCategory()));
        return new MessageDTO("Main product image updated");
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public MessageDTO uploadImage(MultipartFile file, String nameProduct) throws IOException{
        class UploadImage{
            MessageDTO uploadImage() throws IOException{
        
                // Find product
                Products product = productsRep.findByName(nameProduct)
                        .orElseThrow(() -> new RuntimeException(productNotFound));
                
                // Read original image
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                
                // Set max image sizes
                int maxWidth = 600;
                int maxHeight = 600;
                
                // Variables to new Width and Height of the image
                int newWidth;
                int newHeight;
                
                // Create new image if resizing is not required
                if (originalImage.getWidth() < maxWidth && originalImage.getHeight() < maxHeight)
                    return createImage(file, product, null);
                
                // Calculate new image size
                if (originalImage.getWidth() > originalImage.getHeight()) {
                    newWidth = maxWidth;
                    newHeight = (originalImage.getHeight() * maxWidth) / originalImage.getWidth();
                } else {
                    newHeight = maxHeight;
                    newWidth = (originalImage.getWidth() * maxHeight) / originalImage.getHeight();
                }
                
                byte[] resizedImageData = rescaleAndConvert(originalImage, newWidth, newHeight);
        
                // Create and save the new image
                return createImage(file, product, resizedImageData);
            }

            private MessageDTO createImage(MultipartFile file, Products product, byte[] resizedImageData) throws IOException {
                
                String originalFileName = file.getOriginalFilename();
                String contentType = file.getContentType();

                if(originalFileName == null || contentType == null) throw new IOException("Empty file");
                
                contentType = contentType.replace("image/", "");
                originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                byte[] imageData = resizedImageData != null ? resizedImageData : file.getBytes();

                Images newImage = imagesRep.save(new Images(null, originalFileName, contentType, imageData, product.id()));
        
                // Set the new image as the main image if there is no main image assigned
                if (product.idMainImage() == null) {
                    productsRep.save(
                        new Products(
                            product.id(), product.name(), product.description(), product.shortDescription(), 
                            product.unitPrice(), product.stock(), product.openToPublic(), newImage.id(), product.idCategory()));
                }
                return new MessageDTO("Image created");
            }

            private byte[] rescaleAndConvert(BufferedImage originalImage, int newWidth, int newHeight) throws IOException{
                // Rescale the image to the new size
                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = resizedImage.createGraphics();
                graphics2D.drawImage(scaledImage, 0, 0, null);
                graphics2D.dispose();
        
                // Convert resized image to bytes
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", outputStream);
                byte[] resizedImageData = outputStream.toByteArray();
                outputStream.close();
                return resizedImageData;
            }
        }
        UploadImage innerClass = new UploadImage();
        return innerClass.uploadImage();
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public MessageDTO deleteImage(String parameter){
        if (parameter.matches(".*[a-zA-Z].*")) {
            Products product = productsRep.findByName(parameter)
                .orElseThrow(() -> new RuntimeException(productNotFound));
            long idImage = product.idMainImage();
            productsRep.save(
                new Products(
                    product.id(), product.name(), product.description(), product.shortDescription(), product.unitPrice(), 
                    product.stock(), product.openToPublic(), null, product.idCategory()));
            imagesRep.deleteById(idImage);
        }else{
            long idImage = Long.parseLong(parameter);
            imagesRep.deleteById(idImage);
        }

        return new MessageDTO("Image deleted");
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public MessageDTO hideOrShowProduct(boolean visibility, String productName){
        Products product = productsRep.findByName(productName)
            .orElseThrow(() -> new RuntimeException(productNotFound));
        productsRep.save(new Products(
            product.id(), product.name(), product.description(), product.shortDescription(), product.unitPrice(), 
            product.stock(), visibility, product.idMainImage(), product.idCategory()));
        return new MessageDTO("Product Updated");
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public MessageDTO updateProductInfo(int option, String nameProduct, String newInfo){
        // Find product
        Products product = productsRep.findByName(nameProduct)
            .orElseThrow(() -> new RuntimeException(productNotFound));

        
        switch (option) {
            case 0:
                productsRep.save(new Products(
                    product.id(), newInfo, product.description(), product.shortDescription(), product.unitPrice(), 
                    product.stock(), product.openToPublic(), product.idMainImage(), product.idCategory()));
            break;
            case 1:
                productsRep.save(new Products(
                    product.id(), product.name(), product.description(), product.shortDescription(), Double.parseDouble(newInfo), 
                    product.stock(), product.openToPublic(), product.idMainImage(), product.idCategory()));
            break;
            case 2:
                productsRep.save(new Products(
                    product.id(), product.name(), newInfo, product.shortDescription(), product.unitPrice(), 
                    product.stock(), product.openToPublic(), product.idMainImage(), product.idCategory()));
            break;
            case 3:
                productsRep.save(new Products(
                    product.id(), product.name(), product.description(), product.shortDescription(), product.unitPrice(), 
                    Integer.parseInt(newInfo), product.openToPublic(), product.idMainImage(), product.idCategory()));
            break;
            case 4:
                productsRep.save(new Products(
                    product.id(), product.name(), product.description(), newInfo, product.unitPrice(), 
                    product.stock(), product.openToPublic(), product.idMainImage(), product.idCategory()));
            break;
        
            default:
                throw new RuntimeException("Invalid option");
        }
        return new MessageDTO("Info updated");
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public Boolean createProduct(String categoryName){

        Categories category = categoriesRep.findByName(categoryName)
            .orElseThrow(() -> new RuntimeException(productNotFound));

        if (Boolean.TRUE.equals(productsRep.existsByName("Nombre del nuevo producto"))) {
            Products product = productsRep.findByName("Nombre del nuevo producto")
                .orElseThrow(() -> new RuntimeException(productNotFound));
            productsRep.save(new Products(
                    product.id(), product.name(), product.description(), product.shortDescription(), product.unitPrice(), 
                    product.stock(), product.openToPublic(), product.idMainImage(), category.id()));
            return false;
        }

        productsRep.save(new Products(
            null, "Nombre del nuevo producto", "Descripción del nuevo producto", "descripción corta del nuevo producto", 0, 
            0, false, null, category.id()));
            
        return true;
    }

    public CartResponse getShoppingCartProducts(CartRequest req, boolean admin, boolean pdf) throws Exception{

        List<CartDtoRespose> data = new ArrayList<>();

        double total = 0;
        int amountOfProducts = 0;
        for (CartDtoRequest elem : req.data()) {
            Products product = productsRep.findById(elem.id())
                .orElseThrow(() -> new RuntimeException(productNotFound));

            
            
            if (admin) product = new Products(
                product.id(), product.name(), product.description(), product.shortDescription(), product.unitPrice(), 
                product.stock(), product.openToPublic(), product.idMainImage(), product.idCategory());

            if (!product.openToPublic() && !admin)
                throw new RuntimeException("That product does not exist");
                
            

            ImagesDTO cartImage = null;
            String cartPrizePDF = null;
            String cartSubtotalPDF = null;
            double cartPrize = 0;
            double cartSubtotal = 0;
            
            if (product.idMainImage() != null) {
                @SuppressWarnings("null")
                Images mainImage = imagesRep.findById(product.idMainImage())
                    .orElseThrow(() -> new RuntimeException("Image not found"));
                cartImage = setImageDTO(mainImage);
            }

            if (pdf) {
                cartPrizePDF = String.format("%,.2f", product.unitPrice());
                cartSubtotalPDF = String.format("%,.2f", product.unitPrice()*elem.amount());
            }else{
                cartPrize = product.unitPrice();
                cartSubtotal = product.unitPrice()*elem.amount();
            }

            CartDtoRespose cart = 
                new CartDtoRespose(
                    elem.id(), cartImage, product.name(), cartPrize, elem.amount(), cartSubtotal, 
                    cartPrizePDF, cartSubtotalPDF);
            
            total += product.unitPrice()*elem.amount();
            amountOfProducts += elem.amount();
            data.add(cart);
        }

        return new CartResponse(data, total, amountOfProducts);
    }

    //This only converts our database data to DTOs
    private List<ProductsDTO> convertToProductsDTOList(List<Products> productsList, boolean all) {

        List<ProductsDTO> productsDTOList = new ArrayList<>();
        
        productsList.stream().forEach(product ->{
            try {
                if(product.openToPublic() || all){
                    List<ImagesDTO> productImagesDTO = getProductImages(product, false);
                    String hidden = null;
                    
                    if (!product.openToPublic()) hidden = "Yes";
                    ProductsDTO productDTO = new ProductsDTO(product.id() , product.name(), product.unitPrice(), 
                        product.description(), product.shortDescription(), product.stock(), productImagesDTO, null, hidden);
                    productsDTOList.add(productDTO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    
        return productsDTOList;
    }

    //This function allow us to find one or all product images
    private List<ImagesDTO> getProductImages(Products product, boolean allImages){
        List<ImagesDTO> productImagesDTO = new ArrayList<>();
        if(product.idMainImage() == null) return productImagesDTO;

        @SuppressWarnings("null")
        Images mainImage = imagesRep.findById(product.idMainImage())
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if(allImages){
            List<Images> productImages = imagesRep.findByIdProduct(product.id());
            
            productImages.stream().forEach(productImage ->{
                long idImage = productImage.id();
                String imgName = productImage.name();
                String contentType = productImage.contentType();
                String base64Image = Base64.getEncoder().encodeToString(productImage.data());
                ImagesDTO imageDTO = new ImagesDTO(idImage ,imgName, contentType, base64Image);

                if (mainImage.equals(productImage)) productImagesDTO.add(0, imageDTO);
                else productImagesDTO.add(imageDTO);
            });
            return productImagesDTO;
        }

        ImagesDTO imageDTO = setImageDTO(mainImage);
        
        productImagesDTO.add(imageDTO);
        return productImagesDTO;
    }

    private ImagesDTO setImageDTO(Images image){

        long idMainImage = image.id();
        String imgName = image.name();
        String contentType = image.contentType();
        String base64Image = Base64.getEncoder().encodeToString(image.data());

        return new ImagesDTO(idMainImage , imgName, contentType, base64Image);
    }
}
