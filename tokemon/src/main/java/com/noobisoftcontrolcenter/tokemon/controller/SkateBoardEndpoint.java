package com.noobisoftcontrolcenter.tokemon.controller;

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
import com.noobisoftcontrolcenter.tokemon.service.PinataService;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;

import io.swagger.annotations.ApiOperation;
import jakarta.annotation.PostConstruct;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/skateboard")
public class SkateBoardEndpoint {

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
            "QmV7R7dtVhxmY3YdiuU2oyfmR2QdTTzdwVxNsJgAS5h4DP",
            "QmWr32ZybFgSeAGDz5MeBHDgH4qwPygKWUw5C7Je6aas3p",
            "QmUDH2QYqgRyC2UnCFBmAYuRnT72ts75ArA9oBFE5SkmM1",
            "QmWUZ2kksfhGoqCSyM3vMSJdK6sk9buPmqTGbTLHc9w8cE",
            "QmZQNbwYdkm2eF4Qvc9b4F3Ybmby4XqwzjDQomXfm5L1kE",
            "QmdJGnG1AZeHmYCJjjFng89GecVmuUtQWz48K3gBmTJDDg",
            "QmbiZ2BE52EFJ6t7g2yWq7d4SeDbigN5U4qmGjK1HvrQuk",
            "QmUPxrV868LoUq2BdFxXnAC9tvo95efY9wiomrYPgVg5AU",
            "QmdN7hZ2UiBXPMmHogJVeWSGBP4XGrok8zWZ37dMWpJvT1"
    };

    @PostConstruct
    public void init() {
        tokenAdmin = AccountId.fromString(tokenAdminId);
        tokenAdminPrivateKey = PrivateKey.fromString(tokenAdminKey);
    }

    @ApiOperation("Gets or mints SkateTokens for a user")
    @GetMapping("/getStakesForUser")
    public List<Map<String, Object>> getCardsForUser(@RequestParam String userAccountId,
                                                     @RequestParam String userPrivateKey) throws Exception {
        List<Map<String, Object>> results = new ArrayList<>();
        TokenId cardTokenId = TokenId.fromString(tokenId);
        AccountId accountId = AccountId.fromString(userAccountId);
        PrivateKey accountPrivateKey = PrivateKey.fromString(userPrivateKey); // Needed to sign association

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
