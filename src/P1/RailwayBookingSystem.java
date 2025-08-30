package P1;

import java.util.*;
import java.sql.*;
import java.io.*;

class Main {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/project";
        String user = "root";
        String password = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);

        Connection con = DriverManager.getConnection(url, user, password);

        // if (con != null) {
        // System.out.println("Connection SUccessfull");
        // } else {
        // System.out.println("Connection Failed");
        // }
        BookingSystem bookingSystem = new BookingSystem(con);
        Scanner sc = new Scanner(System.in);
        HashMap<Integer, Double> ca = new HashMap<>();
        while (true) {
            System.out.println("Railway Reservation System");
            System.out.println("1. Display Trains");
            System.out.println("2. Seat Availability");
            System.out.println("3. Book Ticket");
            System.out.println("4. Display Ticket Details");
            System.out.println("5. Cancel Ticket");
            System.out.println("6. Exit");
            System.out.print("Enter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter source : ");
                    String source = sc.nextLine();
                    System.out.print("Enter destination: ");
                    String destination = sc.nextLine();
                    Queue trains = bookingSystem.displayAvailableTrains(source, destination);
                    System.out.println("Available trains :");
                    if (trains.isEmpty()) {
                        System.out.println("No such trains found");
                    } else {
                        Train train;
                        while ((train = trains.dequeue()) != null) {
                            System.out.println("-------------------------------------------------------------");
                            System.out.println("Train Number : " + train.getTrainNumber() + ", Name : "
                                    + train.getName() + ", Available Seats : " + train.getAvailableSeats()
                                    + ", Train Time : " + train.getTime() + ", Ticket Price : " + train.getAmount());
                        }
                    }
                    System.out.println("-------------------------------------------------------------");
                    System.out.println(" ");
                    break;

                case 2:
                    System.out.print("Enter train number : ");
                    int trainNumber = sc.nextInt();
                    boolean available = bookingSystem.checkSeatAvailability(trainNumber);
                    System.out.println("-------------------------------------------------------------");
                    System.out.println("Seats available : " + available);
                    System.out.println("-------------------------------------------------------------");
                    System.out.println(" ");
                    break;

                case 3:
                    System.out.print("Enter Source: ");
                    String source1 = sc.nextLine();
                    System.out.print("Enter destination: ");
                    String destination1 = sc.nextLine();
                    Queue trains1 = bookingSystem.displayAvailableTrains(source1, destination1);
                    System.out.println("Available trains: ");
                    if (trains1.isEmpty()) {
                        System.out.println("No such trains found");
                    } else {
                        Train train;
                        while ((train = trains1.dequeue()) != null) {
                            System.out.println("-------------------------------------------------------------");
                            System.out.println("Train Number : " + train.getTrainNumber() + ", Name : "
                                    + train.getName() + ", Available Seats : " + train.getAvailableSeats()
                                    + ", Train Time : " + train.getTime() + ", Ticket Price : " + train.getAmount());
                            System.out.println("-------------------------------------------------------------");
                        }

                        System.out.println(" ");
                        System.out.print("Enter train number : ");
                        trainNumber = sc.nextInt();
                        System.out.print("Enter Number of passengers to Book: ");
                        int total = sc.nextInt();
                        sc.nextLine();
                        while (total > 0) {
                            Train t = Train.getData(trainNumber, con);
                            ca.put(trainNumber, t.getAmount());

                            System.out.print("Enter passenger name: ");
                            String passengerName = sc.nextLine();
                            System.out.print("Enter passenger age : ");
                            int age = sc.nextInt();
                            sc.nextLine();

                            boolean b1 = true;
                            boolean b2 = true;
                            while (b1) {
                                System.out.print("Enter passenger contact: ");
                                String contact = sc.nextLine();
                                if (contact.length() == 10) {
                                    b1 = false;
                                    Timestamp departureTime = new Timestamp(System.currentTimeMillis());
                                    while (b2) {
                                        
                                        System.out.print("Enter ticket price: ");
                                        double amount = sc.nextDouble();
                                        sc.nextLine(); //
                                        if (ca.get(trainNumber) == amount) {
                                            b2 = false;
                                            Ticket ticket = bookingSystem.bookTicket(trainNumber, passengerName, age,
                                                    contact,
                                                    departureTime, amount);
                                            Ticket bookedTicket = bookingSystem
                                                    .displayTicketDetails(ticket.getTicketId());
                                            File f = new File(bookedTicket.getTicketId() + ".txt");
                                            f.createNewFile();
                                            FileWriter fw = new FileWriter(f);
                                            fw.write("Ticket Details: \n");
                                            fw.write("Ticket ID: " + bookedTicket.getTicketId() + "\n");
                                            fw.write("Source: " + source1 + "\n");
                                            fw.write("Destination: " + destination1 + "\n");
                                            fw.write("Train Number: " + bookedTicket.getTrainNumber() + "\n");
                                            fw.write("Passenger ID: " + bookedTicket.getPassengerId() + "\n");
                                            fw.write("Time: " + bookedTicket.getDepartureTime());
                                            System.out
                                                    .println("Ticket booked successfully. Ticket ID: "
                                                            + ticket.getTicketId());
                                            fw.close();

                                        } else {
                                            System.out.println("-------Enter Proper amount--------");
                                            continue;
                                        }
                                    }
                                } else {
                                    System.out.println("-------Enter proper 10 digit number--------");
                                    continue;
                                }
                            }
                            total--;
                        }

                        System.out.println(" ");
                    }
                    break;

                case 4:
                    con.setAutoCommit(false);
                    System.out.print("Enter ticket ID: ");
                    int ticketId = sc.nextInt();
                    Ticket bookedTicket1 = bookingSystem.displayTicketDetails(ticketId);
                    if (bookedTicket1 == null) {
                        System.out.println("No ticket ID available");
                    } else {
                        System.out.println("Are you sure you want to cancel the ticket?..yes/no");
                        sc.nextLine();
                        String check = sc.nextLine();
                        if (check.equalsIgnoreCase("yes")) {

                            boolean canceled = bookingSystem.cancelTicket(ticketId);
                            System.out.println("Ticket canceled: " + canceled);
                            System.out.println(" ");
                            con.commit();

                        } else {
                            System.out.println("Ticket canceled: false");
                        }

                    }
                    System.out.println(" ");

                    break;

                case 5:
                    System.out.print("Enter ticket ID: ");
                    ticketId = sc.nextInt();
                    Ticket bookedTicket = bookingSystem.displayTicketDetails(ticketId);
                    if (bookedTicket != null) {
                        System.out.println("-------------------------------------------------------------");
                        System.out.println("Ticket Details: ");
                        System.out.println("Ticket ID: " + bookedTicket.getTicketId());
                        System.out.println("Train Number: " + bookedTicket.getTrainNumber());
                        System.out.println("Passenger ID: " + bookedTicket.getPassengerId());
                        System.out.println("Departure Time: " + bookedTicket.getDepartureTime());
                        System.out.println("-------------------------------------------------------------");
                    } else {
                        System.out.println("Ticket not found.");
                    }
                    System.out.println(" ");
                    break;

                case 6:
                    System.out.println("Exiting...");
                    Thread.sleep(600);
                    System.out.println("Thank you for visiting");
                    System.out.println(" ");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println("");
            }
        }
    }
}
