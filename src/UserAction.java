import java.util.ArrayList;
import java.util.Scanner;
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
    public  void depositAmt(Account user) { // to deposit
        if (!(user instanceof User)) { // check if it is user
            System.out.println("Invalid account type.");
            return; // Exit if not a valid user account
        }

        User tempUser = (User) user; // copy user

        System.out.print("Enter the Amount: ");
        long amount = Long.parseLong(ATM.getScanner().nextLine());

        if (amount <= 0) { // validation of amount
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
        if (totalAmt != amount) { // Check if the denominations match the total amount
            System.out.println("Mismatch between entered denominations and total amount.");
            return; // Exit if there's a mismatch
        }

        for (Notes note : ATM.getNoteInventory()) { // setting the amount
            switch (note.getNote()) {
                case "2000":
                    note.setCount(note.getCount() + rs2000);
                    break;
                case "500":
                    note.setCount(note.getCount() + rs500);
                    break;
                case "200":
                    note.setCount(note.getCount() + rs200);
                    break;
                case "100":
                    note.setCount(note.getCount() + rs100);
                    break;
            }
        }

        tempUser.setBalance(tempUser.getBalance() + amount); // setting temp balance of user
        ATM.setBalance(ATM.getBalance() + amount);// setting temp balance of atm
        tempUser.getTransHistory().add(new Transaction(tempUser.getId(), "Deposit", amount, tempUser.getBalance())); // adding transaction

        System.out.println("Deposit successful.");
    }
    @Override
    public  void withdrawAmt(Account user) { // to withdraw
        if (!(user instanceof User)) { // Check if it is a user
            System.out.println("Invalid account type.");
            return;
        }

        User tempUser = (User) user; // Cast to User

        System.out.print("Enter the Amount to Withdraw: ");
        long amount;
        try { // Get the withdrawal amount from the user.
            amount = Long.parseLong(ATM.getScanner().nextLine());
        } catch (NumberFormatException e) { // If the input is not a number.
            System.out.println("Invalid input. Please enter a valid numeric amount.");
            return; // Exit
        }

        // Validate amount
        if (amount <= 0 || amount > tempUser.getBalance()) {
            System.out.println("Invalid amount. Please check your balance.");
            return; // Exit
        }

        ArrayList<Notes> clonedInventory = new ArrayList<>();
        for (Notes note : ATM.getNoteInventory()) {
            clonedInventory.add(note.clone()); // Clone the notes
        }

        ArrayList<String> transactionDetails = new ArrayList<>();
        long remainingAmount = amount;

        for (Notes note : clonedInventory) { // Iterates notes in clonedInventory
            long noteValue = Long.parseLong(note.getNote());
            long count = Math.min(remainingAmount / noteValue, note.getCount()); // Get the number of notes

            if (count > 0) {
                note.setCount(note.getCount() - count); // Reduce note count
                remainingAmount -= count * noteValue;
                transactionDetails.add("Dispensed " + count + " notes of ₹" + noteValue);
            }

            if (remainingAmount == 0) break; // Stop if the exact amount is dispensed
        }

        if (remainingAmount > 0) { // Check if any amount is left undisposed
            System.out.println("Unable to dispense the exact amount. Please try a different value.");
            return;
        }

        ATM.setNoteInventory(clonedInventory); // Update ATM note inventory
        tempUser.setBalance(tempUser.getBalance() - amount); // Update user balance
        ATM.setBalance(ATM.getBalance() - amount); // Update ATM balance

        for (String detail : transactionDetails) { // Display transaction details
            System.out.println(detail);
        }

        // Add transaction to user history
        tempUser.getTransHistory().add(new Transaction(tempUser.getId(), "Withdrawal", amount, tempUser.getBalance()));

        System.out.println("Withdrawal successful.");
    }
    @Override
    public  void viewTransactions(Account user) { // view Transactions
        if (!(user instanceof User)) { // check if user
            System.out.println("Invalid account type.");
            return;
        }

        User tempUser = (User) user; // clone
        ArrayList<Transaction> transactions = tempUser.getTransHistory(); // storing transaction

        if (transactions.isEmpty()) { // If there are no transactions
            System.out.println("No transaction history available.");
        } else { // If there are transactions
            System.out.println("Transaction History:");
            for (Transaction transaction : transactions) { // Go through and display each transaction.
                System.out.println(transaction);
            }
        }
    }
}
