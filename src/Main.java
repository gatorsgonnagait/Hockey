import javax.swing.*;

/**
 * Main class that creates UI and starts the simulator
 * @author Aditi Datta
 * @version 1
 */
public class Main {

    public static void main(String[] args) {

        JFrame menuFrame = new JFrame();
        Menu menu = new Menu(1366, 768);
        menuFrame.add(menu);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.pack();
        menuFrame.setVisible(true);


        //GameDriver gd = new GameDriver();
    }
}
