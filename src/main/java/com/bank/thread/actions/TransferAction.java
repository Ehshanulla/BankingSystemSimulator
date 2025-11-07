package com.bank.thread.actions;

import com.bank.beans.Account;
import com.bank.service.BankOperations;

public class TransferAction implements TransactionAction {
    private final Account toAccount;

    public TransferAction(Account toAccount) {
        this.toAccount = toAccount;
    }

    public void execute(BankOperations bank, Account acc, double amount) throws Exception {
        bank.transfer(acc.getAccountNumber(), this.toAccount.getAccountNumber(), amount);
    }
}