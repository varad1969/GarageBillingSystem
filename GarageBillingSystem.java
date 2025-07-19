import java.sql.*;
import java.util.*;

public class GarageBillingSystem {
    static final String DB_URL = "jdbc:mysql://localhost:3306/garage_billing";
    static final String DB_USER = "root";
    static final String DB_PASS = "Techtube@2005"; // 🔒 Replace with your MySQL password

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n========= GARAGE BILLING SYSTEM =========");
            System.out.println("1. Add Customer");
            System.out.println("2. Add Vehicle");
            System.out.println("3. Add Service");
            System.out.println("4. Create Job Card");
            System.out.println("5. View Job Cards");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: addCustomer(); break;
                case 2: addVehicle(); break;
                case 3: addService(); break;
                case 4: createJobCard(); break;
                case 5: viewJobCards(); break;
                case 0: System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    static void addCustomer() {
        try (Connection con = getConnection()) {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter phone: ");
            String phone = sc.nextLine();
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter address: ");
            String address = sc.nextLine();

            String sql = "INSERT INTO customers (name, phone, email, address) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.executeUpdate();
            System.out.println("✅ Customer added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addVehicle() {
        try (Connection con = getConnection()) {
            System.out.print("Enter Customer ID: ");
            int customerId = sc.nextInt(); sc.nextLine();
            System.out.print("Enter Registration No: ");
            String regNo = sc.nextLine();
            System.out.print("Enter Brand: ");
            String brand = sc.nextLine();
            System.out.print("Enter Model: ");
            String model = sc.nextLine();
            System.out.print("Enter Type (Two-wheeler/Four-wheeler): ");
            String type = sc.nextLine();

            String sql = "INSERT INTO vehicles (customer_id, registration_no, brand, model, type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, customerId);
            ps.setString(2, regNo);
            ps.setString(3, brand);
            ps.setString(4, model);
            ps.setString(5, type);
            ps.executeUpdate();
            System.out.println("✅ Vehicle added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void addService() {
        try (Connection con = getConnection()) {
            System.out.print("Enter Service Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Cost: ");
            double cost = sc.nextDouble(); sc.nextLine();

            String sql = "INSERT INTO services (name, cost) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setDouble(2, cost);
            ps.executeUpdate();
            System.out.println("✅ Service added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void createJobCard() {
        try (Connection con = getConnection()) {
            System.out.print("Enter Vehicle ID: ");
            int vehicleId = sc.nextInt(); sc.nextLine();
            System.out.print("Enter Service Date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            List<Integer> serviceIds = new ArrayList<>();
            double totalCost = 0;

            while (true) {
                System.out.print("Enter Service ID to add (0 to finish): ");
                int sid = sc.nextInt(); sc.nextLine();
                if (sid == 0) break;

                PreparedStatement ps = con.prepareStatement("SELECT cost FROM services WHERE id = ?");
                ps.setInt(1, sid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    totalCost += rs.getDouble("cost");
                    serviceIds.add(sid);
                } else {
                    System.out.println("⚠️ Invalid Service ID.");
                }
            }

            PreparedStatement insertJob = con.prepareStatement(
                    "INSERT INTO job_cards (vehicle_id, service_date, total_cost) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertJob.setInt(1, vehicleId);
            insertJob.setString(2, date);
            insertJob.setDouble(3, totalCost);
            insertJob.executeUpdate();

            ResultSet keys = insertJob.getGeneratedKeys();
            if (keys.next()) {
                int jobId = keys.getInt(1);
                for (int sid : serviceIds) {
                    PreparedStatement link = con.prepareStatement("INSERT INTO job_services (job_card_id, service_id) VALUES (?, ?)");
                    link.setInt(1, jobId);
                    link.setInt(2, sid);
                    link.executeUpdate();
                }
                System.out.println("✅ Job card created. Total cost: ₹" + totalCost);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void viewJobCards() {
        try (Connection con = getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT jc.id, v.registration_no, jc.service_date, jc.total_cost FROM job_cards jc JOIN vehicles v ON jc.vehicle_id = v.id");

            System.out.println("\n----- Job Cards -----");
            while (rs.next()) {
                System.out.println("Job ID: " + rs.getInt(1));
                System.out.println("Vehicle: " + rs.getString(2));
                System.out.println("Date: " + rs.getDate(3));
                System.out.println("Total: ₹" + rs.getDouble(4));
                System.out.println("---------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
