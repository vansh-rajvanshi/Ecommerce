package com.example.E_commerce.Project.Controller;

import com.example.E_commerce.Project.Service.CategoryService;
import com.example.E_commerce.Project.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @GetMapping("/")
    public String getAllCategory(Model model) {
        model.addAttribute("navbar", categoryService.getAllCategories());
        model.addAttribute("category", categoryService.getRandomCategories());
        model.addAttribute("featuredproducts", productService.getFeatured());
        return "index";
    }
    @GetMapping("/home")
    public String homePage(Model model){
        model.addAttribute("navbar",categoryService.getAllCategories());
        model.addAttribute("category",categoryService.getRandomCategories());
        model.addAttribute("featuredproducts", productService.getFeatured());
        return "home";
    }
    @GetMapping("/contact")
    public String contact(Model model){
        model.addAttribute("navbar",categoryService.getAllCategories());
        return "contact";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("navbar",categoryService.getAllCategories());
        return "aboutus";
    }
    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("navbar",categoryService.getAllCategories());
        return "faq";
    }
    @GetMapping("/errorpage")
    public String errorPage() {
        return "ErrorPage";
    }
    @GetMapping("/verification")
    public String verify(){
        return "verification";
    }

}
