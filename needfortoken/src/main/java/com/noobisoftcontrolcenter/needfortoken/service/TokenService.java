package com.noobisoftcontrolcenter.needfortoken.service;

import com.hedera.hashgraph.sdk.*;
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
            logger.info("Token created successfully: {}", tokenId);
            return tokenId;
        } catch (Exception e) {
            logger.error("Error occurred while creating token: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void associateTokenToAccount(String accountId, String privateKey, String tokenId) throws Exception {
        try {
            Client client = Client.forTestnet();
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            TransactionResponse transactionResponse = new TokenAssociateTransaction()
                    .setAccountId(AccountId.fromString(accountId))
                    .setTokenIds(List.of(TokenId.fromString(tokenId)))
                    .freezeWith(client)
                    .sign(PrivateKey.fromString(privateKey))
                    .execute(client);

            transactionResponse.getReceipt(client);
            logger.info("Token {} associated with account {}", tokenId, accountId);
        } catch (Exception e) {
            logger.error("Error occurred while associating token to account: {}", e.getMessage(), e);
            throw e;
        }
    }
}
