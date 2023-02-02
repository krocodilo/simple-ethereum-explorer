package explorer.logic;

import explorer.logic.api.*;
import explorer.logic.data.Txn;
import explorer.logic.utils.Utils;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static explorer.logic.utils.Utils.fromWeiToETH;

public class Explorer {

    /**
     * Receives and delegates requests from the frontend (controllers)
     */

    private final String address;
    private final ArrayList<Txn> transactions = new ArrayList<>();


    public Explorer(String address) throws Exception {

        if( ! isValidAddress(address) )
            throw new Exception("Address is invalid.");
        this.address = address;
    }

    /**
     * Get the address you are exploring
     * @return String with the address
     */
    public String getAddress() {
        return address;
    }

    public static boolean isValidAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }

    /**
     * Get ETH balance for a certain address, on a certain date
     * @param address String containing the address
     * @param date if null or empty, will get current balance
     * @return balance
     * @throws Exception with a message
     */
    public static BigDecimal getBalance(String address, String date) throws Exception {

        Infura infura = new Infura();
        BigInteger unixtime;

        if( date == null || date.isBlank() )
            // If unspecified date, get current balance
            return infura.getBalance(address, null);


        // Parse date string into unix time
        try{
            unixtime = Utils.getUnixTimeFromDate( Utils.parseDate(date) );
        } catch (ParseException e) {
            throw new Exception("Invalid date format.", e);
        }

        // Must not be older than the first Ethereum block
        if( unixtime.compareTo(BigInteger.valueOf( 1438214400 )) < 0)
            throw new Exception("There are no blocks at that date.");

        // Validate if date is in the past
        if( unixtime.compareTo(BigInteger.valueOf( Instant.now().getEpochSecond() )) > 0)
            throw new Exception("The specified date must be in the past.");

        BigInteger block = Etherscan.getBlockNumByTimestamp(unixtime);

        return infura.getBalance(address, block);
    }

    /**
     * Get a subsection of the transaction list.
     * @param block Block Number from which to start the list
     * @param pagesize Size of the pages
     * @param page Page Number (min = 1)
     * @return List of transactions
     * @throws Exception If any issue fetching the transactions from the API
     */
    public List<Txn> getPageOfTransactions(BigInteger block, int pagesize, long page) throws Exception {

        // Update txns list
        if( transactions.isEmpty()) // If not empty, must update
            fetchTransactionsSinceBlock(block);

        // Make sure page is not out of range
        if(page > transactions.size()/pagesize)
            page = transactions.size()/pagesize;
        if( page < 1)
            page = 1;

        page--;     // because arrays start at zero

        int firstElem = Math.max( (int) page * pagesize, 0 );
        int lastElem = Math.min( firstElem + pagesize, transactions.size() );

        return transactions.subList( firstElem, lastElem );
    }

    public void fetchTransactionsSinceBlock(BigInteger block) throws Exception {
        // fetch transactions or update the list saved locally

//        if( new BigInteger( transactions.get(0).blockNumber() ).compareTo( block ) > 0)
//            // If 'block' is lower than the earliest block in the list

        transactions.addAll( Etherscan.getTxnsSinceBlock(address, block) );
    }

    /**
     * Get the list of transactions of an address after a certain block
     * @param block Block number
     * @return List of transactions
     * @throws Exception with a message. In case of any error in the connection or the reply from the API
     */
    public ArrayList<Txn> getTxnsSinceBlock(BigInteger block) throws Exception {
        return Etherscan.getTxnsSinceBlock(address, block);
    }

    /**
     * Get all transaction info
     * @param txnHash Transaction Hash
     * @return Map/Dictionary with the name and value of each field
     * @throws Exception with a message. In case of error in the connection to the API, or error in the response
     */
    public static Map<String, String> getTransactionInfo(String txnHash) throws Exception {

        Infura infura = new Infura();

        Transaction txn = infura.getTransactionInfo(txnHash);


        // Must leave this method in order of display in the webpage
        return Map.of(
                "Block Hash", txn.getBlockHash() != null? txn.getBlockHash() : "Pending",
                "Block Number", txn.getBlockNumber().toString() != null? txn.getBlockNumber().toString() : "Pending",
                "Index", txn.getTransactionIndex().toString() != null? txn.getTransactionIndex().toString() : "Pending",  // is null if Pending
                "From", txn.getFrom(),
                "To", txn.getTo() != null? txn.getTo() : "(Contract Creation)",
                "Value Transferred", fromWeiToETH( txn.getValue().toString() ).toPlainString(),
//                "Gas Used", "ND!!!!!!!!!!!!!",   // Only Etherscan shows the amount used. Others show only the committed
                "Gas Limit", txn.getGas().toString(),
                "Gas Price", txn.getGasPrice().toString(),
                "Input", txn.getInput()
        );
    }
}
