package Account;// Account.java
import java.util.ArrayList;
import Transaction.*;

public abstract class Account {
    private String id; // store id
    private String password; // store password
    private ArrayList<Transaction> transHistory;// store transaction
    public Account(String id, String password) { // Constructor
        this.id = id;
        this.password = password;
        this.transHistory = new ArrayList<>();
    }
    public Account() {} // Constructor
    public String getId() {
        return id;
    }
    // getter and setter
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public ArrayList<Transaction> getTransHistory() {
        return transHistory;
    }
    public void setTransHistory(ArrayList<Transaction> transHistory) {
        this.transHistory = transHistory;
    }
    public ArrayList<Transaction> getTransactions(){
        return this.transHistory;
    }
}
