import java.sql.*;
import java.io.*;
import java.util.*;

public class Transity {

    public static void main(String[] args) throws Exception {
        // startup sequence
        TransitDatabase db = new TransitDatabase("auth.cfg", "project.sql");
        runConsole(db);

        System.out.println("Exiting...");
    }

    private static void printHelp() {

        System.out.println("============ Commands for running database ============");
        System.out.println("h - Get help");
        System.out.println("route <route id> - Get all the stops along a route");
        System.out.println("stop <stop id> - Get all the routes by stop");
        System.out.println("ms - Show routes with the most stops"); // opti
        System.out.println("mr - Show stops with the most routes"); // opti
        System.out.println(
                "stopCount <order> - Show number of stops at all routes (enter asc or desc for order)"); // opti

        System.out.println("\n\nq - Exit the database program");
        System.out.println("=======================================================");
    }

    public static void runConsole(TransitDatabase db) {

        System.out.println("======Transity Database=====");
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the Transity Database! Type h for help. ");
        System.out.println("[TransityDB Input] >>> ");
        String line = console.next();
        String[] parts;
        String arg = "";

        while (line != null && !line.equals("q")) {
            parts = line.split("\\s+");

            if (line.indexOf(" ") > 0)
                arg = line.substring(line.indexOf(" ")).trim();

            // For triggering command
            if (parts[0].equals("h"))
                printHelp();
            else if (parts[0].equals("ms")) {
                db.getMostStops();
            } else if (parts[0].equals("mr")) {
                db.getMostRoutes();
            }

            else if (parts[0].equals("route")) {
                if (parts.length >= 2)
                    db.displayRoute(arg);
                else
                    System.out.println("Please include route id after 'route' command");
            }

            else if (parts[0].equals("stop")) {
                if (parts.length >= 2)
                    db.displayStop(arg);
                else
                    System.out.println("Please include stop id after 'route' command");
            }
            /***
             * else if (parts[0].equals("l")) {
             * try {
             * if (parts.length >= 2)
             * db.lookupByID(arg);
             * else
             * System.out.println("Require an argument for this command");
             * } catch (Exception e) {
             * System.out.println("id must be an integer");
             * }
             * }
             * 
             * else if (parts[0].equals("sell")) {
             * try {
             * if (parts.length >= 2)
             * db.lookupWhoSells(arg);
             * else
             * System.out.println("Require an argument for this command");
             * } catch (Exception e) {
             * System.out.println("id must be an integer");
             * }
             * }
             * 
             * else if (parts[0].equals("notsell")) {
             * try {
             * if (parts.length >= 2)
             * db.whoDoesNotSell(arg);
             * else
             * System.out.println("Require an argument for this command");
             * } catch (Exception e) {
             * System.out.println("id must be an integer");
             * }
             * }
             * 
             * else if (parts[0].equals("authors")) {
             * 
             * db.top5Author();
             * 
             * } else if (parts[0].equals("mc")) {
             * db.mostCities();
             * }
             * 
             * else if (parts[0].equals("notread")) {
             * db.ownBooks();
             * }
             * 
             * else if (parts[0].equals("all")) {
             * db.readAll();
             * }
             * 
             * else if (parts[0].equals("mr")) {
             * db.mostReadPerCountry();
             * }
             */
            else
                System.out.println("Read the help with h, or find help somewhere else.");

            System.out.print("db > ");
            line = console.nextLine();
        }

        console.close();
    }

}

class TransitDatabase {
    private Connection connection;

    public TransitDatabase(String authFile, String sqlFile) {
        connection = null;
        Properties properties = new Properties();
        try {
            FileInputStream configFile = new FileInputStream(authFile);
            properties.load(configFile);
            configFile.close();
        } catch (IOException e) {
            System.out.println("Can't read file.");
            System.exit(1);
        }

        String username = (properties.getProperty("username"));
        String password = (properties.getProperty("password"));

        if (username == null) {
            System.out.println("Username not provided.");
            System.exit(1);
        } else if (password == null) {
            System.out.println("Password not provided.");
            System.exit(1);
        } else {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;database=cs3380;user=" + username + ";"
                                + "password=" + password
                                + ";encrypt=false;trustServerCertificate=false;loginTimeout=30;");
                System.out.println("Connection established......");
                buildData(sqlFile);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void buildData(String file) {
        try {
            Scanner readIn = new Scanner(new File(file));
            System.out.println("Building Data!");
            while (readIn.hasNextLine()) {
                try {
                    String sql = readIn.nextLine();
                    System.out.println(sql);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Data Built......");
        } catch (IOException e) {
            System.out.println("Invalid file");
        }
    }

    public void displayRoute(String name) {
        System.out.println("Yay you triggered the displayRoute thing"); // Delete
        try {
            String sql = "Select * from Route;";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("route") + " - " + resultSet.getString("route") + " "
                        + resultSet.getString("route name") + ", " + resultSet.getInt("name "));

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void displayStop(String name) {
        System.out.println("Yay you triggered the display stop thing"); // Delete
    }

    public void getMostStops() {

    }

    public void getMostRoutes() {

    }

    /***
     * public void lookupByID(String id) {
     * 
     * }
     * 
     * public void getMostPublishers() {
     * 
     * }
     * 
     * public void lookupWhoSells(String id) {
     * 
     * }
     * 
     * public void whoDoesNotSell(String id) {
     * 
     * }
     * 
     * public void top5Author() {
     * 
     * }
     * 
     * public void mostReadPerCountry() {
     * 
     * }
     * 
     * public void ownBooks() {
     * 
     * }
     * 
     * public void readAll() {
     * 
     * }
     * 
     * public void mostCities() {
     * 
     * }
     **/
}