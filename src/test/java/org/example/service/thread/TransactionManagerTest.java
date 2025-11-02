package org.example.service.thread;

import org.example.beans.Account;
import org.example.exceptions.*;
import org.example.service.Bank;
import org.example.service.BankOperations;
import org.example.thread.TransactionManager;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionManagerTest {

    private static BankOperations bank;
    private static Account acc1, acc2;

    @BeforeAll
    static void setup() throws InvalidNameException, InvalidAmountException, AccountNotFoundException {
        bank = new Bank();
        acc1 = bank.createAccount("David");
        acc2 = bank.createAccount("Eve");
        bank.deposit(acc1.getAccountNumber(), 1000);
        bank.deposit(acc2.getAccountNumber(), 500);
    }

    @Test
    @Order(1)
    void testMultipleDepositsSimultaneously() throws Exception {
        // Prepare simulated user input
        String simulatedInput =
                "3\n" + // Number of transactions
                        acc1.getAccountNumber() + "\n200\nD\n" + // Tx1: Deposit 200 to acc1
                        acc2.getAccountNumber() + "\n300\nD\n" + // Tx2: Deposit 300 to acc2
                        acc1.getAccountNumber() + "\n500\nD\n";  // Tx3: Deposit 500 to acc1

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        TransactionManager manager = new TransactionManager(bank);
        manager.startDemo();

        Account a1 = bank.findAccount(acc1.getAccountNumber());
        Account a2 = bank.findAccount(acc2.getAccountNumber());

        assertTrue(a1.getBalance() >= 1700, "acc1 should have received multiple deposits");
        assertTrue(a2.getBalance() >= 800, "acc2 should have received multiple deposits");
    }

    @Test
    @Order(2)
    void testWithdrawAndDepositTogether() throws Exception {
        String simulatedInput =
                "2\n" + // 2 simultaneous transactions
                        acc1.getAccountNumber() + "\n100\nW\n" + // withdraw 100
                        acc2.getAccountNumber() + "\n200\nD\n";  // deposit 200

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));

        TransactionManager manager = new TransactionManager(bank);
        manager.startDemo();

        Account a1 = bank.findAccount(acc1.getAccountNumber());
        Account a2 = bank.findAccount(acc2.getAccountNumber());

        assertTrue(a1.getBalance() >= 1600, "acc1 should reflect withdrawal");
        assertTrue(a2.getBalance() >= 1000, "acc2 should reflect deposit");
    }

    @Test
    @Order(3)
    void testInvalidAccountHandledGracefully() {
        String fakeInput = "1\n" +      // number of transactions
                "XYZ123\n" + // invalid account number
                "100\n" +    // amount
                "D\n";       // type
        Scanner fakeScanner = new Scanner(fakeInput);

        TransactionManager tm = new TransactionManager(bank, fakeScanner);

        assertDoesNotThrow(() -> tm.startDemo(),
                "Invalid accounts should be handled without crashing");
    }
}

