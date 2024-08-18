package com.noobisoftcontrolcenter.needfortoken;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@ApiOperation("Login controller")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @ApiOperation("Login endpoint")
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String accountId) {
        User user = userRepository.findById(email).orElse(null);

        if (user != null && user.getAccountId().equals(accountId)) {
            // Ideally, you'd return a JWT token or some session identifier here
            return "Login successful for user: " + email;
        } else {
            return "Invalid email or account ID";
        }
    }
}