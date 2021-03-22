// Ого, какой я важный, мистер бумажный: решил банкомат запилить...

import java.util.Scanner;

public class ATM {
    // Где-нибудь здесь можно приткнуть интерфейс регистрации пользователя (и аккаунтов), так как все необходимые методы в общем то имеются
    // Просто мне лень :)
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Не более чем один сканер читает in (как я понял)
        Bank theBank =  new Bank("Sber");
        User aUser = theBank.addUser("Matvey", "Evgeniev", "666");
        Account newAccount = new Account("Checking", aUser, theBank); // Гарем пополняется расчетным счетом
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        // Банкомат начинает РАБоту
        while (true) {
            curUser = ATM.mainMenuPrompt(theBank, sc); // Обработаем нового user
            ATM.printUserMenu(curUser, sc); // Пока user не вышел, запрашиваем меню
        }
    }
    public static User mainMenuPrompt(Bank theBank, Scanner sc) {
        String userID;
        String pin;
        User authUser;
        do {
            System.out.printf("\nWelcum to %s\n", theBank);
            System.out.print("Enter user ID: ");
            userID = sc.nextLine();
            System.out.print("Enter pin: ");
            pin = sc.nextLine();
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/pin combo");
            }
        } while (authUser == null);
        return authUser;
    }
    // Потом думаю, что переделаю
    public static void printUserMenu(User curUser, Scanner sc) {

        int choice; // тяжелая ноша

        do {
            curUser.printAccountsSummary(); //Досье
            System.out.println("Choose your destiny!");
            // Чего холопу дозволено делать
            System.out.println(" 1) Show transaction history");
            System.out.println(" 2) Withdraw");
            System.out.println(" 3) Deposit");
            System.out.println(" 4) Transfer");
            System.out.println(" 5) Quit");
            System.out.println();
            System.out.print("Choose your fighter: ");
            choice = sc.nextInt();
            if (choice < 1 || choice > 5) {
                System.out.println("You re Invalid");
                continue;
            }
            switch (choice) {
                case 1:
                    ATM.showTransHistory(curUser, sc); // Да да, это история трапов
                    break;
                case 2:
                    ATM.withdraw(curUser, sc);
                    break;
                case 3:
                    ATM.deposit(curUser, sc);
                    break;
                case 4:
                    ATM.transfer(curUser, sc);
                    break;
                case 5:
                    sc.nextLine();
                    break;
            }
        } while (choice != 5);


    }
    public static void showTransHistory(User curUser, Scanner sc) {
        int theAcct;
        // Выбираем тяночку
        do {
            System.out.printf("Choose the account: ");
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= curUser.numAccts()) {
                System.out.println("You re Invalid");
            }
        } while (theAcct < 0 || theAcct >= curUser.numAccts());
        curUser.printAcctTransHistory(theAcct);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void withdraw (User curUser, Scanner sc) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;
        do {
            System.out.printf("Choose the account: ");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= curUser.numAccts()) {
                System.out.println("You re Invalid");
            }
        } while (fromAcct < 0 || fromAcct >= curUser.numAccts());
        acctBal = curUser.getAcctBalance(fromAcct);
        do {
            System.out.printf("Enter the amount (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0 || amount > acctBal) {
                System.out.println("You re Invalid");
            }
        } while (amount < 0 || amount > acctBal);
        sc.nextLine();
        System.out.printf("Enter a mem: ");
        memo = sc.nextLine();
        curUser.addAcctTransaction(fromAcct, -1 * amount, memo);
    }
    public static void deposit (User curUser, Scanner sc) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;
        do {
            System.out.printf("Choose the account: ");
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= curUser.numAccts()) {
                System.out.println("You re Invalid");
            }
        } while (toAcct < 0 || toAcct >= curUser.numAccts());
        do {
            System.out.printf("Enter the amount: ");
            amount = sc.nextDouble();
            if (amount < 0 ) {
                System.out.println("You re Invalid");
            }
        } while (amount < 0);
        sc.nextLine();
        System.out.printf("Enter a mem: ");
        memo = sc.nextLine();
        curUser.addAcctTransaction(toAcct, amount, memo);
    }
    public static void transfer (User curUser, Scanner sc) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do {
            System.out.printf("Choose the account you transfer from: ");
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= curUser.numAccts()) {
                System.out.println("You re Invalid");
            }
        } while (fromAcct < 0 || fromAcct >= curUser.numAccts());
        acctBal = curUser.getAcctBalance(fromAcct);
        do {
            System.out.printf("Choose the account you transfer to: ");
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= curUser.numAccts()) {
                System.out.println("You re Invalid");
            }
        } while (toAcct < 0 || toAcct >= curUser.numAccts());
        do {
            System.out.printf("Enter the amount (max $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0 || amount > acctBal) {
                System.out.println("You re Invalid");
            }
        } while (amount < 0 || amount > acctBal);
        curUser.addAcctTransaction(fromAcct, -1 * amount, String.format("Transfer to account %s", curUser.getAcctUUID(toAcct)));
        curUser.addAcctTransaction(toAcct, amount, String.format("Transfer from account %s", curUser.getAcctUUID(fromAcct)));
    }
}
