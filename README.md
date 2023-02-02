# Simple Ethereum Explorer

### Task

Create an application that will allow a user to view transaction data from the Ethereum blockchain associated with a specific wallet address W that the user inputs, starting with block B. The application should get information on:

- wallets (addresses) and
- amounts of ETH associated with transactions made to and from the given wallet W and
- show them in a simple human-readable way (ideally, through a web page).

The application should collect and display ALL transaction data starting from the given block B.

---

#### Example

If a user requests to view transactions associated with the address `0xaa7a9ca87d3694b5755f213b5d04094b8d0f0a6f` from block `9000000` to the current block, your application should be able to crawl and visualize all transaction data (addresses that have sent and received tokens from the address `0xaa7a9ca87d3694b5755f213b5d04094b8d0f0a6f`, and how much ETH was used for a given transaction) in that period of time.

---

#### Bonus Points

- Given a date in `YYYY-MM-DD` format, the program should return the exact value of ETH that was available on the given address at YYYY-MM-DD 00:00 UTC time.

- Do the same task above to include tokens amounts (other than ETH)

---
---
---

## My Solution
### \> Description
Java Spring Boot application, that serves web content (on port 8080, by default), and runs a REST service to provide historical ETH balance, at the user's request.

It shows current and historical ETH balance, a list of all transactions, and information for each transaction. In the transaction list, it shows icons for when transactions have errors, or when the transaction was an interaction with a smart contract.

The only feature it does not implement is the listing of token balances (and token historical balance).

---
### \> Technologies Used
- Backend
  - Java language, with the Web3j library (for connecting to Infura and also includes some utilities).
  - Spring Boot framework - for the creation and management of webservices.

- Frontend (besides HTML and CSS)
  - Bootstrap - for quick and easy-to-use themes.
  - Javascript - for fetching information from a REST API created using Spring Boot.
  - Thymeleaf - works with Spring Boot, for rendering variables and programming logic in the Views, when passed through the Controllers.
  
- Data Providers
  - Infura API - to get the historical balance of an address at a specific timestamp/block.
  - Etherscan API - to get block number closest to a certain timestamp, and to get the transactions list.

---
### \> Further Improvements
- Have a cache for the most recent requests.
  - Ideally, find a way to identify requests for different sessions, attributing a new `Explorer()` object to each new session.
- Split API requests into different threads, to avoid waiting so long for large transaction lists.
- Pagination in the UI, when showing the list of transactions (I left some unused code for that in the Explorer class).
- Sometimes, Etherscan API is unable to find closest block to certain timestamps.


- To get historical token balances:
  - Alchemy API is able to serve that information, as well as, provide the list of transactions to/by an address AND distinguish between the type of transfer (basic, ERC-20, etc...) AND provide current and historical ETH balance. I only discovered this API after developing this program.
  - If not willing to use an API:
    - ERC-20 standard defines some basic necessary methods. Most importantly, `transfer()` and `transferFrom()`.
The call for these methods is saved in binary in the **input** field of transactions,
and follows the format: `'transfer(' + 'toAddress' + 'value'`. The first part appears as `0xa9059cbb` at the start of the **input** field (which is the same string as in the **methodId** field, provided by the Etherscan API).
This could be used to identify and read token transactions directly from the blockchain.

---
### \> Instructions
- `java -version` - make sure you use the binaries of Java 17 or more recent.
- `java -jar packages/<filename>.jar`
- Access `localhost:8080` on your browser.
- Ctrl+C, to exit the program.