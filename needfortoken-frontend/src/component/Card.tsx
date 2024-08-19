import { NftMeta } from "../types";

const Card = ({ nft, card }: { nft: NftMeta; card: string }) => {
  const { name, image: src } = nft;

  if (name === "card") return;
  return (
    <div
      // style={{ height: "100vh", width: "100vw" }}
      className=" flex justify-center items-center"
    >
      <div
        style={{ width: "300px" }}
        className=" relative flex justify-center items-center"
      >
        <img src={card} alt="Card" />
        <img
          src={src}
          alt="Monster"
          className="absolute max-w-[50%] max-h-[50%] object-cover"
        />
        <div
          style={{ right: "25px", top: "25px" }}
          className="absolute flex flex-col items-end text-white"
        >
          <h2 style={{ fontSize: "40px" }} className="font-bold font-bebas">
            {name}
          </h2>
          <p className="text-lg">USER XXX</p>
        </div>
        <div className="absolute left-6 bottom-6 flex justify-center text-white">
          <p
            style={{
              fontSize: "40px",
            }}
            className="text-lg font-bold font-bebas text-red-50"
          >
            #NFT
          </p>
        </div>
      </div>
    </div>
  );
};

export default Card;
