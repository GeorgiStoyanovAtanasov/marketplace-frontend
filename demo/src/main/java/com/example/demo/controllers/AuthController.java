package com.example.demo.controllers;

import com.example.demo.clients.ManagerClient;
import com.example.demo.dtos.LoginUserDto;
import com.example.demo.dtos.ManagerDTO;
import com.example.demo.dtos.RegisterUserDto;
import com.example.demo.clients.AuthClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final AuthClient authClient;
    private ManagerClient managerClient;

    @Autowired
    public AuthController(AuthClient authClient, ManagerClient managerClient) {
        this.authClient = authClient;
        this.managerClient = managerClient;
    }

    @GetMapping("/user/register")
    public String registerForm(Model model) {
        model.addAttribute("userRegister", new RegisterUserDto());
        return "user/register";
    }

    @PostMapping("/user/register")
    public String register(@Valid @ModelAttribute RegisterUserDto registerUserDto) {
        if (!(registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword()))) {
            return "redirect:/user/register";
        }
       if(!authClient.register(registerUserDto)){
           return "redirect:/user/register";
       }
        return "redirect:/authentication/login";
    }

    @GetMapping("/authentication/login")
    public String login(Model model) {
        model.addAttribute("loginUser", new LoginUserDto());
        return "login";
    }

    @PostMapping("/authentication/login")
    public String createSession(@ModelAttribute LoginUserDto loginUserDto, HttpSession httpSession) {
        try {
//bez tova dava to many redirects error
            java.net.CookieManager cm = new java.net.CookieManager();
            java.net.CookieHandler.setDefault(cm);

            String token = authClient.authenticate(loginUserDto).getBody().getToken();
            httpSession.setAttribute("session", token);
            return "redirect:/events";
        } catch (Exception e){
            return "redirect:/authentication/login";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // Retrieve the session; do not create a new one if it doesn't exist
        HttpSession session = request.getSession(false);

        // Invalidate the session if it exists
        if (session != null) {
            session.invalidate();
        }

        // Redirect the user to the events page or wherever you'd like after logout
        return "redirect:/home";
    }

    @GetMapping("/manager/register")
    public String managerRegisterForm(Model model) {
        model.addAttribute("managerRegister", new RegisterUserDto());
        return "manager/register";
    }

    @PostMapping("/manager/register")
    public String registerManager(@Valid @ModelAttribute RegisterUserDto registerUserDto, RedirectAttributes redirectAttributes) {
        if (!(registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword()))) {
            return "redirect:/manager/register";
        }
        ManagerDTO managerDTO = managerClient.registerManager(registerUserDto).getBody();
        return "redirect:/authentication/login";
    }
}
