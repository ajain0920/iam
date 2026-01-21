package com.learning.iam.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {

    @GetMapping("/")
    public String home() {
        return "Welcome! <br>" +
                "<a href='/billing/invoice'>Go to invoice page</a> <br>" +
                "<a href='/admin/approve'>Go to payment approval page</a>";
    }


    @GetMapping("/billing/invoice")
    @PreAuthorize("hasAuthority('SCOPE_billing.read')")
    public String getInvoice() {
        return "Invoice details";
    }

    @PostMapping("/admin/approve")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String approvePayment() {
        // return "Hello " + user.getFullName() + "! email: " + user.getEmail() + "<br>" +
        return "Payment approved";
    }
}
