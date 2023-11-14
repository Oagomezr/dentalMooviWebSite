package com.dentalmoovi.website;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.enums.RolesList;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ImgRep;
import com.dentalmoovi.website.repositories.ProductsRep;
import com.dentalmoovi.website.repositories.RolesRep;

import jakarta.annotation.PostConstruct;

@Component
public class InitialTestData {
    private final CategoriesRep categoriesRep;
    private final ProductsRep productsRep;
    private final ImgRep imagesRep;
    private final RolesRep rolesRep;
    
    public InitialTestData(CategoriesRep categoriesRep, ProductsRep productsRep,
            ImgRep imagesRep, RolesRep rolesRep) {
        this.categoriesRep = categoriesRep;
        this.productsRep = productsRep;
        this.imagesRep = imagesRep;
        this.rolesRep = rolesRep;
    }

    @PostConstruct
    public void init(){

        //Roles Part ----------------------------------------------------------------------------------------------
        Set<Roles> rolesSet = new HashSet<>();

        Roles user = new Roles();
        user.setRole(RolesList.USER_ROLE);
        Roles admin = new Roles();
        admin.setRole(RolesList.USER_ROLE);

        rolesSet.add( user );
        rolesSet.add( admin );
        rolesRep.saveAll(rolesSet);

        // Categories Part ------------------------------------------------------------------------------------
        Set<Categories> categoriesSet = new HashSet<>();

        // Parent categories
        Categories desechables = Utils.setCategory("DESECHABLES", null);
        Categories cuidadoDental = Utils.setCategory("HIGIENE ORAL", null);
        Categories instrumentacionDental = Utils.setCategory("INSTRUMENTACIÓN DENTAL", null);
            // Dental instrumentation sub-categories
            Categories hojaBisturi = Utils.setCategory("HOJA DE BISTURÍ", instrumentacionDental);
            Categories jeringas = Utils.setCategory("JERINGAS",  instrumentacionDental);
            Categories mangoBisturi = Utils.setCategory("MANGOS PARA BISTURÍ",  instrumentacionDental);
            Categories pinzasDental = Utils.setCategory("PINZAS DENTALES",  instrumentacionDental);
            Categories portaAgujas = Utils.setCategory("PORTA AGUJAS", instrumentacionDental);
            Categories tijeras = Utils.setCategory("TIJERAS", instrumentacionDental);
            categoriesSet.addAll(List.of(hojaBisturi, jeringas, mangoBisturi, pinzasDental, portaAgujas, tijeras));
        Categories ortodoncia = Utils.setCategory("ORTODONCIA", null);
            // Orthodontics sub-categories
            Categories alambres = Utils.setCategory("ALAMBRES", ortodoncia);
            Categories arcos = Utils.setCategory("ARCOS", ortodoncia);
            Categories auxiliares = Utils.setCategory("AUXILIARES", ortodoncia);
            Categories brakets = Utils.setCategory("BRACKETS", ortodoncia);
                // Brakets sub-categories
                Categories carriere = Utils.setCategory("CARRIERE", brakets);
                Categories deltaForce = Utils.setCategory("DELTA FORCE", brakets);
                //Categories estandar = Utils.setCategory("ESTANDAR", brakets);
                Categories mbt = Utils.setCategory("MBT", brakets);
                Categories roth = Utils.setCategory("ROTH", brakets);
                categoriesSet.addAll(List.of(carriere, deltaForce, /* estandar, */ mbt, roth));
            Categories distalizador = Utils.setCategory("DISTALIZADOR", ortodoncia);
            Categories elastomeros = Utils.setCategory("ELASTOMEROS", ortodoncia);
            Categories instrumentosOrtodonticos = Utils.setCategory("INSTRUMENTOS ORTODONTICOS", ortodoncia);
            Categories microImplantes = Utils.setCategory("MICROIMPLANTES", ortodoncia);
            Categories pinzasOrtodoncia = Utils.setCategory("PINZAS ORTODONCIA", ortodoncia);
            Categories tubos = Utils.setCategory("TUBOS", ortodoncia);
            categoriesSet.addAll(List.of(alambres, arcos, auxiliares, brakets, distalizador, elastomeros, instrumentosOrtodonticos, microImplantes, pinzasOrtodoncia, tubos));
        Categories ortopedia = Utils.setCategory("ORTOPEDIA", null);
        Categories rehabilitacionOral = Utils.setCategory("REHABILITACIÓN ORAL", null);
            // Oral rehabilitation sub-categories
            Categories adhesivos = Utils.setCategory("ADHESIVOS", rehabilitacionOral);
            Categories bisacrilicos = Utils.setCategory("BISACRÍLICOS", rehabilitacionOral);
            Categories cemento = Utils.setCategory("CEMENTO", rehabilitacionOral);
            Categories compomeros = Utils.setCategory("COMPOMEROS", rehabilitacionOral);
            Categories postes = Utils.setCategory("POSTES", rehabilitacionOral);
            Categories provisionales = Utils.setCategory("PROVICIONALES", rehabilitacionOral);
            Categories rebases = Utils.setCategory("REBASES", rehabilitacionOral);
            Categories reconstructor = Utils.setCategory("RECONSTRUCTOR", rehabilitacionOral);
            Categories resinas = Utils.setCategory("RESINAS", rehabilitacionOral);
            categoriesSet.addAll(List.of(adhesivos, bisacrilicos, cemento, compomeros, postes, provisionales, rebases, reconstructor, resinas));
        categoriesSet.addAll(List.of(desechables, cuidadoDental, instrumentacionDental, ortodoncia, ortopedia, rehabilitacionOral));

        categoriesRep.saveAll(categoriesSet);

        Set<Products> products = new HashSet<>();

        Products adhesivo1 = Utils.setProduct("Adhesivo1", "description adhesivo1", 13000,4, adhesivos, true);
        Products adhesivo2 = Utils.setProduct("Adhesivo2", "description adhesivo2", 13000,4, adhesivos, true);
        Products alambre1 = Utils.setProduct("Alambre1", "description alambre1", 13000,4, alambres, true);
        Products alambre2 = Utils.setProduct("Alambre2", "description alambre2", 13000,4, alambres, true);
        Products arco1 = Utils.setProduct("Arco1", "description arco1", 13000,4, arcos, true);
        Products arco2 = Utils.setProduct("Arco2", "description arco2", 13000,4, arcos, true);
        Products auxiliar1 = Utils.setProduct("Auxiliar1", "description Auxiliar1", 13000,4, auxiliares, true);
        Products auxiliar2 = Utils.setProduct("auxiliar2", "description auxiliar2", 13000,4, auxiliares, true);
        Products bisacrilico1 = Utils.setProduct("bisacrilico1", "description bisacrilico1", 13000,4, bisacrilicos, true);
        Products bisacrilico2 = Utils.setProduct("bisacrilico2", "description bisacrilico2", 13000,4, bisacrilicos, true);
        Products carrierBraket1 = Utils.setProduct("carrierBraket1", "description carrierBraket1", 13000,4, brakets, true);
        Products cemento1 = Utils.setProduct("cemento1", "description cemento1", 13000,4, cemento, true);
        Products cepilloOral = Utils.setProduct("cepilloOral", "description cepilloOral", 13000,4, cuidadoDental, true);
        Products compomero1 = Utils.setProduct("compomero1", "description compomero1", 13000,4, compomeros, true);
        Products compomero2 = Utils.setProduct("compomero2", "description compomero2", 13000,4, compomeros, true);
        Products deltaForceBraket1 = Utils.setProduct("deltaForceBraket1", "description deltaForceBraket1", 13000,4, brakets, true);
        Products distalizador1 = Utils.setProduct("distalizador1", "description distalizador1", 13000,4, distalizador, true);
        Products distalizador2 = Utils.setProduct("distalizador2", "description distalizador2", 13000,4, distalizador, true);
        Products elastomeros1 = Utils.setProduct("elastomeros1", "description elastomeros1", 13000,4, elastomeros, true);
        Products elastomeros2 = Utils.setProduct("elastomeros2", "description elastomeros2", 13000,4, elastomeros, true);
        Products estandarBraket1 = Utils.setProduct("estandarBraket1", "description estandarBraket1", 13000,4, brakets, true);
        Products estandarBraket2 = Utils.setProduct("estandarBraket2", "description estandarBraket2", 13000,4, brakets, true);
        Products guantesLatex = Utils.setProduct("guantesLatex", "description guantesLatex", 13000,4, desechables, true);
        Products hojaBisturi1 = Utils.setProduct("hojaBisturi1", "description hojaBisturi1", 13000,4, hojaBisturi, true);
        Products hojaBisturi2 = Utils.setProduct("hojaBisturi2", "description hojaBisturi2", 13000,4, hojaBisturi, true);
        Products instrumentoOrtodoncia1 = Utils.setProduct("instrumentoOrtodoncia1", "description instrumentoOrtodoncia1", 13000,4, instrumentosOrtodonticos, true);
        Products instrumentoOrtodoncia2 = Utils.setProduct("instrumentoOrtodoncia2", "description instrumentoOrtodoncia2", 13000,4, instrumentosOrtodonticos, true);
        Products jeringa1 = Utils.setProduct("jeringa1", "description jeringa1", 13000,4, jeringas, true);
        Products jeringa2 = Utils.setProduct("jeringa2", "description jeringa2", 13000,4, jeringas, true);
        Products mango1 = Utils.setProduct("mango1", "description mango1", 13000,4, mangoBisturi, true);
        Products mango2 = Utils.setProduct("mango2", "description mango2", 13000,4, mangoBisturi, true);
        Products mBTBraket1 = Utils.setProduct("mBTBraket1", "description mBTBraket1", 13000,4, mbt, true);
        Products mBTBraket2 = Utils.setProduct("mBTBraket2", "description mBTBraket2", 13000,4, mbt, true);
        Products ortopedia1 = Utils.setProduct("ortopedia1", "description ortopedia1", 13000,4, ortopedia, true);
        Products ortopedia2 = Utils.setProduct("ortopedia2", "description ortopedia2", 13000,4, ortopedia, true);
        Products pinzaDental1 = Utils.setProduct("pinzaDental1", "description pinzaDental1", 13000,4, pinzasDental, true);
        Products pinzaDental2 = Utils.setProduct("pinzaDental2", "description pinzaDental2", 13000,4, pinzasDental, true);
        Products pinzaOrtodoncia1 = Utils.setProduct("pinzaOrtodoncia1", "description pinzaOrtodoncia1", 13000,4, pinzasOrtodoncia, true);
        Products pinzaOrtodoncia2 = Utils.setProduct("pinzaOrtodoncia2", "description pinzaOrtodoncia2", 13000,4, pinzasOrtodoncia, true);
        Products portaAguja1 = Utils.setProduct("portaAguja1", "description portaAguja1", 13000,4, portaAgujas, true);
        Products portaAguja2 = Utils.setProduct("portaAguja2", "description portaAguja2", 13000,4, portaAgujas, true);
        Products protectorCepillo = Utils.setProduct("protectorCepillo", "description protectorCepillo", 13000,4, cuidadoDental, true);
        Products provisional1 = Utils.setProduct("provicionales1", "description provicionales1", 13000,4, provisionales, true);
        Products provisional2 = Utils.setProduct("provicionales2", "description provicionales2", 13000,4, provisionales, true);
        Products rebase1 = Utils.setProduct("rebase1", "description rebase1", 13000,4, rebases, true);
        Products rebase2 = Utils.setProduct("rebase2", "description rebase2", 13000,4, rebases, true);
        Products reconstructor1 = Utils.setProduct("reconstructor1", "description reconstructor1", 13000,4, reconstructor, true);
        Products reconstructor2 = Utils.setProduct("reconstructor2", "description reconstructor2", 13000,4, reconstructor, true);
        Products resina1 = Utils.setProduct("resina1", "description resina1", 13000,4, resinas, true);
        Products resina2 = Utils.setProduct("resina2", "description resina2", 13000,4, resinas, true);
        Products rothBraket1 = Utils.setProduct("rothBraket1", "description rothBraket1", 13000,4, roth, true);
        Products rothBraket2 = Utils.setProduct("rothBraket2", "description rothBraket2", 13000,4, roth, true);
        Products tapaBocas1 = Utils.setProduct("tapabocas1", "description tapabocas1", 13000,4, desechables, true);
        Products tigera1 = Utils.setProduct("tigera1", "description tigera1", 13000,4, tijeras, true);
        Products tubo1 = Utils.setProduct("tubo1", "description tubo1", 13000,4, tubos, true);
        Products tubo2 = Utils.setProduct("tubo2", "description tubo2", 13000,4, tubos, true);

        List<Images> images = new ArrayList<>();
        Images adhesivo1Image= Utils.setImage("adhesivo1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo1.jpg"), adhesivo1);
        Images adhesivo12Image= Utils.setImage("adhesivo1-2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo1);
        Images adhesivo13Image= Utils.setImage("adhesivo1-3Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), adhesivo1);
        Images adhesivo14Image= Utils.setImage("adhesivo1-4Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), adhesivo1);

        Images adhesivo2Image= Utils.setImage("adhesivo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo2);
        Images alambre1Image= Utils.setImage("alambre1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), alambre1);
        Images alambre2Image= Utils.setImage("alambre2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), alambre2);
        Images arco1Image= Utils.setImage("arco1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco1.jpg"), arco1);
        Images arco2Image= Utils.setImage("arco2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco2.jpg"), arco2);
        Images auxiliar1Image= Utils.setImage("auxiliar1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar1.jpg"), auxiliar1);
        Images auxiliar2Image= Utils.setImage("auxiliar2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar2.jpg"), auxiliar2);
        Images bisacrilico1Image= Utils.setImage("bisacrilico1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico1.jpg"), bisacrilico1);
        Images bisacrilico2Image= Utils.setImage("bisacrilico2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico2.jpg"), bisacrilico2);
        Images carrierBraket1Image= Utils.setImage("carrierBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\carrierBraket1.jpg"), carrierBraket1);
        Images cemento1Image= Utils.setImage("cemento1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\cemento1.jpg"), cemento1);
        Images cepilloOralImage= Utils.setImage("cepilloOralImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\CEPILLO-ORAL.jpg"), cepilloOral);
        Images compomero1Image= Utils.setImage("compomero1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\compomero1.png"), compomero1);
        Images deltaForceBraket1Image= Utils.setImage("deltaForceBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\dentalForceBraket1.jpg"), deltaForceBraket1);
        Images distalizador1Image= Utils.setImage("distalizador1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador1.jpg"), distalizador1);
        Images distalizador2Image= Utils.setImage("distalizador2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador2.jpg"), distalizador2);
        Images elastomeros1Image= Utils.setImage("elastomeros1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero1.jpg"), elastomeros1);
        Images elastomeros2Image= Utils.setImage("elastomeros2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero2.jpg"), elastomeros2);
        Images estandarBraket1Image= Utils.setImage("estandarBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket1.jpg"), estandarBraket1);
        Images estandarBraket2Image= Utils.setImage("estandarBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket2.jpg"), estandarBraket2);
        Images guantesLatexImage= Utils.setImage("guantesLatexImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\Guantes-latex.jpg"), guantesLatex);
        Images hojaBisturi1Image= Utils.setImage("hojaBisturi1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi1.jpg"), hojaBisturi1);
        Images hojaBisturi2Image= Utils.setImage("hojaBisturi2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi2.jpg"), hojaBisturi2);
        Images instrumentoOrtodoncia1Image= Utils.setImage("instrumentoOrtodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia1.jpg"), instrumentoOrtodoncia1);
        Images instrumentoOrtodoncia2Image= Utils.setImage("instrumentoOrtodoncia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia2.jpg"), instrumentoOrtodoncia2);
        Images jeringa1Image= Utils.setImage("jeringa1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa1.jpg"), jeringa1);
        Images jeringa2Image= Utils.setImage("jeringa2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa2.jpg"), jeringa2);
        Images mango1Image= Utils.setImage("mango1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango1.jpg"), mango1);
        Images mango2Image= Utils.setImage("mango2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango2.jpg"), mango2);
        Images mBTBraket1Image= Utils.setImage("MBTBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket1.jpg"), mBTBraket1);
        Images mBTBraket2Image= Utils.setImage("MBTBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket2.jpg"), mBTBraket2);
        Images ortopedia1Image= Utils.setImage("ortopedia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia1.jpg"), ortopedia1);
        Images ortopedia2Image= Utils.setImage("ortopedia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia2.jpg"), ortopedia2);
        Images pinza1Image= Utils.setImage("pinza1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza1.jpg"), pinzaDental1);
        Images pinza2Image= Utils.setImage("pinza2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza2.jpg"), pinzaDental2);
        Images pinza1Ortodoncia1Image= Utils.setImage("pinza1Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia1.jpg"), pinzaOrtodoncia1);
        Images pinza2Ortodoncia1Image= Utils.setImage("pinza2Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia2.jpg"), pinzaOrtodoncia2);
        Images portaaguja1Image= Utils.setImage("portaaguja1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja1.jpg"), portaAguja1);
        Images portaaguja2Image= Utils.setImage("portaaguja2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja2.jpg"), portaAguja2);
        Images protectorCepilloImage= Utils.setImage("protectorCepilloImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\protectorCepillo.jpg"), protectorCepillo);
        Images provisional1Image= Utils.setImage("provisional1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional1.jpg"), provisional1);
        Images provisional2Image= Utils.setImage("provisional2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional2.jpg"), provisional2);
        Images rebase1Image= Utils.setImage("rebase1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase1.png"), rebase1);
        Images rebase2Image= Utils.setImage("rebase2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase2.jpg"), rebase2);
        Images reconstructor1Image= Utils.setImage("reconstructor1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor1.jpg"), reconstructor1);
        Images reconstructor2Image= Utils.setImage("reconstructor2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor2.jpg"), reconstructor2);
        Images resina1Image= Utils.setImage("resina1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina1.jpg"), resina1);
        Images resina2Image= Utils.setImage("resina2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina2.jpg"), resina2);
        Images rothBraket1Image= Utils.setImage("rothBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket1.jpg"), rothBraket1);
        Images rothBraket2Image= Utils.setImage("rothBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket2.jpg"), rothBraket2);
        Images tigera1Image= Utils.setImage("tigera1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tigera1.jpg"), tigera1);
        Images tubo1Image= Utils.setImage("tubo1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo1.png"), tubo1);
        Images tubo2Image= Utils.setImage("tubo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo2.jpg"), tubo2);


        images.addAll(List.of(
            adhesivo1Image, adhesivo2Image, alambre1Image, alambre2Image, arco1Image, arco2Image, auxiliar1Image,
            auxiliar2Image, bisacrilico1Image, bisacrilico2Image, carrierBraket1Image, cemento1Image, cepilloOralImage,
            compomero1Image, deltaForceBraket1Image, distalizador1Image, distalizador2Image, elastomeros1Image,
            elastomeros2Image, estandarBraket1Image, estandarBraket2Image, guantesLatexImage, hojaBisturi1Image,
            hojaBisturi2Image, instrumentoOrtodoncia1Image, instrumentoOrtodoncia2Image, jeringa1Image, jeringa2Image,
            mango1Image, mango2Image, mBTBraket1Image, mBTBraket2Image, ortopedia1Image, ortopedia2Image, pinza1Image,
            pinza2Image, pinza1Ortodoncia1Image, pinza2Ortodoncia1Image, portaaguja1Image, portaaguja2Image,
            protectorCepilloImage, provisional1Image, provisional2Image, rebase1Image, rebase2Image, reconstructor1Image,
            reconstructor2Image, resina1Image, resina2Image, rothBraket1Image, rothBraket2Image, tigera1Image,
            tubo1Image, tubo2Image, adhesivo12Image, adhesivo13Image, adhesivo14Image
        ));

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

        imagesRep.saveAll(images);

        adhesivo1.setMainImage(adhesivo1Image);
        adhesivo2.setMainImage(adhesivo2Image);
        alambre1.setMainImage(alambre1Image);
        alambre2.setMainImage(alambre2Image);
        arco1.setMainImage(arco1Image);
        arco2.setMainImage(arco2Image);
        auxiliar1.setMainImage(auxiliar1Image);
        auxiliar2.setMainImage(auxiliar2Image);
        bisacrilico1.setMainImage(bisacrilico1Image);
        bisacrilico2.setMainImage(bisacrilico2Image);
        carrierBraket1.setMainImage(carrierBraket1Image);
        cemento1.setMainImage(cemento1Image);
        cepilloOral.setMainImage(cepilloOralImage);
        compomero1.setMainImage(compomero1Image);
        deltaForceBraket1.setMainImage(deltaForceBraket1Image);
        distalizador1.setMainImage(distalizador1Image);
        distalizador2.setMainImage(distalizador2Image);
        elastomeros1.setMainImage(elastomeros1Image);
        elastomeros2.setMainImage(elastomeros2Image);
        estandarBraket1.setMainImage(estandarBraket1Image);
        estandarBraket2.setMainImage(estandarBraket2Image);
        guantesLatex.setMainImage(guantesLatexImage);
        hojaBisturi1.setMainImage(hojaBisturi1Image);
        hojaBisturi2.setMainImage(hojaBisturi2Image);
        instrumentoOrtodoncia1.setMainImage(instrumentoOrtodoncia1Image);
        instrumentoOrtodoncia2.setMainImage(instrumentoOrtodoncia2Image);
        jeringa1.setMainImage(jeringa1Image);
        jeringa2.setMainImage(jeringa2Image);
        mango1.setMainImage(mango1Image);
        mango2.setMainImage(mango2Image);
        mBTBraket1.setMainImage(mBTBraket1Image);
        mBTBraket2.setMainImage(mBTBraket2Image);
        ortopedia1.setMainImage(ortopedia1Image);
        ortopedia2.setMainImage(ortopedia2Image);
        pinzaDental1.setMainImage(pinza1Image);
        pinzaDental2.setMainImage(pinza2Image);
        pinzaOrtodoncia1.setMainImage(pinza1Ortodoncia1Image);
        pinzaOrtodoncia1.setMainImage(pinza2Ortodoncia1Image);
        portaAguja1.setMainImage(portaaguja1Image);
        portaAguja2.setMainImage(portaaguja2Image);
        protectorCepillo.setMainImage(protectorCepilloImage);
        provisional1.setMainImage(provisional1Image);

        provisional2.setMainImage(provisional2Image);
        rebase1.setMainImage(rebase1Image);
        rebase2.setMainImage(rebase2Image);
        reconstructor1.setMainImage(reconstructor1Image);
        reconstructor2.setMainImage(reconstructor2Image);
        resina1.setMainImage(resina1Image);
        resina2.setMainImage(resina2Image);
        rothBraket1.setMainImage(rothBraket1Image);
        rothBraket2.setMainImage(rothBraket2Image);

        tigera1.setMainImage(tigera1Image);
        tubo1.setMainImage(tubo1Image);
        tubo2.setMainImage(tubo2Image);

        productsRep.saveAll(products);
    }   
}
