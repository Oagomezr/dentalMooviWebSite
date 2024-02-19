package com.dentalmoovi.website;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ImgRep;
import com.dentalmoovi.website.repositories.ProductsRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Profile("test")
public class InitialTestData {
    private final CategoriesRep categoriesRep;
    private final ProductsRep productsRep;
    private final ImgRep imagesRep;
    private final RolesRep rolesRep;
    private final UserRep userRep;

    @Value("${spring.mail.otherPassword}")
    private String password;

    @Value("${spring.mail.username}")
    private String email;

    @PostConstruct
    public void init(){

        //Roles Part ----------------------------------------------------------------------------------------------
        Set<Roles> rolesSet = new HashSet<>();

        Roles user = new Roles();
        user.setRole(RolesList.USER_ROLE);
        Roles admin = new Roles();
        admin.setRole(RolesList.ADMIN_ROLE);

        rolesSet.add( user );
        rolesSet.add( admin );
        rolesRep.saveAll(rolesSet);

        //User Part ---------------------------------------------------------------
        UserDTO dentalMooviDTO = Utils.setUserDTO("Dental", "Moovi", email, "314-453-6435", GenderList.UNDEFINED, null, "123456", password);

            //create admin role
            Roles adminRole = rolesRep.findByRole(RolesList.ADMIN_ROLE)
                .orElseThrow(() -> new RuntimeException("Role not found"));
            Roles userRole = rolesRep.findByRole(RolesList.USER_ROLE)
                .orElseThrow(() -> new RuntimeException("Role not found"));

            //encrypt the password
            String hashedPassword = new BCryptPasswordEncoder().encode(dentalMooviDTO.getPassword()); 

            //set and save user
            Users adminUser = Utils.setUser(dentalMooviDTO.getFirstName(), dentalMooviDTO.getLastName(), 
                dentalMooviDTO.getEmail(), dentalMooviDTO.getCelPhone(), dentalMooviDTO.getGender(), hashedPassword, dentalMooviDTO.getBirthdate(), adminRole, userRep);
            adminUser.addRole(userRole);
            adminUser = userRep.save(adminUser);

        // Parent categories
        Categories desechables = categoriesRep.save(new Categories(null, "DESECHABLES", null));
        Categories cuidadoDental = categoriesRep.save(new Categories(null, "HIGIENE ORAL", null));
        Categories instrumentacionDental = categoriesRep.save(new Categories(null, "INSTRUMENTACIÓN DENTAL", null));
            // Dental instrumentation sub-categories
            Categories hojaBisturi = categoriesRep.save(new Categories(null, "HOJA DE BISTURÍ", instrumentacionDental.id()));
            Categories jeringas = categoriesRep.save(new Categories(null, "JERINGAS",  instrumentacionDental.id()));
            Categories mangoBisturi = categoriesRep.save(new Categories(null, "MANGOS PARA BISTURÍ",  instrumentacionDental.id()));
            Categories pinzasDental = categoriesRep.save(new Categories(null, "PINZAS DENTALES",  instrumentacionDental.id()));
            Categories portaAgujas = categoriesRep.save(new Categories(null, "PORTA AGUJAS", instrumentacionDental.id()));
            Categories tijeras = categoriesRep.save(new Categories(null, "TIJERAS", instrumentacionDental.id()));
        Categories ortodoncia = categoriesRep.save(new Categories(null, "ORTODONCIA", null));
            // Orthodontics sub-categories
            Categories alambres = categoriesRep.save(new Categories(null, "ALAMBRES", ortodoncia.id()));
            Categories arcos = categoriesRep.save(new Categories(null, "ARCOS", ortodoncia.id()));
            Categories auxiliares = categoriesRep.save(new Categories(null, "AUXILIARES", ortodoncia.id()));
            Categories brakets = categoriesRep.save(new Categories(null, "BRACKETS", ortodoncia.id()));
                // Brakets sub-categories
                /* Categories carriere = */ categoriesRep.save(new Categories(null, "CARRIERE", brakets.id()));
                /* Categories deltaForce = */ categoriesRep.save(new Categories(null, "DELTA FORCE", brakets.id()));
                //Categories estandar = categoriesRep.save(new Categories(null, "ESTANDAR", brakets));
                Categories mbt = categoriesRep.save(new Categories(null, "MBT", brakets.id()));
                Categories roth = categoriesRep.save(new Categories(null, "ROTH", brakets.id()));
            Categories distalizador = categoriesRep.save(new Categories(null, "DISTALIZADOR", ortodoncia.id()));
            Categories elastomeros = categoriesRep.save(new Categories(null, "ELASTOMEROS", ortodoncia.id()));
            Categories instrumentosOrtodonticos = categoriesRep.save(new Categories(null, "INSTRUMENTOS ORTODONTICOS", ortodoncia.id()));
            /* Categories microImplantes = */ categoriesRep.save(new Categories(null, "MICROIMPLANTES", ortodoncia.id()));
            Categories pinzasOrtodoncia = categoriesRep.save(new Categories(null, "PINZAS ORTODONCIA", ortodoncia.id()));
            Categories tubos = categoriesRep.save(new Categories(null, "TUBOS", ortodoncia.id()));
        Categories ortopedia = categoriesRep.save(new Categories(null, "ORTOPEDIA", null));
        Categories rehabilitacionOral = categoriesRep.save(new Categories(null, "REHABILITACIÓN ORAL", null));
            // Oral rehabilitation sub-categories
            Categories adhesivos = categoriesRep.save(new Categories(null, "ADHESIVOS", rehabilitacionOral.id()));
            Categories bisacrilicos = categoriesRep.save(new Categories(null, "BISACRÍLICOS", rehabilitacionOral.id()));
            Categories cemento = categoriesRep.save(new Categories(null, "CEMENTO", rehabilitacionOral.id()));
            Categories compomeros = categoriesRep.save(new Categories(null, "COMPOMEROS", rehabilitacionOral.id()));
            /* Categories postes = */ categoriesRep.save(new Categories(null, "POSTES", rehabilitacionOral.id()));
            Categories provisionales = categoriesRep.save(new Categories(null, "PROVICIONALES", rehabilitacionOral.id()));
            Categories rebases = categoriesRep.save(new Categories(null, "REBASES", rehabilitacionOral.id()));
            Categories reconstructor = categoriesRep.save(new Categories(null, "RECONSTRUCTOR", rehabilitacionOral.id()));
            Categories resinas = categoriesRep.save(new Categories(null, "RESINAS", rehabilitacionOral.id()));

        Set<Products> products = new HashSet<>();

        Products adhesivo1 = Utils.setProduct("Adhesivo1", "description adhesivo1", "descripción corta", 13000,4, adhesivos.id(), true, productsRep);
        Products adhesivo2 = Utils.setProduct("Adhesivo2", "description adhesivo2", "descripción corta", 13000,4, adhesivos.id(), false, productsRep);
        Products alambre1 = Utils.setProduct("Alambre1", "description alambre1", "descripción corta", 13000,4, alambres.id(), true, productsRep);
        Products alambre2 = Utils.setProduct("Alambre2", "description alambre2", "descripción corta", 13000,4, alambres.id(), true, productsRep);
        Products arco1 = Utils.setProduct("Arco1", "description arco1", "descripción corta", 13000,4, arcos.id(), true, productsRep);
        Products arco2 = Utils.setProduct("Arco2", "description arco2", "descripción corta", 13000,4, arcos.id(), true, productsRep);
        Products auxiliar1 = Utils.setProduct("Auxiliar1", "description Auxiliar1", "descripción corta", 13000,4, auxiliares.id(), true, productsRep);
        Products auxiliar2 = Utils.setProduct("auxiliar2", "description auxiliar2", "descripción corta", 13000,4, auxiliares.id(), true, productsRep);
        Products bisacrilico1 = Utils.setProduct("bisacrilico1", "description bisacrilico1", "descripción corta", 13000,4, bisacrilicos.id(), true, productsRep);
        Products bisacrilico2 = Utils.setProduct("bisacrilico2", "description bisacrilico2", "descripción corta", 13000,4, bisacrilicos.id(), true, productsRep);
        Products carrierBraket1 = Utils.setProduct("carrierBraket1", "description carrierBraket1", "descripción corta", 13000,4, brakets.id(), true, productsRep);
        Products cemento1 = Utils.setProduct("cemento1", "description cemento1", "descripción corta", 13000,4, cemento.id(), true, productsRep);
        Products cepilloOral = Utils.setProduct("cepilloOral", "description cepilloOral", "descripción corta", 13000,4, cuidadoDental.id(), true, productsRep);
        Products compomero1 = Utils.setProduct("compomero1", "description compomero1", "descripción corta", 13000,4, compomeros.id(), true, productsRep);
        Products compomero2 = Utils.setProduct("compomero2", "description compomero2", "descripción corta", 13000,4, compomeros.id(), true, productsRep);
        Products deltaForceBraket1 = Utils.setProduct("deltaForceBraket1", "description deltaForceBraket1", "descripción corta", 13000,4, brakets.id(), true, productsRep);
        Products distalizador1 = Utils.setProduct("distalizador1", "description distalizador1", "descripción corta", 13000,4, distalizador.id(), true, productsRep);
        Products distalizador2 = Utils.setProduct("distalizador2", "description distalizador2", "descripción corta", 13000,4, distalizador.id(), true, productsRep);
        Products elastomeros1 = Utils.setProduct("elastomeros1", "description elastomeros1", "descripción corta", 13000,4, elastomeros.id(), true, productsRep);
        Products elastomeros2 = Utils.setProduct("elastomeros2", "description elastomeros2", "descripción corta", 13000,4, elastomeros.id(), true, productsRep);
        Products estandarBraket1 = Utils.setProduct("estandarBraket1", "description estandarBraket1", "descripción corta", 13000,4, brakets.id(), true, productsRep);
        Products estandarBraket2 = Utils.setProduct("estandarBraket2", "description estandarBraket2", "descripción corta", 13000,4, brakets.id(), true, productsRep);
        Products guantesLatex = Utils.setProduct("guantesLatex", "description guantesLatex", "descripción corta", 13000,4, desechables.id(), true, productsRep);
        Products hojaBisturi1 = Utils.setProduct("hojaBisturi1", "description hojaBisturi1", "descripción corta", 13000,4, hojaBisturi.id(), true, productsRep);
        Products hojaBisturi2 = Utils.setProduct("hojaBisturi2", "description hojaBisturi2", "descripción corta", 13000,4, hojaBisturi.id(), true, productsRep);
        Products instrumentoOrtodoncia1 = Utils.setProduct("instrumentoOrtodoncia1", "description instrumentoOrtodoncia1", "descripción corta", 13000,4, instrumentosOrtodonticos.id(), true, productsRep);
        Products instrumentoOrtodoncia2 = Utils.setProduct("instrumentoOrtodoncia2", "description instrumentoOrtodoncia2", "descripción corta", 13000,4, instrumentosOrtodonticos.id(), true, productsRep);
        Products jeringa1 = Utils.setProduct("jeringa1", "description jeringa1", "descripción corta", 13000,4, jeringas.id(), true, productsRep);
        Products jeringa2 = Utils.setProduct("jeringa2", "description jeringa2", "descripción corta", 13000,4, jeringas.id(), true, productsRep);
        Products mango1 = Utils.setProduct("mango1", "description mango1", "descripción corta", 13000,4, mangoBisturi.id(), true, productsRep);
        Products mango2 = Utils.setProduct("mango2", "description mango2", "descripción corta", 13000,4, mangoBisturi.id(), true, productsRep);
        Products mBTBraket1 = Utils.setProduct("mBTBraket1", "description mBTBraket1", "descripción corta", 13000,4, mbt.id(), true, productsRep);
        Products mBTBraket2 = Utils.setProduct("mBTBraket2", "description mBTBraket2", "descripción corta", 13000,4, mbt.id(), true, productsRep);
        Products ortopedia1 = Utils.setProduct("ortopedia1", "description ortopedia1", "descripción corta", 13000,4, ortopedia.id(), true, productsRep);
        Products ortopedia2 = Utils.setProduct("ortopedia2", "description ortopedia2", "descripción corta", 13000,4, ortopedia.id(), true, productsRep);
        Products pinzaDental1 = Utils.setProduct("pinzaDental1", "description pinzaDental1", "descripción corta", 13000,4, pinzasDental.id(), true, productsRep);
        Products pinzaDental2 = Utils.setProduct("pinzaDental2", "description pinzaDental2", "descripción corta", 13000,4, pinzasDental.id(), true, productsRep);
        Products pinzaOrtodoncia1 = Utils.setProduct("pinzaOrtodoncia1", "description pinzaOrtodoncia1", "descripción corta", 13000,4, pinzasOrtodoncia.id(), true, productsRep);
        Products pinzaOrtodoncia2 = Utils.setProduct("pinzaOrtodoncia2", "description pinzaOrtodoncia2", "descripción corta", 13000,4, pinzasOrtodoncia.id(), true, productsRep);
        Products portaAguja1 = Utils.setProduct("portaAguja1", "description portaAguja1", "descripción corta", 13000,4, portaAgujas.id(), true, productsRep);
        Products portaAguja2 = Utils.setProduct("portaAguja2", "description portaAguja2", "descripción corta", 13000,4, portaAgujas.id(), true, productsRep);
        Products protectorCepillo = Utils.setProduct("protectorCepillo", "description protectorCepillo", "descripción corta", 13000,4, cuidadoDental.id(), true, productsRep);
        Products provisional1 = Utils.setProduct("provicionales1", "description provicionales1", "descripción corta", 13000,4, provisionales.id(), true, productsRep);
        Products provisional2 = Utils.setProduct("provicionales2", "description provicionales2", "descripción corta", 13000,4, provisionales.id(), true, productsRep);
        Products rebase1 = Utils.setProduct("rebase1", "description rebase1", "descripción corta", 13000,4, rebases.id(), true, productsRep);
        Products rebase2 = Utils.setProduct("rebase2", "description rebase2", "descripción corta", 13000,4, rebases.id(), true, productsRep);
        Products reconstructor1 = Utils.setProduct("reconstructor1", "description reconstructor1", "descripción corta", 13000,4, reconstructor.id(), true, productsRep);
        Products reconstructor2 = Utils.setProduct("reconstructor2", "description reconstructor2", "descripción corta", 13000,4, reconstructor.id(), true, productsRep);
        Products resina1 = Utils.setProduct("resina1", "description resina1", "descripción corta", 13000,4, resinas.id(), true, productsRep);
        Products resina2 = Utils.setProduct("resina2", "description resina2", "descripción corta", 13000,4, resinas.id(), true, productsRep);
        Products rothBraket1 = Utils.setProduct("rothBraket1", "description rothBraket1", "descripción corta", 13000,4, roth.id(), true, productsRep);
        Products rothBraket2 = Utils.setProduct("rothBraket2", "description rothBraket2", "descripción corta", 13000,4, roth.id(), true, productsRep);
        Products tapaBocas1 = Utils.setProduct("tapabocas1", "description tapabocas1", "descripción corta", 13000,4, desechables.id(), true, productsRep);
        Products tigera1 = Utils.setProduct("tigera1", "description tigera1", "descripción corta", 13000,4, tijeras.id(), true, productsRep);
        Products tubo1 = Utils.setProduct("tubo1", "description tubo1", "descripción corta", 13000,4, tubos.id(), true, productsRep);
        Products tubo2 = Utils.setProduct("tubo2", "description tubo2", "descripción corta", 13000,4, tubos.id(), true, productsRep);

        List<Images> images = new ArrayList<>();
        
        imagesRep.save(new Images(null, "adhesivo1-2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo1.getId()));
        /* Images adhesivo13Image= */ imagesRep.save(new Images(null, "adhesivo1-3Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), adhesivo1.getId()));
        /* Images adhesivo14Image= */ imagesRep.save(new Images(null, "adhesivo1-4Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), adhesivo1.getId()));
        Images adhesivo1Image= imagesRep.save(new Images(null, "adhesivo1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo1.jpg"), adhesivo1.getId()));

        Images adhesivo2Image= imagesRep.save(new Images(null, "adhesivo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo2.getId()));
        Images alambre1Image= imagesRep.save(new Images(null, "alambre1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), alambre1.getId()));
        Images alambre2Image= imagesRep.save(new Images(null, "alambre2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), alambre2.getId()));
        Images arco1Image= imagesRep.save(new Images(null, "arco1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco1.jpg"), arco1.getId()));
        Images arco2Image= imagesRep.save(new Images(null, "arco2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco2.jpg"), arco2.getId()));
        Images auxiliar1Image= imagesRep.save(new Images(null, "auxiliar1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar1.jpg"), auxiliar1.getId()));
        Images auxiliar2Image= imagesRep.save(new Images(null, "auxiliar2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar2.jpg"), auxiliar2.getId()));
        Images bisacrilico1Image= imagesRep.save(new Images(null, "bisacrilico1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico1.jpg"), bisacrilico1.getId()));
        Images bisacrilico2Image= imagesRep.save(new Images(null, "bisacrilico2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico2.jpg"), bisacrilico2.getId()));
        Images carrierBraket1Image= imagesRep.save(new Images(null, "carrierBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\carrierBraket1.jpg"), carrierBraket1.getId()));
        Images cemento1Image= imagesRep.save(new Images(null, "cemento1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\cemento1.jpg"), cemento1.getId()));
        Images cepilloOralImage= imagesRep.save(new Images(null, "cepilloOralImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\CEPILLO-ORAL.jpg"), cepilloOral.getId()));
        Images compomero1Image= imagesRep.save(new Images(null, "compomero1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\compomero1.png"), compomero1.getId()));
        Images deltaForceBraket1Image= imagesRep.save(new Images(null, "deltaForceBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\dentalForceBraket1.jpg"), deltaForceBraket1.getId()));
        Images distalizador1Image= imagesRep.save(new Images(null, "distalizador1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador1.jpg"), distalizador1.getId()));
        Images distalizador2Image= imagesRep.save(new Images(null, "distalizador2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador2.jpg"), distalizador2.getId()));
        Images elastomeros1Image= imagesRep.save(new Images(null, "elastomeros1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero1.jpg"), elastomeros1.getId()));
        Images elastomeros2Image= imagesRep.save(new Images(null, "elastomeros2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero2.jpg"), elastomeros2.getId()));
        Images estandarBraket1Image= imagesRep.save(new Images(null, "estandarBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket1.jpg"), estandarBraket1.getId()));
        Images estandarBraket2Image= imagesRep.save(new Images(null, "estandarBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket2.jpg"), estandarBraket2.getId()));
        Images guantesLatexImage= imagesRep.save(new Images(null, "guantesLatexImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\Guantes-latex.jpg"), guantesLatex.getId()));
        Images hojaBisturi1Image= imagesRep.save(new Images(null, "hojaBisturi1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi1.jpg"), hojaBisturi1.getId()));
        Images hojaBisturi2Image= imagesRep.save(new Images(null, "hojaBisturi2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi2.jpg"), hojaBisturi2.getId()));
        Images instrumentoOrtodoncia1Image= imagesRep.save(new Images(null, "instrumentoOrtodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia1.jpg"), instrumentoOrtodoncia1.getId()));
        Images instrumentoOrtodoncia2Image= imagesRep.save(new Images(null, "instrumentoOrtodoncia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia2.jpg"), instrumentoOrtodoncia2.getId()));
        Images jeringa1Image= imagesRep.save(new Images(null, "jeringa1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa1.jpg"), jeringa1.getId()));
        Images jeringa2Image= imagesRep.save(new Images(null, "jeringa2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa2.jpg"), jeringa2.getId()));
        Images mango1Image= imagesRep.save(new Images(null, "mango1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango1.jpg"), mango1.getId()));
        Images mango2Image= imagesRep.save(new Images(null, "mango2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango2.jpg"), mango2.getId()));
        Images mBTBraket1Image= imagesRep.save(new Images(null, "MBTBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket1.jpg"), mBTBraket1.getId()));
        Images mBTBraket2Image= imagesRep.save(new Images(null, "MBTBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket2.jpg"), mBTBraket2.getId()));
        Images ortopedia1Image= imagesRep.save(new Images(null, "ortopedia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia1.jpg"), ortopedia1.getId()));
        Images ortopedia2Image= imagesRep.save(new Images(null, "ortopedia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia2.jpg"), ortopedia2.getId()));
        Images pinza1Image= imagesRep.save(new Images(null, "pinza1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza1.jpg"), pinzaDental1.getId()));
        Images pinza2Image= imagesRep.save(new Images(null, "pinza2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza2.jpg"), pinzaDental2.getId()));
        Images pinza1Ortodoncia1Image= imagesRep.save(new Images(null, "pinza1Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia1.jpg"), pinzaOrtodoncia1.getId()));
        Images pinza2Ortodoncia1Image= imagesRep.save(new Images(null, "pinza2Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia2.jpg"), pinzaOrtodoncia2.getId()));
        Images portaaguja1Image= imagesRep.save(new Images(null, "portaaguja1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja1.jpg"), portaAguja1.getId()));
        Images portaaguja2Image= imagesRep.save(new Images(null, "portaaguja2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja2.jpg"), portaAguja2.getId()));
        Images protectorCepilloImage= imagesRep.save(new Images(null, "protectorCepilloImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\protectorCepillo.jpg"), protectorCepillo.getId()));
        Images provisional1Image= imagesRep.save(new Images(null, "provisional1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional1.jpg"), provisional1.getId()));
        Images provisional2Image= imagesRep.save(new Images(null, "provisional2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional2.jpg"), provisional2.getId()));
        Images rebase1Image= imagesRep.save(new Images(null, "rebase1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase1.png"), rebase1.getId()));
        Images rebase2Image= imagesRep.save(new Images(null, "rebase2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase2.jpg"), rebase2.getId()));
        Images reconstructor1Image= imagesRep.save(new Images(null, "reconstructor1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor1.jpg"), reconstructor1.getId()));
        Images reconstructor2Image= imagesRep.save(new Images(null, "reconstructor2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor2.jpg"), reconstructor2.getId()));
        Images resina1Image= imagesRep.save(new Images(null, "resina1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina1.jpg"), resina1.getId()));
        Images resina2Image= imagesRep.save(new Images(null, "resina2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina2.jpg"), resina2.getId()));
        Images rothBraket1Image= imagesRep.save(new Images(null, "rothBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket1.jpg"), rothBraket1.getId()));
        Images rothBraket2Image= imagesRep.save(new Images(null, "rothBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket2.jpg"), rothBraket2.getId()));
        Images tigera1Image= imagesRep.save(new Images(null, "tigera1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tigera1.jpg"), tigera1.getId()));
        Images tubo1Image= imagesRep.save(new Images(null, "tubo1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo1.png"), tubo1.getId()));
        Images tubo2Image= imagesRep.save(new Images(null, "tubo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo2.jpg"), tubo2.getId()));

        productsRep.saveAll(products);

        imagesRep.saveAll(images);

        adhesivo1.setIdMainImage(adhesivo1Image.id());
        adhesivo2.setIdMainImage(adhesivo2Image.id());
        alambre1.setIdMainImage(alambre1Image.id());
        alambre2.setIdMainImage(alambre2Image.id());
        arco1.setIdMainImage(arco1Image.id());
        arco2.setIdMainImage(arco2Image.id());
        auxiliar1.setIdMainImage(auxiliar1Image.id());
        auxiliar2.setIdMainImage(auxiliar2Image.id());
        bisacrilico1.setIdMainImage(bisacrilico1Image.id());
        bisacrilico2.setIdMainImage(bisacrilico2Image.id());
        carrierBraket1.setIdMainImage(carrierBraket1Image.id());
        cemento1.setIdMainImage(cemento1Image.id());
        cepilloOral.setIdMainImage(cepilloOralImage.id());
        compomero1.setIdMainImage(compomero1Image.id());
        deltaForceBraket1.setIdMainImage(deltaForceBraket1Image.id());
        distalizador1.setIdMainImage(distalizador1Image.id());
        distalizador2.setIdMainImage(distalizador2Image.id());
        elastomeros1.setIdMainImage(elastomeros1Image.id());
        elastomeros2.setIdMainImage(elastomeros2Image.id());
        estandarBraket1.setIdMainImage(estandarBraket1Image.id());
        estandarBraket2.setIdMainImage(estandarBraket2Image.id());
        guantesLatex.setIdMainImage(guantesLatexImage.id());
        hojaBisturi1.setIdMainImage(hojaBisturi1Image.id());
        hojaBisturi2.setIdMainImage(hojaBisturi2Image.id());
        instrumentoOrtodoncia1.setIdMainImage(instrumentoOrtodoncia1Image.id());
        instrumentoOrtodoncia2.setIdMainImage(instrumentoOrtodoncia2Image.id());
        jeringa1.setIdMainImage(jeringa1Image.id());
        jeringa2.setIdMainImage(jeringa2Image.id());
        mango1.setIdMainImage(mango1Image.id());
        mango2.setIdMainImage(mango2Image.id());
        mBTBraket1.setIdMainImage(mBTBraket1Image.id());
        mBTBraket2.setIdMainImage(mBTBraket2Image.id());
        ortopedia1.setIdMainImage(ortopedia1Image.id());
        ortopedia2.setIdMainImage(ortopedia2Image.id());
        pinzaDental1.setIdMainImage(pinza1Image.id());
        pinzaDental2.setIdMainImage(pinza2Image.id());
        pinzaOrtodoncia1.setIdMainImage(pinza1Ortodoncia1Image.id());
        pinzaOrtodoncia1.setIdMainImage(pinza2Ortodoncia1Image.id());
        portaAguja1.setIdMainImage(portaaguja1Image.id());
        portaAguja2.setIdMainImage(portaaguja2Image.id());
        protectorCepillo.setIdMainImage(protectorCepilloImage.id());
        provisional1.setIdMainImage(provisional1Image.id());

        provisional2.setIdMainImage(provisional2Image.id());
        rebase1.setIdMainImage(rebase1Image.id());
        rebase2.setIdMainImage(rebase2Image.id());
        reconstructor1.setIdMainImage(reconstructor1Image.id());
        reconstructor2.setIdMainImage(reconstructor2Image.id());
        resina1.setIdMainImage(resina1Image.id());
        resina2.setIdMainImage(resina2Image.id());
        rothBraket1.setIdMainImage(rothBraket1Image.id());
        rothBraket2.setIdMainImage(rothBraket2Image.id());

        tigera1.setIdMainImage(tigera1Image.id());
        tubo1.setIdMainImage(tubo1Image.id());
        tubo2.setIdMainImage(tubo2Image.id());

        products.addAll(List.of(
            adhesivo1, adhesivo2, alambre1, alambre2, arco1, arco2, auxiliar1, auxiliar2, bisacrilico1,
            bisacrilico2, carrierBraket1, cemento1, cepilloOral, compomero1, compomero2, deltaForceBraket1,
            distalizador1, distalizador2, elastomeros1, elastomeros2, estandarBraket1, estandarBraket2,
            guantesLatex, hojaBisturi1, hojaBisturi2, instrumentoOrtodoncia1, instrumentoOrtodoncia2,
            jeringa1, jeringa2, mango1, mango2, mBTBraket1, mBTBraket2, ortopedia1, ortopedia2, pinzaDental1,
            pinzaDental2, pinzaOrtodoncia1, pinzaOrtodoncia2, portaAguja1, portaAguja2, protectorCepillo,
            provisional1, provisional2, rebase1, rebase2, reconstructor1, reconstructor2,
            resina1, resina2, rothBraket1, rothBraket2, tapaBocas1, tigera1, tubo1, tubo2
        ));

        productsRep.saveAll(products);
    }   
}
