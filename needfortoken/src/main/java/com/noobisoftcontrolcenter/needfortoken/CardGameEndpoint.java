package com.noobisoftcontrolcenter.needfortoken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
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

    @Autowired
    private PinataService pinataService;

    private static final String[] CID = {
        "QmWEDxGL5LJs4WTELCNSGjaBqAAa3y7CmM6j4aWqN4Xf2X",
        "QmT9zHArvAJmV9wj1nQUESJoYZsSaLCuBZuzzgSDa7vscx",
        "QmQ3Ex2mwmj9VRK8MBPTozeYcNZJ7hQgkB52uwmHc21Nvt",
        "QmUp3S2izfabxTmUbMtmACdWi55KFkLPbLqYnC5m6tsFyi",
};

    @ApiOperation("Get cards for user endpoint")
    @GetMapping("/getCardsForUser")
    public List<Map<String, Object>> getCardsForUser(@RequestParam String userMail) throws Exception {
        final List<Map<String, Object>> results = new ArrayList<>();

        TokenId cardTokenId = tokenService.createToken("CardToken", "CTKN", 1000);

        if (tokenAdmin == null) {
            tokenAdmin = AccountId.fromString(tokenAdminId);
            tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
        }

        final AccountId accountId = adminBackendService.getHederaAccountForUser(userMail);
        final List<Nft> nfts = nftRepository.findByOwnerAndType(accountId, cardTokenId);

        List<String> nftMetadata = List.of(CID);

        if (nfts.isEmpty()) {
            final List<Long> serials = nftClient.mintNfts(cardTokenId, nftMetadata);

            for (Long serial : serials) {
                nftClient.transferNft(cardTokenId, serial, tokenAdmin, tokenAdminPrivateKey, accountId);
            }

            for (String ipfsHash : nftMetadata) {
                results.addAll((Collection<? extends Map<String, Object>>) pinataService.getMetadata(ipfsHash));
            }
        } else {
            for (Nft nft : nfts) {
                results.add(Map.of("metadata", new String(nft.metadata())));
            }
        }

        return results;
    }
}
