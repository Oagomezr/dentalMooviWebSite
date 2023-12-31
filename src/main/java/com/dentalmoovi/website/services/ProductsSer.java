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

import com.dentalmoovi.website.Utils;
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

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductsSer {
    
    private final ProductsRep productsRep;
    private final CategoriesRep categoriesRep;
    private final ImgRep imagesRep;
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
                Collections.sort(allProducts, (product1, product2) -> product1.getName().compareTo(product2.getName()));

                /*We cannot show the costumer N amount of products if N is a high number,
                so we need to do pagination*/
                int startIndex = (currentPage - 1) * productsPerPage;
                int endIndex = Math.min(startIndex + productsPerPage, allProducts.size());
                List<Products> currentPageProducts = allProducts.subList(startIndex, endIndex);
                
                //Classic DTO
                List<ProductsDTO> productsDTO = convertToProductsDTOList(currentPageProducts, all);

                /*One of the best practices in programming is not send List or Arrays as response,
                instead Objects as response*/
                ProductsResponse productsResponse = new ProductsResponse();
                productsResponse.setTotalProducts(allProducts.size());
                productsResponse.setPaginatedProducts(productsDTO.size());
                productsResponse.setData(productsDTO);
                
                return productsResponse;
            }

            //Get all subcategories
            private List<String> getNamesCategories(Categories parentCategory) {
                List<Categories> subCategories = categoriesRep.findByParentCategory(parentCategory.getId());
                List<String> subcategoriesNames = new ArrayList<>();
                subCategories.stream().forEach(subCategory ->{
                    subcategoriesNames.add(subCategory.getName());
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

                if (!admin && !product.isOpenToPublic())
                    throw new RuntimeException("Something wrong");
                
                //Get product's category
                Categories category = categoriesRep.findById(product.getIdCategory())
                    .orElseThrow(() -> new RuntimeException(categoryNotFound));

                //Get location producto since parent category until its subcategory
                List<String> location = getLocationProduct(category);

                //Get Product images
                List<ImagesDTO> productImagesDTO = getProductImages(product, true);

                ProductsDTO productsDTO = setProductDTO(name, product.getUnitPrice(), product.getDescription(), 
                                            product.getShortDescription(), product.getStock(), productImagesDTO);
                productsDTO.setLocation(location);

                if (admin && !product.isOpenToPublic())
                    productsDTO.setHidden("yes");

                return productsDTO;
            }

            //It's a function with the aim of find the location products inside the categories
            private List<String> getLocationProduct(Categories category){
                List<String> location = new ArrayList<>(List.of(category.getName()));
                if(category.getIdParentCategory() != null) 
                    location.addAll(getLocationProduct(
                        categoriesRep.findById(category.getIdParentCategory())
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
            ProductsResponse productsResponse = new ProductsResponse();
            productsResponse.setTotalProducts(productsDTO.size());
            productsResponse.setPaginatedProducts(productsDTO.size());
            productsResponse.setData(productsDTO);
            return productsResponse;

        }

        //Get all query results
        List<Products> allProductsFound = productsRep.findByNameContaining(name, productsPerPage, ((currentPage-1)*productsPerPage));

        //Convert those results in DTOs
        List<ProductsDTO> productsDTO = convertToProductsDTOList(allProductsFound, all);

        //Put the DTOs inside the Object
        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setTotalProducts(productsRep.countProductsByContaining(name));
        productsResponse.setPaginatedProducts(productsDTO.size());
        productsResponse.setData(productsDTO);
        return productsResponse;
    }

    @CacheEvict(cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, allEntries = true)
    public MessageDTO updateMainImage(long idImage, String productName){
        Products product = productsRep.findByName(productName)
            .orElseThrow(() -> new RuntimeException(productNotFound));
        product.setIdMainImage(idImage);
        productsRep.save(product);
        return new MessageDTO("Main product image updated");
    }

    @CacheEvict(cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, allEntries = true)
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
                Images newImage = Utils.setImage(originalFileName, contentType, imageData, product.getId(), imagesRep);
        
                // Set the new image as the main image if there is no main image assigned
                if (product.getIdMainImage() == null) {
                    product.setIdMainImage(newImage.getId());
                    productsRep.save(product);
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

    @CacheEvict(cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, allEntries = true)
    public MessageDTO deleteImage(String parameter){
        if (parameter.matches(".*[a-zA-Z].*")) {
            Products product = productsRep.findByName(parameter)
                .orElseThrow(() -> new RuntimeException(productNotFound));
            long idImage = product.getIdMainImage();
            product.setIdMainImage(null);
            productsRep.save(product);
            imagesRep.deleteById(idImage);
        }else{
            long idImage = Long.parseLong(parameter);
            imagesRep.deleteById(idImage);
        }

        return new MessageDTO("Image deleted");
    }

    @CacheEvict(cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, allEntries = true)
    public MessageDTO hideOrShowProduct(boolean visibility, String productName){
        Products product = productsRep.findByName(productName)
            .orElseThrow(() -> new RuntimeException(productNotFound));
        product.setOpenToPublic(visibility);
        productsRep.save(product);
        return new MessageDTO("Product Updated");
    }

    @CacheEvict(cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, allEntries = true)
    public MessageDTO updateProductInfo(int option, String nameProduct, String newInfo){
        // Find product
        Products product = productsRep.findByName(nameProduct)
            .orElseThrow(() -> new RuntimeException(productNotFound));
        switch (option) {
            case 0:
                product.setName(newInfo);
                productsRep.save(product);
            break;
            case 1:
                product.setUnitPrice(Double.parseDouble(newInfo));
                productsRep.save(product);
            break;
            case 2:
                product.setDescription(newInfo);
                productsRep.save(product);
            break;
            case 3:
                product.setStock(Integer.parseInt(newInfo));
                productsRep.save(product);
            break;
            case 4:
                product.setShortDescription(newInfo);
                productsRep.save(product);
            break;
        
            default:
                throw new RuntimeException("Invalid option");
        }
        return new MessageDTO("Info updated");
    }

    @CacheEvict(cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, allEntries = true)
    public Boolean createProduct(String categoryName){

        Categories category = categoriesRep.findByName(categoryName)
            .orElseThrow(() -> new RuntimeException(productNotFound));

        if (Boolean.TRUE.equals(productsRep.existsByName("Nombre del nuevo producto"))) {
            Products product = productsRep.findByName("Nombre del nuevo producto")
                .orElseThrow(() -> new RuntimeException(productNotFound));
            product.setIdCategory(category.getId());
            productsRep.save(product);
            return false;
        }
        
        Utils.setProduct("Nombre del nuevo producto", "Descripción del nuevo producto", "descripción corta del nuevo producto", 0, 0, category.getId(), false, productsRep);
        return true;
    }

    //This only converts our database data to DTOs
    private List<ProductsDTO> convertToProductsDTOList(List<Products> productsList, boolean all) {

        List<ProductsDTO> productsDTOList = new ArrayList<>();
        
        productsList.stream().forEach(product ->{
            try {
                if(product.isOpenToPublic() || all){
                    List<ImagesDTO> productImagesDTO = getProductImages(product, false);
                    ProductsDTO productDTO = setProductDTO(product.getName(), product.getUnitPrice(), 
                        product.getDescription(), product.getShortDescription(), product.getStock(), productImagesDTO);
                    if (!product.isOpenToPublic()) productDTO.setHidden("Yes");
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
        if(product.getIdMainImage() == null) return productImagesDTO;

        Images mainImage = imagesRep.findById(product.getIdMainImage())
                .orElseThrow(() -> new RuntimeException("Image not found"));

        if(allImages){
            List<Images> productImages = imagesRep.findByIdProduct(product.getId());
            
            productImages.stream().forEach(productImage ->{
                long idImage = productImage.getId();
                String imgName = productImage.getName();
                String contentType = productImage.getContentType();
                String base64Image = Base64.getEncoder().encodeToString(productImage.getData());
                ImagesDTO imageDTO = setImageDTO(idImage ,imgName, contentType, base64Image);

                if (mainImage.equals(productImage)) productImagesDTO.add(0, imageDTO);
                else productImagesDTO.add(imageDTO);
            });
            return productImagesDTO;
        }

        long idMainImage = mainImage.getId();
        String imgName = mainImage.getName();
        String contentType = mainImage.getContentType();
        String base64Image = Base64.getEncoder().encodeToString(mainImage.getData());
        ImagesDTO imageDTO = setImageDTO(idMainImage , imgName, contentType, base64Image);
        productImagesDTO.add(imageDTO);
        return productImagesDTO;
    }

    private ImagesDTO setImageDTO(long id ,String name, String contenType, String base64){
        ImagesDTO imageDTO = new ImagesDTO();
        imageDTO.setId(id);
        imageDTO.setName(name);
        imageDTO.setContentType(contenType);
        imageDTO.setImageBase64(base64);
        return imageDTO;
    }

    private ProductsDTO setProductDTO(String name, double unitPrice, String description, 
                                        String shortDescription ,int stock, List<ImagesDTO> imagesDTO){
        ProductsDTO productDTO = new ProductsDTO();
        productDTO.setNameProduct(name);
        productDTO.setUnitPrice(unitPrice);
        productDTO.setDescription(description);
        productDTO.setShortDescription(shortDescription);
        productDTO.setStock(stock);
        productDTO.setImages(imagesDTO);
        return productDTO;
    }

    
}
