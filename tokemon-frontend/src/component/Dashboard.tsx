import Skateboard from "./Skateboard";
import { useUserNfts } from "../hooks";
import { getUser } from "../utils";
import { useEffect, useMemo, useState } from "react";
import Skeleton from "./Skeleton";
import { useNavigate } from "react-router-dom";
import { skateBaseUrl } from "../constants";

export default function Dashboard() {
  const navigate = useNavigate();
  const [skate, setSkate] = useState("");
  const { isLoading, nfts } = useUserNfts(getUser());

  const validNfts = useMemo(
    () => nfts.filter((nft) => nft.name !== "card"),
    [nfts]
  );

  useEffect(() => {
    const activeUser = getUser();
    if (!activeUser) {
      return navigate("/");
    }
  }, [navigate]);

  useEffect(() => {
    const getSkate = async () => {
      const cachedSkate = localStorage.getItem("skateboard");

      if (!cachedSkate) {
        try {
          const response = await fetch(skateBaseUrl);
          const blob = await response.blob();
          const reader = new FileReader();
          reader.onloadend = () => {
            const dataUrl = reader.result as string;
            localStorage.setItem("skateboard", dataUrl);
            setSkate(dataUrl);
          };
          reader.readAsDataURL(blob);
        } catch (error) {
          console.error(`Failed to fetch image}:`, error);
        }
      } else {
        setSkate(cachedSkate);
      }
    };

    getSkate();
  }, []);

  return (
    <div className=" py-24 sm:py-32 h-screen">
      <div className="mx-auto max-w-7xl px-6 lg:px-8 flex flex-col justify-center items-center">
        <div className="mx-auto max-w-2xl lg:mx-0">
          <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
            NEED FOR TOKEN #NFT
          </h2>
          <span
            style={{
              padding: "5px",
              background: "green",
              borderRadius: "5px",
              color: "white",
            }}
          >
            {getUser()}
          </span>
          <p className="mt-6 text-lg leading-8 text-gray-400">
            Welcome to Need for token. If you are new, you will be provided with
            three NFTs to enjoy the game. These tokens can be used accross
            different games which actually affirms ownership of the token. Happy
            gaming!!!
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
              <Skateboard key={nft.image} nft={nft} skate={skate} />
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
