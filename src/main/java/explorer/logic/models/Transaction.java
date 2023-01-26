package explorer.logic.models;

public record Transaction(String blockNumber, String hash, String timeStamp,
                          String from, String to, String ethValueWei,
                          String gasUsed, String contractAddress,
                          boolean contractInteraction) {

}
