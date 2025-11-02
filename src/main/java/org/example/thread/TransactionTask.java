package org.example.thread;

import org.example.thread.actions.TransactionAction;
import org.example.beans.Account;
import org.example.service.BankOperations;

public class TransactionTask implements Runnable {
    private final BankOperations bank;
    private final Account acc;
    private final double amount;
    private final TransactionAction action;

    public TransactionTask(BankOperations bank, Account acc, double amount, TransactionAction action) {
        this.bank = bank;
        this.acc = acc;
        this.amount = amount;
        this.action = action;
    }

    @Override
    public void run() {
        synchronized (bank) {
            try {
                action.execute(bank, acc, amount);
                System.out.println(Thread.currentThread().getName() + " Transaction completed for " + acc.getAccountNumber());
            } catch (Exception e) {
                System.err.println(Thread.currentThread().getName() + " X " + e.getMessage());
            }
        }
    }
}


