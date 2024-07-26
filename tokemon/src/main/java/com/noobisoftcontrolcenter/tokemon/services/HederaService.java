package com.noobisoftcontrolcenter.tokemon.services;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.AccountId;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
public class HederaService {

    private final Client hederaClient;

    public HederaService(
            @Value("${spring.hedera.accountId}") String accountId,
            @Value("${spring.hedera.privateKey}") String privateKey,
            @Value("${spring.hedera.network.name}") String networkName
    ) {
        try {
            hederaClient = networkName.equalsIgnoreCase("mainnet") ? Client.forMainnet() : Client.forTestnet();
            hederaClient.setOperator(AccountId.fromString(accountId), PrivateKey.fromString(privateKey));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Hedera configuration", e);
        }
    }

    // Add methods to interact with the Hedera network
}
