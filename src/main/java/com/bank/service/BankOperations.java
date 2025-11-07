package com.bank.service;

import com.bank.beans.Account;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientBalanceException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.exceptions.InvalidNameException;

public interface BankOperations {

    Account findAccount(String accNo) throws AccountNotFoundException;

    Account createAccount(String name) throws InvalidNameException;

    void deposit(String accNo, double amount)
            throws InvalidAmountException, AccountNotFoundException;

    void withDraw(String accNo, double amount)
            throws InvalidAmountException, AccountNotFoundException, InsufficientBalanceException;

    void transfer(String fromAcc, String toAcc, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException;

    void showBalance(String accNo) throws AccountNotFoundException;

    void showAllAccounts();

    void showAccountsAboveBalance(double minBalance);
}

