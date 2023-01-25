package explorer.logic;

import explorer.logic.api.*;
import explorer.logic.models.Transaction;
import explorer.logic.utils.HttpsConnection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;

public class Explorer {
    /**
     * Has the logic of the program.
     */

    private final String address;

    public Explorer(String address) {
        this.address = address;
    }

    public BigDecimal getBalance(String date) throws ParseException, HttpsConnection.ConnectionException, HttpsConnection.APIException {

        BigInteger block = Etherscan.getBlockNumByTimestamp(date);

        Infura infura = new Infura();

        return infura.getBalance(address, block);
    }

    public ArrayList<Transaction> getTransactionsSinceBlock(BigInteger block) throws HttpsConnection.APIException, HttpsConnection.ConnectionException {
        return Etherscan.getTxnsSinceBlock(address, block);
    }
}
