import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

public class gui {

    public static void main(String args[]){

        Map<String, String> mods = new LinkedHashMap<String,String>();
        putting(mods);

        //Creating the Frame
        JFrame frame = new JFrame("Fran Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //String[] mods = {"beans", "ben", "flying", "beans", "ben", "flying", "beans", "ben", "flying", "beans", "ben", "flying", "flying"};

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
        modList2.setLayout(new GridLayout(mods.size() / 2, 3));
        JLabel list = new JLabel("List Of Mods");
        JLabel mod;
        modList.add(list);

        //Adding Components to the frame.s
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        /*for (int i = 0; i <= mods.size() - 1; i++)  
        {
            mod = new JLabel((String) mods.get(i));
            modList2.add(mod);
            //frame.getContentPane().add(BorderLayout.CENTER, mod);
        }*/
        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            mod = new JLabel("- " + entry.getKey());
            modList2.add(mod);
        }

        frame.getContentPane().add(BorderLayout.NORTH, modList);
        frame.getContentPane().add(BorderLayout.CENTER, modList2);
        frame.setVisible(true);

        //String[] urlsAlways = {"https://mediafilez.forgecdn.net/files/4239/205/jei-1.19.2-fabric-11.5.0.297.jar", };
        //String[] customUrls = {""};

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        download.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    download(mods);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void putting(Map<String, String> mods)
    {
        mods.put("JEI", "https://mediafilez.forgecdn.net/files/4239/205/jei-1.19.2-fabric-11.5.0.297.jar");
        //mods.put("Enchancements", "1298");
        //mods.put("Origins", "1877");
        //mods.put("Fabric API", "2001");
    }

    public static int getSize(Map<String, String> mods)
    {
        int height = 100;
        for(int  i = 0; i <= mods.size() - 1; i++)
        {
            height += 15;
        }
        return height;
    }

    public static void download(Map<String, String> mods) throws Exception
    {
        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            File file = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar");
            File file2 = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/Fran/" + entry.getKey() + ".jar");

            if(!file.exists() && !file2.exists())
            {
                downloadFile(entry.getValue(), entry.getKey() + ".jar");
                Files.move(Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/Fran/" + entry.getKey() + ".jar"), Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar"));
            }
            else
            {
                Files.move(Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/Fran/" + entry.getKey() + ".jar"), Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar"));
            }

        }
    }

    public static void downloadFile(String urlString, String fileName) throws Exception {
        URL url = new URL(urlString);

        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/Fran/" + fileName));
        }
    }
}