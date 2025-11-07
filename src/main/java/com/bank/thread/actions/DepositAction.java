package com.bank.thread.actions;

import com.bank.beans.Account;
import com.bank.service.BankOperations;

public class DepositAction implements TransactionAction {
    @Override
    public void execute(BankOperations bank, Account acc, double amount) throws Exception {
        bank.deposit(acc.getAccountNumber(), amount);
    }
}
