package P1;
import java.sql.*;

class BookingSystem {
    private Connection con;

    BookingSystem(Connection con) {
        this.con = con;
    }

    Queue displayAvailableTrains(String source, String destination) throws Exception {
        Queue trains = new Queue(21);
        String sql = "select * from trains where source = ? AND destination = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, source);
        ps.setString(2, destination);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            Train train = new Train(rs.getInt("trainNumber"), rs.getString("name"), rs.getString("source"),
                    rs.getString("destination"), rs.getInt("totalSeats"), rs.getString("Time"), rs.getDouble("amount"));
            train.setAvailableSeats(rs.getInt("availableSeats"));
            trains.enqueue(train);

        }

        return trains;
    }

    boolean checkSeatAvailability(int trainNumber) throws Exception {
        String sql = "call selectSeat(?)";
        CallableStatement cs = con.prepareCall(sql);
        cs.setInt(1, trainNumber);
        ResultSet rs = cs.executeQuery();
        if (rs.next()) {
            int availableSeats = rs.getInt("availableSeats");
            if (availableSeats > 0) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    Ticket bookTicket(int trainNumber, String passengerName, int age, String contact, Timestamp departureTime,
            double amount)
            throws Exception {
        Train train = Train.getData(trainNumber, con);
        if (train != null && train.bookSeat()) {
            Passenger passenger = new Passenger(passengerName, age, contact);
            passenger.saveToDatabase(con);
            Ticket ticket = new Ticket(trainNumber, passenger.getPassengerId(), departureTime, amount);
            ticket.saveToDatabase(con);
            payment pay = new payment(ticket.getTicketId(), amount);
            pay.saveToDatabase(con);
            return ticket;
        } else {
            return null;
        }
    }

    boolean cancelTicket(int ticketId) throws Exception {
        Ticket ticket = Ticket.getData(ticketId, con);
        if (ticket != null) {
            Train train = Train.getData(ticket.getTrainNumber(), con);
            payment pay = payment.getData(ticketId, con);
            if (train != null) {
                pay.updateInDatabase(con, ticketId);
                train.cancelSeat();
                

                ticket.deleteData(con);

                Passenger passenger = Passenger.getData(ticket.getPassengerId(), con);
                if (passenger != null) {
                    passenger.deleteData(con);
                }

                return true;
            }
        }
        return false;
    }

    Ticket displayTicketDetails(int ticketId) throws Exception {
        return Ticket.getData(ticketId, con);
    }
}
