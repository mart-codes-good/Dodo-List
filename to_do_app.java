/**
 * JMJ
 * @author Martin Tejada
 * @version 1.0
 * @since 2025-03-10
 * 
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.awt.*;
import javax.swing.*;

public class to_do_app{

    private ImageIcon dodo = new ImageIcon("dodolist.png"); // Image of dodo bird (pixel art)
    private final String task_file = "tasks.text";
    private DefaultListModel<String> list = new DefaultListModel<>(); 
    // default list model is an optimized list for jlist, defined here since it is used in multiple methods
        public static void main(String[] args) {

        // Call the UI function to set up the application
        new to_do_app().UI();
    }
    
    public void UI(){ // This is the main User Interface for the to-do list

        JFrame frame = new JFrame("Dodo List"); // Creating the main frame (window) for the to-do list.
        frame.setIconImage(dodo.getImage());
        
        JList<String> todo = new JList<>(list);      // Creating a list to hold tasks
        JScrollPane scroll = new JScrollPane(todo);  // Adding it to a JScrollPane to enable scrolling when there are too many tasks

        todo.setBackground(Color.decode("#fce1f7")); 
        scroll.setBackground(Color.decode("#c6fbff")); //Set background color for the scroll pane
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // adding custom padding

        JPanel butt_panel = new JPanel(); // panel to hold both add and delete task buttons
        butt_panel.setBackground(Color.decode("#c6fbff"));
        
        JButton add = new JButton("Add task"); // buttons to add and delete tasks
        JButton del = new JButton("Remove task");
        butt_panel.add(add); butt_panel.add(del); // adding to panel

        add.addActionListener(e -> {add_task();}); // call function add_task when add button is pressed
        del.addActionListener(e -> {del_task();}); // call function del_task when del button is pressed

        frame.setLayout(new BorderLayout());      // Setting the layout for the main frame and adding components to the center and bottom
        frame.add(scroll, BorderLayout.CENTER);
        frame.add(butt_panel, BorderLayout.SOUTH);

        frame.getContentPane();                     
        frame.setSize(400,600);        // set size of frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close application when close frame
        frame.setLocationRelativeTo(null);         // Centering the frame on the screen
        frame.setVisible(true);                     // Make the frame visible to the user

        loadTasks();
    }

    public void add_task(){ // Creates a pop-up message asking to add a task or cancel in case of a misclick

        JDialog add = new JDialog();     // This creates a JDialog message (which is like a pop up) named add
        JLabel ques = new JLabel("Add a task"); // A label holding instrcutions
        JTextField user = new JTextField(); // Text field so you can write out the task you want to add
        
        add.setIconImage(dodo.getImage());

        JPanel butts = new JPanel(); // buttons with panel
        JButton ok = new JButton("Add Task"); // Add task and cancel buttons
        JButton no = new JButton("Cancel");
        butts.add(ok); butts.add(no);

        ok.addActionListener(e -> {         // if add task button is pressed
            String task = user.getText();   // get what is written in the text field
            list.addElement(task);          // add it to the list of tasks
            saveTasks();
            add.dispose();                  // close pop up (the JDialog we made) message
        });

        no.addActionListener(e -> {
            add.dispose();                  // Close JDialog message
        });

        
        butts.setBackground(Color.decode("#c6fbff"));
        add.getContentPane().setBackground(Color.decode("#c6fbff"));

        add.setLayout(new GridLayout(3,1)); // Layout for the pop up message, instructing how the components are organized
        add.add(ques); add.add(user); add.add(butts);
        add.setSize(200,200);
        add.setLocationRelativeTo(null);
        add.setVisible(true);

    }
    public void del_task(){ // Similar like add_task but instead you click on what task you want to delete

        JDialog del = new JDialog();        
        JLabel ques = new JLabel("Delete a task");

        del.setIconImage(dodo.getImage());

        JList<String> tasks = new JList<>(list);
        JScrollPane scroll = new JScrollPane(tasks);

        JPanel butts = new JPanel(); // buttons with panel
        JButton ok = new JButton("Remove");
        JButton no = new JButton("Cancel");
        butts.add(ok); butts.add(no);

        ok.addActionListener(e -> {
            int selectedIndex = tasks.getSelectedIndex(); // This sets the integer "selectedIndex" to what task was clicked on
            // -1 means a task had not been selected. If none is selected, the user can press Cancel instead
            if(selectedIndex != -1){
                list.remove(selectedIndex); // remove the task from the task list
                tasks.setModel(list);       // adjusting the list so it shows the updated list
                saveTasks();
            }
        });

        no.addActionListener(e -> {
            del.dispose();
        });

        butts.setBackground(Color.decode("#c6fbff"));
        del.getContentPane().setBackground(Color.decode("#c6fbff"));

        del.setLayout(new GridLayout(3,1));
        del.add(ques); del.add(scroll); del.add(butts);
        del.setSize(200,200);
        del.setLocationRelativeTo(null);
        del.setVisible(true);

    }

    private void saveTasks() {
        try {
            List<String> taskList = Arrays.asList(listToArray());
            Files.write(Paths.get(task_file), taskList, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try {
            List<String> tasks = Files.readAllLines(Paths.get(task_file));
            tasks.forEach(list::addElement);
        } catch (IOException e) {
            System.out.println("No previous tasks found.");
        }
    }

    private String[] listToArray() { // Required to convert from DefualtListModel to ArrayList since load and save operations don't work with Default list model
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }


}
// JMJ