package com.example.E_commerce.Project.Repository;

import com.example.E_commerce.Project.Entity.Categories;
import com.example.E_commerce.Project.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoriesRepository extends JpaRepository<Categories,Integer> {
    List<Categories> findAll();

    List<Categories> findById(int id);
    @Query(value = "SELECT * FROM categories ORDER BY RANDOM() LIMIT 4", nativeQuery = true)
    List<Categories> findRandomCategories();

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findProductsByCategoryId(@Param("categoryId") int categoryId);
}
