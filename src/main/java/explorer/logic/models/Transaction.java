package explorer.logic.models;

public record Transaction(String blockNum, String hash, String timestamp,
                          String from, String to, String ethValue,
                          String gasUsed, String contractAddress) {

}
