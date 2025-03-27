package CommanAction;
import Account.*;

public interface UserActions extends Action {
    void depositAmt(Account user);
    void withdrawAmt(Account user);
    void viewTransactions(Account user);
}