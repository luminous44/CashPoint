

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

   
   
    
}
