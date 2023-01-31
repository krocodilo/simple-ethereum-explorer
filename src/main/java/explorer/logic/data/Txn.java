package explorer.logic.data;

public record Txn(
        String blockNumber,
        String hash,
        String timeStamp,
        String from,
        String to,
        String ethValue,
        String txnFee,
        boolean contractCreated,
        boolean contractInteraction,  // Indicate if this transaction was an interaction with a contract
        boolean hasError
) {

}
