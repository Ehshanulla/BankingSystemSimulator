package com.bank.service;

import com.bank.beans.Account;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientBalanceException;
import com.bank.exceptions.InvalidAmountException;
import com.bank.exceptions.InvalidNameException;

import java.util.*;



public class Bank implements BankOperations{
    private Map<String, Account> accounts = new HashMap<>();
    private int baseNumber = 1000 + new Random().nextInt(9000);;

    @Override
    public Account createAccount(String name) throws InvalidNameException{
        if(name == null || name.trim().isEmpty()){
            throw new InvalidNameException("name cannnot be empty");
        }

        String initials = name.substring(0,2).toUpperCase();
        String accNo;

        do {
            accNo = initials + baseNumber++;
        } while (accounts.containsKey(accNo));

        Account acc = new Account(accNo,name);

        accounts.put(accNo,acc);
        System.out.println("Account created successfully! Account No: " + accNo);
        return acc;
    }


    public Account findAccount(String accNo) throws AccountNotFoundException{
        return Optional.ofNullable(accounts.get(accNo))
                .orElseThrow(()->new AccountNotFoundException("Account is not present"));
    }


    @Override
    public  void deposit(String accNo, double amount)
            throws InvalidAmountException, AccountNotFoundException {
        if (amount <= 0)
            throw new InvalidAmountException("Deposit amount must be positive");
        Account acc = findAccount(accNo);
        synchronized(acc){
            acc.setBalance(acc.getBalance() + amount);

            System.out.println("Deposit successful!");
        }


    }



    @Override
    public void withDraw(String accNo, double amount)
            throws InvalidAmountException, AccountNotFoundException, InsufficientBalanceException {
        if (amount <= 0)
            throw new InvalidAmountException("Withdrawal amount must be positive");

        Account acc = findAccount(accNo);

        if (acc.getBalance() < amount)
            throw new InsufficientBalanceException("Insufficient balance");

        synchronized (acc){
            acc.setBalance(acc.getBalance() - amount);

            System.out.println("Withdrawal successful!");
        }
    }



    @Override
    public void transfer(String fromAcc, String toAcc, double amount)
            throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException {
        if (amount <= 0)
            throw new InvalidAmountException("Transfer amount must be positive");

        Account source = findAccount(fromAcc);
        Account target = findAccount(toAcc);

        Object lock1 = source.getAccountNumber().compareTo(target.getAccountNumber()) < 0 ? source : target;
        Object lock2 = (lock1 == source) ? target : source;

        synchronized (lock1) {
            synchronized (lock2) {
                if (source.getBalance() < amount)
                    throw new InsufficientBalanceException("Not enough balance");
                source.setBalance(source.getBalance() - amount);
                target.setBalance(target.getBalance() + amount);
                System.out.println("Transfer successful!");
            }
        }
    }


    @Override
    public void showBalance(String accNo) throws AccountNotFoundException{
        if(accounts.get(accNo) == null) throw new AccountNotFoundException("Account not found");
        accounts.values().stream().filter(account -> accNo.equals(account.getAccountNumber())).
                    forEach((account -> System.out.println("Account - " + account.getAccountNumber() + " balance " + account.getBalance())));
    }


    @Override
    public void showAllAccounts() {
        System.out.println("\nAll Accounts:");
        accounts.values().stream().forEach(a ->
                System.out.println(a.getAccountNumber() + " - " + a.getAccountHolderName()));
    }


    @Override
    public void showAccountsAboveBalance(double minBalance) {
        System.out.println("\nAccounts with balance above " + minBalance + ":");

        accounts.values().stream()
                .filter(acc -> acc.getBalance() > minBalance)
                .sorted(Comparator.comparingDouble(Account::getBalance).reversed())
                .forEach(acc -> System.out.println(
                        acc.getAccountNumber() + " - " +
                                acc.getAccountHolderName() + " : " +
                                acc.getBalance()));
    }
}
