package users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import authentication.UserAuthentication;
import database.DBconnection;

public class Reader implements User {
    private String name;
    private int id;

    public Reader(String name,int id){
        this.name=name;
        this.id=id;
    }

    public String getName(){
        return name;
    }
    
    public int getid(){
        return id;
    }

    public static boolean login(String username, String password){
        return UserAuthentication.authenticateUser(username, password, "reader");
    }

    public static boolean register(String username, String password){
        return UserAuthentication.registerUser(username, password);
    }

    public void borrowBook(int bookId){
        String checkSql = "SELECT is_lended FROM books WHERE id = ?";
        String borrowSql = "INSERT INTO borrowed_books (reader_id, book_id, borrow_date) VALUES (?, ?, CURDATE())";
        String updateBookSql = "UPDATE books SET is_lended = 1 WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement borrowStmt = conn.prepareStatement(borrowSql);
             PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {

            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getBoolean("is_lended")) {
                System.out.println("Error: Book is already borrowed.");
                return;
            }

            // Insert into borrowed_books
            borrowStmt.setInt(1, this.id);
            borrowStmt.setInt(2, bookId);
            borrowStmt.executeUpdate();

            // Update book status
            updateBookStmt.setInt(1, bookId);
            updateBookStmt.executeUpdate();

            System.out.println(name + " successfully borrowed book ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // if(!book.isLended()){
        //     borrowedBooks.add(book);
        //     book.setLended(true);
        //     System.out.println(name + " borrowed " + book.getTitle());
        // }
        // else{
        //     System.out.println("Book is already lended");
        // }
    }
    
    public void returnBook(int bookId){
        String returnSql = "UPDATE borrowed_books SET return_date = CURDATE() WHERE book_id = ? AND reader_id = ? AND return_date IS NULL";
        String updateBookSql = "UPDATE books SET is_lended = 0 WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement returnStmt = conn.prepareStatement(returnSql);
             PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql)) {

            returnStmt.setInt(1, bookId);
            returnStmt.setInt(2, this.id);
            int rowsUpdated = returnStmt.executeUpdate();

            if (rowsUpdated > 0) {
                updateBookStmt.setInt(1, bookId);
                updateBookStmt.executeUpdate();
                System.out.println(name + " returned book ID: " + bookId);
            } else {
                System.out.println("Error: No active borrow record found for this book.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        // if(borrowedBooks.contains(book)){
        //     borrowedBooks.remove(book);
        //     book.setLended(false);
        //     System.out.println(name+" returned "+ book.getTitle());
        // }
        // else{
        //     System.out.println("Error: "+name+" cannot return "+ book.getTitle()+" as it is not borrowed by the user.");
        // }
        
    public void viewBorrowedBooks() {
        String sql = "SELECT books.id, books.title FROM borrowed_books JOIN books ON borrowed_books.book_id = books.id WHERE borrowed_books.reader_id = ? AND borrowed_books.return_date IS NULL";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, this.id);
                ResultSet rs = stmt.executeQuery();
                System.out.println(name + "'s Borrowed Books:");
                while (rs.next()) {
                    System.out.println("- " + rs.getInt("id") + ": " + rs.getString("title"));
                }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    public void viewAvailableBooks(){
        try(Connection conn=DBconnection.getConnection()){
            String sql = "Select books.id, books.title from books " +
            "where is_lended=0";
            PreparedStatement stmt=conn.prepareStatement(sql);
            ResultSet rs=stmt.executeQuery();

            System.out.println("\nAvailable Books:");
            while(rs.next()){
                System.out.println("Book ID: "+rs.getInt("id")+
                ", Title: "+rs.getString("title"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
