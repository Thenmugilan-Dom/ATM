package Account;

public class User extends Account {
    private long balance; // store balance
    public User(String id , String pin){ // constructor
        super(id,pin); // Call the constructor of the superclass (Account) to initialize the id and pin.
    }
    // Getter for balance
    public long getBalance() {
        return balance; // Return the balance field of the object.
    }
    // Setter for balance
    public void setBalance(long balance) {
        this.balance = balance; // Set the object's balance field to the provided balance value.
    }
}
