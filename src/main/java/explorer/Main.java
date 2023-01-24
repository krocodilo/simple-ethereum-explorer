package explorer;

import explorer.logic.utils.HttpsConnection;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println(
                HttpsConnection.callAPI(
                        "https://api.etherscan.io/api?module=account&action=balance&address=0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae&tag=latest&apikey=YourApiKeyToken")
        );
    }
}
