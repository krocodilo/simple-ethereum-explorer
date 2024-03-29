package explorer.logic.api;

import explorer.logic.data.Txn;
import explorer.logic.utils.HttpsConnection;
import explorer.logic.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;

import static explorer.logic.utils.Utils.fromWeiToETH;

public class Etherscan {

    private static final String etherscanApiKey = "Q5RM9IVMRDHPK1VFYP7JUDFBC83BJG4R9I";

    private static final String ethercanApiStart = "https://api.etherscan.io/api";

    /**
     * Finds the number of the first block that was mined in that day
     * @param unixtime Unix Time
     * @return Block Number
     * @throws Exception with a message. Happens if unable to read a number from the API response
     */
    public static BigInteger getBlockNumByTimestamp(BigInteger unixtime) throws Exception {

        String baseURL = ethercanApiStart +
                "?module=block&action=getblocknobytime&timestamp=%s&closest=after&apikey=" + etherscanApiKey;

        // Request Etherscan for block number at specified date
        String resp = (String) HttpsConnection.callAPI(
                String.format(baseURL, unixtime.toString())
        );

        BigInteger blocknum;
        try{
            blocknum = new BigInteger(resp);
        } catch (NumberFormatException e){
            throw new HttpsConnection.APIException("API was unable to find closest block to that date.");
        }

        return blocknum;
    }

    /**
     * Fetches a list of transactions of an address posted on the blockchain after a certain block
     * @param address String containing the address
     * @param startBlock - A block from where to start (including itself)
     * @return ArrayList of Txn objects
     * @throws Exception with a message. In case of issues with the API
     */
    public static ArrayList<Txn> getTxnsSinceBlock(String address, BigInteger startBlock) throws Exception {

        String baseURL = ethercanApiStart +
                "?module=account&action=txlist&address=%s&startblock=%s&endblock=9999999999999&sort=asc&apikey=" + etherscanApiKey;

        ArrayList<Txn> txns = new ArrayList<>();

        int numTxnsReceived;
        BigInteger firstBlock = startBlock;

        do {
            numTxnsReceived = 0;

            JSONArray resp = (JSONArray) HttpsConnection.callAPI(
                    String.format(baseURL, address, firstBlock.toString())
            );

            for(Object obj : resp){
                JSONObject j = (JSONObject) obj;

                String txnFee = new BigInteger(j.getString("gasUsed")).multiply(new BigInteger(j.getString("gasPrice"))).toString();

                // Save JSON data to a transaction object
                txns.add( new Txn(
                        j.getString("blockNumber"),
                        j.getString("hash"),
                        Utils.getDateFromUnixTime( Long.parseLong(j.getString("timeStamp")) ),
                        j.getString("from"),

                        // If To field is empty, it signifies a contract was created:
                        j.getString("to").isBlank() ? j.getString("contractAddress") : j.getString("to"),

                        // Save value transfered in ETH, instead of Wei units:
                        fromWeiToETH( j.getString("value") ).toPlainString(),

                        // Save txn fee amount in ETH instead of Wei units:
                        fromWeiToETH( txnFee ).toPlainString(),

                        // Is a contract creation transaction if this field is not empty:
                        ! j.getString("contractAddress").isBlank(),

                        // Indicate if this transaction was an interaction with a contract:
                        ! j.getString("functionName").isBlank(),

                        Integer.parseInt(j.getString("isError")) != 0   // 0 = no error
                    ));
                numTxnsReceived++;
            }

            // set the start block of the next request to the block of the last transaction processed
            if(numTxnsReceived == 10000)
                firstBlock = new BigInteger( txns.get(txns.size()-1).blockNumber() );

            // Etherscan's API limit is 10k transactions per request
            // 10k transactions = approx. 14 MB downloaded
        } while(numTxnsReceived == 10000);

        return txns;
    }
}
