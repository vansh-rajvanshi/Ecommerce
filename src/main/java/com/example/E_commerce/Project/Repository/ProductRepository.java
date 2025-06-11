package com.example.E_commerce.Project.Repository;

import com.example.E_commerce.Project.Entity.Categories;
import com.example.E_commerce.Project.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT p FROM Product p WHERE p.productType = 'featured'")
    List<Product> findFeaturedProduct();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findById(@Param("id") int id);

    Optional<Product>findByCategory(Categories categories);

    List<Product> findAll();

    void deleteById(int id);
}
