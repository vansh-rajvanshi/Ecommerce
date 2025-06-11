package com.example.E_commerce.Project.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.E_commerce.Project.Entity.Product;
import com.example.E_commerce.Project.Entity.User;
import com.example.E_commerce.Project.Service.CategoryService;
import com.example.E_commerce.Project.Service.ProductService;
import com.example.E_commerce.Project.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Controller
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserServiceImpl userService;
    @GetMapping("/productdetail/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        model.addAttribute("navbar",categoryService.getAllCategories());
        Product product = productService.getProductById(id);
        if (product != null) {
            Product relatedProducts =productService.getProductByCategory(product.getCategory());
            model.addAttribute("product", product);
            model.addAttribute("related",relatedProducts);
            return "productdetail";
        } else {
            return "404";
        }
    }
    @GetMapping("/addProduct")
    public String product(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        model.addAttribute("addProduct",categoryService.getAllCategories());
        model.addAttribute("newProduct",new Product());
        return "addproduct";
    }
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("newProduct") Product product,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            // Validate the file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File is missing or empty.");
            }

            // Extract the original file name and ensure it's not null or empty
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("The file name is missing or empty.");
            }

            // Uploading to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", UUID.randomUUID().toString(), // Optional: Set a public ID if needed
                    "overwrite", true
            ));

            // Get the URL of the uploaded image
            String imageUrl = (String) uploadResult.get("url");
            product.setImageUrl(imageUrl); // Set the image URL in the product

            // Save the product to the database
            productService.saveProduct(product);

            // Add a success message to the model
            redirectAttributes.addFlashAttribute("success", "Product " + product.getName() + " added successfully!");

            // Redirect to the product list or a success page
            return "redirect:/viewProduct"; // Adjust the redirect path as needed
        }catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("error","File size exceeds the maximum limit of 20MB.");
            return "addProduct";
        } catch (Exception e) {
            // Add an error message to the model
            redirectAttributes.addFlashAttribute("error", "Failed to upload image or save product: " + e.getMessage());

            // Return to the form with the error message
            return "addProduct"; // Return to the add product form
        }
    }

    @GetMapping("/viewProduct")
    public String viewProduct(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        model.addAttribute("products", productService.getAll());
        return "viewProduct";
    }

    @GetMapping("/editProduct/{id}")
    public String update(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        model.addAttribute("update",new Product());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "EditProduct";
    }
    @PostMapping("/editProduct/{id}")
    public String updateProduct(@PathVariable int id,
                                @ModelAttribute Product product,
                                @RequestParam("file") MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        Product productToUpdate = productService.getProductById(id);
        try {
            // Check if the file is provided
            if (file != null && !file.isEmpty()) {
                // Validate the original file name
                String originalFilename = file.getOriginalFilename();
                if (originalFilename == null || originalFilename.isEmpty()) {
                    throw new IllegalArgumentException("The file name is missing or empty.");
                }

                // Uploading to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                        "public_id", UUID.randomUUID().toString(), // Optional: Set a public ID if needed
                        "overwrite", true
                ));

                // Get the URL of the uploaded image
                String imageUrl = (String) uploadResult.get("url");
                productToUpdate.setImageUrl(imageUrl); // Set the new image URL
            } else {
                // If no new file is provided, keep the existing image URL
                productToUpdate.setImageUrl(productToUpdate.getImageUrl());
            }

            // Update product details
            productToUpdate.setName(product.getName());
            productToUpdate.setDescription(product.getDescription());
            productToUpdate.setPrice(product.getPrice());
            productToUpdate.setOriginalPrice(product.getOriginalPrice());
            productToUpdate.setRating(product.getRating());
            productToUpdate.setProductType(product.getProductType());
            productToUpdate.setSpeakerType(product.getSpeakerType());
            productToUpdate.setBrand(product.getBrand());
            productToUpdate.setModelName(product.getModelName());
            productToUpdate.setSpecialFeature(product.getSpecialFeature());

            // Save the updated product
            productService.saveProduct(productToUpdate);

            // Add a success message to the model
            redirectAttributes.addFlashAttribute("success", "Product successfully updated!");

            // Redirect to the product list or a success page
            return "redirect:/viewProduct";
        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("error","File size exceeds the maximum limit of 20MB.");
            return "viewProduct";
        } catch (Exception e) {
            // Add an error message to the model
            redirectAttributes.addFlashAttribute("error", "Failed to update the product: " + e.getMessage());

            // Return to the form with the error message
            return "viewProduct"; // Redirect back to the view page if there's an error
        }
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id,RedirectAttributes redirectAttributes){
        try{
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success","Product delete successfully!!!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error","Product is not deleted!!");
        }
        return "redirect:/viewProduct";
    }
    @GetMapping("/allProduct")
    public String getALLProduct(Model model){
        model.addAttribute("navbar",categoryService.getAllCategories());
        model.addAttribute("product",productService.getAll());
        return "allProduct";
    }
}