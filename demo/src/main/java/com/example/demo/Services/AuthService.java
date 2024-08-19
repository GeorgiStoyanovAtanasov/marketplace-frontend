package com.example.demo.Services;

import com.example.demo.clients.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuthService {
    UserClient userClient;

    @Autowired
    public AuthService(UserClient userClient) {
        this.userClient = userClient;
    }

    public void getRoles(Model model) {
        List<String> roles = new ArrayList<>();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        model.addAttribute("roles", roles);
        if (session != null) {
            String token = (String) session.getAttribute("session");
            roles = userClient.getRoles(token).getBody();
            model.addAttribute("roles", roles);
        }
    }

    public List<String> getAuthorities() {
        List<String> roles = new ArrayList<>();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        if(session == null){
            return Collections.emptyList();
        }
        String token = (String) session.getAttribute("session");
        roles = userClient.getRoles(token).getBody();
        return roles;
    }

    public boolean hasSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        return session != null;
    }
}
