package Transaction;

import java.util.Date;

public class Transaction {
    private String transactionId; // id
    private String type; // type of ransaction
    private long amount; // total amount
    private long newBalance; // balance amount
    private Date date; // time
    public Transaction(String transactionId, String type, long amount, long newBalance) { // constructor
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.newBalance = newBalance;
        this.date = new Date();
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", newBalance=" + newBalance +
                ", date=" + date +
                '}';
    }
}
