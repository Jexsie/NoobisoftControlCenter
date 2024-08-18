import Card from "./Card";
import { useUserNfts } from "../hooks";
import { useMemo } from "react";
import { getUser } from "../utils";

export default function Dashboard() {
  const { isLoading, nfts } = useUserNfts(getUser());

  if (isLoading) return <div>Loading...</div>;

  const card = useMemo(
    () => (nfts.length ? nfts.find((nft) => nft.name === "card").image : null),
    [nfts]
  );

  const validNfts = useMemo(
    () => nfts.filter((nft) => nft.name !== "Skateboard"),
    [nfts]
  );
  console.log(card, "sdfgdfg");
  return (
    <div className=" py-24 sm:py-32 h-screen">
      <div className="mx-auto max-w-7xl px-6 lg:px-8 flex flex-col justify-center items-center">
        <div className="mx-auto max-w-2xl lg:mx-0">
          <h2 className="text-3xl font-bold tracking-tight text-black sm:text-4xl">
            JOHN DOE
          </h2>
          <p className="mt-6 text-lg leading-8 text-gray-600">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Velit
            voluptas quaerat, quidem cumque corrupti dolorem esse vitae, beatae
            culpa dolores nisi libero ut incidunt, at nemo quam dolorum.
            Praesentium, eaque.
          </p>
        </div>
        <ul
          role="list"
          className="mx-auto mt-20 flex justify-center items-center flex-wrap gap-16"
        >
          {validNfts.map((nft) => (
            <Card key={nft.image} nft={nft} card={card} />
          ))}
        </ul>
      </div>
    </div>
  );
}
