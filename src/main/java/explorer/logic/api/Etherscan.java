package explorer.logic.api;

import explorer.logic.models.Transaction;
import explorer.logic.utils.HttpsConnection;
import explorer.logic.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Etherscan {

    private static final String etherscanApiKey = "Q5RM9IVMRDHPK1VFYP7JUDFBC83BJG4R9I";

    private static final String ethercanApiStart = "https://api.etherscan.io/api";

    /**
     * Finds the number of the first block that was mined in that day
     * @param date - String in format "YYYY-MM-DD"
     * @return Block Number
     * @throws Exception
     */
    public static BigInteger getBlockNumByTimestamp(BigInteger unixtime) throws Exception {

        String baseURL = ethercanApiStart +
                "?module=block&action=getblocknobytime&timestamp=%s&closest=after&apikey=" + etherscanApiKey;

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
            JSONArray resp = null;
            try {
                resp = (JSONArray) HttpsConnection.callAPI(
                        String.format(baseURL, address, firstBlock.toString())
                );
            } catch (Exception e) {
                e.printStackTrace();    // TODO
            }

            for(Object obj : resp){
                JSONObject j = (JSONObject) obj;

                String txnFee = new BigInteger(j.getString("gasUsed")).multiply(new BigInteger(j.getString("gasPrice"))).toString();

                // Save JSON data to a transaction object
                txns.add( new Transaction(
                        j.getString("blockNumber"),
                        j.getString("hash"),
                        Utils.getDateFromUnixTime( Long.parseLong(j.getString("timeStamp")) ),
                        j.getString("from"),

                        // If To field is empty, it signifies a contract was created:
                        j.getString("to").isBlank() ? j.getString("contractAddress") : j.getString("to"),

                        // Save value transfered in ETH, instead of Wei units:
                        Convert.fromWei(j.getString("value"), Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_EVEN).toPlainString(),

                        // Save txn fee amount in ETH instead of Wei units:
                        Convert.fromWei(txnFee, Convert.Unit.ETHER).setScale(4, RoundingMode.HALF_EVEN).toPlainString(),

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
        } while(numTxnsReceived == 10000);

        return txns;
    }
}
