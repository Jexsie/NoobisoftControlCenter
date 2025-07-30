package com.noobisoftcontrolcenter.tokemon.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.noobisoftcontrolcenter.tokemon.model.MetadataRequest;
import com.noobisoftcontrolcenter.tokemon.service.PinataService;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;

import io.swagger.annotations.ApiOperation;
import jakarta.annotation.PostConstruct;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(AdminEndpoint.class);

    @Autowired
    private NftClient nftClient;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private PinataService pinataService;

    @Value("${spring.hedera.accountId}")
    private String tokenAdminId;

    @Value("${spring.hedera.privateKey}")
    private String tokenAdminKey;

    private AccountId tokenAdmin;
    private PrivateKey tokenAdminPrivateKey;

    @PostConstruct
    public void init() {
        tokenAdmin = AccountId.fromString(tokenAdminId);
        tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
    }

    public record AccountAndKeyData(String accountId, String privateKey) {
    }

    @ApiOperation("Creates SkateToken type")
    @GetMapping("/createSkateTokenType")
    public String createTokenType() throws Exception {
        TokenId cardTokenId = nftClient.createNftType("SkateToken", "STKN");
        return cardTokenId.toString();
    }

    @ApiOperation("Creates a new Hedera account")
    @GetMapping("/createNewAccount")
    public AccountAndKeyData createNewAccount() throws Exception {
        var account = accountClient.createAccount(Hbar.ZERO);
        return new AccountAndKeyData(account.accountId().toString(), account.privateKey().toString());
    }

    @ApiOperation("Pins JSON metadata to IPFS")
    @PostMapping("/pinJson")
    public String pinJson(@RequestBody MetadataRequest metadataRequest) {
        try {
            return pinataService.pinJson(metadataRequest);
        } catch (Exception e) {
            logger.error("Failed to pin JSON to IPFS", e);
            return "Failed to pin JSON to IPFS: " + e.getMessage();
        }
    }

    @ApiOperation("Transfers all NFTs from an account to the admin")
    @GetMapping("/clearAccountNfts")
    public String clearAccountNfts(@RequestParam String addressId) throws Exception {
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
