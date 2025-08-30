package P1;

import java.sql.*;

class Train {
    private int trainNumber;
    private String name;
    private String source;
    private String destination;
    private int availableSeats;
    private int totalSeats;
    private String Time;
    private double amount;

    Train(int trainNumber, String name, String source, String destination, int totalSeats, String Time, double amount) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.Time = Time;
        this.amount = amount;
    }

    double getAmount() {
        return amount;
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    String getTime() {
        return Time;
    }

    void setTime(String time) {
        Time = time;
    }

    int getTrainNumber() {
        return trainNumber;
    }

    void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getSource() {
        return source;
    }

    void setSource(String source) {
        this.source = source;
    }

    String getDestination() {
        return destination;
    }

    void setDestination(String destination) {
        this.destination = destination;
    }

    int getAvailableSeats() {
        return availableSeats;
    }

    void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    int getTotalSeats() {
        return totalSeats;
    }

    void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    boolean checkSeatAvailability() {
        return availableSeats > 0;
    }

    boolean bookSeat() {
        if (availableSeats > 0) {
            availableSeats--;
            return true;
        } else {
            return false;
        }
    }

    boolean cancelSeat() {
        if (availableSeats < totalSeats) {
            availableSeats++;
            return true;
        } else {
            return false;
        }
    }

    

    public static Train getData(int trainNumber, Connection con) throws Exception {
        String sql = "select * from trains where trainNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, trainNumber);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Train train = new Train(
                    rs.getInt("trainNumber"),
                    rs.getString("name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getInt("totalSeats"),
                    rs.getString("Time"), rs.getDouble("amount"));
            train.setAvailableSeats(rs.getInt("availableSeats"));
            return train;
        } else {
            return null;
        }

    }

    void deleteData(Connection con) throws SQLException {
        String sql = "delete from trains where trainNumber = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, this.trainNumber);
        ps.executeUpdate();

    }
}
