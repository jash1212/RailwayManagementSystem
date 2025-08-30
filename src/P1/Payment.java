package P1;

import java.sql.*;

class payment {
    private int paymentId;
    private int ticketId;
    private double amount;
    private String paymentStatus;
    private Timestamp paymentDate;
    private Timestamp refundDate;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    payment(int ticketId, double amount) {
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentStatus = "Paid";
        this.paymentDate = new Timestamp(System.currentTimeMillis());
    }

    void saveToDatabase(Connection con) throws Exception {
        String sql = "insert into payments (ticketId, amount, paymentStatus, paymentDate) values (?, ?, ?, ?)";
        PreparedStatement ps1 = con.prepareStatement(sql);
        ps1.setInt(1, ticketId);
        ps1.setDouble(2, amount);
        ps1.setString(3, paymentStatus);
        ps1.setTimestamp(4, paymentDate);
        ps1.executeUpdate();
    }

    void updateInDatabase(Connection con, int ticketId) throws Exception {
        String sql = "update payments set paymentStatus = ?, refundDate = ? where ticketId = ?";
        refundDate = new Timestamp(System.currentTimeMillis());
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "Refund");
        ps.setTimestamp(2, refundDate);
        ps.setInt(3, ticketId);
        ps.executeUpdate();
    }

    static payment getData(int ticketId, Connection con) throws Exception {
        String sql = "select * from payments where ticketId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, ticketId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            payment pay = new payment(rs.getInt("ticketId"), rs.getDouble("amount"));
            pay.setTicketId(rs.getInt("ticketId"));
            return pay;
        } else {
            return null;
        }
    }

}
