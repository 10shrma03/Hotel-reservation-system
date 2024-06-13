import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "imshrma";

    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("com.sql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            while(true){
                System.out.println();
                System.out.println("Hotel Management System");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View reservations");
                System.out.println("3. Get room number");
                System.out.println("4. Update reservations");
                System.out.println("5. Deleter reservations");
                System.out.println("0. Exit");
                int choice = sc.nextInt();
                switch (choice){
                    case 1 :
                        reserveRoom(connection, sc);
                        break;
                    case 2 :
                        viewReservations(connection);
                        break;

                    case 3 :
                      getRoomNumber(connection, sc);
                        break;

                    case 4 :
                        updateReservations(connection, sc);
                            break;

                    case 5 :
                        deleteReservations(connection, sc);
                        break;

                    case 0 :
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid Choice");
                }


            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection, Scanner sc){
        try{
            System.out.println("Enter Guest name : ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter room number : ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact number : ");
            String contactNumber = sc.next();

            String query = "INSERT INTO reservations (guest_name, room_number, contact_number)" +
                    "VALUES('" + guestName +
                    "'," + roomNumber +
                    ",'" + contactNumber + "');";

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(query);

                if(affectedRows > 0){
                    System.out.println("Reservation successful");
                }
                else{
                    System.out.println("Reservation failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
    }
}

    private static void viewReservations(Connection connection) throws SQLException {
        String query = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations;";
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)){
            while (rs.next()){
                int id = rs.getInt("reservation_id");
                String name = rs.getString("guest_name");
                int roomNo = rs.getInt("room_number");
                String contactNo = rs.getString("contact_number");
                String data = rs.getTimestamp("reservation_date").toString();
                System.out.printf("%d, %s, %d, %s, %s", id, name, roomNo, contactNo, data);
            }

        }
    }

    private static void getRoomNumber(Connection connection, Scanner sc) {
        try{
            System.out.println("Enter guest id : ");
            int id = sc.nextInt();
            System.out.println("Enter guest name : ");
            String name = sc.next();

            String query = "SELECT room_number FROM reservations WHERE reservation_id =" + id + " AND guest_name = '" + name + "';";
            try(Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)) {
                if(rs.next()){
                    int roomNo = rs.getInt("room_number");
                    System.out.println("Room number is : " + roomNo);
                }
                else{
                    System.out.println("Reservation does not exist");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int id) {
        try {
            String query = "SELECT * from reservations where reservation_id =" + id + ";";
            try(Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)){
                return rs.next();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private static void updateReservations(Connection connection, Scanner sc) throws SQLException{
        try{
            System.out.println("Enter reservation id to update : ");
            int id = sc.nextInt();
            if(!reservationExists(connection, id)){
                System.out.println("Reservation doesn't exist");
                return;
            }

            System.out.println("Enter new guest name : ");
            String name = sc.next();
            sc.nextLine();
            System.out.println("Enter new room number : ");
            int roomNo = sc.nextInt();
            System.out.println("Enter new contact number : ");
            String contactNo = sc.next();

            String query = "UPDATE reservations SET guest_name = '" + name + "', room_number = "+ roomNo + ", contact_number = '" +
                    contactNo + "' WHERE reservation_id = " + id + ";";

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(query);
                if(affectedRows > 0){
                    System.out.println("Updated successfully");
                }
                else{
                    System.out.println("Error in update");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteReservations(Connection connection, Scanner sc){
        try{
            System.out.println("Enter reservation id to delete : ");
            int id = sc.nextInt();

            if(!reservationExists(connection, id)){
                System.out.println("Reservation doesn't exist");
                return;
            }

            String query = "DELETE from reservations WHERE reservation_id =" + id + ";";

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(query);
                if(affectedRows > 0){
                    System.out.println("Deletion successfull");
                }
                else{
                    System.out.println("Error in deletion");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void exit() throws InterruptedException{
        System.out.println("Exiting System");
        int i = 5;
        while (i != 0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thanks for using our system");
    }
}

