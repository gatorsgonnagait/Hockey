import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Mesa on 10/20/2016.
 */
public class Menu extends JPanel{

    Controller controller;
    public ArrayList<Controller> foundControllers = new ArrayList<>();
    Color color;

    int height;
    int width;
    int horizontalMiddle;
    int verticalCenter;
    int circleDiameter;

    public Menu(int width, int height){
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setVisible(true);
        horizontalMiddle = height/2;
        verticalCenter = width/2;
        circleDiameter = height/10;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillOval(verticalCenter - circleDiameter/2, height/5 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.fillOval(verticalCenter - circleDiameter/2, height/5 * 2 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.fillOval(verticalCenter - circleDiameter/2, height/5 * 3 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.fillOval(verticalCenter - circleDiameter/2, height/5 * 4 - circleDiameter/2, circleDiameter, circleDiameter);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospace", Font.BOLD, 22));
        g2d.drawString("1", verticalCenter- 7, height/5+ 7);
        g2d.drawString("2", verticalCenter, height/5 * 2);
        g2d.drawString("3", verticalCenter, height/5 * 3);
        g2d.drawString("4", verticalCenter, height/5 * 4);

        g2d.setColor(Color.GRAY);

        g2d.fillOval(width/4, height/3, circleDiameter, circleDiameter);
        g2d.setColor(color);
    }

    private void searchForControllers() {
        //mouse pad is automatically allowed
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        System.out.println(controllers.length);
        for(int i = 0; i < controllers.length; i++){
            controller = controllers[i];

            if (controller.getType() == Controller.Type.GAMEPAD ) {
                // Add new controller to the list of all controllers.
                foundControllers.add(controller);
                System.out.println(foundControllers.get(0));
                System.out.println(foundControllers.get(0).getPortNumber());
                System.out.println("controller is plugged in ");

            }
        }
    }




}
