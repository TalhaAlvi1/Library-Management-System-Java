package random;

import java.util.*;
import java.time.LocalDate;

class Book {
    private String id;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

class Member {
    private String id;
    private String name;
    private List<Book> borrowedBooks;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }
}

class Library {
    private List<Book> books;
    private List<Member> members;
    private Map<Book, LocalDate> borrowedDates;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        borrowedDates = new HashMap<>();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book.getTitle());
    }

    public void addMember(Member member) {
        members.add(member);
        System.out.println("Member added: " + member.getName());
    }

    public void borrowBook(Member member, Book book) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            member.getBorrowedBooks().add(book);
            borrowedDates.put(book, LocalDate.now());
            System.out.println("Book '" + book.getTitle() + "' borrowed by " + member.getName());
        } else {
            System.out.println("Book is not available for borrowing.");
        }
    }

    public void returnBook(Member member, Book book) {
        if (member.getBorrowedBooks().contains(book)) {
            book.setAvailable(true);
            member.getBorrowedBooks().remove(book);
            LocalDate borrowedDate = borrowedDates.remove(book);
            System.out.println("Book '" + book.getTitle() + "' returned by " + member.getName());

            long daysBorrowed = borrowedDate.until(LocalDate.now()).getDays();
            System.out.println("Book was borrowed for " + daysBorrowed + " days");
        } else {
            System.out.println("This book was not borrowed by this member.");
        }
    }

    public List<Book> getBooks() { return books; }
    public List<Member> getMembers() { return members; }

    public void displayAvailableBooks() {
        System.out.println("\nAvailable Books:");
        for (Book book : books) {
            if (book.isAvailable()) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    public void displayMemberBooks(Member member) {
        System.out.println("\nBooks borrowed by " + member.getName() + ":");
        for (Book book : member.getBorrowedBooks()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor());
        }
    }
}

public class main {
    public static void main(String[] args) {
        Library library = new Library();

        Book book1 = new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book("B002", "To Kill a Mockingbird", "Harper Lee");
        Book book3 = new Book("B003", "1984", "George Orwell");

        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);

        Member member1 = new Member("M001", "John Doe");
        Member member2 = new Member("M002", "Jane Smith");

        library.addMember(member1);
        library.addMember(member2);

        System.out.println("\nInitial available books:");
        library.displayAvailableBooks();

        library.borrowBook(member1, book1);
        library.borrowBook(member2, book2);

        System.out.println("\nAvailable books after borrowing:");
        library.displayAvailableBooks();

        System.out.println("\nBooks borrowed by members:");
        library.displayMemberBooks(member1);
        library.displayMemberBooks(member2);

        library.returnBook(member1, book1);

        System.out.println("\nFinal available books:");
        library.displayAvailableBooks();
    }
}