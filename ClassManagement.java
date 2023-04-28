import java.sql.*;
import java.util.Scanner;

public class ClassManagement {
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = Database.getDatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean running = true;

        Scanner scanner = new Scanner(System.in);
        String input = "";

        /**
         * CHANGE TO -1 AFTER CM IMPLEMENTED
         */
        int activeClassId = 1; //Used to keep track of active class
        
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
                        running = categoryAssignmentManagement(activeClassId, con, scanner);
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
     * @param c SQL connection
     * @return tell the main menu to quit or not
     */
    private static boolean categoryAssignmentManagement(int classId, Connection c, Scanner scanner){
        boolean running = true;
        boolean rVal = true;
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
                        showCategories(classId, c);
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
     * @param c SQL connection
     */
    private static void showCategories(int classId, Connection c){
        int id = classId;
        Statement s = null;
        
        try{
            c.setAutoCommit(false);
            s = c.createStatement();
            ResultSet rSet = s.executeQuery("SELECT gradebook.categories.category_name AS name, gradebook.categories.weight FROM gradebook.categories WHERE gradebook.categories.class_id="+Integer.toString(id)+";");
            ResultSetMetaData rsmd = rSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int i=1; i<=columnCount; i++){
                if(i>1){
                    System.out.print(", ");
                }
                System.out.print(rsmd.getColumnName(i));
            }
            System.out.println(" ");
            while(rSet.next()){
                for(int i=1; i<=columnCount; i++){
                    if(i>1){
                        System.out.print(", ");
                    }
                    System.out.print(rSet.getString(i)+" ");
                }
                System.out.println(" ");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
            try {
                c.rollback();
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
        }finally{
            try{
                if(s!=null){
                    s.close();
                }
                c.setAutoCommit(true);
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        
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
