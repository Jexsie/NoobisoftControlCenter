import axios from "axios";
import { tokenId } from "../constants";
import { AccountMeta } from "../types";

export async function dataFetcher<T>(url: string) {
  return await axios
    .get<T>(url)
    .then((res) => res.data)
    .catch((error) => {
      throw new Error(error);
    });
}

export async function isUserAssociated(accountId: string): Promise<boolean> {
  try {
    const response = await axios.get<AccountMeta>(
      `https://testnet.mirrornode.hedera.com/api/v1/accounts/${accountId}/nfts?limit=1`
    );
    return (
      response.data?.nfts.some((nfts) => nfts.token_id === tokenId) ?? false
    );
  } catch (error) {
    console.error("Error checking if user is associated:", error);
    return false;
  }
}
