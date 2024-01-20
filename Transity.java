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

        System.out.println("================================ Commands for running database =========================");
        System.out.println("h - Get help");
        System.out.println("b - Build Database (will take forever)\n");
        System.out.println("-------------------------------- Commands for route information ------------------------");
        System.out.println("routeAZ         \t\t- Shows routes ordered by their names alphabetically from A to Z");
        System.out.println("routes <asc|desc> \t\t- Shows routes ");
        System.out.println("stopsAt <route id> \t\t- Get all the stops along a route");
        System.out.println("routeCoverage <coverage> \t- Shows all routes by coverage routes");
        System.out.println("\t\t\t        where <coverage> can be one of the following:");
        System.out.println("\t\t\t \treg - regular routes | fdr - feeder routes");
        System.out.println("\t\t\t \texp - express routes | supexp - super express routes");
        System.out.println("\t\t\t \trt - rapid transit routes");

        System.out
                .println("\n-------------------------------- Commands for stop information -------------------------");
        System.out.println("stops <asc|desc>   \t\t- Shows stops");
        System.out.println("routesAt <stop id> \t\t- Get all the routes at a stop");
        System.out.println(
                "stopsD <direction> \t\t- Shows stops based on direction they face (east, west, north, south)");
        System.out
                .println("stopsDf <direction> \t\t- Shows 10 stops farthest at direction (east, west, north, south) ");

        System.out
                .println("\n------------------------------ Commands for on time performance information -------------");
        System.out.println(
                "(On time performance of routes is measured by the average deviation time from scheduled time)");
        System.out.println("\notp <asc|desc>    \t\t - Lists on time performance of routes (shows 20 items)");
        System.out.println(
                "otpTime <asc|desc>         \t - on time performance of all routes by the hour of the day (24 hour) (shows 20 items)");
        System.out
                .println(
                        "otpStops <asc|desc> \t\t - Top 10 stops based on the average deviation from scheduled time (shows 20 items)");
        System.out
                .println("otpS <stop id>  \t\t - Lists on time performance of routes at a given stop (shows 20 items)");

        System.out
                .println("\n----------------------------- Commands for transit pass up information -----------------");
        System.out.println("\ntpu <asc|desc>   \t\t - Shows routes with their passup counts (shows 20 items)");
        System.out.println(
                "tpuTime <asc|desc> \t\t - Lists the most pass up count by hour of day (24 hour) (shows 20 items)");
        System.out.println(
                "tpuMonth <asc|desc> \t\t - Lists the most pass up count by month in the year (shows 20 items)");
        System.out.println(
                "tpuYear <asc|desc> \t\t - List pass up counts by year (from 2018 to 2021) from least to most (shows 20 items)");
        System.out.println("tpuStops <asc|desc> \t\t - Top 10 stops based on the # of passups (shows 20 items)");
        System.out.println("tpuS <stop id> \t\t\t - Number of pass ups of each route at a given stop (shows 20 items)");
        System.out.println(
                "tpuCoverage <coverage> \t\t - Number of pass ups of each route by coverage type (shows 20 items)");
        System.out.println("\t\t\t         where <coverage> can be one of the following:");
        System.out.println("\t\t\t \t reg - regular routes | fdr - feeder routes");
        System.out.println("\t\t\t \t exp - express routes | supexp - super express routes");
        System.out.println("\t\t\t \t rt - rapid transit routes\n");

        System.out
                .println("------------------------------------------------------------------------------------------");

        System.out.println("\n\nq - Exit the database program");
        System.out.println("=======================================================================================");
    }

    public static void runConsole(TransitDatabase db) {

        System.out.println("======Transity Database=====");
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the Transity Database! Type h for help. ");
        System.out.print("[TransityDB Input] >>> ");
        String line = console.next();
        String[] parts;
        String arg = "";
        String[] coverageList = { "reg", "fdr", "exp", "supexp", "rt" };
        String[] directions = { "north", "east", "west", "south" };

        while (line != null && !line.equals("q")) {
            parts = line.split("\\s+");

            if (line.indexOf(" ") > 0)
                arg = line.substring(line.indexOf(" ")).trim();

            // For triggering command
            if (parts[0].equals("h")) {
                printHelp();
            } else if (parts[0].equals("b")) {
                db.buildData();
            } else if (parts[0].equals("routeAZ")) {
                db.getAlphabeticalRoutes();
            } else if (parts[0].equals("routes")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getRoutes(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("stopsAt")) {
                if (parts.length >= 2)
                    db.getStopsAt(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("routeCoverage")) {
                if (parts.length >= 2 && Arrays.asList(coverageList).contains(arg))
                    db.getRoutesByCoveraage(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("stops")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getStops(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("routesAt")) {
                if (parts.length >= 2) {
                    try {
                        if (parts.length >= 2)
                            db.getRoutesAt(Integer.parseInt(arg));
                        else
                            System.out.println("Require an argument for this command");
                    } catch (Exception e) {
                        System.out.println("id must be an integer");
                    }
                }
            } else if (parts[0].equals("stopsD")) {
                if (parts.length >= 2 && Arrays.asList(directions).contains(arg))
                    db.getStopsD(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("stopsDf")) {
                if (parts.length >= 2 && Arrays.asList(directions).contains(arg))
                    db.getStopsDf(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("otp")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getOtp(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("otpTime")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getotpTime(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("otpS")) {
                if (parts.length >= 2) {
                    try {
                        if (parts.length >= 2)
                            db.getOtpS(Integer.parseInt(arg));
                        else
                            System.out.println("Require an argument for this command");
                    } catch (Exception e) {
                        System.out.println("id must be an integer");
                    }
                }
            } else if (parts[0].equals("otpStops")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getOtpStops(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("tpu")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getTpu(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("tpuTime")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getTpuTime(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("tpuMonth")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getTpuMonth(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("tpuYear")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getTpuYear(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("tpuS")) {
                if (parts.length >= 2) {
                    try {
                        if (parts.length >= 2)
                            db.getTpuS(Integer.parseInt(arg));
                        else
                            System.out.println("Require an argument for this command");
                    } catch (Exception e) {
                        System.out.println("id must be an integer");
                    }
                }
            } else if (parts[0].equals("tpuStops")) {
                if (parts.length >= 2 && (arg.toLowerCase().equals("asc") || arg.toLowerCase().equals("desc")))
                    db.getTpuStops(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else if (parts[0].equals("tpuCoverage")) {
                if (parts.length >= 2 && Arrays.asList(coverageList).contains(arg))
                    db.getTpuCoverage(arg);
                else
                    System.out.println("Require a valid argument for this command");
            } else
                System.out.println("Read the help with h, or find help somewhere else.");

            System.out.print("db > ");
            line = console.nextLine();
        }
        console.close();
    }

}

class TransitDatabase {
    private Connection connection;
    private String sqlFile;

    public TransitDatabase(String authFile, String sqlFile) {
        connection = null;
        if (sqlFile == null) {
            System.out.println("No SQL file.");
            System.exit(1);
        } else {
            this.sqlFile = sqlFile;
        }

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

        if ((username == null || username.equals("umID")) || (password == null || password.equals("sID"))) {
            System.out.println("Username/password not provided.");
            System.exit(1);
        } else {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;database=cs3380;user=" + username + ";"
                                + "password=" + password
                                + ";encrypt=false;trustServerCertificate=false;loginTimeout=30;");
                System.out.println("Connection established......");
            } catch (SQLException e) {
                System.out.println("Couldn't login.");
                System.exit(1);
            }
        }

    }

    public void buildData() {
        try {
            Scanner readIn = new Scanner(new File(sqlFile));
            System.out.println("Building Data! - this might take a while");
            while (readIn.hasNextLine()) {
                try {
                    String sql = readIn.nextLine();
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    System.out.println("Inseted 1 row");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Data Built......");
        } catch (IOException e) {
            System.out.println("Invalid file");
        }
    }

    // ------------------------------ Methods about routes
    public void getAlphabeticalRoutes() {
        try {
            String sql = "select * from route order by CAST(name AS VARCHAR(100)) ;";

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("route") + " - " + resultSet.getString("name") + " - "
                        + resultSet.getString("coverage"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getRoutes(String order) {
        try {
            String sql = "select * from route order by route";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("route") + " - " + resultSet.getString("name") + " - "
                        + resultSet.getString("coverage"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getStopsAt(String id) {
        try {
            String sql = "select route, Stops.stopID as SID, Stops.name as Name from stopsAt join Stops on stopsAt.stopID = Stops.stopID where route = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("route") + " - " + resultSet.getString("SID") + " - "
                        + resultSet.getString("Name"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getRoutesByCoveraage(String coverage) {
        try {
            String sql = "select * from route where coverage = ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            if (coverage.equals("fdr")) {
                statement.setString(1, "feeder");
            } else if (coverage.equals("exp")) {
                statement.setString(1, "express");
            } else if (coverage.equals("supexp")) {
                statement.setString(1, "super express");
            } else if (coverage.equals("rt")) {
                statement.setString(1, "rapid transit");
            } else {
                statement.setString(1, "regular");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("route") + " - " + resultSet.getString("name") + " - "
                        + resultSet.getString("coverage"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // ------------------------------ Methods about stops
    public void getStops(String order) {
        try {
            String sql = "select * from stops order by stopID";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("stopID") + " - " + resultSet.getString("name") + " - "
                        + resultSet.getString("direction"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getStopsD(String direction) {
        try {
            String sql = "select * from stops where direction= ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (direction.toLowerCase().equals("east")) {
                statement.setString(1, "Eastbound");
            } else if (direction.toLowerCase().equals("west")) {
                statement.setString(1, "Westbound");
            } else if (direction.toLowerCase().equals("north")) {
                statement.setString(1, "Northbound");
            } else {
                statement.setString(1, "Southbound");
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("stopID") + " - " + resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
        } catch (

        SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getStopsDf(String direction) {
        int count = 0;
        try {
            String sql = "select stopID, name from stops";
            if (direction.toLowerCase().equals("east")) {
                sql += " order by longitude desc;";
            } else if (direction.toLowerCase().equals("west")) {
                sql += " order by longitude;";
            } else if (direction.toLowerCase().equals("north")) {
                sql += " order by latitude desc;";
            } else {
                sql += " order by latitude;";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 10) {
                System.out.println(resultSet.getString("stopID") + " - " + resultSet.getString("name"));
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (

        SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getRoutesAt(int id) {
        try {
            String sql = "select stopsAt.route as route, route.name as name from route join stopsAt on route.route = stopsAt.route where stopsAt.stopID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("route") + " - " + resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // ------------------------------ Methods about on time performance
    public void getOtp(String order) {
        int count = 0;
        try {
            String sql = "select route,sum(deviation)/count(deviation) as AverageDeviation from onTime group by route order by AverageDeviation";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(resultSet.getString("route") + ": " + resultSet.getString("AverageDeviation")
                        + " Seconds from scheduled time on average");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getotpTime(String order) {
        int count = 0;
        try {
            String sql = "select CAST(time AS VARCHAR(100)) as time2,sum(deviation)/count(deviation) as AverageDeviation from onTime group by CAST(time AS VARCHAR(100)) order by AverageDeviation, count(deviation)";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(resultSet.getString("time2") + "hrs: " + resultSet.getString("AverageDeviation")
                        + " Seconds from scheduled time on average");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getOtpS(int stopID) {
        int count = 0;
        try {
            String sql = "select route,sum(deviation)/count(deviation) as AverageDeviation from onTime join onTimeLocation on onTime.onID = onTimeLocation.onID where stopID= ? group by route order by AverageDeviation DESC;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, stopID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(resultSet.getString("route") + ": " + resultSet.getString("AverageDeviation")
                        + " Seconds from scheduled time on average");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getOtpStops(String order) {
        int count = 0;
        try {
            String sql = "select stopID,sum(deviation)/count(deviation) as AverageDeviation from onTime join onTimeLocation on onTime.onID = onTimeLocation.onID group by stopID order by AverageDeviation";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(
                        "Stop - " + resultSet.getString("stopID") + ": " + resultSet.getString("AverageDeviation")
                                + " Seconds from scheduled time on average");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // ------------------------------ Methods about transit passups
    public void getTpu(String order) {
        int count = 0;
        try {
            String sql = "select route,count(passUpId) as Num from passUps where route is not null group by route order by Num";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(
                        resultSet.getString("route") + ": " + resultSet.getString("Num") + " passUps since 2018");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getTpuTime(String order) {
        int count = 0;
        try {
            String sql = "select CAST(time AS VARCHAR(100)) as time2,count(passUpId) as Num from passUps group by CAST(time AS VARCHAR(100)) order by Num";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(
                        resultSet.getString("time2") + "hrs: " + resultSet.getString("Num") + " passUps since 2018");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getTpuMonth(String order) {
        int count = 0;
        try {
            String sql = "select month,count(passUpId) as Num from passUps where month is not null group by month order by Num";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(
                        "Month " + resultSet.getString("Month") + ": " + resultSet.getString("Num")
                                + " passUps since 2018");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getTpuYear(String order) {
        int count = 0;
        try {
            String sql = "select year,count(passUpId) as Num from passUps where year is not null group by year order by Num";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(resultSet.getString("year") + ": " + resultSet.getString("Num"));
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getTpuS(int id) {
        int count = 0;
        try {
            String sql = "select passUps.route as route2,count(passUps.passUpId) as Num from passUps join passUpLocation on passUps.passUpId = PassUpLocation.passUpId join stopsAt on stopsAt.stopID = passUpLocation.stopID where passUpLocation.stopID= ? AND passUps.route is not null group by passUps.route order by Num DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(resultSet.getString("route2") + ": " + resultSet.getString("Num")
                        + " passUps since 2018");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getTpuStops(String order) {
        int count = 0;
        try {
            String sql = "select stopID,count(passUps.passUpId) as Num from passUps join passUpLocation on passUps.passUpId=PassUpLocation.passUpId where stopID is not null group by stopID order by Num";
            if (order.toLowerCase().equals("desc")) {
                sql += " desc;";
            } else {
                sql += ";";
            }
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println("Stop - " + resultSet.getString("stopID") + ": " + resultSet.getString("Num")
                        + " passUps since 2018");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    public void getTpuCoverage(String coverage) {
        int count = 0;
        try {
            String sql = "select route,count(passUps.passUpId) as Num from passUps join passUpLocation on passUps.passUpId=PassUpLocation.passUpId where route in (select route from route where coverage=?) group by route";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (coverage.equals("fdr")) {
                statement.setString(1, "feeder");
            } else if (coverage.equals("exp")) {
                statement.setString(1, "express");
            } else if (coverage.equals("supexp")) {
                statement.setString(1, "super express");
            } else if (coverage.equals("rt")) {
                statement.setString(1, "rapid transit");
            } else {
                statement.setString(1, "regular");
            }
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next() && count < 20) {
                System.out.println(resultSet.getString("route") + ": "
                        + resultSet.getString("Num")
                        + " passUps since 2018");
                count++;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

}