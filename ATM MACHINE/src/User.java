import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {

    private String firstName;

    private String lastName;

    private String uuid; // universal unique identifier

    private byte pinHash[]; // хешируем пароль

    private ArrayList<Account> accounts; // А что, человеку нельзя иметь fake?

    public User(String firstName, String lastName, String pin, Bank theBank)  {
        this.firstName = firstName;
        this.lastName = lastName;
        // А сейчас мы просто обхешируемся (в целях безопасности конечно же)
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // Криптография - это круто!
            this.pinHash = md.digest(pin.getBytes());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("На комиссию по алгоритмам, мудила!");
            e.printStackTrace();
            System.exit(1);
        }

        this.uuid = theBank.getNewUserUUID();

        accounts = new ArrayList<Account>();

        System.out.printf("Ave %s %s with ID %s\n", lastName, firstName, this.uuid);
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    public String getUUID() {
        return this.uuid;
    }

    public boolean validatePin(String aPin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // Еще раз повторюсь, что криптография - отурк!
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("На комиссию по алгоритмам, мудила!");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }
    public void printAccountsSummary() {
        System.out.printf("\n\n%s %s's accounts summary", this.firstName, this.lastName);
        for (int i = 0; i < this.accounts.size(); i++) {
            System.out.printf("\n%d) %s\n", i + 1 ,this.accounts.get(i).getSummaryLine());
        }
        System.out.println();
    }
    public int numAccts() {
        return this.accounts.size();
    }
    public void printAcctTransHistory(int theAcct) {
        this.accounts.get(theAcct).printTransHistory();
    }
    public double getAcctBalance(int ai) {
        return this.accounts.get(ai).getBalance();
    }
    public String getAcctUUID(int ai) {
        return this.accounts.get(ai).getUUID();
    }
    public void addAcctTransaction(int ai, double amount, String memo) {
        this.accounts.get(ai).addTransaction(amount,memo);
    }
}
