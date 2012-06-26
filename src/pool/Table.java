package pool;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Table extends JFrame {

    public static final int ONE_SEC = 1000;
    public static final int REFRESH_RATE = 20;
    //

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    //

    static void createAndShowGUI(){
    	new Table();
    }

    public Table() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setTitle("Pooll Ball");
        setResizable(false);
        setContentPane(new Cloth());
        setVisible(true);
    }
    //
}