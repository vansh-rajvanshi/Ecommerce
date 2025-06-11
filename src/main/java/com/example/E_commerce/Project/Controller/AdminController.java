package com.example.E_commerce.Project.Controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.E_commerce.Project.Entity.Categories;
import com.example.E_commerce.Project.Entity.User;
import com.example.E_commerce.Project.Service.CategoryService;
import com.example.E_commerce.Project.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class AdminController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private Cloudinary cloudinary;


    @GetMapping("/admin")
    public String admin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        return "admin";
    }
    @GetMapping("/profile")
    public String Profile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        return "profile";
    }
}
