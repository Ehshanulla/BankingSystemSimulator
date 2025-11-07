package com.bank.thread;

import com.bank.beans.Account;
import com.bank.service.BankOperations;
import com.bank.thread.actions.DepositAction;
import com.bank.thread.actions.TransactionAction;
import com.bank.thread.actions.TransferAction;
import com.bank.thread.actions.WithdrawAction;

import java.util.*;

public class TransactionManager {

    private final BankOperations bank;
    private final Scanner sc;

    // Default constructor (interactive mode)
    public TransactionManager(BankOperations bank) {
        this(bank, new Scanner(System.in));
    }

    // Overloaded constructor for testing (inject fake input)
    public TransactionManager(BankOperations bank, Scanner scanner) {
        this.bank = bank;
        this.sc = scanner;
    }

    public void startDemo() {
        try {
            System.out.print("\nEnter number of transactions: ");
            if (!sc.hasNextInt()) {
                System.out.println("No input available for transaction count. Exiting...");
                return;
            }
            int n = sc.nextInt();
            sc.nextLine(); // consume newline

            List<Thread> threads = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                System.out.println("\n--- Transaction " + i + " ---");

                System.out.print("Enter account number: ");
                if (!sc.hasNextLine()) break;
                String accNo = sc.nextLine().trim();

                System.out.print("Enter amount: ");
                if (!sc.hasNextDouble()) break;
                double amt = sc.nextDouble();
                sc.nextLine();

                System.out.print("Enter type (D/W/T): ");
                if (!sc.hasNextLine()) break;
                String type = sc.nextLine().trim().toUpperCase();

                Account acc;
                try {
                    acc = bank.findAccount(accNo);
                } catch (Exception e) {
                    System.err.println("X " + e.getMessage());
                    i--;
                    continue;
                }

                TransactionAction action;
                try {
                    action = getTransactionAction(type, acc, amt);
                } catch (Exception e) {
                    System.err.println("X " + e.getMessage());
                    i--;
                    continue;
                }

                Thread t = new Thread(new TransactionTask(bank, acc, amt, action), "Tx-" + i);
                threads.add(t);
            }

            System.out.println("\n Starting all transactions...");
            threads.forEach(Thread::start);

            for (Thread t : threads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    System.out.println("⚠ Thread interrupted: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("⚠ Unexpected error: " + e.getMessage());
        }
    }

    private TransactionAction getTransactionAction(String type, Account acc, double amt) throws Exception {
        switch (type) {
            case "D":
                return new DepositAction();
            case "W":
                return new WithdrawAction();
            case "T":
                System.out.print("Enter receiver account number: ");
                if (!sc.hasNextLine()) throw new Exception("No receiver account input provided");
                String toAccNo = sc.nextLine().trim();
                Account toAcc = bank.findAccount(toAccNo);
                return new TransferAction(toAcc);
            default:
                throw new Exception("Invalid transaction type: " + type);
        }
    }
}
