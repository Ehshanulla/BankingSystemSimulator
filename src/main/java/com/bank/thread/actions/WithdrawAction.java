package com.bank.thread.actions;

import com.bank.beans.Account;
import com.bank.service.BankOperations;

public class WithdrawAction implements TransactionAction {
    public void execute(BankOperations bank, Account acc, double amount) throws Exception {
        bank.withDraw(acc.getAccountNumber(), amount);
    }
}