package com.noobisoftcontrolcenter.needfortoken.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.noobisoftcontrolcenter.needfortoken.service.AdminBackendService;
import com.noobisoftcontrolcenter.needfortoken.service.PinataService;
import com.noobisoftcontrolcenter.needfortoken.service.TokenService;
import com.openelements.hedera.base.NftRepository;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.Nft;
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

    @Autowired
    private AccountClient accountClient;

    private static final String[] CID = {
            "QmRGHv17TkToCWhKSiW7SkyYvJ4TbXX8ib59tMjAtGpCWM",
            "QmPasPVE2p5CLejL4MSNRFJdvNQddjChLfJFDmy7NbCaf8",
            "QmYsEQAwLGu4UPgVJt3kdoibtsC1tpyZenBaeEZMKBgmfD",
            "Qmbe4DPkTM4X2mRts3Xyy7YxfFUg76BgjRqq6jTA2QHn39",
            "QmZytyteD2u9QrVpBzDUnoDqHFDhB4zb59XZke5rUwm4kW",
            "QmVbfAzkJouNtWxUA4nr1bxzwmiw4GmgHLWMspLBLyUw9h",
            "QmTZkGdc29MSUP8eXynipEWtdG6qK6BwzwRRFSz6r6dXpB",
            "QmQuPJv6GsNq8q9Zs4rimT1ZTbpTEtiWjDALw8w3c5wDBu",
            "Qmf4BLov3HZifCknS9eSE9fUWFtmE8WRd3LXjLSkhoBhBJ"
    };

    private final static String TOKEN_ID = "0.0.5219756";

    @ApiOperation("Get cards for user endpoint")
    @CrossOrigin
    @GetMapping("/getCardsForUser")
    public List<Map<String, Object>> getCardsForUser(@RequestParam String userMail) throws Exception {
        final List<Map<String, Object>> results = new ArrayList<>();

        TokenId cardTokenId = TokenId.fromString(TOKEN_ID);

        if (tokenAdmin == null) {
            // throw new IllegalStateException("Admin account not found");
            tokenAdmin = AccountId.fromString(tokenAdminId);
            tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
        }

        final AdminBackendService.AccountAndKey accountAndKey = adminBackendService.getHederaAccountForUser(userMail);
        final AccountId accountId = accountAndKey.accountId();
        final PrivateKey accountPrivateKey = accountAndKey.privateKey();
        final List<Nft> nfts = nftRepository.findByOwnerAndType(accountId, cardTokenId);

        if (nfts.isEmpty()) {
            nftClient.associateNft(cardTokenId, accountId, accountPrivateKey);
            List<String> nftMetadata = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < 4; i++) {
                int index = random.nextInt(CID.length);
                nftMetadata.add(CID[index]);
            }

            final List<Long> serials = nftClient.mintNfts(cardTokenId, nftMetadata);

            for (Long serial : serials) {
                nftClient.transferNft(cardTokenId, serial, tokenAdmin, tokenAdminPrivateKey, accountId);
            }

            for (String ipfsHash : nftMetadata) {
                results.addAll(pinataService.getMetadata(ipfsHash));
            }
        } else {
            for (Nft nft : nfts) {
                results.addAll(pinataService.getMetadata(new String(nft.metadata())));
            }
        }

        for (Nft nft: getSupportedNftsForAccount(accountId)) {
            results.addAll(pinataService.getMetadata(new String(nft.metadata())));
        }

        return results;
    }

    private List<Nft> getSupportedNftsForAccount(AccountId accountId) throws Exception {
        List<Nft> results = new ArrayList<>();
        List<Nft> allNfts = nftRepository.findByOwner(accountId);

        for(Nft nft: allNfts) {
            if (!nft.tokenId().toString().equals(TOKEN_ID)) {
                List<Map<String, Object>> metadata = pinataService.getMetadata(new String(nft.metadata()));

                results.add(nft);
            }
        }
        return results;
    }
}
