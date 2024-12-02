package com.noobisoftcontrolcenter.tokemon;

import java.util.*;

import com.openelements.hedera.base.*;
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
public class SkateBoardEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(SkateBoardEndpoint.class);

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
            "QmYhq5X1PWsqLLDdM5QAMvu1dgPa3PZx7jUEBmGaFT4uHz",
            "QmYXiT5uDgrwXnaZumSnuzqyDUKdv5k9oaLPT8MxT3GpmP",
            "QmTJ6AJb1XeKJnA9Qj5vtborshwhgqDwUgA86r12qUiTvK",
            "Qmcc4x7SFc8miRsbrxBwj7TsbkWCP4S3oeWrqGryxYmeWg",
            "QmdoFGvEXYhpT5xpvS8ebDZvFw558CRqAHkD67gXUNME26",
            "QmXsCUBDa8hX5epkQTaKyTcNhQu1rkkV4JT3dCWBQ2txgS",
            "QmS8AQmxxSrtbrwXXh8R7WJhVAe9aPfaJbr9DtNnUMzjgW",
            "QmUzmbSX9EzvSgZyzkpPsR7e4FZhssufZHrZzLkKDnoVyM",
            "QmaGBWgoXAqTqnt2RM3w3sihab9upykXDt6XYzLJEqGMBW",
    };

    private final static String TOKEN_ID = "0.0.4700136";

    @ApiOperation("Get cards for user endpoint")
    @GetMapping("/getStakesForUser")
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
                results.addAll((Collection<? extends Map<String, Object>>) pinataService.getMetadata(ipfsHash));
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
