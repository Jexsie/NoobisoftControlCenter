package com.noobisoftcontrolcenter.needfortoken.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfo;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;

@Service
public class HederaAssociationService {

    private static final Logger logger = LoggerFactory.getLogger(HederaAssociationService.class);

    @Value("${spring.hedera.accountId}")
    private String operatorId;

    @Value("${spring.hedera.privateKey}")
    private String operatorKey;

    private final Client client;

    public HederaAssociationService() {
        this.client = Client.forTestnet();
    }

    /**
     * Associates an account with a token
     * @param accountId The account to associate
     * @param privateKey The private key of the account (for signing)
     * @param tokenId The token to associate with
     * @return Status of the association operation
     */
    public Status associateAccountWithToken(String accountId, String privateKey, String tokenId) {
        try {
            // Set up the client with operator credentials
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            // Parse the parameters
            AccountId hederaAccountId = AccountId.fromString(accountId);
            PrivateKey hederaPrivateKey = PrivateKey.fromString(privateKey);
            TokenId hederaTokenId = TokenId.fromString(tokenId);

            // Create and execute token association transaction
            TokenAssociateTransaction associateTx = new TokenAssociateTransaction()
                .setAccountId(hederaAccountId)
                .setTokenIds(Collections.singletonList(hederaTokenId))
                .freezeWith(client)
                .sign(hederaPrivateKey);

            TransactionResponse response = associateTx.execute(client);
            TransactionReceipt receipt = response.getReceipt(client);

            logger.info("Token association result for account {} with token {}: {}", accountId, tokenId, receipt.status);
            return receipt.status;

        } catch (Exception e) {
            logger.error("Error associating account {} with token {}: {}", accountId, tokenId, e.getMessage());
            throw new RuntimeException("Failed to associate account with token: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if an account is associated with a token
     * @param accountId The account to check
     * @param tokenId The token to check
     * @return true if associated, false otherwise
     */
    public boolean isAccountAssociatedWithToken(String accountId, String tokenId) {
        try {
            // Set up the client with operator credentials
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            // Parse the parameters
            AccountId hederaAccountId = AccountId.fromString(accountId);
            TokenId hederaTokenId = TokenId.fromString(tokenId);

            // Get account info and check token relationships
            AccountInfo accountInfo = new AccountInfoQuery()
                .setAccountId(hederaAccountId)
                .execute(client);

            boolean isAssociated = accountInfo.tokenRelationships.containsKey(hederaTokenId);
            logger.info("Account {} association check with token {}: {}", accountId, tokenId, isAssociated);
            return isAssociated;

        } catch (Exception e) {
            logger.error("Error checking token association for account {} with token {}: {}", accountId, tokenId, e.getMessage());
            return false;
        }
    }
} 