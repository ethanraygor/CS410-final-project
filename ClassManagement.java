import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class ClassManagement {

    /**
     * MAIN
     */

    /**
     * Main
     * @param args
     */
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
                    activeClassId = classManagement(activeClassId, con, scanner);
                    break;
                case "cam":
                    if(activeClassId>0){
                        running = categoryAssignmentManagement(activeClassId, con, scanner);
                    }else{
                        System.out.println("Activate a class first using Class Management");
                    }
                    break;
                case "sm":
                    studentManagement(activeClassId, con, scanner);
                    break;
                case "gr":
                    if(activeClassId>0){
                        running = gradeReporting(activeClassId, con, scanner);
                    }else{
                        System.out.println("Activate a class first using Class Management");
                    }
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
        System.out.println("");
        System.out.println("cm : Class Management");
        System.out.println("cam : Category and Assignment Management");
        System.out.println("sm : Student Management");
        System.out.println("gr : Grade Reporting");
        System.out.println("q : Quit");
    }

    /**
     * Class Management
     */

    /**
     * manage classes
     * @return
     */
    private static int classManagement(int classId, Connection c, Scanner scanner){
        // boolean running = true;
        int rVal = classId;
        String input = "";
        String[] args;

        classManagementMenu();
        //print class Management menu
        input = scanner.next();
        args = input.split("\\s+");

        switch(args[0]){
            case "cl":
                createClass(args, c);
                break;
            case "lc":
                ListClasses(c);
                break;
            case "al":
                rVal = activateClass(args, c);
                break;
            case "sc":
                System.out.println(classId);
                break;
            case "m":
                
                break;
            default:
                System.out.println("invalid selection");
                break;
        }
        return rVal;
    }

    /**
     * creates a class in the class table
     * @param args contains course number, term, section number, and description
     * @param c SQL connection
     */
    private static void createClass(String[] args, Connection c){
        Connection connection = c;
        Statement sqlStatement = null;

        try {
        	sqlStatement = connection.createStatement();
            sqlStatement.executeUpdate("insert into gradebook.classes (course_number, term, section, class_description) values ("+ args[1]+", "+ args[2]+", "+ Integer.parseInt(args[3])+", "+ args[4]+")");

        } catch (SQLException sqlException) {
            System.out.println("Failed add class to table");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * list the content of the class teble
     * @param c SQL connection
     */
    private static void ListClasses(Connection c){
        Connection connection = c;
        Statement sqlStatement = null;

        try {
        	sqlStatement = connection.createStatement();
            sqlStatement.executeUpdate("SELECT gradebook.classes.*, val.num_students FROM ( SELECT class_id, COUNT(student_id) AS num_students FROM enroll GROUP BY class_id ) val INNER JOIN gradebook.classes ON val.class_id = gradebook.classes.class_id");

        } catch (SQLException sqlException) {
            System.out.println("Failed to get list of class");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * selects the active class
     * @param c
     * @param scanner
     * @return
     */
    private static int activateClass(String[] args, Connection c){
        Connection connection = c;
        Statement sqlStatement = null;
        int rval = -1;

        try {
        	sqlStatement = connection.createStatement();
            ResultSet rs = null;
            if(args.length == 2){
                rs = sqlStatement.executeQuery("SELECT * FROM classes WHERE course_number = "+args[1]);
                if(rs.getFetchSize() == 1){
                    rval = Integer.parseInt(rs.getString("class_id"));
                }
            } else if(args.length == 3){
                rs = sqlStatement.executeQuery("SELECT * FROM classes WHERE course_number = "+args[1]+" and term = "+args[2]);
                if(rs.getFetchSize() == 1){
                    rval = Integer.parseInt(rs.getString("class_id"));
                }
            }else if(args.length == 4){
                rs = sqlStatement.executeQuery("SELECT * FROM classes WHERE course_number = "+args[1]+" and term = "+args[2]+" and section = "+args[3]+";");
                if(rs.getFetchSize() == 1){
                    rval = Integer.parseInt(rs.getString("class_id"));
                }
            }else{
                System.out.println("Failed activate a class");
            }

        } catch (SQLException sqlException) {
            System.out.println("Failed activate a class");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
        return rval;
    }

     /**
     * Prints the Category and Assignemnt Management Menu
     */
    private static void classManagementMenu(){
        System.out.println("");
        System.out.println("lc : List classes");
        System.out.println("cl [course number (e.g. CS410)] [term (e.g. Sp23)] [section number] [description] : Create a class");
        System.out.println("al [course number (e.g. CS410)] [term (e.g. Sp23)] [section number] : Select a class to active term and section number are optional");
        System.out.println("sc : show-class shows the currently-active class");
        System.out.println("m : Main Menu");
    }

    /**
     * CATEGORY AND ASSIGNMENT MANAGEMENT
     */

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

    /**
     * Student Management
     */

    /**
     * Manage students
     * @param classId
     * @param c
     * @param scanner
     * @return
     */
    private static int studentManagement(int classId, Connection c, Scanner scanner){
        // boolean running = true;
        int rVal = classId;
        String input = "";
        String[] args;

        studentManagementMenu();
        
        //print class Management menu
        input = scanner.next();
        args = input.split("\\s+");

        switch(args[0]){
            case "as":
                addStudent(args, c, classId);
                break;
            case "ase":
                addStudentE(args, c, classId);
                break;
            case "ss":
            showStudents(c, classId);
                break;
            case "sss":
                showStudentsString(c);
                break;
            case "ga":
                gradeAssignment(c, Integer.parseInt(args[1]), args[2], args[3]);
                    break;
            case "m":
                
                break;
            default:
                System.out.println("invalid selection");
                break;
        }
        return rVal;
    }

    /**
     * adds a student and enrolls them in the current class. If the student already exists, enroll them in the class
     * @param args
     * @param c
     */
    private static void addStudent(String[] args, Connection c, int classId){
        Connection connection = c;
        Statement sqlStatement = null;
        int studentid = Integer.parseInt(args[1]);
        String username = "";
        String firstNmae = "";
        String lastNmae = "";
        
        try {
        	sqlStatement = connection.createStatement();
            ResultSet rs = sqlStatement.executeQuery("SELECT * FROM students WHERE student_id = "+ args[1]);

            if(rs.getFetchSize() == 1){
                username =  rs.getString("username");
                firstNmae = rs.getString("student_firstname");
                lastNmae = rs.getString("student_lastname");

                // check that the name is correct if not update and print a statement
                if(!username.equals(args[2])) {
                    System.out.println("warning: user name is being updated");
                    // username = args[2];
                }
                if(!firstNmae.equals(args[3])){ 
                    System.out.println("warning: first name is being updated");
                    // firstNmae = args[3];
                }
                if(!lastNmae.equals(args[4])){ 
                    System.out.println("warning: last name is being updated");
                    // lastNmae = args[4];
                }
                
            } else{
                //student does not exits
                //add student to student table
                sqlStatement.executeUpdate("insert into students (student_id, username, student_firstname, student_lastname) values ("+args[1]+", "+args[2]+", "+args[3]+", "+args[4]);                   
            }

            //add student to class
            sqlStatement.executeUpdate("insert into enroll (class_id, student_id) values ("+classId+", "+studentid);
            
        } catch (SQLException sqlException) {
            System.out.println("Failed add student");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * enrolls an already-existing student in the current class
     * @param args
     * @param c
     */
    private static void addStudentE(String[] args, Connection c, int classId){
        Connection connection = c;
        Statement sqlStatement = null;
        
        try {
        	sqlStatement = connection.createStatement();
            sqlStatement.executeUpdate("insert into enroll (class_id, student_id) values ("+classId+", "+args[1]+")");
            
        } catch (SQLException sqlException) {
            System.out.println("Failed add student, student may not exist");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * show the student in the current class
     * @param args
     * @param c
     */
    private static void showStudents(Connection c, int classId){
        Connection connection = c;
        Statement sqlStatement = null;

        int studentid = 0;
        String username = "";
        String firstNmae = "";
        String lastNmae = "";
        
        try {
        	sqlStatement = connection.createStatement();
            ResultSet rs = sqlStatement.executeQuery("SELECT students.* FROM ( SELECT student_id FROM enroll WHERE class_id = "+classId+")val INNER JOIN students ON val.student_id = students.student_id");
            while(rs.next()){
                studentid =  Integer.parseInt(rs.getString("student_id"));
                username =  rs.getString("username");
                firstNmae = rs.getString("student_firstname");
                lastNmae = rs.getString("student_lastname");
                
                System.out.println(studentid +" "+username+" "+firstNmae+" "+lastNmae);
            }
            
        } catch (SQLException sqlException) {
            System.out.println("Failed add student, student may not exist");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * show the students with string in there name
     * @param args
     * @param c
     */
    private static void showStudentsString(Connection c){
        Connection connection = c;
        Statement sqlStatement = null;

        int studentid = 0;
        String username = "";
        String firstNmae = "";
        String lastNmae = "";
        
        try {
        	sqlStatement = connection.createStatement();
            ResultSet rs = sqlStatement.executeQuery("SELECT * FROM students WHERE LOWER(student_firstname) LIKE LOWER('%string%') OR LOWER(username) LIKE LOWER('%string%') OR LOWER(student_lastname) LIKE LOWER('%string%')");
            while(rs.next()){
                studentid = Integer.parseInt(rs.getString("student_id"));
                username =  rs.getString("username");
                firstNmae = rs.getString("student_firstname");
                lastNmae = rs.getString("student_lastname");
                
                System.out.println(studentid +" "+username+" "+firstNmae+" "+lastNmae);
            }
            
        } catch (SQLException sqlException) {
            System.out.println("Failed show student string");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * assignes grades to student for a given assignment
     * @param c
     * @param grade
     * @param username
     * @param assignmentname
     */
    private static void gradeAssignment(Connection c, int grade, String username, String assignmentname){
        Connection connection = c;
        Statement sqlStatement = null;

        try {

            //if grade exceed max show warning
            ResultSet rs = sqlStatement.executeQuery("SELECT * FROM assignments where assignment_name = "+assignmentname);
            int maxPoint = Integer.parseInt(rs.getString("assignment_value"));
            int assignmentId = Integer.parseInt(rs.getString("assignment_id"));
            if(grade > maxPoint){
                System.out.println("warning: Invalide number of points "+maxPoint);
            } else {
                //else
                //if student already has grade replace the grade
                rs = sqlStatement.executeQuery("SELECT * FROM students where username = "+username);
                int studentId = Integer.parseInt(rs.getString("student_id"));

                rs = sqlStatement.executeQuery("SELECT * FROM assigned where student_id = "+assignmentId+" and assignment_id = "+studentId);
                if(rs.getFetchSize() > 0){
                    sqlStatement.executeUpdate("update assigned SET grade = "+grade+" where student_id = "+studentId+" and assignment_id = "+assignmentId);
                } else{
                    sqlStatement.executeUpdate("insert into assigned (student_id, assignment_id, grade) values ("+studentId+", "+assignmentId+", "+grade+")");
                }
                //else add the grade
            }
            
        } catch (SQLException sqlException) {
            System.out.println("Failed grade assignment");
            // System.out.println(sqlException.getMessage());

        } finally {
            try {
                if (sqlStatement != null)
                    sqlStatement.close();
            } catch (SQLException se2) {}
        }
    }

    /**
     * student Management Menu
     */
    private static void studentManagementMenu(){
        System.out.println("");
        System.out.println("as : add-student username studentid First Last");
        System.out.println("ase : add-student username — enrolls an already-existing student in the current class"); 
        System.out.println("ss : show-students - show all students in the current class"); 
        System.out.println("sss : show-students string - show all students with 'string' in their name or username");
        System.out.println("ga : grade assignmentname username grad");
        System.out.println("m : menu");
    }
    /**
     * GRADE REPORTING AND CALCULATION
     */

    /**
     * Handles Grade Reporting
     * @param classID id of current active class
     * @param c SQL connection
     * @param scan System.in Scanner
     * @return false to quit
     */
    private static boolean gradeReporting(int classID, Connection c, Scanner scan){
        boolean rVal = true;
        boolean running = true;
        String input = "";
        String[] args;

        while(running){
            printGRMenu();

            input = scan.nextLine();
            args = input.split("\\s+");

            switch(args[0]){
                case "student-grades":
                    if(args.length!=2){
                        System.out.println("student-grades takes 1 arg");
                    }else{
                        studentGrades(args, classID, c);
                    }
                    break;
                case "gradebook":
                    if(args.length!=1){
                        System.out.println("gradebook takes 0 args");
                    }else{
                        gradebook(classID, c);
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
     * Prints gradebook of active class
     * @param classID id of active class
     * @param c SQL Connection
     */
    private static void gradebook(int classID, Connection c) {
        Statement s = null;

        System.out.println("");
        
        try{
            c.setAutoCommit(false);
            s = c.createStatement();
            ResultSet rSet = s.executeQuery("SELECT gradebook.students.student_id, gradebook.students.username, SUM(gradebook.assigned.grade * gradebook.assignments.assignment_value * gradebook.categories.weight / table1.total_weight) / 100 AS attempted_grade, SUM(COALESCE(gradebook.assigned.grade, 0) * gradebook.assignments.assignment_value * gradebook.categories.weight / table1.total_weight) / 100 AS possible_grade FROM gradebook.students JOIN gradebook.enroll ON gradebook.students.student_id = gradebook.enroll.student_id JOIN gradebook.categories ON gradebook.enroll.class_id = gradebook.categories.class_id JOIN (SELECT category_id, SUM(weight) AS total_weight FROM gradebook.categories WHERE class_id = "+Integer.toString(classID)+" GROUP BY category_id) table1 ON gradebook.categories.category_id = table1.category_id JOIN gradebook.assignments ON gradebook.categories.category_id = gradebook.assignments.category_id LEFT JOIN gradebook.assigned ON gradebook.students.student_id = gradebook.assigned.student_id AND gradebook.assignments.assignment_id = gradebook.assigned.assigned_id WHERE gradebook.enroll.class_id = "+Integer.toString(classID)+" GROUP BY gradebook.students.student_id, gradebook.students.username");
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
     * prints grades of specific student in a class
     * @param args contains username
     * @param classID active class id
     * @param c SQL connection
     */
    private static void studentGrades(String[] args, int classID, Connection c) {
        int id = classID;
        String username = args[1];
        int stID = -1;
        String studentID = "0";
        int attempted = 0;
        int scored = 0;
        int possible = 0;
        HashMap<String, Double> weights = new HashMap<String, Double>();
        HashSet<String> categories = new HashSet<String>();
        HashMap<String, Double> possibleScores = new HashMap<String, Double>();
        HashMap<String, Double> attemptedScores = new HashMap<String, Double>();
        String currCategory = "";
        Statement s = null;
        Statement s2 = null;

        System.out.println("");
        
        try{
            c.setAutoCommit(false);
            s2 = c.createStatement();
            ResultSet rSet2 = s2.executeQuery("SELECT gradebook.students.student_id FROM gradebook.students WHERE gradebook.students.username='"+username+"'");
            if(rSet2.next()){
                studentID = rSet2.getString(1);
            }
            stID = Integer.parseInt(studentID);
            s2.close();
            s2 = c.createStatement();
            rSet2 = s2.executeQuery("SELECT AVG(gradebook.categories.weight) AS weight, gradebook.categories.category_name AS cat_name FROM gradebook.categories INNER JOIN gradebook.assignments ON gradebook.categories.category_id=gradebook.assignments.category_id WHERE gradebook.categories.class_id="+Integer.toString(classID)+" GROUP BY cat_name;");
            while(rSet2.next()){
                weights.put(rSet2.getString(2), Double.parseDouble(rSet2.getString(1)));
                categories.add(rSet2.getString(2));
            }
            s = c.createStatement();
            ResultSet rSet = s.executeQuery("SELECT gradebook.assignments.assignment_name AS name, gradebook.assignments.assignment_value AS points, gradebook.assigned.grade AS score, gradebook.assigned.grade / gradebook.assignments.assignment_value AS grade, gradebook.categories.category_name AS category FROM gradebook.assignments INNER JOIN gradebook.categories ON gradebook.assignments.category_id = gradebook.categories.category_id INNER JOIN gradebook.assigned ON gradebook.assignments.assignment_id=gradebook.assigned.assigned_id AND gradebook.assigned.student_id="+Integer.toString(stID)+" WHERE gradebook.categories.class_id="+Integer.toString(id)+" ORDER BY gradebook.assignments.category_id");
            ResultSetMetaData rsmd = rSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            System.out.println("assignment , possible points , points earned , score , category");
            while(rSet.next()){
                for(int i=1; i<=columnCount; i++){
                    if(i>1){
                        System.out.print(", ");
                    }
                    System.out.print(rSet.getString(i)+" ");
                }
                System.out.println(" ");
            }
            System.out.println("");
            s.close();
            s = c.createStatement();
            rSet = s.executeQuery("SELECT gradebook.assignments.assignment_name AS name, gradebook.assignments.assignment_value AS points, gradebook.assigned.grade AS score, gradebook.assigned.grade / gradebook.assignments.assignment_value AS grade, gradebook.categories.category_name AS category FROM gradebook.assignments INNER JOIN gradebook.categories ON gradebook.assignments.category_id = gradebook.categories.category_id INNER JOIN gradebook.assigned ON gradebook.assignments.assignment_id=gradebook.assigned.assigned_id AND gradebook.assigned.student_id="+Integer.toString(stID)+" WHERE gradebook.categories.class_id="+Integer.toString(id)+" ORDER BY gradebook.assignments.category_id");
            System.out.println("category , attempted score , total score");
            boolean first = true;
            double score;
            while(rSet.next()){
                if(!currCategory.equals(rSet.getString(5))){
                    if(!first){
                        if(attempted==0){
                            System.out.print("null , ");
                            attemptedScores.put(currCategory, null);
                        }else{
                            score = (double)scored / (double)attempted;
                            attemptedScores.put(currCategory, score);
                            System.out.print(Double.toString(score)+" , ");
                        }
                        if(possible==0){
                            System.out.println("null");
                            possibleScores.put(currCategory, null);
                        }else{
                            score = (double)scored / (double)possible;
                            possibleScores.put(currCategory, score);
                            System.out.println(Double.toString(score));
                        }
                    }else{
                        first=false;
                    }
                    currCategory = rSet.getString(5);
                    System.out.print(currCategory+" , ");
                    attempted = 0;
                    scored = 0;
                    possible = 0;
                }
                possible+=Integer.parseInt(rSet.getString(2));
                if(rSet.getString(3)!=null){
                    attempted+=Integer.parseInt(rSet.getString(2));
                    scored+=Integer.parseInt(rSet.getString(3));
                }
            }
            if(attempted==0){
                System.out.print("null , ");
                attemptedScores.put(currCategory, null);
            }else{
                score = (double)scored / (double)attempted;
                attemptedScores.put(currCategory, score);
                System.out.print(Double.toString(score)+" , ");
            }
            if(possible==0){
                System.out.println("null");
                possibleScores.put(currCategory, null);
            }else{
                score = (double)scored / (double)possible;
                possibleScores.put(currCategory, score);
                System.out.println(Double.toString(score));
            }
            System.out.println("");
            double totalAttempted = 0.0;
            double totalPossible = 0.0;
            double totalWeight = 0.0;
            for(String cat : categories){
                if(attemptedScores.get(cat)!=null){
                    totalAttempted += attemptedScores.get(cat)*weights.get(cat);
                }
                if(possibleScores.get(cat)!=null){
                    totalPossible += possibleScores.get(cat)*weights.get(cat);
                }
                totalWeight += weights.get(cat);
            }
            totalAttempted = totalAttempted/totalWeight;
            totalPossible = totalPossible/totalWeight;
            System.out.println("Attempted Grade : "+Double.toString(totalAttempted));
            System.out.println("Total Grade     : "+Double.toString(totalPossible));
            System.out.println("");
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
     * Grade Reporting Menu
     */
    private static void printGRMenu() {
        System.out.println("");
        System.out.println("student-grades username : show student’s current grades");
        System.out.println("gradebook : show the current class’s gradebook");
        System.out.println("m : Main Menu");
        System.out.println("q : Quit");
    }
}
