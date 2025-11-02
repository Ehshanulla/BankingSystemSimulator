package org.example.thread.actions;

import org.example.beans.Account;
import org.example.service.BankOperations;

public class TransferAction implements TransactionAction {
    private final Account toAccount;

    public TransferAction(Account toAccount) {
        this.toAccount = toAccount;
    }

    public void execute(BankOperations bank, Account acc, double amount) throws Exception {
        bank.transfer(acc.getAccountNumber(), this.toAccount.getAccountNumber(), amount);
    }
}