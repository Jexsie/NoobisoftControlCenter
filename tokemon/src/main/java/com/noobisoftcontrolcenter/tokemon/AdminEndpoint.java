package com.noobisoftcontrolcenter.tokemon;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.NftClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class AdminEndpoint {

    @Autowired
    private NftClient nftClient;

    @Autowired
    private AccountClient accountClient;

    @ApiOperation("Creates card token type")
    @GetMapping("/createCardTokenType")
    public String createTokenType() throws  Exception {
        TokenId cardTokenId = nftClient.createNftType("CardToken", "CTKN");
        return cardTokenId.toString();
    }

    public record AccountAndKeyData(String accountId, String privateKey) {
    }

    @GetMapping("/createNewAccount")
    public AccountAndKeyData createNewAccount() throws Exception {
        final Account account = accountClient.createAccount(Hbar.ZERO);
        return new AccountAndKeyData(account.accountId().toString(), account.privateKey().toString());
    }


    @Autowired
    private PinataService pinataService;

    @PostMapping("/pinJson")
    public String pinJson(@RequestBody MetadataRequest metadataRequest) {
        try {
            return pinataService.pinJson(metadataRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to pin JSON to IPFS: " + e.getMessage();
        }
    }
}
