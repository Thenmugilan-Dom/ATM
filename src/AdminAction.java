import java.util.ArrayList;
import java.util.Scanner;
import Account.*;
import Notes.Notes;
import CommanAction.*;
import Transaction.Transaction;

public class AdminAction implements AdminActions {
    @Override
    public Account login(ArrayList<Account> adminList, Scanner scanner) { // login method
        System.out.print("Enter Admin ID: ");
        String id = scanner.nextLine();

        for (Account account : adminList) { // iterate all account
            if (account instanceof Admin && account.getId().equals(id)) { // check if admin && id is correct
                for (int attempts = 0; attempts < 3; attempts++) { // attempt check
                    System.out.print("Enter Admin PIN: ");
                    String pin = scanner.nextLine();
                    if (account.getPassword().equals(pin)) {// chech password
                        System.out.println("Admin login successful.");
                        return account; // Return the account if the login is successful.
                    } else { // If the PIN is incorrect:
                        System.out.println("Incorrect PIN. " + (2 - attempts) + " attempts remaining.");
                    }
                }
                System.out.println("Too many failed attempts. Account locked.");
                return null; // attempts are used, lock the account and return null.
            }
        }

        System.out.println("Admin not found.");
        return null;
    }
    @Override
    public  void addUser(Scanner scanner) { // add user
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        for (Account account : ATM.accountList()) { // iterate all account
            if (account.getId().equals(userId)) { // check if user is exist
                System.out.println("User ID already exists.");
                return;  // Exit if the user ID is already taken.
            }
        }

        System.out.print("Enter User Password: ");
        String password = scanner.nextLine();
        User newUser = new User(userId, password);
        System.out.println("USer added successfully !");
        ATM.setAccountList(newUser); // add new user
    }
    @Override
    public  void deleteUser(String id) { //delete user
        User user =  ATM.findCustomerById(id); // method to find user
        if (user != null) { // check if it is null
            ATM.accountList().remove(user); // remove it
            System.out.println("User deleted successfully.");
        } else { // No user
            System.out.println("User not found.");
        }
    }
    @Override
    public  void viewAllAccounts() { // method to view all account
        ArrayList<User> users = ATM.getCustomerList(); // array list of user
        if (users.isEmpty()) { // check if empty
            System.out.println("No accounts found.");
        } else {
            for (User user : users) { // iterate all user
                System.out.println("User ID: " + user.getId() + ", Balance: " + user.getBalance());
            }
        }
    }
    @Override
    public  void handleTransactionHistory(Account adminAccount, Scanner scanner) {
        System.out.println("Select an option:");
        System.out.println("1. View your transaction history (Admin)");
        System.out.println("2. View all users' transaction history");
        System.out.println("3. View a particular user's transaction history");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                // Option 1: Show the admin's transaction history
                viewAdminTransactions(adminAccount);
                break;
            case 2:
                // Option 2: Show all users' transaction history
                viewAllTransactions();
                break;
            case 3:
                // Option 3: Show a particular user's transaction history
                viewParticularUserTransactions(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
    @Override
    public void viewAdminTransactions(Account adminAccount) {
        if (adminAccount instanceof Admin) {  // Check if the account is an Admin.
            Admin admin = (Admin) adminAccount;
            System.out.println("Transactions for Admin: " + admin.getId());
            ArrayList<Transaction> transactions = admin.getTransHistory();
            if (transactions.isEmpty()) { // If no transactions are found
                System.out.println("No transactions found.");
            } else { // If there are transactions
                for (Transaction transaction : transactions) { // Go through each transaction and display it.
                    System.out.println(transaction);
                }
            }
        } else {
            System.out.println("Error: Admin account not found.");
        }
    }
    @Override
    public void viewParticularUserTransactions(Scanner scanner) {
        System.out.print("Enter the User ID to view their transaction history: ");
        String userId = scanner.nextLine();
        User user = ATM.findCustomerById(userId);

        if (user != null) { // If the user is found
            System.out.println("Transactions for User: " + user.getId());
            ArrayList<Transaction> transactions = user.getTransHistory();
            if (transactions.isEmpty()) { // If no transactions are found
                System.out.println("No transactions found.");
            } else { // If there are transactions
                for (Transaction transaction : transactions) { // Display each transaction
                    System.out.println(transaction);
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }
    @Override
    public  void viewAllTransactions() { // view All Transactions
        ArrayList<User> users = ATM.getCustomerList();// array list of user
        for (User user : users) { // iterate all user
            System.out.println("Transactions for User: " + user.getId());
            if (user.getTransHistory().isEmpty()) { // check if empty
                System.out.println("No transactions found.");
            } else {
                for (Transaction transaction : user.getTransHistory()) { // iterate all transaction
                    System.out.println(transaction);
                }
            }
        }
    }
    @Override
    public  void addATMoney(Account adminAccount, Scanner scanner) {
        if (!(adminAccount instanceof Admin)) {  // Check if the account is an admin.
            System.out.println("Error: Only admins can perform this operation.");
            return; // Exit if the account is not an admin.
        }

        System.out.println("Enter total amount to deposit:");
        long amount = Long.parseLong(scanner.nextLine());

        System.out.println("Enter denominations (counts):");
        System.out.print("2000: ");
        int rs2000 = Integer.parseInt(scanner.nextLine());
        System.out.print("500: ");
        int rs500 = Integer.parseInt(scanner.nextLine());
        System.out.print("200: ");
        int rs200 = Integer.parseInt(scanner.nextLine());
        System.out.print("100: ");
        int rs100 = Integer.parseInt(scanner.nextLine());

        long calculatedAmount = rs2000 * 2000L + rs500 * 500L + rs200 * 200L + rs100 * 100L;
        if (calculatedAmount == amount) {   // Check if the calculated amount matches the entered amount.
            for (Notes note : ATM.getNoteInventory()) {  // Iterate through the ATM's note inventory.
                switch (note.getNote()) { // Update the note count for each denomination.
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
            ATM.setBalance(ATM.getBalance() + amount);  // Update the ATM's total balance.
            adminAccount.getTransactions().add(new Transaction("admin", "Deposit", amount, ATM.getBalance())); // Add the deposit transaction to the admin's transaction history.

            System.out.println("Cash added successfully.");
        } else { // If the amount and denominations don't match, show an error.
            System.out.println("Mismatch in total amount and denominations.");
        }
    }
}
