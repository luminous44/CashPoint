

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    Connection con;
    Scanner sc;

    User(Connection con, Scanner sc) throws SQLException {
        this.con = con;
        this.sc = sc;
     
    }

    

    public void userRegistration() throws SQLException {
        
        sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        while (!email.contains("@")) { // Basic email validation
            System.out.print("Invalid email. Please enter a valid email: ");
            email = sc.nextLine();
        }

        if (!isExist(email)) {
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            while (password.length() < 6) { // Basic password validation
                System.out.print("Password must be at least 6 characters. Try again: ");
                password = sc.nextLine();
            }

            PreparedStatement ps = con.prepareStatement("INSERT INTO user (name,email,password) VALUES(?,?,?);");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int r = ps.executeUpdate();
            if (r > 0) {
                System.out.println("Registration Successfully");
            } else {
                System.out.println("Failed to Registration!!! Try Again");
            }
        }else{
            System.out.println("User with this email already exist");
        }

    }
    
    public boolean isExist(String email) throws SQLException{
        
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM user WHERE email ='"+email+"'");
        if(rs.next()){
            return true;
        }      
        return false;
    }
    
    public String Login() throws SQLException{
        sc.nextLine();
        System.out.print("Enter your email : ");
        String emailId = sc.nextLine();
        System.out.print("Enter Password : ");
        String pass = sc.nextLine();
        
        PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE email = ? AND password = ?;");
        ps.setString(1, emailId);
        ps.setString(2, pass);    
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return emailId;
        }
        return null;
    }

}
