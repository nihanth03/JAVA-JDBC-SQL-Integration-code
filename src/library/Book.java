package library;

public class Book {
    private String title;
    private String author;
    private Section section;
    private boolean isLended;

    public Book(String title, String author, Section section) {
        this.title = title;
        this.author = author;
        this.section = section;
        this.isLended = false;
        section.addBook(this);
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isLended() { return isLended; }
    public void setLended(boolean lended) { isLended = lended; }
    public String getSection() { return section.getName(); }
    
}
