import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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

    int xAxisPercentage = 0;
    int yAxisPercentage = 0;
    String buttonIndex = "";
    int buttonInputLimitFrames = 0;

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
        Image num1 = Toolkit.getDefaultToolkit().getImage("img/one.png");
        Image num2 = Toolkit.getDefaultToolkit().getImage("img/two.png");
        Image num3 = Toolkit.getDefaultToolkit().getImage("img/three.png");
        Image num4 = Toolkit.getDefaultToolkit().getImage("img/four.png");
        g2d.drawImage(num1,verticalCenter - circleDiameter/2, height/5 - circleDiameter/2, circleDiameter, circleDiameter, this);
        g2d.drawImage(num2, verticalCenter - circleDiameter/2, height/5 * 2 - circleDiameter/2, circleDiameter, circleDiameter, this );
        g2d.drawImage(num3, verticalCenter - circleDiameter/2, height/5 * 3 - circleDiameter/2, circleDiameter, circleDiameter, this);
        g2d.drawImage(num4, verticalCenter - circleDiameter/2, height/5 * 4 - circleDiameter/2, circleDiameter, circleDiameter, this);
        /*
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
        */
        g2d.setColor(Color.GRAY);

        g2d.fillOval(width/4 - circleDiameter/2, height/3 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.fillOval(width/4 - circleDiameter/2, height/3*2 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.fillOval(width/4*3 - circleDiameter/2, height/3 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.fillOval(width/4*3 - circleDiameter/2, height/3*2 - circleDiameter/2, circleDiameter, circleDiameter);
        g2d.setColor(color);
    }

    public void assignPlayers(){
        for( Controller controller : foundControllers ){
            gamepad();
            //everything will happen here
        }
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

    public int getAxisValueInPercentage(float axisValue) {
        return (int)(((2 - (1 - axisValue)) * 100) / 2);
    }

    public void gamepad(){

        controller.poll();
        net.java.games.input.Component[] components = controller.getComponents();

        buttonInputLimitFrames++;

        for(int i=0; i < components.length; i++) {
            net.java.games.input.Component component = components[i];
            net.java.games.input.Component.Identifier componentIdentifier = component.getIdentifier();

            if (componentIdentifier.getName().matches("^[0-9]*$")) { // If the component identifier name contains only numbers, then this is a button.
                // Is button pressed?
                boolean isItPressed = true;
                if (component.getPollData() == 0.0f) {
                    isItPressed = false;
                }
                else{
                    buttonIndex = component.getIdentifier().toString();
                    buttonActions();

                }
                continue;
            }

            if (component.isAnalog()) {
                float axisValue = component.getPollData();
                //System.out.println(axisValue);
                int axisValueInPercentage = getAxisValueInPercentage(axisValue);


                // X axis
                if (componentIdentifier == net.java.games.input.Component.Identifier.Axis.X) {
                    xAxisPercentage = axisValueInPercentage;
                    continue; // Go to next component.
                }
                // Y axis
                if (componentIdentifier == net.java.games.input.Component.Identifier.Axis.Y) {
                    yAxisPercentage = axisValueInPercentage;
                    continue; // Go to next component.
                }

            }
            //if button index is not null, wait a half a second il next input
        }
    }


    public void buttonActions(){

        if(buttonInputLimitFrames >20) {

            if (buttonIndex.equals("0")) {
                pressZeroButton();

            } else if (buttonIndex.equals("1") || buttonIndex.equals("3")) {
                //pressOneButton();

            } else if (buttonIndex.equals("2")) {
                //pressTwoButton();
            }

            if (buttonIndex != "") {
                buttonInputLimitFrames = 0;
            }

        }
    }

    public void pressZeroButton(){

    }


}
