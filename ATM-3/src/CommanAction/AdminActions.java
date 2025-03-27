package CommanAction;
import Account.*;
import java.util.Scanner;

public interface AdminActions extends Action {
    void addUser(Scanner scanner);
    void deleteUser(String id);
    void viewAllAccounts();
    void handleTransactionHistory(Account adminAccount, Scanner scanner);
    void viewAdminTransactions(Account adminAccount);
    void viewParticularUserTransactions(Scanner scanner);
    void viewAllTransactions();
    void addATMoney(Account adminAccount, Scanner scanner);

}
