package com.noobisoftcontrolcenter.tokemon.service;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.AccountInfoQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenInfoQuery;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;

@Service
public class TokenAssociationService {

    private static final Logger logger = LoggerFactory.getLogger(TokenAssociationService.class);

    @Value("${spring.hedera.accountId}")
    private String operatorId;

    @Value("${spring.hedera.privateKey}")
    private String operatorKey;

    /**
     * Associates an account with a token if not already associated
     * Note: This method requires the account's private key for signing
     * @param accountId The account to associate
     * @param tokenId The token to associate with
     * @param accountPrivateKey The private key of the account (for signing)
     * @return true if association was successful or already existed, false otherwise
     */
    public boolean associateAccountWithToken(AccountId accountId, TokenId tokenId, PrivateKey accountPrivateKey) {
        try {
            // Create client
            Client client = Client.forTestnet();
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            // Check if already associated by trying to get token info
            try {
                // Try to get account token relationship - if it fails, not associated
                new AccountInfoQuery()
                    .setAccountId(accountId)
                    .execute(client);
                
                // If we get here, account exists, now check token association
                var tokenRelationship = new TokenInfoQuery()
                    .setTokenId(tokenId)
                    .execute(client);
                
                logger.info("Token {} already associated with account {}", tokenId, accountId);
                return true;
            } catch (Exception e) {
                // Account might not be associated, proceed with association
                logger.info("Account {} not associated with token {}, proceeding with association", accountId, tokenId);
            }

            // Create and execute token association transaction
            TokenAssociateTransaction associateTx = new TokenAssociateTransaction()
                .setAccountId(accountId)
                .setTokenIds(Collections.singletonList(tokenId))
                .freezeWith(client)
                .sign(accountPrivateKey);

            TransactionResponse response = associateTx.execute(client);
            TransactionReceipt receipt = response.getReceipt(client);

            if (receipt.status == Status.SUCCESS) {
                logger.info("Successfully associated account {} with token {}", accountId, tokenId);
                return true;
            } else {
                logger.error("Failed to associate account {} with token {}. Status: {}", accountId, tokenId, receipt.status);
                return false;
            }

        } catch (Exception e) {
            logger.error("Error associating account {} with token {}: {}", accountId, tokenId, e.getMessage());
            return false;
        }
    }

    /**
     * Checks if an account is associated with a token
     * @param accountId The account to check
     * @param tokenId The token to check
     * @return true if associated, false otherwise
     */
    public boolean isAccountAssociatedWithToken(AccountId accountId, TokenId tokenId) {
        try {
            Client client = Client.forTestnet();
            client.setOperator(AccountId.fromString(operatorId), PrivateKey.fromString(operatorKey));

            // Try to get account token relationship
            var accountInfo = new AccountInfoQuery()
                .setAccountId(accountId)
                .execute(client);

            // Check if the token is in the account's token relationships
            return accountInfo.tokenRelationships.containsKey(tokenId);

        } catch (Exception e) {
            logger.error("Error checking token association for account {} with token {}: {}", accountId, tokenId, e.getMessage());
            return false;
        }
    }
} 