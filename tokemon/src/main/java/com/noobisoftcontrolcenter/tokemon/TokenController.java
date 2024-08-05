package com.noobisoftcontrolcenter.tokemon;

import com.hedera.hashgraph.sdk.TokenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private TokenService tokenService;

    @GetMapping("/createToken")
    public String createToken(
            @RequestParam String tokenName,
            @RequestParam String tokenSymbol,
            @RequestParam long initialSupply) {
        try {
            TokenId newTokenId = tokenService.createToken(tokenName, tokenSymbol, initialSupply);
            logger.info("Token created successfully with ID: {}", newTokenId);
            return "The new token ID is: " + newTokenId;
        } catch (Exception e) {
            logger.error("Error creating token", e);
            return "Error creating token: " + e.getMessage();
        }
    }
}

