import Skateboard from "./Skateboard";
import { useUserNfts } from "../hooks";
import { getUser } from "../utils";
import { useEffect, useMemo } from "react";
import Skeleton from "./Skeleton";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const navigate = useNavigate();
  const { isLoading, nfts } = useUserNfts(getUser());
  const skateboard = useMemo(
    () =>
      nfts.length ? nfts.find((nft) => nft.name === "Skateboard")?.image : null,
    [nfts]
  );

  const validNfts = useMemo(
    () => nfts.filter((nft) => nft.name !== "card"),
    [nfts]
  );

  useEffect(() => {
    const activeUser = getUser();
    if (!activeUser) {
      return navigate("/login");
    }
  }, [navigate]);

  return (
    <div className=" py-24 sm:py-32 h-screen">
      <div className="mx-auto max-w-7xl px-6 lg:px-8 flex flex-col justify-center items-center">
        <div className="mx-auto max-w-2xl lg:mx-0">
          <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
            TOKEMON #TKMN
          </h2>
          <p className="mt-6 text-lg leading-8 text-gray-400">
            Welcome to Tokemon. If you are new, you will be provided with three
            NFTs to enjoy the game. These tokens can be used accross different
            games which actually affirms ownership of the token. Happy gaming!!!
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
