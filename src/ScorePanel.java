import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by alien on 7/13/16.
 */
public class ScorePanel extends JPanel implements Runnable{

    DecimalFormat dec = new DecimalFormat("00");
    JTextField minute = new JTextField(2);
    JTextField second = new JTextField(2);
    JTextField scoreField1 = new JTextField(2);
    JTextField scoreField2 = new JTextField(2);
    int score1 = 0;
    int score2 = 0;
    public static int fps = 0;
    Thread t;

    public ScorePanel(){
        int style = Font.BOLD;
        Font font = new Font ("Garamond", style , 16);
        setPreferredSize(new Dimension(1000,50));

        minute.setText("00");
        second.setText("00");
        minute.setFont(font);
        second.setFont(font);
        minute.setEditable(false);
        second.setEditable(false);
        minute.setBackground(Color.WHITE);
        second.setBackground(Color.WHITE);

        scoreField1.setText("00");
        scoreField2.setText("00");
        scoreField1.setFont(font);
        scoreField2.setFont(font);
        scoreField1.setEditable(false);
        scoreField2.setEditable(false);
        scoreField1.setBackground(Color.WHITE);
        scoreField2.setBackground(Color.WHITE);

        setLayout(new FlowLayout());
        add(scoreField1);
        add(scoreField2);
        add(new JLabel("              "));
        add(minute);
        add(second);
        setVisible(true);
    }

    public int getScore1() {
        return score1;
    }

    public void addScore1(int score) {
        score1+=score;
        this.scoreField1.setText(""+dec.format(score1));
    }

    public int getScore2() {
        return score2;
    }

    public void addScore2(int score) {
        score2+=score;
        this.scoreField2.setText(""+dec.format(score2));
    }

    public static int getFps() {
        return fps;
    }

    @Override
    public void addNotify() {
        super.addNotify();

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        int min = 0;
        int sec = 0;


        for(int i = 300; i >= 0; i--){
            sec = (i % 60);
            min = i / 60;
            this.minute.setText(""+dec.format(min));
            this.second.setText(""+dec.format(sec));
            //updateAll();
            repaint();

            fps = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateAll(){

    }
}
