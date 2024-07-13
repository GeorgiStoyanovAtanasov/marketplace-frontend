package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.HttpURLConnection;

@Controller
public class AuthController {
    private final AuthClient authClient;

    @Autowired
    public AuthController(AuthClient authClient){
        this.authClient = authClient;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginUser", new LoginUserDto());
        return "login";
    }
    @PostMapping("/login")
    public String createSession(@ModelAttribute LoginUserDto loginUserDto, HttpSession httpSession){
//bez tova dava to many redirects error
        java.net.CookieManager cm = new java.net.CookieManager();
        java.net.CookieHandler.setDefault(cm);

        String token = authClient.authenticate(loginUserDto).getBody().getToken();

        httpSession.setAttribute("session", token);

        return "redirect:/events";
    }

}
