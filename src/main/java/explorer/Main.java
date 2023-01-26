package explorer;

import explorer.logic.Explorer;
import explorer.logic.models.Transaction;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) throws Exception {


        Explorer eth = new Explorer("0x5abfec25f74cd88437631a7731906932776356f9");

        System.out.println("\n\n ---> " +
                eth.getBalance("2020-01-10").toString()
                + " ETH\n\n"
        );

        for(Transaction t : eth.getTransactionsSinceBlock(BigInteger.ZERO))
            System.out.println("\n" + t.toString());
    }
}
