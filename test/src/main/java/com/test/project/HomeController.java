package com.test.project;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println(authentication.getName());
            System.out.println(authentication.getPrincipal());

        }
        return "home";
    }



    @RequestMapping("/map")
    public String getMap(){
        return "map/map";
    }
}
