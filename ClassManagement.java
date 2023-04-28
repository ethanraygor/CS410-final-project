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

        int activeClassId = -1; //Used to keep track of active class
        
        while (running){
            printMenu();
            input = scanner.next();
            input = input.toLowerCase();

            switch(input){
                case "cm":
                        /**
                         * Use activeClassId to keep track of active class or update the other functions
                        */
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
                    if(activeClassId>0){
                        running = categoryAssignmentManagement(activeClassId);
                    }else{
                        System.out.println("Activate a class first using Class Management");
                    }
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

    /**
     * Prints the Main Menu
     */
    private static void printMenu(){
        System.out.println("cm : Class Management");
        System.out.println("cam : Category and Assignment Management");
        System.out.println("sm : Student Management");
        System.out.println("gr : Grade Reporting");
        System.out.println("gc : Grade Calculation");
        System.out.println("q : Quit");
    }

    /**
     * Handles the Category and Assignment Management Menu
     * @param classId the id of the currently selected class
     * @return tell the main menu to quit or not
     */
    private static boolean categoryAssignmentManagement(int classId){
        boolean running = true;
        boolean rVal = true;
        Scanner scanner = new Scanner(System.in);
        String input = "";
        String[] args;

        while(running){
            printCAMMenu();

            input = scanner.nextLine();
            args = input.split("\\s+");

            switch(args[0]){
                case "show-categories":
                    if(args.length!=1){
                        System.out.println("invalid selection");
                    }else{
                        showCategories(classId);
                    }
                    break;
                case "add-category":
                    if(args.length!=3){
                        System.out.println("invalid selection");
                    }else{
                        addCategory(args, classId);
                    }
                    break;
                case "show-assignment":
                    if(args.length!=1){
                        System.out.println("invalid selection");
                    }else{
                        showAssignments(classId);
                    }
                    break;
                case "add-assignment":
                    if(args.length!=5){
                        System.out.println("invalid selection");
                    }else{
                        addAssignment(args, classId);
                    }
                    break;
                case "m":
                    running = false;
                    break;
                case "q":
                    running = false;
                    rVal = false;
                    break;
                default:
                    System.out.println("invalid selection");
                    break;
            }
        }

        scanner.close();

        return rVal;
    }

    /**
     * Adds an Assignment in the current class
     * @param args contains name, category, description, and points
     * @param classId current class ID
     */
    private static void addAssignment(String[] args, int classId) {
        int id = classId;
        String name = args[1];
        String category = args[2];
        String description = args[3];
        int points = Integer.parseInt(args[4]);
    }

    /**
     * Shows the assignments of the current class
     * @param classId current class ID
     */
    private static void showAssignments(int classId) {
        int id = classId;
    }

    /**
     * Adds a category to the current class
     * @param args contains name and weight of category
     * @param classId current class ID
     */
    private static void addCategory(String[] args, int classId) {
        int id = classId;
        String name = args[1];
        int weight = Integer.parseInt(args[2]);
    }

    /**
     * Shows the categories in the current class
     * @param classId current class ID
     */
    private static void showCategories(int classId) {
        int id = classId;
    }

    /**
     * Prints the Category and Assignemnt Management Menu
     */
    private static void printCAMMenu(){
        System.out.println("show-categories : list the categories with their weights");
        System.out.println("add-category name weight : add a new category"); 
        System.out.println("show-assignment : list the assignments with their point values, grouped by category"); 
        System.out.println("add-assignment name category description points : add a new assignment");
        System.out.println("m : Main Menu");
        System.out.println("q : Quit");
    }
}
