import java.util.ArrayList;
import java.util.Scanner;
import Notes.*;
import Account.*;
import CommanAction.*;
import Notes.Notes;
import Transaction.Transaction;

public class UserAction implements UserActions {

    @Override
    public Account login(ArrayList<Account> users, Scanner scanner) { // to login
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        for (Account account : users) { // iterates all users
            if (account instanceof User && account.getId().equals(userId)) { // check if user and id
                for (int attempts = 0; attempts < 3; attempts++) { // calculate the attempts
                    System.out.print("Enter User PIN: ");
                    String userPin = scanner.nextLine();

                    if (account.getPassword().equals(userPin)) { //check the user pin
                        System.out.println("User login successful.");
                        return account;
                    } else {
                        System.out.println("Incorrect PIN. " + (2 - attempts) + " attempts remaining.");
                    }
                }
                System.out.println("Account locked due to too many failed attempts.");
                return null; // If all attempts are used, lock the account and return null.
            }
        }

        System.out.println("Invalid User ID.");
        return null;
    }
    @Override
    public void depositAmt(Account user) {
        if (!(user instanceof User)) {
            System.out.println("Invalid account type.");
            return;
        }

        User tempUser = (User) user;

        System.out.print("Enter the Amount: ");
        long amount = Long.parseLong(ATM.getScanner().nextLine());

        if (amount <= 0) {
            System.out.println("Invalid amount. Please enter a positive value.");
            return;
        }

        System.out.println("Enter denominations:");
        System.out.print("2000₹ = ");
        int rs2000 = Integer.parseInt(ATM.getScanner().nextLine());
        System.out.print("500₹ = ");
        int rs500 = Integer.parseInt(ATM.getScanner().nextLine());
        System.out.print("200₹ = ");
        int rs200 = Integer.parseInt(ATM.getScanner().nextLine());
        System.out.print("100₹ = ");
        int rs100 = Integer.parseInt(ATM.getScanner().nextLine());

        long totalAmt = (2000L * rs2000) + (500L * rs500) + (200L * rs200) + (100L * rs100);
        if (totalAmt != amount) {
            System.out.println("Mismatch between entered denominations and total amount.");
            return;
        }

        // Now, just modify this section to use Notes
        Note<Notes> noteContainer = new Note<Notes>(); // Using the generic Note container

        // Adding notes directly into the container
        noteContainer.add(new Notes("2000", rs2000) {});  // Anonymous class for Notes
        noteContainer.add(new Notes("500", rs500) {});
        noteContainer.add(new Notes("200", rs200) {});
        noteContainer.add(new Notes("100", rs100) {});

        // Update user balance and ATM balance
        tempUser.setBalance(tempUser.getBalance() + amount);
        ATM.setBalance(ATM.getBalance() + amount);

        // Add transaction
        tempUser.getTransHistory().add(new Transaction(tempUser.getId(), "Deposit", amount, tempUser.getBalance()));

        System.out.println("Deposit successful.");
    }
    @Override
    public void  withdrawAmt(Account user) {
        if (!(user instanceof User)) {
            System.out.println("Invalid account type.");
            return;
        }

        User customer = (User) user;

        long amt = Long.parseLong(ATM.getScanner().nextLine());// it is store the withdrawl amount
        long finalAmt=amt;//It is used to final amount
        ArrayList<String> Note_tr=new ArrayList<>();// it is used to display the Note's transaction
        Note<Notes> Note_Dp=new Note<Notes>();// it is used to clone the Note's ArrayList
        for (Notes note:ATM.getNoteInventory().getAll()){// it used to get the note's object in Arraylist
            Note_Dp.add(note.clone());// It is used to add the clone the the orginal notes arraylist
        }
        while(amt!=0){// it is where the amount is != 0
            for (Notes note:Note_Dp.getAll()){// it is used to get the Note to from cloned arraylist object of Note
                String notes= note.getNote();// It is used to store the note
                switch (notes){// it where checks for the notes
                    case "2000","500","200","100":// it is notes with the notes name
                        amt=(long) perform_Withdrawal(amt,Note_tr,note);// this method made an calculation and retrun the final amount
                        break;
                }
            }
            if(amt==0) { // if final amount is 0 it will enter the this statement
                ATM.setNoteInventory(Note_Dp);// it is set the the cloned arraylist with the orginal arraylist
                customer.setBalance(customer.getBalance()-finalAmt);//Reasignning the customers balance
                ATM.setBalance(ATM.getBalance()-finalAmt);//Reasignning the ATM balance
                for (String notes:Note_tr){// Display the Notes Transaction
                    System.out.println("* "+notes);
                }
                user.getTransactions().add(new Transaction(customer.getId(), "Withdrawal", finalAmt, customer.getBalance()));//Add the trnsaction with customers account
            }
            else{
                System.out.println("There is no denomination! Reenter the amount");
            }
            break;
        }
        for (Notes note:ATM.getNoteInventory().getAll()){// it display the balance notes in the list
            System.out.printf("Note:%S Count%d \n",note.getNote(),note.getCount());
        }
    }
    @Override
    public void viewTransactions(Account user) {
        if (!(user instanceof User)) {
            System.out.println("Invalid account type.");
            return;
        }

        User tempUser = (User) user;
        ArrayList<Transaction> transactions = tempUser.getTransHistory();

        if (transactions.isEmpty()) {
            System.out.println("No transaction history available.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }
    public double perform_Withdrawal(double Amount,ArrayList<String> note_Tr,Notes note){//It is used calculate the withdrawal denomination
        long count =(long) (Amount/Long.parseLong(note.getNote()));// it is used to store the denomination count for withdrawal amount
        if (Long.parseLong(note.getNote())<=Amount&& note.getCount()>0){// it check the denomination and and those count is greater than 0
            if (count<= note.getCount()) {//if count is greater than or equal to denomination count
                Amount = Amount - (count * Long.parseLong(note.getNote()));//reduce the amount and store it
                note.setCount(note.getCount() - count);//set the note's count
                note_Tr.add("You got " + note.getNote() + " count " + count);//add it on notes transaction
            }
            else {
                Amount = Amount - (note.getCount() * Long.parseLong(note.getNote()));//reduce the amount and store it
                note.setCount(0);//set the note's count
                note_Tr.add("You got " + note.getNote() + " count " + count);//add it on notes transaction
            }
            return Amount;
        }
        return Amount;
    }
}
