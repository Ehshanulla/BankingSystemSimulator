package org.example.thread.actions;

import org.example.beans.Account;
import org.example.service.BankOperations;

public interface TransactionAction {
    void execute(BankOperations var1, Account var2, double var3) throws Exception;
}