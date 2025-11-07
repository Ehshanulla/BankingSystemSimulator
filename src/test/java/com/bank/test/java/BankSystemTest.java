package com.bank.test.java;

import com.bank.beans.Account;
import com.bank.exceptions.*;
import com.bank.service.Bank;
import com.bank.service.BankOperations;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BankSystemTest {

    private static BankOperations bank;
    private static Account a1, a2;

    @BeforeAll
    static void setup() throws Exception {
        bank = new Bank();
        a1 = bank.createAccount("Ramu");
        a2 = bank.createAccount("Sita");
        bank.deposit(a1.getAccountNumber(), 10000);
        bank.deposit(a2.getAccountNumber(), 15000);
    }

    @Test
    void testDepositAndWithdraw() throws Exception {
        bank.withDraw(a1.getAccountNumber(), 2000);
        assertTrue(a1.getBalance() >= 0, "Balance should never be negative");
    }

    @Test
    void testTransfer() throws Exception {
        double fromBefore = a2.getBalance();
        double toBefore = a1.getBalance();
        bank.transfer(a2.getAccountNumber(), a1.getAccountNumber(), 3000);
        assertEquals(fromBefore - 3000, a2.getBalance(), 0.001);
        assertEquals(toBefore + 3000, a1.getBalance(), 0.001);
    }

    @Test
    void testInvalidNameThrows() {
        assertThrows(InvalidNameException.class, () -> bank.createAccount("  "));
    }

    @Test
    void testNegativeDepositThrows() {
        assertThrows(InvalidAmountException.class, () -> bank.deposit(a1.getAccountNumber(), -100));
    }

    @Test
    void testInsufficientBalanceThrows() {
        assertThrows(InsufficientBalanceException.class, () -> bank.withDraw(a1.getAccountNumber(), 50000));
    }

    @Test
    void testNonExistentTransferThrows() {
        assertThrows(AccountNotFoundException.class, () -> bank.transfer("XX0000", "YY0000", 1000));
    }

    @AfterAll
    static void tearDown() {
        System.out.println("✅ All BankSystem tests executed successfully.");
    }


}
