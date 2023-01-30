package explorer.logic.api;

import explorer.logic.utils.HttpsConnection;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

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
            apiURL.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getBalance(String address, BigInteger block) throws HttpsConnection.ConnectionException {
        EthGetBalance apiresp = null;
        BigInteger wei;
        try {
            apiresp = infuraAPI.ethGetBalance(address, DefaultBlockParameter.valueOf(block))
                    .send();
            wei = apiresp.getBalance();
        } catch (Exception e){
            throw new HttpsConnection.ConnectionException(e);
        }

        return Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
    }
}
