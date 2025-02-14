package CommanAction;
import Account.*;
import java.util.ArrayList;
import java.util.Scanner;

public interface Action
{
    Account login(ArrayList<Account> adminList, Scanner scanner);
}
