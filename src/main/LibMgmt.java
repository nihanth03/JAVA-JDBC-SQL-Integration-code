package main;

import java.util.*;
// import java.io.Console;

import authentication.UserAuthentication;
// import library.Book;
// import library.Section;
import users.Librarian;
import users.Reader;
// import database.DBconnection;
// import java.sql.*;

public class LibMgmt {
    public static void main(String args[]){
        
        // Console console=System.console();
        Scanner sc=new Scanner(System.in);

        System.out.println("Welcome to the Library");
        System.out.println("Press (1) for login - Press (2) to Register as a reader");
        int choice=sc.nextInt();
        sc.nextLine();
        if(choice==2){
            System.out.println("Enter new username: ");
            String newUsername=sc.nextLine();

            System.out.println("Enter new password: ");
            String newPassword=sc.nextLine();

            boolean isRegistered = Reader.register(newUsername, newPassword);
            if (isRegistered){
                System.out.println("Registration successful! You can now log in.");
            } 
            else {
                System.out.println("Registration failed. Try a different username.");
            }
            sc.close();
            return;
        }
       
        System.out.println("Enter Username:");
        String username=sc.nextLine();
        System.out.println("Enter password: ");
        String password=sc.nextLine();
        System.out.println("Enter Role (librarian/reader)");
        String role=sc.nextLine().toLowerCase();

        boolean isAuth=UserAuthentication.authenticateUser(username, password, role);

        if (isAuth) {
            System.out.println("Login successful! Welcome, " + username);

            if (role.equals("librarian")) {
                Librarian librarian = new Librarian(username, UserAuthentication.getUserId(username));
                while (true) {
                    System.out.println("\n1. Add Book");
                    System.out.println("2. Remove Book");
                    System.out.println("3. View All Books");
                    System.out.println("4. View Borrowed Books");
                    System.out.println("5. Exit");
                    System.out.print("Enter choice: ");
                    int option = sc.nextInt();

                    switch (option) {
                        case 1:
                            System.out.println("Enter book title: ");
                            sc.nextLine(); 
                            String title = sc.nextLine();
                            System.out.println("Enter author: ");
                            String author = sc.nextLine();
                            System.out.println("Enter section ID: ");
                            int sectionId = sc.nextInt();
                            librarian.addBook(title, author, sectionId);
                            break;

                        case 2:
                            System.out.print("Enter book ID to remove: ");
                            int bookId = sc.nextInt();
                            librarian.removeBook(bookId);
                            break;

                        case 3:
                            librarian.viewAllBooks();
                            break;

                        case 4:
                            librarian.viewBorrowedBooks();
                            break;

                        case 5:
                            System.out.println("Goodbye!");
                            return;

                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                }
            } else if (role.equals("reader")) {
                Reader reader = new Reader(username, UserAuthentication.getUserId(username));
                
                while (true) {
                    System.out.println("\n1. Borrow Book");
                    System.out.println("2. Return Book");
                    System.out.println("3. View Borrowed Books");
                    System.out.println("4. View Available Books");
                    System.out.println("5. Exit");
                    System.out.print("Enter choice: ");
                    int option = sc.nextInt();

                    switch (option) {
                        case 1:
                            System.out.println("Enter book ID to borrow: ");
                            int borrowBookId = sc.nextInt();
                            reader.borrowBook(borrowBookId);
                            break;
                
                        case 2:
                            System.out.println("Enter book ID to return: ");
                            int returnBookId = sc.nextInt();
                            reader.returnBook(returnBookId);
                            break;
                
                        case 3:
                            reader.viewBorrowedBooks();
                            break;

                        case 4:
                            reader.viewAvailableBooks();
                            break;
                
                        case 5:
                            System.out.println("Goodbye!");
                            return;  // Exits the loop and method
                
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                    }
                }
            }
        } else {
            System.out.println("Login failed! Invalid credentials.");
        }
        sc.close();
    }

}
