package com.learning.iam.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/")
    public String home() {
        return "Welcome! <a href='/private'>Go to private page</a>";
    }

    @GetMapping("/private")
    public String privatePage(@AuthenticationPrincipal OidcUser user) {
        return "Hello " + user.getFullName() + "! Your email: " + user.getEmail();
    }

}
