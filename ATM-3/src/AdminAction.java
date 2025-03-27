// AdminAction.java
import java.util.ArrayList;
import java.util.Scanner;
import Account.*;
import CommanAction.AdminActions;
import NoteList.Note100;
import NoteList.Note200;
import NoteList.Note2000;
import NoteList.Note500;
import Notes.Notes;
import Transaction.Transaction;

public class AdminAction implements AdminActions {
    @Override
    public Account login(ArrayList<Account> adminList, Scanner scanner) {
        System.out.print("Enter Admin ID: ");
        String id = scanner.nextLine();

        for (Account account : adminList) {
            if (account instanceof Admin && account.getId().equals(id)) {
                for (int attempts = 0; attempts < 3; attempts++) {
                    System.out.print("Enter Admin PIN: ");
                    String pin = scanner.nextLine();
                    if (account.getPassword().equals(pin)) {
                        System.out.println("Admin login successful.");
                        return account;
                    } else {
                        System.out.println("Incorrect PIN. " + (2 - attempts) + " attempts remaining.");
                    }
                }
                System.out.println("Too many failed attempts. Account locked.");
                return null;
            }
        }
        System.out.println("Admin not found.");
        return null;
    }
    @Override
    public void addUser(Scanner scanner) {
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();

        for (Account account : ATM.accountList()) {
            if (account.getId().equals(userId)) {
                System.out.println("User ID already exists.");
                return;
            }
        }

        System.out.print("Enter User Password: ");
        String password = scanner.nextLine();
        User newUser = new User(userId, password);
        System.out.println("User added successfully!");
        ATM.setAccountList(newUser);
    }
    @Override
    public void deleteUser(String id) {
        User user = ATM.findCustomerById(id);
        if (user != null) {
            ATM.accountList().remove(user);
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User not found.");
        }
    }
    @Override
    public void viewAllAccounts() {
        ArrayList<User> users = ATM.getCustomerList();
        if (users.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (User user : users) {
                System.out.println("User ID: " + user.getId() + ", Balance: " + user.getBalance());
            }
        }
    }
    @Override
    public void handleTransactionHistory(Account adminAccount, Scanner scanner) {
        System.out.println("Select an option:");
        System.out.println("1. View your transaction history (Admin)");
        System.out.println("2. View all users' transaction history");
        System.out.println("3. View a particular user's transaction history");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                viewAdminTransactions(adminAccount);
                break;
            case 2:
                viewAllTransactions();
                break;
            case 3:
                viewParticularUserTransactions(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
    @Override
    public void viewAdminTransactions(Account adminAccount) {
        if (adminAccount instanceof Admin) {
            Admin admin = (Admin) adminAccount;
            System.out.println("Transactions for Admin: " + admin.getId());
            ArrayList<Transaction> transactions = admin.getTransHistory();
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (Transaction transaction : transactions) {
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

        if (user != null) {
            System.out.println("Transactions for User: " + user.getId());
            ArrayList<Transaction> transactions = user.getTransHistory();
            if (transactions.isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (Transaction transaction : transactions) {
                    System.out.println(transaction);
                }
            }
        } else {
            System.out.println("User not found.");
        }
    }
    @Override
    public void viewAllTransactions() {
        ArrayList<User> users = ATM.getCustomerList();
        for (User user : users) {
            System.out.println("Transactions for User: " + user.getId());
            if (user.getTransHistory().isEmpty()) {
                System.out.println("No transactions found.");
            } else {
                for (Transaction transaction : user.getTransHistory()) {
                    System.out.println(transaction);
                }
            }
        }
    }
    @Override
    public void addATMoney(Account admin, Scanner scanner) {
        System.out.println("Select note denomination to add: 2000, 500, 200, 100");
        String noteType = scanner.nextLine();

        System.out.println("Enter the amount to add:");
        int amount = Integer.parseInt(scanner.nextLine());
        int count =amount/Integer.parseInt(noteType);
        {
            switch (noteType) {
                case "2000", "500", "200", "100":
                        ATM.getNoteInventory().getNote(noteType).setCount(ATM.getNoteInventory().getNote(noteType).getCount()+count);
                        ATM.setBalance(ATM.getBalance()+amount);
                        System.out.println(ATM.getNoteInventory().getNote(noteType).getCount());

                    break;
                default:
                    System.out.println("Invalid denomination.");
                    break;
            }
        }
    }
}
