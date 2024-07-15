package com.example.demo.Services;

import com.example.demo.clients.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private UserClient userClient;
    public List<String> getRolesForUser(String token){
        return userClient.getRoles(token).getBody();
    }
}
