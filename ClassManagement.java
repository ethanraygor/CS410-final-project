import java.sql.*;
import java.util.Scanner;

public class ClassManagement {
    
    public static void main(String[] args) {
        try {
            Connection con = Database.getDatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean running = true;

        Scanner scanner = new Scanner(System.in);
        String input = "";
        
        while (running){
            printMenu();
            input = scanner.next();
            input = input.toLowerCase();

            switch(input){
                case "cm":
                        System.out.print("Create a class: ");
                        Scanner sc = new Scanner(scanner.next());
                        sc.useDelimiter(" ");
                        String courseNumber = sc.next();
                        String term = "";
                        int sectionNumber = 0;
                        if(sc.hasNext()){
                            term = sc.next();
                            if(sc.hasNext()){
                                sectionNumber = sc.nextInt();
                                if(sc.hasNext()){
                                    sectionNumber = sc.nextInt();
                                }
                            }

                        }
                        // reateClass()
                    break;
                case "cam":
                    break;
                case "sm":
                    break;
                case "gr":
                    break;
                case "gc":
                    break;
                case "q":
                    running=false;
                    break;
                default:
                    System.out.println("invalid selection");
                    break;
            }
        }

        scanner.close();
    }

    private static void printMenu(){
        System.out.println("cm - Class Management");
        System.out.println("cam - Category and Assignment Management");
        System.out.println("sm - Student Management");
        System.out.println("gr - Grade Reporting");
        System.out.println("gc - Grade Calculation");
        System.out.println("q - Quit");
    }
}
