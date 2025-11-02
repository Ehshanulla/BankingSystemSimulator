package org.example.service;

import org.example.beans.Account;
import org.example.exceptions.*;
import org.example.service.Bank;
import org.example.service.BankOperations;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankTest {

    private static BankOperations bank;
    private static Account acc1, acc2;

    @BeforeAll
    static void setup() throws InvalidNameException {
        bank = new Bank();
        acc1 = bank.createAccount("Alice");
        acc2 = bank.createAccount("Bob");
    }

    @Test
    @Order(1)
    void testAccountCreation() {
        assertNotNull(acc1);
        assertNotNull(acc2);
    }

    @Test
    @Order(2)
    void testDeposit() throws InvalidAmountException, AccountNotFoundException {
        bank.deposit(acc1.getAccountNumber(), 1000);
        bank.deposit(acc2.getAccountNumber(), 500);
        Account a1 = bank.findAccount(acc1.getAccountNumber());
        assertEquals(1000, a1.getBalance());
    }

    @Test
    @Order(3)
    void testWithdraw() throws Exception {
        bank.withDraw(acc1.getAccountNumber(), 300);
        Account a1 = bank.findAccount(acc1.getAccountNumber());
        assertEquals(700, a1.getBalance());
    }

    @Test
    @Order(4)
    void testTransfer() throws Exception {
        bank.transfer(acc1.getAccountNumber(), acc2.getAccountNumber(), 200);
        assertEquals(500, bank.findAccount(acc1.getAccountNumber()).getBalance());
        assertEquals(700, bank.findAccount(acc2.getAccountNumber()).getBalance());
    }

    @Test
    @Order(5)
    void testInvalidDepositThrowsException() {
        assertThrows(InvalidAmountException.class, () -> bank.deposit(acc1.getAccountNumber(), -100));
    }

    @Test
    @Order(6)
    void testWithdrawMoreThanBalanceThrowsException() {
        assertThrows(InsufficientBalanceException.class, () -> bank.withDraw(acc1.getAccountNumber(), 10000));
    }
}
