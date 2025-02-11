import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//C:\Users\91987\Desktop\OASIS INFOBYTE\TASK1
//javac Online.java
// java -cp ".;lib/mysql-connector-j-9.2.0.jar" Online


public class Online {
   private static final String DB_URL = "jdbc:mysql://localhost:3306/online_reservation";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "mysql@123";
   private static Connection conn;
   private static String loggedInUser;

   public static void main(String[] var0) {
      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_reservation", "root", "mysql@123");
         System.out.println("Database Connection Successful!");
         Scanner var6 = new Scanner(System.in);

         while(true){
            showLoginMenu();
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }
   
   private static void showLoginMenu() throws SQLException{
      Scanner var6 = new Scanner(System.in);
      while (true){
         System.out.println("Welcome to the Online Reservation System");
         System.out.println("1. Login\n2. Register\n3. Exit");
         int var2 = var6.nextInt();
         var6.nextLine();
         switch (var2) {
            case 1:
               login(var6);
               break;
            case 2:
               register(var6);
               break;
            case 3:
               System.exit(0);
               break;
            default:
               System.out.println("Invalid choice.");
            } 
      }
   }

   private static void login(Scanner var0) throws SQLException {
      System.out.print("Enter Username: ");
      String var1 = var0.nextLine(); 
      System.out.print("Enter Password: ");
      String var2 = var0.nextLine();
      String var3 = "SELECT * FROM users WHERE username=? AND password=?";
      PreparedStatement var4 = conn.prepareStatement(var3);
      var4.setString(1, var1);
      var4.setString(2, var2);
      ResultSet var5 = var4.executeQuery();
      if (var5.next()) {
         System.out.println("Login successful! Welcome " + var1);
         loggedInUser = var1;
         mainMenu(var0);
      } else {
         System.out.println("Invalid credentials. Try again.");
      }

   }

   private static void register(Scanner var0) throws SQLException {
      System.out.print("Enter Username: ");
      String var1 = var0.nextLine();
      System.out.print("Enter Password: ");
      String var2 = var0.nextLine();
      String var3 = "INSERT INTO users (username, password) VALUES (?, ?)";
      PreparedStatement var4 = conn.prepareStatement(var3);
      var4.setString(1, var1);
      var4.setString(2, var2);
      var4.executeUpdate();
      System.out.println("Registration successful!");
   }

   private static void mainMenu(Scanner var0) throws SQLException {
      boolean running = true;

      while (running){
      System.out.println("1. Book Ticket\n2. Cancel Ticket\n3. View Tickets\n4. Logout");
      System.out.print("Enter your choice: ");
      int var1 = var0.nextInt();
      var0.nextLine();
      switch (var1) {
         case 1:
            bookTicket(var0);
            break;
         case 2:
            cancelTicket(var0);
            break;
         case 3:
         if (loggedInUser == null){
            System.out.println("You need to log in first!");
            login(var0);
         }
            viewMyTickets();
            break;
         case 4:
            System.out.println("Logged out.");
            loggedInUser = null;
            return;
         default:
            System.out.println("Invalid choice.");
      }
   } 
   if (running){
      System.out.println("Do you want to continue? (Y/N)");
      String response = var0.nextLine().trim().toLowerCase();
      if (!response.equals("yes")){
         running = false;
         System.out.println("Logged out.");
      }
   }

   }

   private static void bookTicket(Scanner var0) throws SQLException {
      System.out.print("Enter username: ");
      String username = var0.nextLine();
      System.out.print("Enter Train Number: ");
      String var1 = var0.nextLine();
      System.out.print("Enter Name: ");
      String var2 = var0.nextLine();
      System.out.print("Enter Class Type: ");
      String var3 = var0.nextLine();
      System.out.print("Enter Date of Journey (YYYY-MM-DD): ");
      String var4 = var0.nextLine();
      System.out.print("Enter From Station: ");
      String var5 = var0.nextLine();
      System.out.print("Enter Destination: ");
      String var6 = var0.nextLine();
      String var7 = "INSERT INTO reservations (username, train_number, name, class_type, date_of_journey, from_station, destination) VALUES (?,?, ?, ?, ?, ?, ?)";
      PreparedStatement var8 = conn.prepareStatement(var7);
      var8.setString(1, username);
      var8.setString(2, var1);
      var8.setString(3, var2);
      var8.setString(4, var3);
      var8.setString(5, var4);
      var8.setString(6, var5);
      var8.setString(7, var6);
      var8.executeUpdate();
      System.out.println("Ticket booked successfully!");
   }

   private static void cancelTicket(Scanner var0) throws SQLException {
      System.out.print("Enter Username: ");
      String var1 = var0.nextLine();
      System.out.println("Enter Train Number: ");
      String trainNumber = var0.nextLine();
      System.out.println("Enter the date of the journey: ");
      String var5 = var0.nextLine();
      System.out.println("Enter the class type: ");
      String var6 = var0.nextLine();
      String var2 = "DELETE FROM reservations WHERE username = ? AND train_number = ? AND date_of_journey = ? AND class_type = ?";
      PreparedStatement var3 = conn.prepareStatement(var2);
      var3.setString(1, var1);
      var3.setString(2, trainNumber);
      var3.setString(3, var5);
      var3.setString(4, var6);
      int var4 = var3.executeUpdate();
      if (var4 > 0) {
         System.out.println("Ticket cancelled successfully!");
      } else {
         System.out.println("No matching reservation found for the given username and train number,");
      }

   }

   private static void viewMyTickets() throws SQLException{

      String query = "SELECT * FROM reservations WHERE username =?";
      PreparedStatement stmt = conn.prepareStatement(query);
      stmt.setString(1, loggedInUser);
      ResultSet rs = stmt.executeQuery();

      System.out.println("Your Booked Tickets:");
      System.out.println("------------------------------------------------");
      boolean hastickets = true;
      while (rs.next()) {
          System.out.println(" | Train: " + rs.getString("train_number") +
                  " | Name: " + rs.getString("name") +
                  " | Class: " + rs.getString("class_type") +
                  " | Date: " + rs.getString("date_of_journey") +
                  " | From: " + rs.getString("from_station") +
                  " | To: " + rs.getString("destination"));
      }
      if(!hastickets){
         System.out.println("No tickets found.");
      }
      System.out.println("------------------------------------------------");
   }
}


