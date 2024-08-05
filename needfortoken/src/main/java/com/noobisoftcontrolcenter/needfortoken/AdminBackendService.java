package com.noobisoftcontrolcenter.needfortoken;

import com.hedera.hashgraph.sdk.AccountId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBackendService {

    @Autowired
    private UserRepository userRepository;

    public AccountId getHederaAccountForUser(String userMail) {
        User user = userRepository.findById(userMail).orElseThrow(() -> new RuntimeException("User not found"));
        return AccountId.fromString(user.getAccountId());
    }
}
