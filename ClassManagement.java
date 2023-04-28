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
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
     * @param scanner System.in scanner
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
                        System.out.println("show-categories takes 0 args");
                    }else{
                        showCategories(classId, c);
                    }
                    break;
                case "add-category":
                    if(args.length!=3){
                        System.out.println("add-category takes 2 args");
                    }else{
                        addCategory(args, classId, c);
                    }
                    break;
                case "show-assignments":
                    if(args.length!=1){
                        System.out.println("show-assignments takes 0 args");
                    }else{
                        showAssignments(classId, c);
                    }
                    break;
                case "add-assignment":
                    if(args.length!=5){
                        System.out.println("add-assignment takes 4 args");
                    }else{
                        addAssignment(args, classId, c);
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
     * @param c SQL connection
     */
    private static void addAssignment(String[] args, int classId, Connection c) {
        int id = classId;
        String name = args[1];
        String category = args[2];
        String description = args[3];
        int points = Integer.parseInt(args[4]);
        int catId = -1;
        String categoryId = "0";
        Statement s = null;
        Statement s2 = null;

        System.out.println("");

        try{
            c.setAutoCommit(false);
            s2 = c.createStatement();
            ResultSet rSet = s2.executeQuery("SELECT gradebook.categories.category_id FROM gradebook.categories WHERE gradebook.categories.class_id="+Integer.toString(id)+" AND gradebook.categories.category_name='"+category+"'");
            if(rSet.next()){
                categoryId = rSet.getString(1);
            }
            catId = Integer.parseInt(categoryId);
            s = c.createStatement();
            s.executeUpdate("INSERT INTO gradebook.assignments (assignment_name, assignment_description, assignment_value, category_id) VALUES ('"+name+"', '"+description+"', "+Integer.toString(points)+", "+Integer.toString(catId)+")");
            System.out.println("Assignment added!");
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
                if(s2!=null){
                    s2.close();
                }
                c.setAutoCommit(true);
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Shows the assignments of the current class
     * @param classId current class ID
     * @param c SQL connection
     */
    private static void showAssignments(int classId, Connection c) {
        int id = classId;
        Statement s = null;

        System.out.println("");
        
        try{
            c.setAutoCommit(false);
            s = c.createStatement();
            ResultSet rSet = s.executeQuery("SELECT gradebook.assignments.assignment_name AS name, gradebook.assignments.assignment_description AS description, gradebook.assignments.assignment_value AS points, gradebook.categories.category_name AS category FROM gradebook.assignments INNER JOIN gradebook.categories ON gradebook.assignments.category_id = gradebook.categories.category_id WHERE gradebook.categories.class_id="+Integer.toString(id)+" ORDER BY gradebook.assignments.category_id");
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
     * Adds a category to the current class
     * @param args contains name and weight of category
     * @param classId current class ID
     * @param c SQL connection
     */
    private static void addCategory(String[] args, int classId, Connection c) {
        int id = classId;
        String name = args[1];
        int weight = Integer.parseInt(args[2]);
        Statement s = null;

        System.out.println("");

        try{
            c.setAutoCommit(false);
            s = c.createStatement();
            s.executeUpdate("INSERT INTO gradebook.categories (category_name, weight, class_id) VALUES ('"+name+"', "+Integer.toString(weight)+", "+Integer.toString(id)+")");
            System.out.println("Category added!");
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
     * Shows the categories in the current class
     * @param classId current class ID
     * @param c SQL connection
     */
    private static void showCategories(int classId, Connection c){
        int id = classId;
        Statement s = null;

        System.out.println("");
        
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
        System.out.println("");
        System.out.println("show-categories : list the categories with their weights");
        System.out.println("add-category name weight : add a new category"); 
        System.out.println("show-assignments : list the assignments with their point values, grouped by category"); 
        System.out.println("add-assignment name category description points : add a new assignment");
        System.out.println("m : Main Menu");
        System.out.println("q : Quit");
    }
}
