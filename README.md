# NFTs4Games

### Project Description

This project includes two interconnected game applications that leverage the Hedera
network for NFT management. Players can log in to either game and access their NFTs
across both. NFTs designed for one game are compatible with others, enhancing gameplay
and creating a unified user experience. This project is based on Hendrik Ebbers blog post
https://dev.to/hendrikebbers/gaming-web3-how-an-open-and-secure-future-of-ownership-in-games-could-look-2ihf

## NFT Integration Across Multiple Games

At NFTs4Games we seamlessly share NFTs between games - Unlock the power of NFT interoperability within your gaming experience, where NFTs created in one game can be utilized across other games in the ecosystem.

## Game Details

- ### Game 1: Card Game(Need for token)

  - **Concept**: A collectible card game where each card is represented as an NFT.

- ### Key Features:
  - Upon login, users can view all their owned cards.
  - New users receive 4 starter cards on their first login.
  - The game interface displays the user's entire card collection.

* ### Game 2: Skateboard Game(Tokemon)

  - **Concept**: A skateboard customization game where each skateboard is an NFT.

- ### Key Features:
  - Players collect custom skateboards, each with unique images, though all share the same shape.
  - New users receive random 4 skateboards when they log in for the first time.
  - The game interface displays the username and a list of available skateboards.

## Technology Stack

- **Backend**: Java Spring Boot for server-side logic
- **Frontend**: React.js, Typescript for game user interfaces
- **Blockchain**: Hedera network for NFT creation and management
- **Storage**: IPFS via Pinata for storing NFT assets and metadata

### How to Run the App

To start the application, follow these steps:

### Prerequisites

- Ensure you have the following installed on your system:
  - **_Java_** :JDK (version 21)
  - **_Maven_** (version 3.8.9)
  - **_Node.js_** (version 20.11.1)
  - **_npm_** (version 10.7.0)

### Running the Application

1.  #### Clone the repository:

        git clone https://github.com/Jexsie/NoobisoftControlCenter.git
        cd NoobisoftControlCenter

2.  #### Create a .env file in the root of project and fill the following details

        spring.hedera.accountId=X.X.XXXXXXXX
        spring.hedera.privateKey=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        spring.hedera.network.name=testnet
        pinata.api.key=xxxxxxxxxxxxxxxxxxxxxx
        pinata.api.secret=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

3.  #### Make the script executable:

        chmod +x start_servers.sh

4.  #### Run the script to start all servers and frontends:
        ./start_servers.sh

This script will:

- **Start game1-server**: Executes Maven commands to build and run the needfortoken backend server on `port 8070`.

- **Start game1-frontend**: Installs necessary dependencies and runs the needfortoken-frontend React application in development mode on `port 3000`.

- **Start game2-server**: Executes Maven commands to build and run the tokemon backend server on `port 8000`.

- **Start game2-frontend**: Installs necessary dependencies and runs the tokemon-frontend React application in development mode on `port 3002`.

### How to Log in with HashPack

To log in to the games using HashPack, follow these steps:

- Install the HashPack Chrome Extension from the Chrome Web Store.
- Set up your HashPack wallet by creating a new account or importing an existing one.
- Open the game application (Tokemon game or Needfortoken Game).
- Click on the "Get started" button. A model will open up, click the 'Connect wallet' button to initialize login.
- Authorize the connection in your HashPack wallet. You may need to approve the login request within the HashPack Chrome extension.
- Once logged in, you will be redirected to the game interface with access to your NFTs.

### Terminating the Application

The script will automatically handle server termination upon receiving a SIGINT or
SIGTERM signal (e.g., by pressing Ctrl+C in the terminal). It ensures all running servers
and frontends are stopped gracefully.
