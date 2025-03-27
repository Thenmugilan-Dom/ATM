import java.util.ArrayList;
import java.util.Scanner;

import NoteList.Note100;
import NoteList.Note200;
import NoteList.Note2000;
import NoteList.Note500;
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
//    @Override
//    public void depositAmt(Account user) {
//        if (!(user instanceof User)) {
//            System.out.println("Invalid account type.");
//            return;
//        }
//
//        User tempUser = (User) user;
//
//        System.out.print("Enter the Amount: ");
//        long amount = Long.parseLong(ATM.getScanner().nextLine());
//
//        if (amount <= 0) {
//            System.out.println("Invalid amount. Please enter a positive value.");
//            return;
//        }
//
//        System.out.println("Enter denominations:");
//        System.out.print("2000₹ = ");
//        int rs2000 = Integer.parseInt(ATM.getScanner().nextLine());
//        System.out.print("500₹ = ");
//        int rs500 = Integer.parseInt(ATM.getScanner().nextLine());
//        System.out.print("200₹ = ");
//        int rs200 = Integer.parseInt(ATM.getScanner().nextLine());
//        System.out.print("100₹ = ");
//        int rs100 = Integer.parseInt(ATM.getScanner().nextLine());
//
//        long totalAmt = (2000L * rs2000) + (500L * rs500) + (200L * rs200) + (100L * rs100);
//        if (totalAmt != amount) {
//            System.out.println("Mismatch between entered denominations and total amount.");
//            return;
//        }
//
//        // Now, just modify this section to use Notes
//        Note<Notes> noteContainer = new Note<Notes>(); // Using the generic Note container
//
//        // Adding notes directly into the container
//        noteContainer.add(new Note2000("2000", rs2000) {});  // Anonymous class for Notes
//        noteContainer.add(new Note500("500", rs500) {});
//        noteContainer.add(new Note200("200", rs200) {});
//        noteContainer.add(new Note100("100", rs100) {});
//
//        // Update user balance and ATM balance
//        tempUser.setBalance(tempUser.getBalance() + amount);
//        ATM.setBalance(ATM.getBalance() + amount);
//
//        // Add transaction
//        tempUser.getTransHistory().add(new Transaction(tempUser.getId(), "Deposit", amount, tempUser.getBalance()));
//
//        System.out.println("Deposit successful.");
//    }
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

    // Get ATM's note inventory
    Note<Notes> atmInventory = ATM.getNoteInventory();

    // Update ATM's note inventory
    if (rs2000 > 0) {
        atmInventory.getNote("2000").setCount(atmInventory.getNote("2000").getCount() + rs2000);
    }
    if (rs500 > 0) {
        atmInventory.getNote("500").setCount(atmInventory.getNote("500").getCount() + rs500);
    }
    if (rs200 > 0) {
        atmInventory.getNote("200").setCount(atmInventory.getNote("200").getCount() + rs200);
    }
    if (rs100 > 0) {
        atmInventory.getNote("100").setCount(atmInventory.getNote("100").getCount() + rs100);
    }

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

        User tempUser = (User) user;

        System.out.print("Enter the Amount to Withdraw: ");
        long amount;
        long amt ;
        try {
            amount = Long.parseLong(ATM.getScanner().nextLine());
            amt = amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric amount.");
            return;
        }

        if (amount <= 0 || amount > tempUser.getBalance()) {
            System.out.println("Invalid amount. Please check your balance.");
            return;
        }
        // Create the Note container
        Note<Notes> noteContainer = new Note<Notes>();
        // Add all available notes to the noteContainer
        noteContainer = ATM.getNoteInventory(); // Assuming ATM's note inventory is filled

        // Variables to track the number of each denomination of notes dispensed
        int rs2000Count = 0;
        int rs500Count = 0;
        int rs200Count = 0;
        int rs100Count = 0;

        // Try to dispense notes starting from the highest denomination
        for (Notes note : noteContainer.getAll()) {
            if (note == null) continue;  // Skip null notes
            String denomination = note.getNote();
            long availableCount = note.getCount();

            if (amount >= Integer.parseInt(denomination) && availableCount > 0) {
                int requiredNotes = (int) (amount / Integer.parseInt(denomination));
                int notesToDispense = Math.min(requiredNotes, (int) availableCount);

                // Adjust the amount and track notes dispensed
                amount -= notesToDispense * Integer.parseInt(denomination);

                switch (denomination) {
                    case "2000":
                        rs2000Count = notesToDispense;
                        break;
                    case "500":
                        rs500Count = notesToDispense;
                        break;
                    case "200":
                        rs200Count = notesToDispense;
                        break;
                    case "100":
                        rs100Count = notesToDispense;
                        break;
                }

                // If we've already dispensed the full amount, we break
                if (amount == 0) {
                    break;
                }
            }
        }

        if (amount > 0) {
            System.out.println("Sorry, we cannot dispense the exact amount with the available denominations.");
        } else {
            // Update user and ATM balance after successful withdrawal
            tempUser.setBalance(tempUser.getBalance() - (amount));
            ATM.setBalance(ATM.getBalance() - amount);

            // Add transaction
            tempUser.getTransHistory().add(new Transaction(tempUser.getId(), "Withdrawal", amt, tempUser.getBalance()));

            // Display the number of notes dispensed
            System.out.println("Dispensed:");
            if (rs2000Count > 0) {
                System.out.println("₹2000 - " + rs2000Count + " notes");
                noteContainer.getNote("2000").setCount(noteContainer.getNote("2000").getCount() - rs2000Count);
            }
            if (rs500Count > 0) {
                System.out.println("₹500 - " + rs500Count + " notes");
                noteContainer.getNote("500").setCount(noteContainer.getNote("500").getCount() - rs500Count);
            }
            if (rs200Count > 0) {
                System.out.println("₹200 - " + rs200Count + " notes");
                noteContainer.getNote("200").setCount(noteContainer.getNote("200").getCount() - rs200Count);
            }
            if (rs100Count > 0) {
                System.out.println("₹100 - " + rs100Count + " notes");
                noteContainer.getNote("100").setCount(noteContainer.getNote("100").getCount() - rs100Count);
            }

            System.out.println("Withdrawal successful.");
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
}
