package com.bank.thread.actions;

import com.bank.beans.Account;
import com.bank.service.BankOperations;

public interface TransactionAction {
    void execute(BankOperations var1, Account var2, double var3) throws Exception;
}