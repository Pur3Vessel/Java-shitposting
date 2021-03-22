import java.util.Date;

public class Transaction {

    private double amount; // Шекели

    private Date timestamp; // Интернет все помнит

    private String memo; // Те самые мемы... (На самом теле это инфа о транзакции)

    private Account inAccount;

    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }
    public Transaction(double amount, Account inAccount, String memo) {
        this(amount, inAccount); // Прикольно, что так можно)
        this.memo = memo;
    }
    public double getAmount() {
        return amount;
    }
    public String getSummaryLine() {
        if (this.amount >= 0) {
            return String.format("%s : $%.02f : %s\n", this.timestamp.toString(), this.amount, this.memo);  // с else свои заморочким с форматом
        } else {
            return String.format("%s : $(%.02f) : %s\n", this.timestamp.toString(), this.amount, this.memo);
        }
    }
}
