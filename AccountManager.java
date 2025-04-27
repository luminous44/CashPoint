

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountManager {

    Connection con;
    Scanner sc;

    AccountManager(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    // withdraw money from wallet
    public void withdrawMoney(long accN) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount : ");
        int amount = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter PIN : ");
        int pin = sc.nextInt();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE accNo = ? AND pin = ?;");
            ps.setLong(1, accN);
            ps.setInt(2, pin);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (amount <= currentBalance) {
                    PreparedStatement ps1 = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE accNo = ?;");
                    ps1.setInt(1, amount);
                    ps1.setLong(2, accN);
                    int row = ps1.executeUpdate();
                    if (row > 0) {
                        System.out.println(amount + " TK. Withdraw Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                    } else {
                        System.out.println("Failed to Withdraw");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Insufficient balance");
                }

            } else {
                System.out.println("Incorrect PIN, Try again");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

     // add money 
     public void addMoney(long accN) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount : ");
        int amount = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter PIN : ");
        int pin = sc.nextInt();
        try {
            con.setAutoCommit(false);
            if (isValidPin(accN, pin)) {
                PreparedStatement ps2 = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE accNo = ?;");
                ps2.setInt(1, amount);
                ps2.setLong(2, accN);
                int row = ps2.executeUpdate();
                if (row > 0) {
                    System.out.println(amount + " TK. Add Successfully");
                    con.commit();
                    con.setAutoCommit(true);
                } else {
                    System.out.println("Failed to Add Money");
                    con.rollback();
                    con.setAutoCommit(true);
                }

            } else {
                System.out.println("Incorrect PIN, Try again");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }
     // valid Pin
     public boolean isValidPin(long acc, int pin) throws SQLException {

        PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE accNo = ? AND pin = ?;");
        ps.setLong(1, acc);
        ps.setInt(2, pin);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }
   
    // check amount

    public void getBalance(long acc) throws SQLException {
        sc.nextLine();
        System.out.print("Enter PIN : ");
        int pin = sc.nextInt();
        con.setAutoCommit(false);
        if (isValidPin(acc, pin)) {
            PreparedStatement ps3 = con.prepareStatement("SELECT * FROM accounts WHERE accNo = ?;");
            ps3.setLong(1, acc);
            ResultSet rs = ps3.executeQuery();
            if (rs.next()) {
                System.out.println("Your current balance = " + rs.getInt("balance") + " TK.");
            } else {
                throw new RuntimeException();
            }
        } else {
            System.out.println("Invalid PIN!!!");
        }

    }

     // Transfer money
     public void transferMoney(long acc) throws SQLException {
        sc.nextLine();
        System.out.print("Enter reciver account number : ");
        long acc2 = sc.nextLong();
        con.setAutoCommit(false);
        PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE accNo = ?;");
        ps.setLong(1, acc2);
        if (ps.executeQuery().next() && acc != acc2) {
            sc.nextLine();
            System.out.print("Enter Amount : ");
            int amount = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter PIN : ");
            int pin = sc.nextInt();
            if (acc != 0 && acc2 != 0) {
                if (isValidPin(acc, pin)) { 
                    PreparedStatement psw = con.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE accNo = ?;");
                    psw.setInt(1, amount);
                    psw.setLong(2, acc);
                    PreparedStatement psd = con.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE accNo = ?;");
                    psd.setInt(1, amount);
                    psd.setLong(2, acc2);
                    if(psw.executeUpdate()>0 && psd.executeUpdate()>0){
                        System.out.println(amount+" Tk. Transferred Successfully");
                        con.commit();
                        con.setAutoCommit(true);
                    }else{
                        System.out.println("Failed Transaction!!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid PIN!!!");
                }
            } else {
                System.out.println("Invalid account number!!!");
            }
        } else {
            System.out.println("Invalid Reciver Account Number!!!");
        }
     con.setAutoCommit(true);
    }
    
}
