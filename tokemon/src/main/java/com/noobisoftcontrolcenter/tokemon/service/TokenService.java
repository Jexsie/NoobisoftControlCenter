package com.noobisoftcontrolcenter.tokemon.service;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${spring.hedera.accountId}")
    private String operatorId;

    @Value("${spring.hedera.privateKey}")
    private String operatorKey;

    /**
     * Creates a new fungible token on Hedera with the specified name, symbol, and initial supply.
     */
    public TokenId createToken(String tokenName, String tokenSymbol, long initialSupply) throws Exception {
        try {
            Client client = Client.forTestnet();
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            TransactionResponse transactionResponse = new TokenCreateTransaction()
                    .setTokenName(tokenName)
                    .setTokenSymbol(tokenSymbol)
                    .setTreasuryAccountId(AccountId.fromString(operatorId))
                    .setInitialSupply(initialSupply)
                    .setAdminKey(PrivateKey.fromString(operatorKey).getPublicKey())
                    .setSupplyKey(PrivateKey.fromString(operatorKey).getPublicKey())
                    .execute(client);

            TokenId tokenId = transactionResponse.getReceipt(client).tokenId;
            logger.info("Created token {} ({}) with ID: {}", tokenName, tokenSymbol, tokenId);
            return tokenId;
        } catch (Exception e) {
            logger.error("❌ Failed to create token {} ({})", tokenName, tokenSymbol, e);
            throw new RuntimeException("Failed to create token", e);
        }
    }

    /**
     * Associates the given token ID with the specified user's account.
     * The user's private key is required to sign the association.
     */
    public void associateTokenToAccount(String accountId, String privateKey, String tokenId) throws Exception {
        try {
            Client client = Client.forTestnet(); // Use Client.forMainnet() for mainnet
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            TransactionResponse response = new TokenAssociateTransaction()
                    .setAccountId(AccountId.fromString(accountId))
                    .setTokenIds(List.of(TokenId.fromString(tokenId)))
                    .freezeWith(client)
                    .sign(PrivateKey.fromString(privateKey)) // Sign with user's private key
                    .execute(client);

            response.getReceipt(client); // Ensure the association went through

            logger.info("✅ Successfully associated token {} with account {}", tokenId, accountId);
        } catch (Exception e) {
            logger.error("❌ Failed to associate token {} to account {}", tokenId, accountId, e);
            throw new RuntimeException("Token association failed", e);
        }
    }
}
