package explorer.logic;

import explorer.logic.api.*;
import explorer.logic.models.Transaction;
import explorer.logic.utils.HttpsConnection;
import org.web3j.crypto.WalletUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Explorer {

    /**
     * Receives and delegates requests from the UI
     */

    private final String address;
    private final ArrayList<Transaction> transactions = new ArrayList<>();


    public Explorer(String address) throws Exception {

        if( ! isValidAddress(address) )
            throw new Exception("Address is invalid.");
        this.address = address;
    }

    public static boolean isValidAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }

    public static BigDecimal getBalance(String address, String date) throws Exception {

        if( ! isValidAddress(address) )
            throw new Exception("Address is invalid.");

        BigInteger block = Etherscan.getBlockNumByTimestamp(date);

        Infura infura = new Infura();

        return infura.getBalance(address, block);
    }

    public String getAddress() {
        return address;
    }

    public List<Transaction> getPageOfTransactions(BigInteger block, int pagesize, long page) throws HttpsConnection.APIException, HttpsConnection.ConnectionException {

        // Update txns list
        if( transactions.isEmpty()) // Todo - temporary. If not empty, must update
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

    public void fetchTransactionsSinceBlock(BigInteger block) throws HttpsConnection.APIException, HttpsConnection.ConnectionException {

//        if( new BigInteger( transactions.get(0).blockNumber() ).compareTo( block ) > 0){
//            // If 'block' is lower than the earliest block in the list
//
//
//        }

        transactions.addAll( Etherscan.getTxnsSinceBlock(address, block) );
    }

    // Temp
    public ArrayList<Transaction> getTxnsSinceBlock(BigInteger block) throws HttpsConnection.APIException, HttpsConnection.ConnectionException {
        return Etherscan.getTxnsSinceBlock(address, block);
    }
}
