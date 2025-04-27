import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingSystem {
  public static void main(String[] args) throws ClassNotFoundException,SQLException{
    
    Scanner sc = new Scanner(System.in);
    final String url = "jdbc:mysql://localhost/digita_wallet";
    final String userName = "root";
    final String password = "29344";

    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(url, userName, password);
    String email;
    long accNumber;
  }
}