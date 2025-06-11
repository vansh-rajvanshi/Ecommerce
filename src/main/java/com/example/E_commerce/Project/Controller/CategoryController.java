package com.example.E_commerce.Project.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.E_commerce.Project.Entity.Categories;
import com.example.E_commerce.Project.Entity.Product;
import com.example.E_commerce.Project.Entity.User;
import com.example.E_commerce.Project.Service.CategoryService;
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

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/category")
    public String showAddCategoryForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        model.addAttribute("newCategory", new Categories());
        return "addcategory";
    }
    @GetMapping("/categorywise/{id}")
    public String getRelatedProducts(@PathVariable("id") int categoryId,Model model) {
        List<Product> relatedProducts = categoryService.getProductsByCategoryId(categoryId);
        model.addAttribute("navbar",categoryService.getAllCategories());
        model.addAttribute("category",relatedProducts);
        return "categorywise";
    }

    @PostMapping("/addNewCategory")
    public String addCategory(
            @ModelAttribute("newCategory") Categories categories,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is missing or empty.");
            }

            String uniqueFileName = UUID.randomUUID().toString();
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("public_id", uniqueFileName));

            String imageUrl = (String) uploadResult.get("url");
            categories.setCat_img(imageUrl);
            categoryService.saveCategory(categories);

            redirectAttributes.addFlashAttribute("success", "Category " + categories.getCat_name() + " added successfully!");
            return "redirect:/category";

        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("error","File size exceeds the maximum limit of 20MB.");
            return "addcategory";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "File upload failed: " + e.getMessage());
            return "addcategory";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Validation Error: " + e.getMessage());
            return "addcategory";
        }
    }

    @GetMapping("/viewcategory")
    public String viewCategory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        List<Categories> categories = categoryService.getAllCategories();

        // Ensure each category's image URL is accessible in the view
        model.addAttribute("categories", categories);

        return "viewcategory";
    }
    @GetMapping("/editCategory/{id}")
    public String update(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        List<Categories> categories = categoryService.getAllCategories();
        model.addAttribute("update",new Categories());
        return "EditCategory";
    }
    @PostMapping("/editCategory/{id}")
    public String updateCategory(
            @PathVariable int id,
            @ModelAttribute Categories categories,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        Categories categories1 = categoryService.findById(id);
        try {
            // Validate file presence
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File is missing or empty.");
            }

            // Uploading to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", UUID.randomUUID().toString(),
                    "overwrite", true
            ));

            // Get the URL of the uploaded image
            String imageUrl = (String) uploadResult.get("url");
            categories1.setCat_img(imageUrl);
            categories1.setCat_name(categories.getCat_name());
            categoryService.saveCategory(categories1);

            // Set success message
            redirectAttributes.addFlashAttribute("success", "Category " + categories.getCat_name() + " updated successfully!");
            return "redirect:/viewcategory";

        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("error","File size exceeds the maximum limit of 20MB.");
        }catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Validation Error: " + e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "File Handling Error: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update the Category!!");
        }

        return "redirect:/viewcategory";
    }
    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id,RedirectAttributes redirectAttributes){
        try{
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("success", "Category is deleted successfully!");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Failed to delete the Category ");
        }
        return "redirect:/viewcategory";
    }
}
