package P1;

import java.sql.*;

class Passenger {
    private int passengerId;
    private String name;
    private int age;
    private String contact;

    Passenger(String name, int age, String contact) {
        this.name = name;
        this.age = age;
        this.contact = contact;
    }

    int getPassengerId() {
        return passengerId;
    }

    void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getAge() {
        return age;
    }

    void setAge(int age) {
        this.age = age;
    }

    String getContact() {
        return contact;
    }

    void setContact(String contact) {
        this.contact = contact;
    }

    void saveToDatabase(Connection con) throws Exception {
        String sql = "insert into passengers (name, age, contact) values (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, this.name);
        ps.setInt(2, this.age);
        ps.setString(3, this.contact);
        ps.executeUpdate();

        ResultSet generatedKeys = ps.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.passengerId = generatedKeys.getInt(1);

        }
    }

    void updateInDatabase(Connection con) throws Exception {
        String sql = "update passengers set name = ?, age = ?, contact = ? where passengerId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, this.name);
        ps.setInt(2, this.age);
        ps.setString(3, this.contact);
        ps.setInt(4, this.passengerId);
        ps.executeUpdate();

    }

    static Passenger getData(int passengerId, Connection con) throws Exception {
        String sql = "{call selectPassenger(?)}";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, passengerId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Passenger passenger = new Passenger(
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("contact"));
            passenger.setPassengerId(rs.getInt("passengerId"));
            return passenger;
        } else {
            return null;
        }
    }

    void deleteData(Connection con) throws Exception {
        String sql = "delete from passengers where passengerId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, this.passengerId);
        ps.executeUpdate();
    }
}
