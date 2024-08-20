import { FaHouse } from "react-icons/fa6";
import { FaGear } from "react-icons/fa6";
import { FaArrowRightFromBracket } from "react-icons/fa6";
import { Link } from "react-router-dom";

const Aside = () => {
  return (
    <aside>
      <div
        style={{ top: "340px" }}
        className=" min-h-0 flex-1 flex overflow-hidden fixed left-0"
      >
        <nav
          aria-label="Sidebar"
          className="hidden lg:block flex-shrink-0 bg-gray-800 overflow-y-auto"
        >
          <div className="relative w-20 flex space-y-16 flex-col p-3">
            <Link to="/" className="text-gray-400 hover:text-red-700">
              <div className="flex-shrink-0 inline-flex items-center justify-center w-14">
                <FaHouse />
              </div>
            </Link>

            <Link to="/login" className="text-gray-400 hover:text-red-700">
              <div className="flex-shrink-0 inline-flex items-center justify-center w-14">
                <FaGear />
              </div>
            </Link>

            <Link to="/login" className="text-gray-400 hover:text-red-700">
              <div className="flex-shrink-0 inline-flex items-center justify-center w-14">
                <FaArrowRightFromBracket />
              </div>
            </Link>
          </div>
        </nav>
      </div>
    </aside>
  );
};

export default Aside;
