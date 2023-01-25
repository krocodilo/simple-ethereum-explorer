package explorer.logic.models;

public class Transaction {

    private String blocknum = "";
    private String hash = "";
    private String timestamp = "";
    private String from = "";
    private String to = "";
    private String ethvalue = "";
    private String gasused = "";
    private String contractaddress = "";   // Used in transactions of contract creation


    public Transaction(String blocknum, String hash, String timestamp, String from, String to, String ethvalue, String gasused, String contractaddress) {
        this.blocknum = blocknum;
        this.hash = hash;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.ethvalue = ethvalue;
        this.gasused = gasused;
        this.contractaddress = contractaddress;
    }
}
