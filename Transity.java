import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;

public class Transity {

    public static void main(String[] args) throws Exception {

        // startup sequence
        TransitDatabase db = new TransitDatabase();
        runConsole(db);

        System.out.println("Exiting...");
    }

    private static void printHelp() {

        System.out.println("======Commands for running database======");
        System.out.println("h - Get help");
        System.out.println("q - Exit the database program");
        System.out.println("=========================================");
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

            if (parts[0].equals("h"))
                printHelp();
            else if (parts[0].equals("mp")) {
                db.getMostPublishers();
            }

            else if (parts[0].equals("s")) {
                if (parts.length >= 2)
                    db.nameSearch(arg);
                else
                    System.out.println("Require an argument for this command");
            }

            else if (parts[0].equals("l")) {
                try {
                    if (parts.length >= 2)
                        db.lookupByID(arg);
                    else
                        System.out.println("Require an argument for this command");
                } catch (Exception e) {
                    System.out.println("id must be an integer");
                }
            }

            else if (parts[0].equals("sell")) {
                try {
                    if (parts.length >= 2)
                        db.lookupWhoSells(arg);
                    else
                        System.out.println("Require an argument for this command");
                } catch (Exception e) {
                    System.out.println("id must be an integer");
                }
            }

            else if (parts[0].equals("notsell")) {
                try {
                    if (parts.length >= 2)
                        db.whoDoesNotSell(arg);
                    else
                        System.out.println("Require an argument for this command");
                } catch (Exception e) {
                    System.out.println("id must be an integer");
                }
            }

            else if (parts[0].equals("authors")) {

                db.top5Author();

            } else if (parts[0].equals("mc")) {
                db.mostCities();
            }

            else if (parts[0].equals("notread")) {
                db.ownBooks();
            }

            else if (parts[0].equals("all")) {
                db.readAll();
            }

            else if (parts[0].equals("mr")) {
                db.mostReadPerCountry();
            }

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

    public TransitDatabase() {
        try {
            String url = "jdbc:sqlite:library.db";
            // create a connection to the database
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    public void nameSearch(String name) {

    }

    public void lookupByID(String id) {

    }

    public void getMostPublishers() {

    }

    public void lookupWhoSells(String id) {

    }

    public void whoDoesNotSell(String id) {

    }

    public void top5Author() {

    }

    public void mostReadPerCountry() {

    }

    public void ownBooks() {

    }

    public void readAll() {

    }

    public void mostCities() {

    }

}