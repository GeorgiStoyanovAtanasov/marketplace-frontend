package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.HttpURLConnection;

@Controller
public class AuthController {
    private final AuthClient authClient;

    @Autowired
    public AuthController(AuthClient authClient){
        this.authClient = authClient;
    }

    @GetMapping("/authentication/login")
    public String login(Model model){
        model.addAttribute("loginUser", new LoginUserDto());
        return "login";
    }
    @PostMapping("/authentication/login")
    public String createSession(@ModelAttribute LoginUserDto loginUserDto, HttpSession httpSession){
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
    public String logout(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);

        session.removeAttribute("session");
        return "redirect:/authentication/login";
    }
}
