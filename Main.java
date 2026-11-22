import java.io.*;
import java.util.*;

/*
 * Student Management System
 * Module 02: Methods, Collections & Exceptions
 *
 * Features: add, view, search, update and delete students.
 * Records are stored in a HashMap and saved to students.txt.
 *
 * Compile: javac Main.java
 * Run:     java Main
 */

// Data class: one student record
class Student {
    private int id;
    private String name;
    private double marks;

    public Student(int id, String name, double marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }

    public int getId()        { return id; }
    public String getName()   { return name; }
    public double getMarks()  { return marks; }

    public void setName(String name)    { this.name = name; }
    public void setMarks(double marks)  { this.marks = marks; }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Marks: " + marks;
    }
}

// Custom checked exception
class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}

// Handles all operations on the collection and the file
class StudentManager {
    private Map<Integer, Student> students = new HashMap<>();

    public void add(Student s) {
        if (students.containsKey(s.getId())) {
            System.out.println("ID already exists.");
            return;
        }
        students.put(s.getId(), s);
        System.out.println("Student added.");
    }

    public Student find(int id) throws StudentNotFoundException {
        Student s = students.get(id);
        if (s == null) {
            throw new StudentNotFoundException("No student with ID " + id);
        }
        return s;
    }

    public void viewAll() {
        if (students.isEmpty()) {
            System.out.println("No records.");
            return;
        }
        for (Student s : students.values()) {
            System.out.println(s);
        }
    }

    public void update(int id, String name, double marks) throws StudentNotFoundException {
        Student s = find(id);
        s.setName(name);
        s.setMarks(marks);
        System.out.println("Student updated.");
    }

    public void delete(int id) throws StudentNotFoundException {
        find(id);               // throws if the ID does not exist
        students.remove(id);
        System.out.println("Student deleted.");
    }

    public void save(String file) {
        try (FileWriter fw = new FileWriter(file)) {
            for (Student s : students.values()) {
                fw.write(s.getId() + "," + s.getName() + "," + s.getMarks() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public void load(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                int id = Integer.parseInt(p[0]);
                students.put(id, new Student(id, p[1], Double.parseDouble(p[2])));
            }
        } catch (IOException e) {
            System.out.println("No saved data found. Starting fresh.");
        }
    }
}

// Menu and user input
public class Main {
    public static void main(String[] args) {
        StudentManager manager = new StudentManager();
        manager.load("students.txt");
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (choice != 6) {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add student");
            System.out.println("2. View all students");
            System.out.println("3. Search by ID");
            System.out.println("4. Update student");
            System.out.println("5. Delete student");
            System.out.println("6. Save and exit");
            System.out.print("Choice: ");

            try {
                choice = sc.nextInt();
                switch (choice) {
                    case 1: {
                        System.out.print("ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Marks: ");
                        double marks = sc.nextDouble();
                        manager.add(new Student(id, name, marks));
                        break;
                    }
                    case 2:
                        manager.viewAll();
                        break;
                    case 3: {
                        System.out.print("ID: ");
                        System.out.println(manager.find(sc.nextInt()));
                        break;
                    }
                    case 4: {
                        System.out.print("ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("New name: ");
                        String name = sc.nextLine();
                        System.out.print("New marks: ");
                        double marks = sc.nextDouble();
                        manager.update(id, name, marks);
                        break;
                    }
                    case 5: {
                        System.out.print("ID: ");
                        manager.delete(sc.nextInt());
                        break;
                    }
                    case 6:
                        break;
                    default:
                        System.out.println("Invalid choice. Enter 1 to 6.");
                }
            } catch (StudentNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Please enter numbers where numbers are needed.");
                sc.nextLine();
            }
        }

        manager.save("students.txt");
        System.out.println("Saved. Goodbye!");
        sc.close();
    }
}
