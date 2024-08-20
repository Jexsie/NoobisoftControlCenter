package com.noobisoftcontrolcenter.tokemon;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AdminEndpoint {

    @Autowired
    private NftClient nftClient;

    @Autowired
    private AccountClient accountClient;

    @Value("${spring.hedera.accountId}")
    private String tokenAdminId;

    @Value("${spring.hedera.privateKey}")
    private String tokenAdminKey;

    private AccountId tokenAdmin;
    private PrivateKey tokenAdminPrivateKey;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private PinataService pinataService;


    @ApiOperation("Creates card token type")
    @GetMapping("/createSkateTokenType")
    public String createTokenType() throws  Exception {
        TokenId cardTokenId = nftClient.createNftType("SkateToken", "STKN");
        return cardTokenId.toString();
    }

    private static final Logger logger = LoggerFactory.getLogger(AdminEndpoint.class);


    public record AccountAndKeyData(String accountId, String privateKey) {
    }

    @GetMapping("/createNewAccount")
    public AccountAndKeyData createNewAccount() throws Exception {
        final Account account = accountClient.createAccount(Hbar.ZERO);
        return new AccountAndKeyData(account.accountId().toString(), account.privateKey().toString());
    }

    @PostMapping("/pinJson")
    public String pinJson(@RequestBody MetadataRequest metadataRequest) {
        try {
            return pinataService.pinJson(metadataRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to pin JSON to IPFS: " + e.getMessage();
        }
    }

    @ApiOperation("Clear account NFTs by transferring them to the admin")
    @GetMapping("/clearAccountNfts")
    public String clearAccountNfts(@RequestParam String addressId) throws Exception {
        if (tokenAdmin == null) {
            tokenAdmin = AccountId.fromString(tokenAdminId);
            tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
        }

        AccountId accountId = AccountId.fromString(addressId);
        List<Nft> nfts = nftRepository.findByOwner(accountId);

        if (nfts.isEmpty()) {
            return "No NFTs found for the given address.";
        }

        for (Nft nft : nfts) {
            nftClient.transferNft(nft.tokenId(), nft.serial(), accountId, tokenAdminPrivateKey, tokenAdmin);
            logger.info("Transferred NFT with TokenID: {} and Serial: {} from {} to admin account.",
                    nft.tokenId(), nft.serial(), addressId);
        }

        return "Successfully transferred all NFTs from the given address to the admin account.";
    }
}
