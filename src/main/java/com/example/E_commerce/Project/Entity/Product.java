package com.example.E_commerce.Project.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private double originalPrice;
    private double rating;
    private String connectivityTechnology;
    private String productType;
    private String speakerType;
    private String brand;
    private String modelName;
    private String specialFeature;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "cat_id")
    private Categories category;

}