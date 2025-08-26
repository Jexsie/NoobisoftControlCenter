package com.noobisoftcontrolcenter.needfortoken.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenId;
import com.noobisoftcontrolcenter.needfortoken.model.MetadataRequest;
import com.noobisoftcontrolcenter.needfortoken.service.HederaAssociationService;
import com.noobisoftcontrolcenter.needfortoken.service.PinataService;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;

import io.swagger.annotations.ApiOperation;


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

    @Autowired
    private HederaAssociationService hederaAssociationService;

    @ApiOperation("Creates card token type")
    @GetMapping("/createCardTokenType")
    public String createTokenType() throws  Exception {
        TokenId cardTokenId = nftClient.createNftType("CardToken", "CTKN");
        return cardTokenId.toString();
    }

    private static final Logger logger = LoggerFactory.getLogger(AdminEndpoint.class);

    public record AccountAndKeyData(String accountId, String privateKey) {
    }

    @GetMapping("/createNewAccount")
    public AccountAndKeyData createNewAccount() throws Exception {
        final Account account = accountClient.createAccount(Hbar.ZERO);
        
        // Automatically associate the new account with the token
        try {
            String tokenId = "0.0.5219756";
            Status status = hederaAssociationService.associateAccountWithToken(
                account.accountId().toString(), 
                account.privateKey().toString(), 
                tokenId
            );
            
            if (status == Status.SUCCESS) {
                logger.info("Successfully associated new account {} with token {}", account.accountId(), tokenId);
            } else {
                logger.warn("Failed to associate new account {} with token {}. Status: {}", account.accountId(), tokenId, status);
            }
        } catch (Exception e) {
            logger.error("Error associating new account {} with token: {}", account.accountId(), e.getMessage());
            // Don't fail the account creation, just log the error
        }
        
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
