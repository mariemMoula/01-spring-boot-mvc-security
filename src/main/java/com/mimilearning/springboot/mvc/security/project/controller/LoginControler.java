package com.mimilearning.springboot.mvc.security.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControler {
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "fancy-login";
    }

    // add request mapping for /acccess-denied
    @GetMapping("/access-denied")
    public String showAaccessDenied() {
        return "access-denied";
    }

}
