package org.example.thread.actions;

import org.example.beans.Account;
import org.example.service.BankOperations;

public class WithdrawAction implements TransactionAction {
    public void execute(BankOperations bank, Account acc, double amount) throws Exception {
        bank.withDraw(acc.getAccountNumber(), amount);
    }
}