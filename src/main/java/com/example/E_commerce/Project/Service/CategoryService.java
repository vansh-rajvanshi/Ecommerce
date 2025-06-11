package com.example.E_commerce.Project.Service;

import com.cloudinary.Cloudinary;
import com.example.E_commerce.Project.Entity.Categories;
import com.example.E_commerce.Project.Entity.Product;
import com.example.E_commerce.Project.Repository.CategoriesRepository;
import com.example.E_commerce.Project.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private Cloudinary cloudinary;
    public List<Product> getProductsByCategoryId(int categoryId) {
        return categoriesRepository.findProductsByCategoryId(categoryId);
    }

    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }
    public List<Categories> getRandomCategories() {
        return categoriesRepository.findRandomCategories();
    }

    public Categories saveCategory(Categories categories) {
        return categoriesRepository.save(categories);
    }
    public void deleteCategory(int id){
        categoriesRepository.deleteById(id);
    }

    public Categories findById(int id){
        return categoriesRepository.findById(id).get(0);
    }
}
