import javax.swing.*;
import java.awt.*;

public class gui {
    public static void main(String args[]){
        //Creating the Frame
        JFrame frame = new JFrame("Fran Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250, 300);

        //Creating the MenuBar and adding components
        /*JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);*/

        String[] mods = {"Beans", "Beas", "Bens", "eans"};


        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton download = new JButton("Download");
        JButton cancel = new JButton("Cancel");
        // Components Added using Flow Layout
        panel.add(download);
        panel.add(cancel);

        // Text Area at the Center
        JPanel modList = new JPanel();
        JPanel modList2 = new JPanel();
        JLabel list = new JLabel("List Of Mods");
        JLabel mod;
        modList.add(list);

        //Adding Components to the frame.s
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        for (int i = 0; i <= mods.length - 1; i++)  
        {
            mod = new JLabel(mods[i]);
            modList2.add(mod);
            //frame.getContentPane().add(BorderLayout.CENTER, mod);
        }
        frame.getContentPane().add(BorderLayout.NORTH, modList);
        frame.getContentPane().add(BorderLayout.AFTER_LINE_ENDS, modList2);
        frame.setVisible(true);
    }
}