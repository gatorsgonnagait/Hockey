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
public class Menu extends JPanel implements Runnable{

    Controller controller;
    public ArrayList<Controller> foundControllers = new ArrayList<>();
    Color color;

    int height;
    int width;
    int horizontalMiddle;
    int verticalCenter;
    int circleDiameter;
    int verticalCenterPos;
    int verticalLeftPos;
    int verticalRightPos;
    int horizontalThirds;
    int horizontalTwoThirds;
    int horizontalFifths;
    int horizontalTwoFifths;
    int horizontalThreeFifths;
    int horizontalFourFifths;

    int xLocation1;
    int yLocation1;
    int xLocation2;
    int yLocation2;
    int xLocation3;
    int yLocation3;
    int xLocation4;
    int yLocation4;


    int xAxisPercentage = 0;
    int yAxisPercentage = 0;
    String buttonIndex = "";
    int buttonInputLimitFrames = 0;
    int stickInputLimitFrames = 0;


    Image colorNum;
    Image num1 = Toolkit.getDefaultToolkit().getImage("img/one.png");
    Image num2 = Toolkit.getDefaultToolkit().getImage("img/two.png");
    Image num3 = Toolkit.getDefaultToolkit().getImage("img/three.png");
    Image num4 = Toolkit.getDefaultToolkit().getImage("img/four.png");
    Image rednum1 = Toolkit.getDefaultToolkit().getImage("img/redone.png");
    Image bluenum2 = Toolkit.getDefaultToolkit().getImage("img/bluetwo.png");



    Controller[] controllerArray = new Controller[4];
    Thread t;

    boolean paintRedButton = false;

    public Menu(int width, int height){
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setVisible(true);
        horizontalMiddle = height/2;
        verticalCenter = width/2;
        circleDiameter = height/10;
        verticalCenterPos = verticalCenter - circleDiameter/2;
        verticalLeftPos = width/4 - circleDiameter/2;
        verticalRightPos = width/4*3 - circleDiameter/2;
        horizontalThirds =  height/3 - circleDiameter/2;
        horizontalTwoThirds = height/3*2 - circleDiameter/2;
        horizontalFifths = height/5 - circleDiameter/2;
        horizontalTwoFifths = height/5 * 2 - circleDiameter/2;
        horizontalThreeFifths = height/5 * 3 - circleDiameter/2;
        horizontalFourFifths = height/5 * 4 - circleDiameter/2;

        xLocation1 = verticalCenterPos;
        yLocation1 = horizontalFifths;
        xLocation2 = verticalCenterPos;
        yLocation2 = horizontalTwoFifths;
        xLocation3 = verticalCenterPos;
        yLocation3 = horizontalThreeFifths;
        xLocation4 = verticalCenterPos;
        yLocation4 = horizontalFourFifths;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GRAY);
        g2d.setColor(color);

        g2d.fillOval(verticalCenterPos, horizontalFifths, circleDiameter, circleDiameter);
        g2d.fillOval(verticalCenterPos, horizontalTwoFifths, circleDiameter, circleDiameter);
        g2d.fillOval(verticalCenterPos, horizontalThreeFifths, circleDiameter, circleDiameter);
        g2d.fillOval(verticalCenterPos, horizontalFourFifths, circleDiameter, circleDiameter);

        g2d.fillOval(verticalLeftPos, horizontalThirds, circleDiameter, circleDiameter);
        g2d.fillOval(verticalLeftPos, horizontalTwoThirds, circleDiameter, circleDiameter);
        g2d.fillOval(verticalRightPos, horizontalThirds, circleDiameter, circleDiameter);
        g2d.fillOval(verticalRightPos, horizontalTwoThirds, circleDiameter, circleDiameter);

        g2d.drawImage(num1, xLocation1, yLocation1, circleDiameter, circleDiameter, this);
        g2d.drawImage(num2, xLocation2, yLocation2, circleDiameter, circleDiameter, this );
        g2d.drawImage(num3, xLocation3, yLocation3, circleDiameter, circleDiameter, this);
        g2d.drawImage(num4, xLocation4, yLocation4, circleDiameter, circleDiameter, this);







        //g2d.fillOval(width/4 - circleDiameter/2, height/3 - circleDiameter/2, circleDiameter, circleDiameter);

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




    }

    public void assignPlayers(){
        int xNeutral = 49;
        int yNeutral = 49;


        for( Controller controller : foundControllers ){
            gamepad();
            //everything will happen here


            //System.out.println(xAxisPercentage);

            if(stickInputLimitFrames >10) {

                if (xAxisPercentage < xNeutral - 40) {
                    //move left

                    if (controllerArray[2] == controller) {
                        controllerArray[2] = null;
                        System.out.println("center 3");
                        xLocation1 = verticalCenterPos;
                        yLocation1 = horizontalFifths;
                    }
                    else if (controllerArray[3] == controller) {
                        controllerArray[3] = null;
                        System.out.println("center 4");
                        xLocation1 = verticalCenterPos;
                        yLocation1 = horizontalFifths;
                    }
                    else {

                        if (controllerArray[0] == null) {
                            controllerArray[0] = controller;
                            System.out.println("left 1");
                            xLocation1 = verticalLeftPos;
                            yLocation1 = horizontalThirds;

                        } else if (controllerArray[0] == controller) {
                            System.out.println("already in left top spot");
                            xLocation1 = verticalLeftPos;
                            yLocation1 = horizontalThirds;
                        }

                        else if (controllerArray[1] == null) {
                            controllerArray[1] = controller;
                            System.out.println("left 2");

                        }
                        else if (controllerArray[1] == controller) {
                            System.out.println("already in left bottom spot");
                        }
                    }

                }
                else if (xAxisPercentage > xNeutral + 40) {


                    if (controllerArray[0] == controller) {
                        controllerArray[0] = null;
                        System.out.println("center 1");
                        xLocation1 = verticalCenterPos;
                        yLocation1 = horizontalFifths;
                    } else if (controllerArray[1] == controller) {
                        controllerArray[1] = null;
                        System.out.println("center 2");
                    }
                    else {

                        if (controllerArray[2] == null) {
                            controllerArray[2] = controller;
                            System.out.println("right 1");
                            xLocation1 = verticalRightPos;
                            yLocation1 = horizontalThirds;
                        }
                        else if (controllerArray[2] == controller) {
                            System.out.println("already in right top spot");
                            xLocation1 = verticalRightPos;
                            yLocation1 = horizontalThirds;
                        }

                        else if (controllerArray[3] == null) {
                            controllerArray[3] = controller;
                            System.out.println("right 2");
                        } else if (controllerArray[3] == controller) {
                            System.out.println("already in right bottom spot");
                        }
                    }
                }

                stickInputLimitFrames = 0;


            }
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
                //System.out.println(foundControllers.get(0));
                System.out.println(controller.getPortNumber());
                //System.out.println(foundControllers.get(1));
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
        stickInputLimitFrames++;

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

    public void stickActions(){

    }

    public void pressZeroButton(){

    }

    @Override
    public void addNotify() {
        super.addNotify();
        t = new Thread(this);
        t.start();
    }


    @Override
    public void run() {
        System.out.println("run");
        searchForControllers();

        while(true) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assignPlayers();
            repaint();
        }
    }
}
