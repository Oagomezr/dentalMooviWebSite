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
        
        /* Images adhesivo12Image= */ Utils.setImage("adhesivo1-2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo1.getId(), imagesRep);
        /* Images adhesivo13Image= */ Utils.setImage("adhesivo1-3Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), adhesivo1.getId(), imagesRep);
        /* Images adhesivo14Image= */ Utils.setImage("adhesivo1-4Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), adhesivo1.getId(), imagesRep);
        Images adhesivo1Image= Utils.setImage("adhesivo1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo1.jpg"), adhesivo1.getId(), imagesRep);

        Images adhesivo2Image= Utils.setImage("adhesivo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo2.getId(), imagesRep);
        Images alambre1Image= Utils.setImage("alambre1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), alambre1.getId(), imagesRep);
        Images alambre2Image= Utils.setImage("alambre2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), alambre2.getId(), imagesRep);
        Images arco1Image= Utils.setImage("arco1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco1.jpg"), arco1.getId(), imagesRep);
        Images arco2Image= Utils.setImage("arco2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco2.jpg"), arco2.getId(), imagesRep);
        Images auxiliar1Image= Utils.setImage("auxiliar1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar1.jpg"), auxiliar1.getId(), imagesRep);
        Images auxiliar2Image= Utils.setImage("auxiliar2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar2.jpg"), auxiliar2.getId(), imagesRep);
        Images bisacrilico1Image= Utils.setImage("bisacrilico1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico1.jpg"), bisacrilico1.getId(), imagesRep);
        Images bisacrilico2Image= Utils.setImage("bisacrilico2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico2.jpg"), bisacrilico2.getId(), imagesRep);
        Images carrierBraket1Image= Utils.setImage("carrierBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\carrierBraket1.jpg"), carrierBraket1.getId(), imagesRep);
        Images cemento1Image= Utils.setImage("cemento1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\cemento1.jpg"), cemento1.getId(), imagesRep);
        Images cepilloOralImage= Utils.setImage("cepilloOralImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\CEPILLO-ORAL.jpg"), cepilloOral.getId(), imagesRep);
        Images compomero1Image= Utils.setImage("compomero1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\compomero1.png"), compomero1.getId(), imagesRep);
        Images deltaForceBraket1Image= Utils.setImage("deltaForceBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\dentalForceBraket1.jpg"), deltaForceBraket1.getId(), imagesRep);
        Images distalizador1Image= Utils.setImage("distalizador1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador1.jpg"), distalizador1.getId(), imagesRep);
        Images distalizador2Image= Utils.setImage("distalizador2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador2.jpg"), distalizador2.getId(), imagesRep);
        Images elastomeros1Image= Utils.setImage("elastomeros1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero1.jpg"), elastomeros1.getId(), imagesRep);
        Images elastomeros2Image= Utils.setImage("elastomeros2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero2.jpg"), elastomeros2.getId(), imagesRep);
        Images estandarBraket1Image= Utils.setImage("estandarBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket1.jpg"), estandarBraket1.getId(), imagesRep);
        Images estandarBraket2Image= Utils.setImage("estandarBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket2.jpg"), estandarBraket2.getId(), imagesRep);
        Images guantesLatexImage= Utils.setImage("guantesLatexImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\Guantes-latex.jpg"), guantesLatex.getId(), imagesRep);
        Images hojaBisturi1Image= Utils.setImage("hojaBisturi1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi1.jpg"), hojaBisturi1.getId(), imagesRep);
        Images hojaBisturi2Image= Utils.setImage("hojaBisturi2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi2.jpg"), hojaBisturi2.getId(), imagesRep);
        Images instrumentoOrtodoncia1Image= Utils.setImage("instrumentoOrtodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia1.jpg"), instrumentoOrtodoncia1.getId(), imagesRep);
        Images instrumentoOrtodoncia2Image= Utils.setImage("instrumentoOrtodoncia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia2.jpg"), instrumentoOrtodoncia2.getId(), imagesRep);
        Images jeringa1Image= Utils.setImage("jeringa1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa1.jpg"), jeringa1.getId(), imagesRep);
        Images jeringa2Image= Utils.setImage("jeringa2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa2.jpg"), jeringa2.getId(), imagesRep);
        Images mango1Image= Utils.setImage("mango1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango1.jpg"), mango1.getId(), imagesRep);
        Images mango2Image= Utils.setImage("mango2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango2.jpg"), mango2.getId(), imagesRep);
        Images mBTBraket1Image= Utils.setImage("MBTBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket1.jpg"), mBTBraket1.getId(), imagesRep);
        Images mBTBraket2Image= Utils.setImage("MBTBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket2.jpg"), mBTBraket2.getId(), imagesRep);
        Images ortopedia1Image= Utils.setImage("ortopedia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia1.jpg"), ortopedia1.getId(), imagesRep);
        Images ortopedia2Image= Utils.setImage("ortopedia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia2.jpg"), ortopedia2.getId(), imagesRep);
        Images pinza1Image= Utils.setImage("pinza1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza1.jpg"), pinzaDental1.getId(), imagesRep);
        Images pinza2Image= Utils.setImage("pinza2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza2.jpg"), pinzaDental2.getId(), imagesRep);
        Images pinza1Ortodoncia1Image= Utils.setImage("pinza1Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia1.jpg"), pinzaOrtodoncia1.getId(), imagesRep);
        Images pinza2Ortodoncia1Image= Utils.setImage("pinza2Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia2.jpg"), pinzaOrtodoncia2.getId(), imagesRep);
        Images portaaguja1Image= Utils.setImage("portaaguja1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja1.jpg"), portaAguja1.getId(), imagesRep);
        Images portaaguja2Image= Utils.setImage("portaaguja2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja2.jpg"), portaAguja2.getId(), imagesRep);
        Images protectorCepilloImage= Utils.setImage("protectorCepilloImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\protectorCepillo.jpg"), protectorCepillo.getId(), imagesRep);
        Images provisional1Image= Utils.setImage("provisional1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional1.jpg"), provisional1.getId(), imagesRep);
        Images provisional2Image= Utils.setImage("provisional2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional2.jpg"), provisional2.getId(), imagesRep);
        Images rebase1Image= Utils.setImage("rebase1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase1.png"), rebase1.getId(), imagesRep);
        Images rebase2Image= Utils.setImage("rebase2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase2.jpg"), rebase2.getId(), imagesRep);
        Images reconstructor1Image= Utils.setImage("reconstructor1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor1.jpg"), reconstructor1.getId(), imagesRep);
        Images reconstructor2Image= Utils.setImage("reconstructor2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor2.jpg"), reconstructor2.getId(), imagesRep);
        Images resina1Image= Utils.setImage("resina1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina1.jpg"), resina1.getId(), imagesRep);
        Images resina2Image= Utils.setImage("resina2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina2.jpg"), resina2.getId(), imagesRep);
        Images rothBraket1Image= Utils.setImage("rothBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket1.jpg"), rothBraket1.getId(), imagesRep);
        Images rothBraket2Image= Utils.setImage("rothBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket2.jpg"), rothBraket2.getId(), imagesRep);
        Images tigera1Image= Utils.setImage("tigera1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tigera1.jpg"), tigera1.getId(), imagesRep);
        Images tubo1Image= Utils.setImage("tubo1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo1.png"), tubo1.getId(), imagesRep);
        Images tubo2Image= Utils.setImage("tubo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo2.jpg"), tubo2.getId(), imagesRep);

        productsRep.saveAll(products);

        imagesRep.saveAll(images);

        adhesivo1.setIdMainImage(adhesivo1Image.getId());
        adhesivo2.setIdMainImage(adhesivo2Image.getId());
        alambre1.setIdMainImage(alambre1Image.getId());
        alambre2.setIdMainImage(alambre2Image.getId());
        arco1.setIdMainImage(arco1Image.getId());
        arco2.setIdMainImage(arco2Image.getId());
        auxiliar1.setIdMainImage(auxiliar1Image.getId());
        auxiliar2.setIdMainImage(auxiliar2Image.getId());
        bisacrilico1.setIdMainImage(bisacrilico1Image.getId());
        bisacrilico2.setIdMainImage(bisacrilico2Image.getId());
        carrierBraket1.setIdMainImage(carrierBraket1Image.getId());
        cemento1.setIdMainImage(cemento1Image.getId());
        cepilloOral.setIdMainImage(cepilloOralImage.getId());
        compomero1.setIdMainImage(compomero1Image.getId());
        deltaForceBraket1.setIdMainImage(deltaForceBraket1Image.getId());
        distalizador1.setIdMainImage(distalizador1Image.getId());
        distalizador2.setIdMainImage(distalizador2Image.getId());
        elastomeros1.setIdMainImage(elastomeros1Image.getId());
        elastomeros2.setIdMainImage(elastomeros2Image.getId());
        estandarBraket1.setIdMainImage(estandarBraket1Image.getId());
        estandarBraket2.setIdMainImage(estandarBraket2Image.getId());
        guantesLatex.setIdMainImage(guantesLatexImage.getId());
        hojaBisturi1.setIdMainImage(hojaBisturi1Image.getId());
        hojaBisturi2.setIdMainImage(hojaBisturi2Image.getId());
        instrumentoOrtodoncia1.setIdMainImage(instrumentoOrtodoncia1Image.getId());
        instrumentoOrtodoncia2.setIdMainImage(instrumentoOrtodoncia2Image.getId());
        jeringa1.setIdMainImage(jeringa1Image.getId());
        jeringa2.setIdMainImage(jeringa2Image.getId());
        mango1.setIdMainImage(mango1Image.getId());
        mango2.setIdMainImage(mango2Image.getId());
        mBTBraket1.setIdMainImage(mBTBraket1Image.getId());
        mBTBraket2.setIdMainImage(mBTBraket2Image.getId());
        ortopedia1.setIdMainImage(ortopedia1Image.getId());
        ortopedia2.setIdMainImage(ortopedia2Image.getId());
        pinzaDental1.setIdMainImage(pinza1Image.getId());
        pinzaDental2.setIdMainImage(pinza2Image.getId());
        pinzaOrtodoncia1.setIdMainImage(pinza1Ortodoncia1Image.getId());
        pinzaOrtodoncia1.setIdMainImage(pinza2Ortodoncia1Image.getId());
        portaAguja1.setIdMainImage(portaaguja1Image.getId());
        portaAguja2.setIdMainImage(portaaguja2Image.getId());
        protectorCepillo.setIdMainImage(protectorCepilloImage.getId());
        provisional1.setIdMainImage(provisional1Image.getId());

        provisional2.setIdMainImage(provisional2Image.getId());
        rebase1.setIdMainImage(rebase1Image.getId());
        rebase2.setIdMainImage(rebase2Image.getId());
        reconstructor1.setIdMainImage(reconstructor1Image.getId());
        reconstructor2.setIdMainImage(reconstructor2Image.getId());
        resina1.setIdMainImage(resina1Image.getId());
        resina2.setIdMainImage(resina2Image.getId());
        rothBraket1.setIdMainImage(rothBraket1Image.getId());
        rothBraket2.setIdMainImage(rothBraket2Image.getId());

        tigera1.setIdMainImage(tigera1Image.getId());
        tubo1.setIdMainImage(tubo1Image.getId());
        tubo2.setIdMainImage(tubo2Image.getId());

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
