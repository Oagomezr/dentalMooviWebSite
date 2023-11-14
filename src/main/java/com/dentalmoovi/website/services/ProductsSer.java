package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dentalmoovi.website.models.dtos.ImagesDTO;
import com.dentalmoovi.website.models.dtos.ProductsDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.responses.ProductsResponse;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ProductsRep;

@Service
public class ProductsSer {
    
    private final ProductsRep productsRep;
    private final CategoriesRep categoriesRep;

    public ProductsSer(ProductsRep productsRep, CategoriesRep categoriesRep){
        this.productsRep = productsRep;
        this.categoriesRep = categoriesRep;
    }

    public ProductsResponse getProductsByCategory(String parentCategoryName, int currentPage, int productsPerPage){
        class GetProductsByCategory{
            ProductsResponse getProductsByCategory() {

                //Search category inside database
                Categories parentCategory = categoriesRep.findByName(parentCategoryName)
                        .orElseThrow(() -> new RuntimeException("Category not found"));
            
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
                List<ProductsDTO> productsDTO = convertToProductsDTOList(currentPageProducts);

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
                List<Categories> subCategories = categoriesRep.findByParentCategoryOrderByName(parentCategory);
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

    public ProductsDTO getProduct(String name){

        class GetProduct{
            ProductsDTO getProduct(){

                //Search product
                Products product = productsRep.findByName(name)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
                //Get product's category
                Categories category = product.getCategory();

                //Get location producto since parent category until its subcategory
                List<String> location = getLocationProduct(category);

                //Get Product images
                List<ImagesDTO> productImagesDTO = getProductImages(product, true);

                ProductsDTO productsDTO = setProductDTO(name, product.getUnitPrice(), product.getDescription(), 
                                                product.getStock(), productImagesDTO);
                productsDTO.setLocation(location);

                return productsDTO;
            }

            //It's a function with the aim of find the location products inside the categories
            private List<String> getLocationProduct(Categories category){
                List<String> location = new ArrayList<>(List.of(category.getName()));
                if(category.getParentCategory() != null) location.addAll(getLocationProduct(category.getParentCategory()));
                return location;
            }
        }

        GetProduct innerClass = new GetProduct();
        return innerClass.getProduct();
    }

    public ProductsResponse getProductsByContaining(String name, boolean limit, int currentPage, int productsPerPage){
        List<Products> productsFound;

        //if the user wants to obtain only the 7 first query results  or all results
        if(limit){

            //Get the first 7 query results from database
            productsFound = productsRep.findTop7ByNameContainingIgnoreCase(name);

            //Convert those results in DTOs
            List<ProductsDTO> productsDTO = convertToProductsDTOList(productsFound);

            //Put the DTOs inside the Object
            ProductsResponse productsResponse = new ProductsResponse();
            productsResponse.setTotalProducts(productsDTO.size());
            productsResponse.setPaginatedProducts(productsDTO.size());
            productsResponse.setData(productsDTO);
            return productsResponse;

        }

        //Get all query results
        List<Products> allProductsFound = productsRep.findByNameContainingIgnoreCase(name);

        //Do pagination
        int startIndex = (currentPage - 1) * productsPerPage;
        int endIndex = Math.min(startIndex + productsPerPage, allProductsFound.size());
        productsFound = allProductsFound.subList(startIndex, endIndex);

        //Convert those results in DTOs
        List<ProductsDTO> productsDTO = convertToProductsDTOList(productsFound);

        //Put the DTOs inside the Object
        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setTotalProducts(allProductsFound.size());
        productsResponse.setPaginatedProducts(productsDTO.size());
        productsResponse.setData(productsDTO);
        return productsResponse;
    }

    //This only converts our database data to DTOs
    private List<ProductsDTO> convertToProductsDTOList(List<Products> productsList) {
        List<ProductsDTO> productsDTOList = new ArrayList<>();
    
        productsList.stream().forEach(product ->{
            if(product.isOpenToPublic()){
                List<ImagesDTO> productImagesDTO = getProductImages(product, false);
                ProductsDTO productDTO = setProductDTO(product.getName(), product.getUnitPrice(), 
                                            product.getDescription(), product.getStock(), productImagesDTO);
                productsDTOList.add(productDTO);
            }
        });
    
        return productsDTOList;
    }

    //This function allow us to find one or all product images
    private List<ImagesDTO> getProductImages(Products product, boolean allImages){
        List<ImagesDTO> productImagesDTO = new ArrayList<>();
        if(product.getMainImage() == null) return productImagesDTO;

        if(allImages){
            product.getImages().stream().forEach(productImage ->{
                String imgName = productImage.getName();
                String contentType = productImage.getContentType();
                String base64Image = Base64.getEncoder().encodeToString(productImage.getData());
                ImagesDTO imageDTO = setImageDTO(imgName, contentType, base64Image);

                if (product.getMainImage() == productImage) productImagesDTO.add(0, imageDTO);
                else productImagesDTO.add(imageDTO);
            });
            return productImagesDTO;
        }

        String imgName = product.getMainImage().getName();
        String contentType = product.getMainImage().getContentType();
        String base64Image = Base64.getEncoder().encodeToString(product.getMainImage().getData());
        ImagesDTO imageDTO = setImageDTO(imgName, contentType, base64Image);
        productImagesDTO.add(imageDTO);
        return productImagesDTO;
    }

    private ImagesDTO setImageDTO(String name, String contenType, String base64){
        ImagesDTO imageDTO = new ImagesDTO();
        imageDTO.setName(name);
        imageDTO.setContentType(contenType);
        imageDTO.setImageBase64(base64);
        return imageDTO;
    }

    private ProductsDTO setProductDTO(String name, double unitPrice, String description, 
                                        int stock, List<ImagesDTO> imagesDTO){
        ProductsDTO productDTO = new ProductsDTO();
        productDTO.setNameProduct(name);
        productDTO.setUnitPrice(unitPrice);
        productDTO.setDescription(description);
        productDTO.setStock(stock);
        productDTO.setImages(imagesDTO);
        return productDTO;
    }
}
