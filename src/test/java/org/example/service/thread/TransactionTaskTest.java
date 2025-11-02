package org.example.service.thread;

import org.example.thread.actions.DepositAction;
import org.example.thread.actions.TransactionAction;
import org.example.thread.actions.TransferAction;
import org.example.thread.actions.WithdrawAction;
import org.example.beans.Account;
import org.example.exceptions.*;
import org.example.service.Bank;
import org.example.service.BankOperations;
import org.example.thread.*;
import org.junit.jupiter.api.*;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionTaskTest {

    private static BankOperations bank;
    private static Account acc;

    @BeforeAll
    static void setup() throws InvalidNameException, InvalidAmountException, AccountNotFoundException {
        bank = new Bank();
        acc = bank.createAccount("Charlie");
        bank.deposit(acc.getAccountNumber(), 500);
    }

    @Test
    @Order(1)
    void testDepositTask() throws InterruptedException, AccountNotFoundException {
        double initial = bank.findAccount(acc.getAccountNumber()).getBalance();

        TransactionAction depositAction = new DepositAction();
        TransactionTask depositTask = new TransactionTask(bank, acc, 200, depositAction);

        Thread t1 = new Thread(depositTask, "DepositThread");
        t1.start();
        t1.join();

        double finalBalance = bank.findAccount(acc.getAccountNumber()).getBalance();
        assertEquals(initial + 200, finalBalance, "Deposit task failed to add balance correctly");
    }

    @Test
    @Order(2)
    void testWithdrawTask() throws InterruptedException, AccountNotFoundException {
        double initial = bank.findAccount(acc.getAccountNumber()).getBalance();

        TransactionAction withdrawAction = new WithdrawAction();
        TransactionTask withdrawTask = new TransactionTask(bank, acc, 100, withdrawAction);

        Thread t2 = new Thread(withdrawTask, "WithdrawThread");
        t2.start();
        t2.join();

        double finalBalance = bank.findAccount(acc.getAccountNumber()).getBalance();
        assertEquals(initial - 100, finalBalance, "Withdraw task failed to subtract balance correctly");
    }

    @Test
    @Order(3)
    void testTransferTask() throws InterruptedException, AccountNotFoundException, InvalidNameException, InvalidAmountException {
        Account receiver = bank.createAccount("David");
        bank.deposit(receiver.getAccountNumber(), 100);

        double senderInitial = bank.findAccount(acc.getAccountNumber()).getBalance();
        double receiverInitial = bank.findAccount(receiver.getAccountNumber()).getBalance();

        TransactionAction transferAction = new TransferAction(receiver);
        TransactionTask transferTask = new TransactionTask(bank, acc, 50, transferAction);

        Thread t3 = new Thread(transferTask, "TransferThread");
        t3.start();
        t3.join();

        double senderFinal = bank.findAccount(acc.getAccountNumber()).getBalance();
        double receiverFinal = bank.findAccount(receiver.getAccountNumber()).getBalance();

        assertEquals(senderInitial - 50, senderFinal, "Transfer did not deduct from sender correctly");
        assertEquals(receiverInitial + 50, receiverFinal, "Transfer did not credit receiver correctly");
    }

    @Test
    @Order(4)
    void testInvalidTransactionHandledGracefully() throws InterruptedException {
        // Anonymous invalid action that throws exception intentionally
        TransactionAction invalidAction = (bankOps, account, amount) -> {
            throw new UnsupportedOperationException("Invalid transaction type");
        };

        TransactionTask invalidTask = new TransactionTask(bank, acc, 100, invalidAction);
        Thread t4 = new Thread(invalidTask, "InvalidActionThread");

        assertDoesNotThrow(() -> {
            t4.start();
            t4.join();
        }, "Invalid transaction type should not crash thread");
    }

    @Test
    @Order(5)
    void testInvalidAccountHandledGracefully() {
        // Simulate user input for 1 transaction
        String fakeInput =
                "1\n" +        // number of transactions
                        "INVALID123\n" + // invalid account number
                        "100\n" +       // amount
                        "D\n";          // type (Deposit)

        Scanner fakeScanner = new Scanner(fakeInput);
        TransactionManager tm = new TransactionManager(bank, fakeScanner);

        assertDoesNotThrow(
                tm::startDemo,
                "Invalid accounts should be handled without crashing"
        );
    }

}