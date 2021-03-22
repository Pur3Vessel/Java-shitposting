import java.util.ArrayList;

// Твоя темная интернет-личность милой аниме-тянки (на самом деле это разные счета)
public class Account {

    private String nickname;


    private String uuid; // universal unique identifier

    private User holder; // Деанон

    private ArrayList<Transaction> transactions; // Покупки снюса в телеге

    public Account(String nickname, User holder, Bank theBank) {
        this.nickname = nickname;
        this.holder = holder;

        this.uuid = theBank.getNewAccountUUID();

        this.transactions = new ArrayList<Transaction>();
    }
    public String getUUID() {
        return this.uuid;
    }
    public String getSummaryLine() {
        // Чичас будем считать баланс
        double balance = this.getBalance();
        if (balance >= 0) {
            return String.format("%s : $%.02f : %s", this.uuid, balance, this.nickname); // 2 знака после точки (запятой :))
        } else {
            return String.format("%s : $(%.02f) : %s", this.uuid, balance, this.nickname);
        }
    }
    public double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }
    public void printTransHistory() {
        System.out.printf("\nTrans history for account %s\n", this.uuid);
        for (int i = this.transactions.size() - 1; i >= 0; i--) {
            System.out.printf(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }
    public void addTransaction(double amount, String memo) {
        Transaction newTrans = new Transaction(amount,  this, memo);
        this.transactions.add(newTrans);
    }
}
