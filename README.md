# jasonRSV2.0
This project aims to publish Jason agent capabilities as a REST service, combining multi-agent system (MAS), REST, 

Objective

The main objective of this project is to expose Jason agent capabilities as services accessible via a RESTful interface. This will allow external clients to interact with these agents in a standardized way using HTTP requests.
Prerequisites

Before getting started, ensure you have the following tools installed:

    Java Development Kit (JDK) version 8 or higher
    Gradle version 7.x or higher

Installation

    Clone the repository
    git clone https://github.com/your-username/jasonRestIot.git
cd jasonRestIot
Build the project

Use Gradle to build the project:

bash

    ./gradlew build

Usage
Running the Jason server

To start the Jason server that exposes agent capabilities via REST, use the Gradle task runJasonServer:

bash

./gradlew runJasonServer

Ensure the server is started before proceeding.
