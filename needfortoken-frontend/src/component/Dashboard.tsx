import Card from "./Card";
import { useUserNfts } from "../hooks";
import { useEffect, useMemo, useState } from "react";
import { getUser } from "../utils";
import Skeleton from "./Skeleton";
import { useNavigate } from "react-router-dom";
import { cardBaseUrl } from "../constants";

export default function Dashboard() {
  const navigate = useNavigate();
  const { isLoading, nfts } = useUserNfts(getUser());
  const [card, setCard] = useState("");
  const validNfts = useMemo(
    () => nfts.filter((nft) => nft.name !== "Skateboard"),
    [nfts]
  );

  useEffect(() => {
    const activeUser = getUser();
    if (!activeUser) {
      return navigate("/");
    }
  }, [navigate]);

  useEffect(() => {
    const getCard = async () => {
      const cachedSkate = localStorage.getItem("card");

      if (!cachedSkate) {
        try {
          const response = await fetch(cardBaseUrl);
          const blob = await response.blob();
          const reader = new FileReader();
          reader.onloadend = () => {
            const dataUrl = reader.result as string;
            localStorage.setItem("card", dataUrl);
            setCard(dataUrl);
          };
          reader.readAsDataURL(blob);
        } catch (error) {
          console.error(`Failed to fetch image}:`, error);
        }
      } else {
        setCard(cachedSkate);
      }
    };

    getCard();
  }, []);

  return (
    <div className=" py-24 sm:py-32 h-screen">
      <div className="mx-auto max-w-7xl px-6 lg:px-8 flex flex-col justify-center items-center">
        <div className="mx-auto max-w-2xl lg:mx-0">
          <h2 className="text-3xl font-bold tracking-tight text-black sm:text-4xl">
            TOKEMON #TKMN
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
          <p className="mt-6 text-lg leading-8 text-gray-600">
            Welcome to Tokemon. If you are new, you will be provided with three
            NFTs to enjoy the game. These tokens can be used accross different
            games which actually affirms ownership of the token. Happy gaming!!!
          </p>
        </div>
        {isLoading ? (
          <div className="m-4 flex gap-6 mx-auto max-w-2xl lg:mx-0">
            <Skeleton />
            <Skeleton />
          </div>
        ) : (
          <ul
            role="list"
            className="mx-auto mt-20 flex justify-center items-center flex-wrap gap-16"
          >
            {validNfts.map((nft, index) => (
              <Card key={index} nft={nft} card={card} />
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
