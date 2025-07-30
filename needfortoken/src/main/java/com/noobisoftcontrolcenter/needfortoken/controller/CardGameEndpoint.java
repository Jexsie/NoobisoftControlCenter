package com.noobisoftcontrolcenter.needfortoken.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenId;
import com.noobisoftcontrolcenter.needfortoken.service.PinataService;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;

import io.swagger.annotations.ApiOperation;
import jakarta.annotation.PostConstruct;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cardgame")
public class CardGameEndpoint {


    @Value("${spring.hedera.accountId}")
    private String tokenAdminId;

    @Value("${spring.hedera.privateKey}")
    private String tokenAdminKey;

    @Value("${spring.hedera.tokenId}")
    private String tokenId;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private NftClient nftClient;

    @Autowired
    private PinataService pinataService;

    private AccountId tokenAdmin;
    private PrivateKey tokenAdminPrivateKey;

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

    @PostConstruct
    public void init() {
        tokenAdmin = AccountId.fromString(tokenAdminId);
        tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
    }

    @ApiOperation("Get cards for user endpoint")
    @CrossOrigin
    @GetMapping("/getCardsForUser")
    public List<Map<String, Object>> getCardsForUser(@RequestParam String userAccountId, @RequestParam String userPrivateKey) throws Exception {
        List<Map<String, Object>> results = new ArrayList<>();

        TokenId cardTokenId = TokenId.fromString(tokenId);
        AccountId accountId = AccountId.fromString(userAccountId);

        PrivateKey accountPrivateKey = PrivateKey.fromString(userPrivateKey);

        List<Nft> nfts = nftRepository.findByOwnerAndType(accountId, cardTokenId);

        if (nfts.isEmpty()) {
            associateTokenToUser(accountId, accountPrivateKey, cardTokenId);

            List<String> nftMetadata = new ArrayList<>();
            Random random = new Random();

            for (int i = 0; i < 4; i++) {
                int index = random.nextInt(CID.length);
                nftMetadata.add(CID[index]);
            }

            List<Long> serials = nftClient.mintNfts(cardTokenId, nftMetadata);

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

        return results;
    }

    private void associateTokenToUser(AccountId userAccountId, PrivateKey userPrivateKey, TokenId tokenId) throws Exception {
        Client client = Client.forTestnet();
        client.setOperator(tokenAdmin, tokenAdminPrivateKey);

        new TokenAssociateTransaction()
                .setAccountId(userAccountId)
                .setTokenIds(List.of(tokenId))
                .freezeWith(client)
                .sign(userPrivateKey)
                .execute(client)
                .getReceipt(client);
    }
}
