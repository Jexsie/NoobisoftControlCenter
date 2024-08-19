import axios from "axios";

export async function dataFetcher<T>(url: string) {
  return await axios
    .get<T>(url)
    .then((res) => res.data)
    .catch((error) => {
      throw new Error(error);
    });
}
