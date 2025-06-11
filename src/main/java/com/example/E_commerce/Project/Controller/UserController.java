package com.example.E_commerce.Project.Controller;

import com.example.E_commerce.Project.Entity.User;
import com.example.E_commerce.Project.Repository.RoleRepository;
import com.example.E_commerce.Project.Service.CategoryService;
import com.example.E_commerce.Project.Service.OtpService;
import com.example.E_commerce.Project.Service.ProductService;
import com.example.E_commerce.Project.Service.UserService;
import com.example.E_commerce.Project.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    private final ConcurrentHashMap<String, UserDto> temporaryUserStore = new ConcurrentHashMap<>();

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model) {
        // Check if email already exists
        if (userService.checkIfEmailExists(userDto.getEmail())) {
            model.addAttribute("error", "Email already exists. Please try another.");
            return "register";
        }

        // Store user temporarily before OTP verification
        temporaryUserStore.put(userDto.getEmail(), userDto);
        boolean otpSent = otpService.sendOtp(userDto.getEmail());
        if (!otpSent) {
            model.addAttribute("error", "Failed to send OTP. Please try again.");
            return "register";
        }

        // Redirect to OTP verification page
        model.addAttribute("email", userDto.getEmail());
        return "redirect:/register/otp-verification?email=" + userDto.getEmail();
    }

    // Display OTP verification page
    @GetMapping("/register/otp-verification")
    public String showOtpVerificationPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "otp-verification";
    }

    // Handle OTP verification
    @PostMapping("/register/otp-verify")
    public String verifyOtp(@RequestParam("email") String email,
                            @RequestParam("otp1") String otp1,
                            @RequestParam("otp2") String otp2,
                            @RequestParam("otp3") String otp3,
                            @RequestParam("otp4") String otp4,
                            @RequestParam("otp5") String otp5,
                            @RequestParam("otp6") String otp6,
                            Model model) {
        String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;

        boolean isVerified = otpService.verifyOtp(email, otp);
        if (isVerified) {
            UserDto userDto = temporaryUserStore.remove(email);  // Retrieve and remove the user data from temporary store
            userService.saveUser(userDto);  // Save the user in the database

            model.addAttribute("success", "OTP Verified Successfully!");
            return "redirect:/login"; // Redirect to dashboard or homepage
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            model.addAttribute("email", email);
            return "otp-verification";
        }
    }

    @PostMapping("/register/resend-otp")
    public String resendOtp(@RequestParam("email") String email, Model model) {
        if (!temporaryUserStore.containsKey(email)) {
            model.addAttribute("error", "Session expired or invalid email.");
            return "otp-verification";
        }
        boolean otpSent = otpService.sendOtp(email);
        if (otpSent) {
            model.addAttribute("success", "OTP has been resent successfully!");
        } else {
            model.addAttribute("error", "Failed to resend OTP. Please try again.");
        }

        model.addAttribute("email", email);
        return "otp-verification";
    }


    //Login Part
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/success")
    public String loginSuccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .findFirst()
                .orElse("");

        if ("ROLE_ADMIN".equals(role)) {
            return "redirect:/admin";
        } else if ("ROLE_USER".equals(role)) {
            return "redirect:/home";
        }

        return "redirect:/index";
    }


    // Forget Password
    @GetMapping("/forgetPassword")
    public String forgetPassword() {
        return "forgetPassword";
    }

    @PostMapping("/forgetPassword/check")
    public String check(@ModelAttribute("email") String email, Model model) {
        if (userService.checkIfEmailExists(email)) {
            boolean otpSent = otpService.sendOtp(email);
            if (!otpSent) {
                model.addAttribute("error", "Failed to send OTP. Please try again.");
                return "forgetPassword";
            }
            return "redirect:/forgetPassword/otpVerify?email=" + email;
        } else {
            model.addAttribute("error", "Email not found. Please check and try again.");
            return "forgetPassword";
        }
    }
    @GetMapping("/forgetPassword/otpVerify")
    public String showOtpVerificationPage1(@RequestParam("email") String email, Model model) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Invalid or missing email.");
            return "forgetPassword";
        }
        model.addAttribute("email", email);
        return "otpVerify";
    }

    @PostMapping("/forgetPassword/otp-verify")
    public String verifyOtp1(@RequestParam("email") String email,
                             @RequestParam("otp1") String otp1,
                             @RequestParam("otp2") String otp2,
                             @RequestParam("otp3") String otp3,
                             @RequestParam("otp4") String otp4,
                             @RequestParam("otp5") String otp5,
                             @RequestParam("otp6") String otp6,
                             Model model) {
        String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;
        boolean isVerified = otpService.verifyOtp(email, otp);
        if (isVerified) {
            model.addAttribute("success", "OTP Verified Successfully!");
            return "redirect:/new-password?email=" + email;
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            model.addAttribute("email", email);
            return "otpVerify";
        }
    }
    @GetMapping("/new-password")
    public String newPassword(@RequestParam("email") String email, Model model) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Invalid or missing email.");
            return "otpVerify";
        }
        model.addAttribute("email", email);
        return "new-password";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 Model model) {
        boolean isUpdated = userService.updatePassword(email, password);
        if (isUpdated) {
            model.addAttribute("success", "Password successfully updated.");
            return "redirect:/login";
        } else {
            // If the password update fails, show an error message on the same page
            model.addAttribute("error", "Failed to update password. Please try again.");
            model.addAttribute("email", email); // Retain email in the form
            return "new-password";
        }
    }
    @PostMapping("/forgetPassword/resend-otp")
    public String resendOtp1(@RequestParam("email") String email, Model model) {
        boolean otpSent = otpService.sendOtp(email);
        if (otpSent) {
            model.addAttribute("success", "OTP has been resent successfully!");
        } else {
            model.addAttribute("error", "Failed to resend OTP. Please try again.");
        }

        model.addAttribute("email", email);
        return "otpVerify";
    }
    @GetMapping("/users")
    public String viewUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "viewUsers";
    }
    @GetMapping("/userProfile")
    public String userProfile(Model model) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Username is the email in your case

        // Fetch user details from the repository
        User user =userService.findUserByEmail(email);

        if (user != null) {
            model.addAttribute("user", user); // Add user details to the model
        } else {
            model.addAttribute("error", "User not found");
        }
        return "userProfile";
    }
}
