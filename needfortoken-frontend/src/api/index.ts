import axios from "axios";
import { User } from "../types";
import { apiUrl } from "../constants";
async function postData(url: string, data: any) {
  return await axios.post(url, data);
}

export async function dataFetcher<T>(url: string) {
  return await axios
    .get<T>(url)
    .then((res) => res.data)
    .catch((error) => {
      throw new Error(error);
    });
}

export async function login(user: User) {
  const params = new URLSearchParams({
    email: user.email,
    accountId: user.accountId,
  });
  return await axios.post(`${apiUrl}api/login?${params}`);
}
