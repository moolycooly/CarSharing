package carsharing;

import javax.xml.crypto.Data;
import java.beans.Customizer;
import java.rmi.UnexpectedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Query {
    public static void createTableCompany() {
        String dropTable = "DROP TABLE IF EXISTS COMPANY;";
        String dropTableCar = "DROP TABLE IF EXISTS CAR;";
        String dropTableCustomer = "DROP TABLE IF EXISTS CUSTOMER;";
        String createTable = "CREATE TABLE IF NOT EXISTS COMPANY(ID INT AUTO_INCREMENT, NAME VARCHAR(20) NOT NULL UNIQUE, PRIMARY KEY (ID));";
        try {
//            Database.statement.execute(dropTableCustomer);
//            Database.statement.execute(dropTableCar);
//            Database.statement.execute(dropTable);
            Database.statement.execute(createTable);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createTableCar() {
        String createTable = "CREATE TABLE IF NOT EXISTS CAR(ID INT AUTO_INCREMENT, NAME VARCHAR(20) NOT NULL UNIQUE, COMPANY_ID INT NOT NULL, FLAG INT DEFAULT 0, PRIMARY KEY (ID), FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID));";
        try {
            Database.statement.execute(createTable);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createTableCustomer() {
        String createTable = "CREATE TABLE IF NOT EXISTS CUSTOMER(ID INT AUTO_INCREMENT, NAME VARCHAR(20) NOT NULL UNIQUE, RENTED_CAR_ID INT, PRIMARY KEY (ID), FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID));";
        try {
            Database.statement.execute(createTable);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void addCompany(String company) throws SQLException {
        String query = "INSERT INTO COMPANY (NAME) VALUES ('"+company+"');";
        Database.statement.executeUpdate(query);
    }
    public static void addCar(int companyid, String name) throws SQLException {
        String query = "INSERT INTO CAR (NAME,COMPANY_ID) VALUES ('"+name+"'," + companyid + ");";
        Database.statement.executeUpdate(query);
    }
    public static void addCustomer(String name) throws SQLException {
        String query = "INSERT INTO CUSTOMER (NAME) VALUES ('"+name+"');";
        Database.statement.executeUpdate(query);
    }
    public static int printCars(int companyid, boolean customer) throws SQLException {
        String query = "SELECT * from CAR WHERE COMPANY_ID="+companyid+" AND FLAG=0;";
        ResultSet res = Database.statement.executeQuery(query);
        int cnt = 0,idx=1;
        if(res.first()==false) {
            if(customer) System.out.println("No available cars in the company name");
            else System.out.println("The car list is empty!");
            return 0;
        }
        if(customer) {
            System.out.println("Choose car:");
        }
        do {
            if(cnt==0) cnt = res.getInt(1);
            System.out.println(idx + ". " + res.getString(2));
            idx++;
        }while(res.next());
        return cnt;
    }
    public static int printCompanies() throws SQLException {
        String query = "SELECT * from COMPANY";
        ResultSet res = Database.statement.executeQuery(query);
        ArrayList<String> companynames = new ArrayList<>();
        int cnt = 0;
        while(res.next()){
            cnt++;
            companynames.add(res.getString(2));
        }
        if(cnt==0){
            System.out.println("The company list is empty!");
            return cnt;
        }
        System.out.println("Choose company:");
        for(int i = 0; i < cnt; i++) {
            System.out.println(i+1 + ". " + companynames.get(i));
        }
        System.out.println("0. Back");
        return cnt;
    }
    public static int printCustomers() throws SQLException {
        String query = "SELECT * from CUSTOMER";
        ResultSet res = Database.statement.executeQuery(query);
        ArrayList<String> customernames = new ArrayList<>();
        int cnt = 0;
        while(res.next()){
            cnt++;
            customernames.add(res.getString(2));
        }
        if(cnt==0){
            System.out.println("The customer list is empty!");
            return cnt;
        }
        System.out.println("Choose a customer:");
        for(int i = 0; i < cnt; i++) {
            System.out.println(i+1 + ". " + customernames.get(i));
        }
        System.out.println("0. Back");
        return cnt;
    }
    public static boolean hasCar(int customerid, boolean customer) throws SQLException {
        String query = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID ="  + customerid + " AND RENTED_CAR_ID IS NOT NULL;";
        ResultSet res = Database.statement.executeQuery(query);
        int cnt=0;
        while(res.next()) cnt++;
        if(cnt==0) {
            if(customer==false)
                System.out.println("You didn't rent a car!");
            return false;
        }
        return true;
    }
    public static void printRentedCars(int customerid) throws SQLException {
        String query = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID ="  + customerid + ";";
        ResultSet res = Database.statement.executeQuery(query);
        res.next();

        query = "SELECT NAME,COMPANY_ID FROM CAR WHERE ID=" + res.getInt(1) +";";
        ResultSet result = Database.statement.executeQuery(query);
        result.next();
        System.out.println("Your rented car:");
        System.out.println(result.getString(1));

//        query = "SELECT NAME FROM COMPANY WHERE ID=" + result.getInt(2) +";";
//        ResultSet result_company = Database.statement.executeQuery(query);
//        result_company.next();

        System.out.println("Company:");
        System.out.println(getName("COMPANY", result.getInt(2)));
    }
    public static void returnRentedCar(int customerid) throws SQLException {
        if(hasCar(customerid,false)==false) return;
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID=NULL WHERE ID="+customerid+";";
        Database.statement.executeUpdate(query);
        System.out.println("You've returned a rented car!");
    }
    public static void rentCar(int customerid, int car_id) throws SQLException {
        String query = "UPDATE CUSTOMER SET RENTED_CAR_ID="+car_id+"WHERE ID="+customerid+";";
        Database.statement.executeUpdate(query);

        query = "UPDATE CAR SET FLAG=1 WHERE ID="+car_id+";";
        Database.statement.executeUpdate(query);

        System.out.println("You rented '" + getName("CAR", car_id) + "'");
    }
    public static String getName(String table,int id) throws SQLException {
        String query1= " WHERE ID=" + id + ";";
        String query2 = "SELECT NAME from " + table + query1;
        ResultSet res=Database.statement.executeQuery(query2);
        res.next();
        return res.getString(1);
    }
}
