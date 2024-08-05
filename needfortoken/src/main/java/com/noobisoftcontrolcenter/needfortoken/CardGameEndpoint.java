package com.noobisoftcontrolcenter.needfortoken;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CardGameEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(CardGameEndpoint.class);

    @Value("${spring.hedera.accountId}")
    private String tokenAdminId;

    @Value("${spring.hedera.privateKey}")
    private String tokenAdminKey;

    private AccountId tokenAdmin;
    private PrivateKey tokenAdminPrivateKey;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private NftClient nftClient;

    @Autowired
    private AdminBackendService adminBackendService;

    @Autowired
    private TokenService tokenService;

    @ApiOperation("Get cards for user endpoint")
    @GetMapping("/getCardsForUser")
    public List<String> getCardsForUser(@RequestParam String userMail) throws Exception {
        final List<String> results = new ArrayList<>();

        TokenId cardTokenId = tokenService.createToken("CardToken", "CTKN", 1000);

        if (tokenAdmin == null) {
            tokenAdmin = AccountId.fromString(tokenAdminId);
            tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
        }

        final AccountId accountId = adminBackendService.getHederaAccountForUser(userMail);
        final List<Nft> nfts = nftRepository.findByOwnerAndType(accountId, cardTokenId);

        // Determine the cards to assign based on the user
        List<String> nftMetadata;
        if (userMail.equals("foo@bar.com")) { // User 1
            nftMetadata = List.of("monster1", "monster2", "monster3");
        } else if (userMail.equals("hendrik@openelements.com")) { // User 2
            nftMetadata = List.of("skate1", "skate2", "skate3");
        } else {
            throw new RuntimeException("Unknown user");
        }

        if (nfts.isEmpty()) {
            final List<Long> serials = nftClient.mintNfts(cardTokenId, nftMetadata);

            for (Long serial : serials) {
                nftClient.transferNft(cardTokenId, serial, tokenAdmin, tokenAdminPrivateKey, accountId);
            }
            results.addAll(nftMetadata);
        } else {
            nfts.forEach(nft -> results.add(new String(nft.metadata())));
        }

        return results;
    }
}
