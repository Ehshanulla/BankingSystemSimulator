package org.example;




import org.example.beans.Account;
import org.example.service.Bank;
import org.example.service.BankOperations;
import org.example.thread.TransactionManager;
import org.example.thread.TransactionTask;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankOperations bank = new Bank();
        int choice;

        do {
            System.out.println("\n===== BANKING SYSTEM MENU =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Show Balance");
            System.out.println("6. Show All Accounts");
            System.out.println("7. Multithreading Demo (Simultaneous Deposits)");
            System.out.println("8. Show accounts above a specific balance");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter your name: ");
                        String name = sc.nextLine();
                        bank.createAccount(name);
                    }
                    case 2 -> {
                        System.out.print("Enter account number: ");
                        String accNo = sc.nextLine();
                        System.out.print("Enter amount to deposit: ");
                        double amt = sc.nextDouble();
                        bank.deposit(accNo, amt);
                    }
                    case 3 -> {
                        System.out.print("Enter account number: ");
                        String accNo = sc.nextLine();
                        System.out.print("Enter amount to withdraw: ");
                        double amt = sc.nextDouble();
                        bank.withDraw(accNo, amt);
                    }
                    case 4 -> {
                        System.out.print("Enter source account number: ");
                        String from = sc.nextLine();
                        System.out.print("Enter destination account number: ");
                        String to = sc.nextLine();
                        System.out.print("Enter amount to transfer: ");
                        double amt = sc.nextDouble();
                        bank.transfer(from, to, amt);
                    }
                    case 5 -> {
                        System.out.print("Enter account number: ");
                        String accNo = sc.nextLine();
                        bank.showBalance(accNo);
                    }
                    case 6 -> bank.showAllAccounts();



                    case 7 -> {
                        TransactionManager manager = new TransactionManager(bank);
                        manager.startDemo();
                        break;
                    }

                    case 8->{
                        System.out.print("Enter minimum balance: ");
                        double min = sc.nextDouble();
                        bank.showAccountsAboveBalance(min);
                    }

                    case 9 -> System.out.println("Exiting... Thank you for using our Banking System!");

                    default -> System.out.println("Invalid choice! Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter numeric values where required.");
                sc.nextLine(); // clear invalid input
                choice = 0; // reset choice to continue loop
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                choice = 0;
            }
        } while (choice != 9);

        sc.close();
    }
}
