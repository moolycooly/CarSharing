package carsharing;

import javax.xml.crypto.Data;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try{
            Database.createConnection();
            Query.createTableCompany();
            Query.createTableCar();
            Query.createTableCustomer();
            Menu.mainMenu();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}