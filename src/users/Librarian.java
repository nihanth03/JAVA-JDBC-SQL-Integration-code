package users;


import database.DBconnection;
import java.sql.*;

import authentication.UserAuthentication;

public class Librarian implements User {
    private String name;
    private int id;

    public Librarian(String name,int id){
        this.name=name;
        this.id=id;
    }

    public String getName(){
        return name;
    }

    public int getid(){
        return id;
    }

    public static boolean login(String username,String password){
        return UserAuthentication.authenticateUser(username, password, "librarian");
    }

    // public void addBook(Section section,Book book){
    //     // section.addBook(book);
    //     // System.out.println(name+" added "+book.getTitle()+" to "+section.getName());
    //     try(Connection conn=DBconnection.getConnection()){
    //         String sql="INSERT INTO books(title,author,section_id,is_lended) VALUES (?,?,?,0)";
    //         PreparedStatement stmt=conn.prepareStatement(sql);
    //         stmt.setString(1,book.getTitle());
    //         stmt.setString(2,book.getAuthor());
    //         stmt.setInt(3,section.getId());
    //         int rowsInserted=stmt.executeUpdate();

    //         if(rowsInserted>0){
    //             section.addBook(book);
    //             System.out.println(name+" added "+book.getTitle()+" to "+section.getName());
    //         }
    //         else{
    //             System.out.println("Error: Book could not be added to the database.");
    //         }

    //     }
    //     catch(SQLException e){
    //         e.printStackTrace();
    //     }
    // }

    // public void removeBook(Section section, Book book){
    //     section.removeBook(book);
    //     System.out.println(name+" removed "+book.getTitle()+" from "+section.getName());
    // }

    public void addBook(String title, String author, int sectionId) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "INSERT INTO books (title, author, section_id) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, sectionId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBook(int bookId) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book removed successfully!");
            } else {
                System.out.println("Book not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllBooks() {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nLibrary Books:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                                   ", Author: " + rs.getString("author") + ", Section ID: " + rs.getInt("section_id") +
                                   ", Lended: " + rs.getBoolean("is_lended"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewBorrowedBooks() {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT books.id, books.title, users.name AS reader_name, borrowed_books.borrow_date " +
                         "FROM borrowed_books " +
                         "JOIN books ON borrowed_books.book_id = books.id " +
                         "JOIN users ON borrowed_books.reader_id = users.id " +
                         "WHERE borrowed_books.return_date IS NULL";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nBorrowed Books:");
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("id") +
                                   ", Title: " + rs.getString("title") +
                                   ", Borrowed by: " + rs.getString("reader_name") +
                                   ", Borrow Date: " + rs.getDate("borrow_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
