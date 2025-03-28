package library;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private int id;
    private String name;
    private List<Book> books;

    public Section(int id, String name){
        this.id=id;
        this.name=name;
        this.books=new ArrayList<>();
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public void addBook(Book book){
        books.add(book);
    }

    public void removeBook(Book book){
        books.remove(book);
    }
}
