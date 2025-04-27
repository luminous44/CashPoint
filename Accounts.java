
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {

    Connection con;
    Scanner sc;

    Accounts(Connection con, Scanner sc) throws SQLException {
        this.con = con;
        this.sc = sc;

    }
    public long openAccount(String email){
            String query = "INSERT INTO accounts(accNo,name,email,balance,pin) VALUES(?,?,?,?,?);";
            sc.nextLine();
            System.out.print("Enter your Full Name : ");
            String name = sc.nextLine();
            System.out.print("Enter Initial amount : ");
            double amount = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter PIN (4) : ");
            int pin = sc.nextInt();
            
            try{
                long accNumber = generateAccountNumber();
                PreparedStatement ps = con.prepareStatement(query);
                ps.setLong(1, accNumber);
                ps.setString(2, name);
                ps.setString(3, email);
                ps.setDouble(4,amount);
                ps.setInt(5,pin);
                int r = ps.executeUpdate();
                if(r>0){
                    System.out.println("Account Created Successfully");
                    return accNumber;
                }else{
                    throw new RuntimeException("Account creation failed");
                }
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        
        throw new RuntimeException("Account Already exist");
    }
    
    public long generateAccountNumber() throws SQLException{
        
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT accNo FROM accounts ORDER BY accNo DESC LIMIT 1;");
        if(rs.next()){
            return rs.getLong("accNo")+1;
        }        
        return 1000100;
            
    }
     public long getAccountNumber(String email) throws SQLException{
        
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT accNo FROM accounts WHERE email ='"+email+"'");
        if(rs.next()){
            return rs.getLong("accNo");
        }      
       throw new RuntimeException("Account doesn't exist");
    }
   public boolean isExist(String email) throws SQLException{
        
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM accounts WHERE email ='"+email+"'");
        if(rs.next()){
            return true;
        }      
        return false;
    }
}
