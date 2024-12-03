package com.noobisoftcontrolcenter.needfortoken.service;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.noobisoftcontrolcenter.needfortoken.model.User;
import com.noobisoftcontrolcenter.needfortoken.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBackendService {
    public record AccountAndKey(AccountId accountId, PrivateKey privateKey){}

    @Autowired
    private UserRepository userRepository;

    public AccountAndKey getHederaAccountForUser(String userMail) {
        User user = userRepository.findById(userMail).orElseThrow(() -> new RuntimeException("User not found"));
        AccountId id = AccountId.fromString(user.getAccountId());
        PrivateKey key = PrivateKey.fromString(user.getPrivateKey());
        return new AccountAndKey(id, key);
    }
}
