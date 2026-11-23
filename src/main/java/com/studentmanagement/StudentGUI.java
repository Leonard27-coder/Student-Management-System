package com.studentmanagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

public class StudentGUI extends JFrame {

    // DATABASE SERVICE
    private StudentService service;

    // CARDLAYOUT
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // FORM FIELDS
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField programmeField;
    private JTextField yearField;
    private JTextField emailField;

    // STUDENT TABLE
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;

    // DASHBOARD STATISTICS
    private JLabel totalStudentsLabel;
    private JLabel programmeCountLabel;

    // STATUS
    private JLabel statusLabel;

    // COLOURS
    private final Color NAVY = new Color(24, 43, 77);
    private final Color BLUE = new Color(43, 105, 190);
    private final Color GREEN = new Color(28, 155, 115);
    private final Color LIGHT_BG = new Color(244, 247, 252);
    private final Color TEXT = new Color(45, 55, 72);
    private final Color BORDER = new Color(225, 230, 238);

    // PAGE NAMES
    private static final String DASHBOARD = "dashboard";
    private static final String STUDENTS = "students";
    private static final String REGISTER = "register";

    // CONSTRUCTOR
    public StudentGUI() {

        service = new StudentService();

        setTitle("Student Management System");
        setSize(1250, 750);
        setMinimumSize(new Dimension(1050, 650));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();

        loadStudents();
    }

    // =====================================
    // MAIN INTERFACE
    // =====================================

    private void createInterface() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(LIGHT_BG);

        root.add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(LIGHT_BG);

        cardPanel.add(createDashboardPanel(), DASHBOARD);
        cardPanel.add(createStudentsPanel(), STUDENTS);
        cardPanel.add(createRegisterPanel(), REGISTER);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(LIGHT_BG);

        main.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 15, 25)
        );

        main.add(createHeader(), BorderLayout.NORTH);
        main.add(cardPanel, BorderLayout.CENTER);
        main.add(createStatusPanel(), BorderLayout.SOUTH);

        root.add(main, BorderLayout.CENTER);

        setContentPane(root);

        showPage(DASHBOARD);
    }

    // =====================================
    // SIDEBAR NAVIGATION
    // =====================================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(NAVY);
        sidebar.setPreferredSize(new Dimension(220, 0));

        sidebar.setBorder(
            BorderFactory.createEmptyBorder(25, 15, 20, 15)
        );

        JLabel logo = new JLabel("SMS");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("STUDENT PORTAL");
        subtitle.setForeground(new Color(190, 205, 230));
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(45));

        JLabel menuTitle = new JLabel("MAIN MENU");
        menuTitle.setForeground(new Color(160, 180, 210));
        menuTitle.setFont(new Font("Arial", Font.BOLD, 11));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(menuTitle);
        sidebar.add(Box.createVerticalStrut(15));

        JButton dashboard = createMenuButton("Dashboard");

        dashboard.addActionListener(e -> {
            loadStudents();
            showPage(DASHBOARD);
        });

        JButton students = createMenuButton("Students");

        students.addActionListener(e -> {
            loadStudents();
            showPage(STUDENTS);
        });

        JButton register = createMenuButton("Register Student");

        register.addActionListener(e -> {
            clearFields();
            showPage(REGISTER);
        });

        sidebar.add(dashboard);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(students);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(register);

        sidebar.add(Box.createVerticalGlue());

        JButton exit = createMenuButton("Exit Application");

        exit.addActionListener(e -> {

            int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit Application",
                JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        sidebar.add(exit);

        return sidebar;
    }

    // =====================================
    // NAVIGATION BUTTON STYLE
    // =====================================

    private JButton createMenuButton(String text) {

        JButton button = new JButton(text);

        button.setForeground(Color.WHITE);
        button.setBackground(NAVY);
        button.setFont(new Font("Arial", Font.PLAIN, 14));

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 43)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(
            new java.awt.event.MouseAdapter() {

                public void mouseEntered(
                    java.awt.event.MouseEvent e
                ) {
                    button.setBackground(BLUE);
                }

                public void mouseExited(
                    java.awt.event.MouseEvent e
                ) {
                    button.setBackground(NAVY);
                }
            }
        );

        return button;
    }

    // =====================================
    // SWITCH BETWEEN PAGES
    // =====================================

    private void showPage(String page) {

        cardLayout.show(cardPanel, page);

        if (page.equals(DASHBOARD)) {
            statusLabel.setText("Dashboard overview");
        }
        else if (page.equals(STUDENTS)) {
            statusLabel.setText("Viewing student records");
        }
        else if (page.equals(REGISTER)) {
            statusLabel.setText("Ready to register a student");
            idField.requestFocusInWindow();
        }
    }

    // =====================================
    // HEADER
    // =====================================

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Student Management System");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(NAVY);

        JLabel description = new JLabel(
            "Manage student information and academic records"
        );

        description.setFont(new Font("Arial", Font.PLAIN, 13));
        description.setForeground(Color.GRAY);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);

        titles.add(title);
        titles.add(Box.createVerticalStrut(5));
        titles.add(description);

        JLabel admin = new JLabel("ADMINISTRATION");
        admin.setFont(new Font("Arial", Font.BOLD, 11));
        admin.setForeground(BLUE);

        header.add(titles, BorderLayout.WEST);
        header.add(admin, BorderLayout.EAST);

        return header;
    }

    // =====================================
    // PAGE 1: DASHBOARD
    // =====================================

    private JPanel createDashboardPanel() {

        JPanel dashboard = new JPanel(new BorderLayout(20, 20));
        dashboard.setBackground(LIGHT_BG);

        JPanel top = new JPanel(new GridLayout(1, 2, 15, 0));
        top.setOpaque(false);

        totalStudentsLabel = new JLabel("0");
        programmeCountLabel = new JLabel("0");

        top.add(
            createStatCard(
                "TOTAL STUDENTS",
                totalStudentsLabel,
                "Registered student records",
                BLUE
            )
        );

        top.add(
            createStatCard(
                "PROGRAMMES",
                programmeCountLabel,
                "Different programmes represented",
                GREEN
            )
        );

        dashboard.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);

        center.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            )
        );

        JLabel welcome = new JLabel("Welcome to SMS");
        welcome.setFont(new Font("Arial", Font.BOLD, 25));
        welcome.setForeground(NAVY);

        JLabel message = new JLabel(
            "Your central platform for managing student academic records."
        );

        message.setFont(new Font("Arial", Font.PLAIN, 14));
        message.setForeground(Color.GRAY);

        center.add(welcome);
        center.add(Box.createVerticalStrut(12));
        center.add(message);
        center.add(Box.createVerticalStrut(35));

        JLabel quickTitle = new JLabel("QUICK ACTIONS");
        quickTitle.setFont(new Font("Arial", Font.BOLD, 13));
        quickTitle.setForeground(NAVY);

        center.add(quickTitle);
        center.add(Box.createVerticalStrut(15));

        JPanel actions = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 12, 0)
        );

        actions.setOpaque(false);

        JButton register = createActionButton("Register Student", BLUE);
        JButton view = createActionButton("View Students", GREEN);

        register.addActionListener(e -> {
            clearFields();
            showPage(REGISTER);
        });

        view.addActionListener(e -> {
            loadStudents();
            showPage(STUDENTS);
        });

        actions.add(register);
        actions.add(view);

        center.add(actions);

        dashboard.add(center, BorderLayout.CENTER);

        return dashboard;
    }

    // =====================================
    // STATISTICS CARD
    // =====================================

    private JPanel createStatCard(
        String title,
        JLabel value,
        String description,
        Color accent
    ) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
            )
        );

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.BOLD, 11));
        heading.setForeground(Color.GRAY);

        value.setFont(new Font("Arial", Font.BOLD, 30));
        value.setForeground(accent);

        JLabel details = new JLabel(description);
        details.setFont(new Font("Arial", Font.PLAIN, 12));
        details.setForeground(Color.GRAY);

        card.add(heading);
        card.add(Box.createVerticalStrut(8));
        card.add(value);
        card.add(Box.createVerticalStrut(5));
        card.add(details);

        return card;
    }

    // =====================================
    // PAGE 2: STUDENTS
    // =====================================

    private JPanel createStudentsPanel() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);

        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
            )
        );

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setOpaque(false);

        JLabel heading = new JLabel("All Student Records");
        heading.setFont(new Font("Arial", Font.BOLD, 19));
        heading.setForeground(NAVY);

        JPanel searchPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        searchPanel.setOpaque(false);

        searchField = new JTextField(16);
        searchField.setPreferredSize(new Dimension(180, 35));

        JButton search = createActionButton("Search", BLUE);
        JButton refresh = createActionButton("Refresh", GREEN);

        searchPanel.add(searchField);
        searchPanel.add(search);
        searchPanel.add(refresh);

        top.add(heading, BorderLayout.WEST);
        top.add(searchPanel, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);

        String[] columns = {
            "Student ID",
            "First Name",
            "Last Name",
            "Programme",
            "Year",
            "Email"
        };

        model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(36);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setSelectionForeground(NAVY);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        JTableHeader tableHeader = table.getTableHeader();

        tableHeader.setFont(new Font("Arial", Font.BOLD, 12));
        tableHeader.setBackground(new Color(235, 241, 249));
        tableHeader.setForeground(NAVY);
        tableHeader.setPreferredSize(new Dimension(100, 40));
        tableHeader.setReorderingAllowed(false);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));

        panel.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        bottom.setOpaque(false);

        JButton update = createActionButton(
            "Update Year", new Color(210, 145, 40)
        );

        JButton delete = createActionButton(
            "Delete Student", new Color(195, 65, 65)
        );

        bottom.add(update);
        bottom.add(delete);

        panel.add(bottom, BorderLayout.SOUTH);

        search.addActionListener(e -> searchStudent());
        searchField.addActionListener(e -> searchStudent());

        refresh.addActionListener(e -> {
            searchField.setText("");
            loadStudents();
        });

        update.addActionListener(e -> updateStudent());
        delete.addActionListener(e -> deleteStudent());

        return panel;
    }

    // =====================================
    // PAGE 3: REGISTER STUDENT
    // =====================================

    private JPanel createRegisterPanel() {

        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(LIGHT_BG);

        // OUTER FORM CONTAINER
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);

        form.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
            )
        );

        // FIX: Give the form a proper width and height.
        form.setPreferredSize(new Dimension(540, 555));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(5, 0, 8, 0);

        JLabel heading = new JLabel("Register New Student");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(NAVY);

        form.add(heading, gbc);

        JLabel subtitle = new JLabel(
            "Enter the student's details below."
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);

        form.add(subtitle, gbc);

        // INPUT FIELDS

        idField = new JTextField();
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        programmeField = new JTextField();
        yearField = new JTextField();
        emailField = new JTextField();

        addFormField(form, gbc, "Student ID", idField);
        addFormField(form, gbc, "First Name", firstNameField);
        addFormField(form, gbc, "Last Name", lastNameField);
        addFormField(form, gbc, "Programme", programmeField);
        addFormField(form, gbc, "Academic Year", yearField);
        addFormField(form, gbc, "Email Address", emailField);

        // BUTTONS

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);

        JButton register = createActionButton(
            "Register Student", BLUE
        );

        JButton clear = createActionButton(
            "Clear Fields", new Color(105, 115, 130)
        );

        register.setPreferredSize(new Dimension(190, 40));
        clear.setPreferredSize(new Dimension(150, 40));

        form.add(register, gbc);

        gbc.gridx = 1;
        form.add(clear, gbc);

        register.addActionListener(e -> registerStudent());
        clear.addActionListener(e -> clearFields());

        page.add(form);

        return page;
    }

    // =====================================
    // FORM FIELD CREATION
    // =====================================

    private void addFormField(
        JPanel form,
        GridBagConstraints gbc,
        String label,
        JTextField field
    ) {

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 3, 0);

        JLabel text = new JLabel(label);
        text.setFont(new Font("Arial", Font.BOLD, 12));
        text.setForeground(TEXT);

        form.add(text, gbc);

        gbc.gridy++;

        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(350, 34));

        field.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            )
        );

        form.add(field, gbc);
    }

    // =====================================
    // BUTTON STYLING
    // =====================================

    private JButton createActionButton(
        String text,
        Color color
    ) {

        JButton button = new JButton(text);

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(135, 36));

        return button;
    }

    // =====================================
    // STATUS BAR
    // =====================================

    private JPanel createStatusPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        statusLabel = new JLabel("System ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        statusLabel.setForeground(Color.GRAY);

        JLabel database = new JLabel("Database: MongoDB");
        database.setFont(new Font("Arial", Font.BOLD, 11));
        database.setForeground(GREEN);

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(database, BorderLayout.EAST);

        return panel;
    }

    // =====================================
    // REGISTER STUDENT
    // =====================================

    private void registerStudent() {

        try {

            String id = idField.getText().trim();
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String programme = programmeField.getText().trim();
            String email = emailField.getText().trim();

            if (
                id.isEmpty() ||
                first.isEmpty() ||
                last.isEmpty() ||
                programme.isEmpty() ||
                yearField.getText().trim().isEmpty() ||
                email.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields."
                );

                return;
            }

            int year = Integer.parseInt(
                yearField.getText().trim()
            );

            if (year < 1 || year > 10) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid academic year. Enter a year between 1 and 10."
                );

                return;
            }

            if (service.findStudent(id) != null) {

                JOptionPane.showMessageDialog(
                    this,
                    "Student ID already exists."
                );

                return;
            }

            Student student = new Student(
                id,
                first,
                last,
                programme,
                year,
                email
            );

            service.addStudent(student);

            JOptionPane.showMessageDialog(
                this,
                "Student registered successfully!"
            );

            clearFields();
            loadStudents();
            showPage(STUDENTS);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Academic Year must be a number."
            );

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // =====================================
    // LOAD ALL STUDENTS
    // =====================================

    private void loadStudents() {

        try {

            model.setRowCount(0);

            List<Document> students = service.getAllStudents();

            for (Document s : students) {

                model.addRow(new Object[] {

                    s.getString("studentId"),
                    s.getString("firstName"),
                    s.getString("lastName"),
                    s.getString("programme"),
                    s.getInteger("year"),
                    s.getString("email")

                });
            }

            updateStatistics(students);

            statusLabel.setText(
                students.size() + " student record(s) loaded"
            );

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // =====================================
    // UPDATE DASHBOARD STATISTICS
    // =====================================

    private void updateStatistics(List<Document> students) {

        totalStudentsLabel.setText(
            String.valueOf(students.size())
        );

        Set<String> programmes = new HashSet<>();

        for (Document student : students) {

            String programme = student.getString("programme");

            if (programme != null) {
                programmes.add(programme);
            }
        }

        programmeCountLabel.setText(
            String.valueOf(programmes.size())
        );
    }

    // =====================================
    // SEARCH STUDENT
    // =====================================

    private void searchStudent() {

        String id = searchField.getText().trim();

        if (id.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Enter a Student ID to search."
            );

            return;
        }

        try {

            Document s = service.findStudent(id);

            model.setRowCount(0);

            if (s == null) {

                JOptionPane.showMessageDialog(
                    this,
                    "Student not found."
                );

                statusLabel.setText("No matching student found");
                return;
            }

            model.addRow(new Object[] {

                s.getString("studentId"),
                s.getString("firstName"),
                s.getString("lastName"),
                s.getString("programme"),
                s.getInteger("year"),
                s.getString("email")

            });

            statusLabel.setText("Search result for " + id);

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // =====================================
    // UPDATE STUDENT YEAR
    // =====================================

    private void updateStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Select a student from the table first."
            );

            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        String id = model.getValueAt(row, 0).toString();
        String currentYear = model.getValueAt(row, 4).toString();

        String input = JOptionPane.showInputDialog(
            this,
            "Enter the new academic year:",
            currentYear
        );

        if (input == null) {
            return;
        }

        try {

            int year = Integer.parseInt(input.trim());

            if (year < 1 || year > 10) {

                JOptionPane.showMessageDialog(
                    this,
                    "Invalid academic year."
                );

                return;
            }

            service.updateStudentYear(id, year);

            JOptionPane.showMessageDialog(
                this,
                "Student academic year updated successfully!"
            );

            loadStudents();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // =====================================
    // DELETE STUDENT
    // =====================================

    private void deleteStudent() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow < 0) {

            JOptionPane.showMessageDialog(
                this,
                "Select a student from the table first."
            );

            return;
        }

        int row = table.convertRowIndexToModel(selectedRow);

        String id = model.getValueAt(row, 0).toString();

        int answer = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete student " + id + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            service.deleteStudent(id);

            JOptionPane.showMessageDialog(
                this,
                "Student deleted successfully!"
            );

            loadStudents();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // =====================================
    // CLEAR REGISTRATION FIELDS
    // =====================================

    private void clearFields() {

        if (idField == null) {
            return;
        }

        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        programmeField.setText("");
        yearField.setText("");
        emailField.setText("");

        idField.requestFocusInWindow();

        if (statusLabel != null) {
            statusLabel.setText("Input fields cleared");
        }
    }

    // =====================================
    // ERROR HANDLING
    // =====================================

    private void showError(Exception ex) {

        JOptionPane.showMessageDialog(
            this,
            ex.getMessage(),
            "System Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    // =====================================
    // START APPLICATION
    // =====================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {

                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );

            } catch (Exception ignored) {
                // Use default appearance.
            }

            new StudentGUI().setVisible(true);
        });
    }
}
