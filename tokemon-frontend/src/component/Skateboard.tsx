import { NftMeta } from "../types";
import { getUser } from "../utils";

const Skateboard = ({ nft, skate }: { nft: NftMeta; skate: string }) => {
  const { name, image: src, game } = nft;

  if (name === "Skateboard") return;
  return (
    <div className=" flex justify-center items-center">
      <div style={{ maxWidth: "428px" }} className="relative">
        <img src={skate} alt="skateboard" className="w-full h-auto" />

        <div className="absolute top-[40%] left-[18%]">
          <img src={src} alt="Monster" className="w-16 h-auto" />
        </div>
        <div className="absolute top-[25%] left-[20%] text-black text-sm font-bold  font-bebas">
          Game: {game}
        </div>
        <div className="absolute top-[30%] left-[20%] text-black text-2xl font-bold  font-bebas">
          {game.toLowerCase() === "tokemon" ? "#TKMN" : "#NFT"}
        </div>
        <div className="absolute bottom-[32%] left-[18%] text-black text-sm rotate-90">
          {getUser()}
        </div>

        <div className="absolute top-[40%] left-[60%]">
          <img src={src} alt="Monster" className="w-16 h-auto" />
        </div>
        <div className="absolute bottom-[42%] right-[22%] text-black text-xl font-bold font-bebas">
          {name}
        </div>
      </div>
    </div>
  );
};

export default Skateboard;
