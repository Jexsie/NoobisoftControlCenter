import { useNavigate } from "react-router-dom";
import {
  HashConnect,
  HashConnectConnectionState,
  SessionData,
} from "hashconnect";
import {
  AccountId,
  LedgerId,
  TokenAssociateTransaction,
  TokenId,
} from "@hashgraph/sdk";
import { setActiveUser } from "../utils";
import { projectId, tokenId } from "../constants";

const appMetadata = {
  name: "Need for Token",
  description: "This is the a need for token game",
  icons: [
    "https://tokemon-frontend-open-elements-8f231e3a.koyeb.app/favicon.ico",
  ],
  url: "https://tokemon-frontend-open-elements-8f231e3a.koyeb.app/",
};

export default function Login({ isOpen, setIsOpen }) {
  const navigate = useNavigate();

  let hashconnect: HashConnect;
  let state: HashConnectConnectionState =
    HashConnectConnectionState.Disconnected;
  let pairingData: SessionData;

  async function init() {
    //create the hashconnect instance
    hashconnect = new HashConnect(
      LedgerId.TESTNET,
      projectId,
      appMetadata,
      true
    );

    //register events
    setUpHashConnectEvents();

    //initialize
    await hashconnect.init();

    //open pairing modal
    hashconnect.openPairingModal();
    await associateTx();
    setActiveUser(pairingData.accountIds[0]);
  }

  function setUpHashConnectEvents() {
    hashconnect.pairingEvent.on((newPairing) => {
      pairingData = newPairing;
    });

    hashconnect.disconnectionEvent.on(() => {
      pairingData = null;
    });

    hashconnect.connectionStatusChangeEvent.on((connectionStatus) => {
      state = connectionStatus;
    });
  }

  const associateTx = async () => {
    try {
      const signer = hashconnect.getSigner(
        AccountId.fromString(pairingData?.accountIds[0])
      );
      const transaction = new TokenAssociateTransaction()
        .setAccountId(AccountId.fromString(pairingData?.accountIds[0]))
        .setTokenIds([TokenId.fromString(tokenId)])
        .freezeWithSigner(signer);

      const response = (await transaction).executeWithSigner(signer);

      if ((await response).toString()) {
        console.log(response, "asociation promise");
        navigate("/dashboard");
      }
    } catch (error) {
      console.error("Error in association transaction:", error);
    }
  };

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
        {state}
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
