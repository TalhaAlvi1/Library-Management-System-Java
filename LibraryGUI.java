package random;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class LibraryGUI extends JFrame {
    private Library library;
    private JTextField bookIdField, bookTitleField, bookAuthorField;
    private JTextField memberIdField, memberNameField;
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    private JComboBox<String> memberComboBox;
    private JComboBox<String> bookComboBox;

    public LibraryGUI() {
        library = new Library();
        setupGUI();
        initializeSampleData();
    }

    private void setupGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Books Management", createBooksPanel());
        tabbedPane.addTab("Members Management", createMembersPanel());
        tabbedPane.addTab("Borrow/Return", createBorrowReturnPanel());

        add(tabbedPane, BorderLayout.CENTER);
        
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        bookIdField = new JTextField();
        bookTitleField = new JTextField();
        bookAuthorField = new JTextField();
        
        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(bookIdField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(bookTitleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(bookAuthorField);
        
        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> addBook());
        formPanel.add(new JLabel(""));
        formPanel.add(addBookButton);

        String[] columns = {"ID", "Title", "Author", "Available"};
        booksTableModel = new DefaultTableModel(columns, 0);
        booksTable = new JTable(booksTableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        memberIdField = new JTextField();
        memberNameField = new JTextField();
        
        formPanel.add(new JLabel("Member ID:"));
        formPanel.add(memberIdField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(memberNameField);
        
        JButton addMemberButton = new JButton("Add Member");
        addMemberButton.addActionListener(e -> addMember());
        formPanel.add(new JLabel(""));
        formPanel.add(addMemberButton);
        
        panel.add(formPanel, BorderLayout.NORTH);
        
        return panel;
    }

    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel controlPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        memberComboBox = new JComboBox<>();
        bookComboBox = new JComboBox<>();
        
        controlPanel.add(new JLabel("Select Member:"));
        controlPanel.add(memberComboBox);
        controlPanel.add(new JLabel("Select Book:"));
        controlPanel.add(bookComboBox);
        
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");
        
        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        
        controlPanel.add(borrowButton);
        controlPanel.add(returnButton);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        return panel;
    }

    private void addBook() {
        String id = bookIdField.getText();
        String title = bookTitleField.getText();
        String author = bookAuthorField.getText();
        
        if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Book book = new Book(id, title, author);
        library.addBook(book);
        updateBooksTable();
        updateBookComboBox();
        
        // Clear fields
        bookIdField.setText("");
        bookTitleField.setText("");
        bookAuthorField.setText("");
    }

    private void addMember() {
        String id = memberIdField.getText();
        String name = memberNameField.getText();
        
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Member member = new Member(id, name);
        library.addMember(member);
        updateMemberComboBox();
        
        // Clear fields
        memberIdField.setText("");
        memberNameField.setText("");
    }

    private void borrowBook() {
        String memberName = (String) memberComboBox.getSelectedItem();
        String bookTitle = (String) bookComboBox.getSelectedItem();
        
        if (memberName == null || bookTitle == null) {
            JOptionPane.showMessageDialog(this, "Please select both member and book", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Member member = findMemberByName(memberName);
        Book book = findBookByTitle(bookTitle);
        
        if (member != null && book != null) {
            library.borrowBook(member, book);
            updateBooksTable();
            updateBookComboBox();
        }
    }

    private void returnBook() {
        String memberName = (String) memberComboBox.getSelectedItem();
        String bookTitle = (String) bookComboBox.getSelectedItem();
        
        if (memberName == null || bookTitle == null) {
            JOptionPane.showMessageDialog(this, "Please select both member and book", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Member member = findMemberByName(memberName);
        Book book = findBookByTitle(bookTitle);
        
        if (member != null && book != null) {
            library.returnBook(member, book);
            updateBooksTable();
            updateBookComboBox();
        }
    }

    private void updateBooksTable() {
        booksTableModel.setRowCount(0);
        for (Book book : library.getBooks()) {
            booksTableModel.addRow(new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.isAvailable() ? "Yes" : "No"
            });
        }
    }

    private void updateMemberComboBox() {
        memberComboBox.removeAllItems();
        for (Member member : library.getMembers()) {
            memberComboBox.addItem(member.getName());
        }
    }

    private void updateBookComboBox() {
        bookComboBox.removeAllItems();
        for (Book book : library.getBooks()) {
            if (book.isAvailable()) {
                bookComboBox.addItem(book.getTitle());
            }
        }
    }

    private Member findMemberByName(String name) {
        for (Member member : library.getMembers()) {
            if (member.getName().equals(name)) {
                return member;
            }
        }
        return null;
    }

    private Book findBookByTitle(String title) {
        for (Book book : library.getBooks()) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    private void initializeSampleData() {

        library.addBook(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald"));
        library.addBook(new Book("B002", "To Kill a Mockingbird", "Harper Lee"));
        library.addBook(new Book("B003", "1984", "George Orwell"));

        library.addMember(new Member("M001", "John Doe"));
        library.addMember(new Member("M002", "Jane Smith"));

        updateBooksTable();
        updateMemberComboBox();
        updateBookComboBox();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}
