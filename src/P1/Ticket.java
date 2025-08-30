package P1;

import java.sql.*;


class Ticket {
    private int ticketId;
    private int trainNumber;
    private int passengerId;
    private Timestamp Time;
    private double amount;

    Ticket(int trainNumber, int passengerId, Timestamp Time, double amount) {
        this.trainNumber = trainNumber;
        this.passengerId = passengerId;
        this.Time = Time;
        this.amount = amount;
    }

    int getTicketId() {
        return ticketId;
    }

    void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    int getTrainNumber() {
        return trainNumber;
    }

    void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    int getPassengerId() {
        return passengerId;
    }

    void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    Timestamp getDepartureTime() {
        return Time;
    }

    void setDepartureTime(Timestamp Time) {
        this.Time = Time;
    }

    void saveToDatabase(Connection con) throws Exception {
        String sql = "insert into tickets (trainNumber, passengerId, Time, amount) values (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, this.trainNumber);
        ps.setInt(2, this.passengerId);
        ps.setTimestamp(3, this.Time);
        ps.setDouble(4, amount);
        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.ticketId = generatedKeys.getInt(1);
        }

    }

    void updateInDatabase(Connection con) throws Exception {
        String sql = "update tickets set trainNumber = ?, passengerId = ?, Time = ?, amount = ? where ticketId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, this.trainNumber);
        ps.setInt(2, this.passengerId);
        ps.setTimestamp(3, this.Time);
        ps.setInt(5, this.ticketId);
        ps.setDouble(4, amount);
        ps.executeUpdate();
    }

    static Ticket getData(int ticketId, Connection con) throws Exception {
        String sql = "call selectTicket(?)";
        CallableStatement cs = con.prepareCall(sql);
        cs.setInt(1, ticketId);
        ResultSet rs = cs.executeQuery();
        if (rs.next()) {
            Ticket ticket = new Ticket(
                    rs.getInt("trainNumber"),
                    rs.getInt("passengerId"),
                    rs.getTimestamp("Time"),
                    rs.getDouble("amount"));
            ticket.setTicketId(rs.getInt("ticketId"));
            return ticket;
        } else {
            return null;
        }
    }

    void deleteData(Connection con) throws Exception {
        String sql = "delete from tickets where ticketId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, this.ticketId);
        ps.executeUpdate();
    }

}
