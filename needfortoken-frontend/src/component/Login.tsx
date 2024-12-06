import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  HashConnect,
  HashConnectConnectionState,
  SessionData,
} from "hashconnect";
import { LedgerId } from "@hashgraph/sdk";
import { setActiveUser } from "../utils";

const appMetadata = {
  name: "Tokemon",
  description: "This is the a tokemon game",
  icons: [
    "https://needfortoken-frontend-open-elements-58d77c8e.koyeb.app/favicon.ico",
  ],
  url: "https://needfortoken-frontend-open-elements-58d77c8e.koyeb.app/",
};

export default function Login({ isOpen, setIsOpen }) {
  const navigate = useNavigate();
  const [status, setStatus] = useState(HashConnectConnectionState.Disconnected);

  const projectId = "8140cdc45c6cb9f9c91446d407554b15";
  let hc: HashConnect;
  const [pairingData, setPairingData] = useState<SessionData | null>(null);

  console.log(pairingData, "pairing data");
  console.log(status, "status");

  async function init() {
    //create the hashconnect instance
    const hashconnect = new HashConnect(
      LedgerId.TESTNET,
      projectId,
      appMetadata,
      true
    );
    hc = hashconnect;

    if (hc && isOpen) {
      //register events
      hc.pairingEvent.on((newPairing) => {
        setPairingData(newPairing);
      });

      hc.disconnectionEvent.on(() => {
        setPairingData(null);
      });

      hc.connectionStatusChangeEvent.on((connectionStatus) => {
        setStatus(connectionStatus);
      });

      //initialize
      await hc.init();

      //open pairing modal
      hc.openPairingModal();
    }
  }

  useEffect(() => {
    if (status === HashConnectConnectionState.Paired) {
      setActiveUser(pairingData.accountIds[0]);
      navigate("/dashboard");
      setIsOpen(false);
    }
  }, [navigate, pairingData?.accountIds, setIsOpen, status]);

  // const disconnect = useCallback(() => {
  //   hc.disconnect();
  // }, [hc]);

  if (!isOpen) return null;

  return (
    <div
      className="z-50 min-w-[100vw] h-screen fixed top-0 flex justify-center items-center"
      style={{ backdropFilter: "blur(2px)" }}
    >
      <div
        style={{ height: "40vh" }}
        className="bg-white border border-gray-200 rounded-lg shadow sm:p-6 md:p-8"
      >
        <div className="space-y-6">
          <button onClick={() => setIsOpen(false)} className="text-red-600">
            Close
          </button>
          <h5 className="text-xl font-medium text-gray-900">
            Sign in to our platform
          </h5>
          <p>Connect your wallet to get started.</p>
          <button
            onClick={init}
            className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800"
          >
            Connect wallet
          </button>
        </div>
      </div>
    </div>
  );
}
