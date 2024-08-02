import Skateboard from "./Skateboard";
import monster11 from "../assets/Monster-11.png";
import monster3 from "../assets/Monster-3.png";

const skins = [
  { name: "MONSTER 11", img: monster11 },
  { name: "MONSTER 3", img: monster3 },
  { name: "MONSTER 3", img: monster3 },
  { name: "MONSTER 3", img: monster3 },
  { name: "MONSTER 3", img: monster3 },
];

export default function Dashboard() {
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
        <ul
          role="list"
          className="mx-auto mt-20 flex justify-center items-center flex-wrap gap-4"
        >
          {skins.map((skin) => (
            <Skateboard name={skin.name} src={skin.img} />
          ))}
        </ul>
      </div>
    </div>
  );
}
