package explorer.logic.api;

import explorer.logic.models.Transaction;
import explorer.logic.utils.HttpsConnection;
import explorer.logic.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;

public class Etherscan {

    private static final String etherscanApiKey = "Q5RM9IVMRDHPK1VFYP7JUDFBC83BJG4R9I";

    private static final String ethercanApiStart = "https://api.etherscan.io/api";

    /**
     * Finds the number of the first block that was mined in that day
     * @param date - String in format "YYYY-MM-DD"
     * @return Block Number
     * @throws ParseException
     * @throws HttpsConnection.ConnectionException
     * @throws HttpsConnection.APIException
     */
    public static BigInteger getBlockNumByTimestamp(String date) throws ParseException, HttpsConnection.ConnectionException, HttpsConnection.APIException {

        String baseURL = ethercanApiStart +
                "?module=block&action=getblocknobytime&timestamp=%s&closest=after&apikey=" + etherscanApiKey;

        BigInteger unixtime = Utils.getUnixTimeFromDate(
                Utils.parseDate(date)   //TODO date must be in the past
        );

        // Request Etherscan for block number at specified date
        String resp = (String) HttpsConnection.callAPI(
                String.format(baseURL, unixtime.toString())
        );

        return new BigInteger(resp);
    }

    /**
     * Fetches a list of transactions of an address posted on the blockchain after a certain block
     * @param address
     * @param startBlock - A block from where to start (including itself)
     * @return ArrayList of Transaction objects
     * @throws HttpsConnection.APIException
     * @throws HttpsConnection.ConnectionException
     */
    public static ArrayList<Transaction> getTxnsSinceBlock(String address, BigInteger startBlock) throws HttpsConnection.APIException, HttpsConnection.ConnectionException {

        String baseURL = ethercanApiStart +
                "?module=account&action=txlist&address=%s&startblock=%s&endblock=9999999999999&sort=asc&apikey=" + etherscanApiKey;

        ArrayList<Transaction> txns = new ArrayList<>();

        int numTxnsReceived;
        BigInteger firstBlock = startBlock;

        do {
            numTxnsReceived = 0;

            // 10k transactions = approx. 14 MB downloaded
            JSONArray resp = (JSONArray) HttpsConnection.callAPI(
                    String.format(baseURL, address, firstBlock.toString())
            );

            for(Object obj : resp){
                JSONObject j = (JSONObject) obj;

                String m = j.getString("methodId");

                txns.add( new Transaction(
                        j.getString("blockNumber"),
                        j.getString("hash"),
                        j.getString("timeStamp"),
                        j.getString("from"),
                        j.getString("to"),
                        j.getString("value"),
                        j.getString("gasUsed"),
                        j.getString("contractAddress"),
                        // Indicate if this transaction was an interaction with (or creation of) a contract:
                        ! m.isBlank()
                                && m.compareToIgnoreCase("0x") != 0
                                && ! j.getString("contractAddress").isBlank()
                    ));
                numTxnsReceived++;
            }

            if(numTxnsReceived == 10000) {
                // set the start block of the next request to the block of the last transaction processed
                firstBlock = new BigInteger( txns.get(txns.size()-1).blockNumber() );
            }

            // Etherscan's API limit is 10k transactions per request
        } while(numTxnsReceived == 10000);

        return txns;
    }
}
