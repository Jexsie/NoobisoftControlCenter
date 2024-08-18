import Skateboard from "./Skateboard";
import { useUserNfts } from "../hooks";
import { getUser } from "../utils";
import { useMemo } from "react";
import Skeleton from "./Skeleton";

export default function Dashboard() {
  const { isLoading, nfts } = useUserNfts(getUser());

  const skateboard = useMemo(
    () =>
      nfts.length ? nfts.find((nft) => nft.name === "Skateboard").image : null,
    [nfts]
  );

  const validNfts = useMemo(
    () => nfts.filter((nft) => nft.name !== "card"),
    [nfts]
  );

  return (
    <div className=" py-24 sm:py-32 h-screen">
      <div className="mx-auto max-w-7xl px-6 lg:px-8 flex flex-col justify-center items-center">
        <div className="mx-auto max-w-2xl lg:mx-0">
          <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
            JOHN DOE
          </h2>
          <p className="mt-6 text-lg leading-8 text-gray-400">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Velit
            voluptas quaerat, quidem cumque corrupti dolorem esse vitae, beatae
            culpa dolores nisi libero ut incidunt, at nemo quam dolorum.
            Praesentium, eaque.
          </p>
        </div>
        {isLoading ? (
          <div className="m-4 flex gap-6 mx-auto max-w-2xl lg:mx-0">
            <Skeleton />;
            <Skeleton />;
          </div>
        ) : (
          <ul
            role="list"
            className="mx-auto mt-20 flex justify-center items-center flex-wrap gap-4"
          >
            {validNfts.map((nft) => (
              <Skateboard key={nft.image} nft={nft} skate={skateboard} />
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
