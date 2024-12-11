import useSWR from "swr";
import { useEffect, useState } from "react";
import { dataFetcher } from "../api";
import { apiUrl } from "../constants";
import { NftMeta } from "../types";

export function useUserNfts(accountId: string) {
  const { isLoading, isValidating, error, data } = useSWR<Array<NftMeta>>(
    `${apiUrl}getCardsForUser?userAccountId=${accountId}`,
    dataFetcher
  );

  const [nfts, setNfts] = useState<Array<NftMeta>>([]);

  useEffect(() => {
    if (!isLoading && data) {
      const preloadImages = async () => {
        const updatedNfts = await Promise.all(
          data.map(async (nft) => {
            const storedImage = localStorage.getItem(nft.image);
            if (!storedImage) {
              try {
                const response = await fetch(
                  `https://ivory-perfect-mosquito-293.mypinata.cloud/ipfs/${
                    nft.image.split("ipfs://")[1]
                  }`
                );
                const blob = await response.blob();
                const reader = new FileReader();
                reader.onloadend = () => {
                  const dataUrl = reader.result as string;
                  localStorage.setItem(nft.image, dataUrl);
                  nft.image = dataUrl;
                };
                reader.readAsDataURL(blob);
              } catch (error) {
                console.error(`Failed to fetch image for ${nft.name}:`, error);
              }
            } else {
              nft.image = storedImage;
            }
            return nft;
          })
        );
        setNfts(updatedNfts);
      };
      preloadImages();
    }
  }, [isLoading, data]);

  return {
    nfts,
    isLoading,
    isValidating,
    error,
  };
}
