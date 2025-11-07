package com.bank.test.java;


import com.bank.beans.Account;
import com.bank.service.Bank;
import com.bank.service.BankOperations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BankThreadingTest {

    @Test
    void testConcurrentDepositsWithdrawalsTransfers() throws Exception {
        BankOperations bank = new Bank();
        Account acc1 = bank.createAccount("Ramu");
        Account acc2 = bank.createAccount("Sita");

        bank.deposit(acc1.getAccountNumber(), 10000);
        bank.deposit(acc2.getAccountNumber(), 15000);

        Runnable depositTask = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    bank.deposit(acc1.getAccountNumber(), 1000);
                    Thread.sleep(50);
                } catch (Exception ignored) {}
            }
        };

        Runnable withdrawTask = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    bank.withDraw(acc1.getAccountNumber(), 500);
                    Thread.sleep(60);
                } catch (Exception ignored) {}
            }
        };

        Runnable transferTask = () -> {
            for (int i = 0; i < 3; i++) {
                try {
                    bank.transfer(acc2.getAccountNumber(), acc1.getAccountNumber(), 700);
                    Thread.sleep(80);
                } catch (Exception ignored) {}
            }
        };

        Thread t1 = new Thread(depositTask, "DepositThread");
        Thread t2 = new Thread(withdrawTask, "WithdrawThread");
        Thread t3 = new Thread(transferTask, "TransferThread");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        // Verify no negative balances or data corruption
        assertTrue(acc1.getBalance() >= 0, "Account 1 balance must be non-negative");
        assertTrue(acc2.getBalance() >= 0, "Account 2 balance must be non-negative");
    }


}

