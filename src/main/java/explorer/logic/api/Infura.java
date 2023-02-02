package explorer.logic.api;

import explorer.logic.utils.HttpsConnection;
import explorer.logic.utils.Utils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class Infura {

    private final HttpService apiURL;
    private final Web3j infuraAPI;

    public Infura() {

        apiURL = new HttpService(
                "https://mainnet.infura.io/v3/05c4e998439e4427b59dd94841f75175"
        );

        infuraAPI = Web3j.build(apiURL);
    }

    public void close() {
        try {
            apiURL.close();       // I think it stays open even if the program reaches the
                                // end of code, keeping it awake
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the ETH balance on a given block, or the current balance
     * @param address String containing the address
     * @param block Block Number. If null, gets the current balance
     * @return BigDecimal ETH amount
     * @throws HttpsConnection.ConnectionException with a message. In case of communication error.
     */
    public BigDecimal getBalance(String address, BigInteger block) throws HttpsConnection.ConnectionException {
        BigInteger wei;
        try {
            wei = infuraAPI.ethGetBalance(address, block == null ? DefaultBlockParameter.valueOf("latest") : DefaultBlockParameter.valueOf(block))
                    .send().getBalance();
        } catch (IOException e){
            throw new HttpsConnection.ConnectionException("Error in API call to provider Infura.", e);
        }

        return Utils.fromWeiToETH(wei.toString());
    }

    /**
     * Gets all information about a given transaction
     * @param txnHash String with the transaction hash
     * @return Transaction object
     * @throws HttpsConnection.ConnectionException with a message. In case of communication error.
     * @throws HttpsConnection.APIException with a message. In case the API reply is empty.
     */
    public Transaction getTransactionInfo(String txnHash) throws HttpsConnection.ConnectionException, HttpsConnection.APIException {
        Optional<Transaction> resp;
        try {
            resp = infuraAPI.ethGetTransactionByHash(txnHash).send().getTransaction();
        } catch (IOException e){
            throw new HttpsConnection.ConnectionException("Error in API call to provider Infura.", e);
        }

        if( resp.isEmpty() )
            throw new HttpsConnection.APIException("Infura API is unable to get that transaction.");

        return resp.get();
    }
}
