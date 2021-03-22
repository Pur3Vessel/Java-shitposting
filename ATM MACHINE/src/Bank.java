import java.util.ArrayList;
import java.util.Random;
// Варганим свой Сбер

public class Bank {

    private String name;

    private ArrayList<User> users;

    private ArrayList<Account> accounts; // удобней будет получить fake тянки, не используя посредником потного user
    public Bank(String name) {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();
    }
    public String getNewUserUUID() {
        String uuid;
        Random rng = new Random(); // О, повезло повезло
        int len = 7;
        boolean repeat = false; // Если повторка
        do {
            repeat = false;
            uuid = "";
            for (int i = 0; i < len; i++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            for (User u:  this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    repeat = true;
                    break;
                }
            }
        } while (repeat);
        return uuid;
    }
    // Неут мыслей в голове, Ctrl c + Ctrl v...
    public String getNewAccountUUID() {
        String uuid;
        Random rng = new Random(); // О, повезло повезло
        int len = 7;
        boolean repeat = false; // Если повторка
        do {
            repeat = false;
            uuid = "";
            for (int i = 0; i < len; i++) {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }
            for (Account a:  this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    repeat = true;
                    break;
                }
            }
        } while (repeat);
        return uuid;
    }
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    public User addUser(String firstname, String lastname, String pin) {
        User newUser = new User(firstname, lastname, pin, this);
        this.users.add(newUser);
        Account newAccount = new Account("Savings", newUser, this); // Каждому по тянке (сберагательному счету)!
        newUser.addAccount(newAccount);
        this.addAccount(newAccount); // А гарем тяночек то пополняется
        return newUser;
    }
    // Вы ввели неправильный логин или пароль...
    public User userLogin(String userID, String pin) {
        for (User u : this.users) {
            if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                return u;
            }
        }
        return null;
    }
    public String toString () {
        return this.name;
    }
}
