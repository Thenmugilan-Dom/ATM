import Account.*;
import Notes.Notes;
import Notes.Note;
import NoteList.Note100;
import NoteList.Note200;
import NoteList.Note500;
import NoteList.Note2000;
import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
private static long balance;
    private static ArrayList<Account> accountList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static AdminAction a = new AdminAction();
    private static UserAction u = new UserAction();
    // Instantiate the Note class for different denominations
    private static Note<Notes> noteInventory = new Note<Notes>();
    public static Scanner getScanner() {
        return scanner;
    }
    public static Note<Notes> getNoteInventory() {
        return noteInventory;
    }
    public static long getBalance() {
        return balance;
    }
    public static void setAccountList(Account account) {
        accountList.add(account);
    }
    public static void setBalance(long balance) {
        ATM.balance = balance;
    }
    public static ArrayList<Account> accountList() {
        return accountList;
    }

    public static void start() throws CloneNotSupportedException {
        accountList.add(new Admin("123", "123")); // Adding default admin

        // Adding default notes to inventory (just for initialization)
        noteInventory.add(new Note2000("2000", 0));
        noteInventory.add(new Note500("500", 0));
        noteInventory.add(new Note200("200", 0));
        noteInventory.add(new Note100("100", 0));

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.println("Enter choice:");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                Account admin = a.login(accountList, scanner);
                if(admin != null) {
                    adminMenu(admin);
                }
            } else if (choice == 2) {
                Account user = u.login(accountList, scanner);
                if(user != null) {
                    userMenu(user);
                }
            } else if (choice == 3) {
                System.out.println("Exiting...");
                isRunning = false;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
    public static void adminMenu(Account admin) {
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("1. Add User");
            System.out.println("2. Delete User");
            System.out.println("3. View Transactions");
            System.out.println("4. View Accounts");
            System.out.println("5. Manage ATM Inventory");
            System.out.println("6. Exit");
            System.out.println("Enter choice:");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                a.addUser(scanner);
            } else if (choice == 2) {
                System.out.println("Enter the User ID to delete:");
                String userId = scanner.nextLine();
                a.deleteUser(userId);
            } else if (choice == 3) {
                a.handleTransactionHistory(admin, scanner);
            } else if (choice == 4) {
                a.viewAllAccounts();
            } else if (choice == 5) {
                manageATMInventory(admin);
            } else if (choice == 6) {
                System.out.println("Exiting admin menu...");
                isRunning = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    public static void userMenu(Account user) { // method to chose userMenu
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transactions");
            System.out.println("5. Exit");
            System.out.print("Enter choice:");
            int choice = Integer.parseInt(scanner.nextLine());

            // call the respective method
            if (choice == 1) {
                if (!(user instanceof User)) { // Check if the account type is not an instance of User
                    System.out.println("Invalid account type.");
                    return; // Exit the userMenu() method if the account type is invalid.
                }
                    System.out.println("Your balance: " + ((User) user).getBalance()); // Print the user's balance by casting the user account to a User object.
            } else if (choice == 2) {
                u.depositAmt(user); // Call the depositAmt() method from UserAction to deposit money into the account.
            } else if (choice == 3) {
                u.withdrawAmt(user); // Call the withdrawAmt() method from UserAction to withdraw money from the account.
            } else if (choice == 4) {
                u.viewTransactions(user); // Call the viewTransactions() method from UserAction to view the user's transaction history.
            } else if (choice == 5) {
                System.out.println("Exiting user menu...");
                isRunning = false;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    public static void manageATMInventory(Account admin) {
        System.out.println("1. Add Cash");
        System.out.println("2. View Cash Inventory");
        System.out.println("3. Exit");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 1) {
            a.addATMoney(admin, scanner);
        } else if (choice == 2) {
            System.out.println("ATM Cash Inventory:");
            for(Notes note:noteInventory.getAll()) {
                System.out.println(note.getNote()+": " + note.getCount());
            }
        } else {
            System.out.println("Exiting ATM inventory management.");
        }
    }

    public static User findCustomerById(String id){ // to find Customer By Id
        for(Account user: accountList ) // Loop through each Account in accountList to search for the user with the given ID.
        {
            if(user.getId().equals(id)){ // Check if the id of the current user matches the provided id.
                return (User)user; // If the ID matches, cast the user to a User object and return it.
            }
        }
        return null; // If no user with the provided ID is found, return null.
    }

    public static ArrayList<User> getCustomerList(){ // to get Customer List
        ArrayList<User> tempUser = new ArrayList<>(); // Initialize a new ArrayList<User> called tempUser to store users.
        for(Account account: accountList ) // Loop through each Account in accountList to check if it is a User.
        {
            if(account instanceof User){ // Check if the current account is an instance of the User class.
                tempUser.add((User)account); // Cast the account to a User and add it to the tempUser list.
            }
        }
        return tempUser;
    }
}
