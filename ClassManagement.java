import java.util.Scanner;

public class ClassManagement {
    
    public static void main(String[] args) {
        System.out.println("Class Management");
        System.out.println("Category and Assignment Management");
        System.out.println("Student Management");
        System.out.println("Grade Reportin");
        System.out.println("Grade Calculation");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        input = input.toLowerCase();

        switch(input){
            case "class management":
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
            default:
                System.out.println("invalid selection");
                break;
        }
    }
}
