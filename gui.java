import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class gui {

    public static int getSize(String[] mods)
    {
        int height = 100;
        for(int  i = 0; i <= mods.length - 1; i++)
        {
            height += 15;
        }
        return height;
    }
    public static void main(String args[]){
        //Creating the Frame
        JFrame frame = new JFrame("Fran Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] mods = {"beans", "ben", "flying", "beans", "ben", "flying", "beans", "ben", "flying", "beans", "ben", "flying", "flying"};

        frame.setSize(250, getSize(mods));

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
        modList2.setLayout(new GridLayout(mods.length / 3, 3));
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
        frame.getContentPane().add(BorderLayout.CENTER, modList2);
        frame.setVisible(true);

        

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Downloading Mods");
            }
        });
    }
}