package com.example.E_commerce.Project.Service;

import com.example.E_commerce.Project.Entity.Categories;
import com.example.E_commerce.Project.Entity.Product;
import com.example.E_commerce.Project.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> getFeatured(){
        return productRepository.findFeaturedProduct();
    }

    public Product getProductById(int id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }
    public Product getProductByCategory(Categories categories){
        Optional<Product> product=productRepository.findByCategory(categories);
        return product.orElse(null);
    }
    public Product saveProduct(Product product){
        return  productRepository.save(product);
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }
}
