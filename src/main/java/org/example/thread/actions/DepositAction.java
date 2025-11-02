package org.example.thread.actions;

import org.example.beans.Account;
import org.example.service.BankOperations;

public class DepositAction implements TransactionAction {
    @Override
    public void execute(BankOperations bank, Account acc, double amount) throws Exception {
        bank.deposit(acc.getAccountNumber(), amount);
    }
}
