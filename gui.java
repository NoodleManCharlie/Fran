import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class gui {

    public static void main(String args[]) {

        //Creating a home that the system can hold mods that the user has previously installed 
        //without deleting them so the mods do not have to be downloaded again in the future
        new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/").mkdir();

        //Creating HashMap of the mods and their links
        Map<String, String> mods = new LinkedHashMap<String, String>();
        putting(mods);

        //Creating the Frame
        JFrame frame = new JFrame("Fran Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //getSize is a method to make the window customizable to the needs of diplaying the mods
        frame.setSize(250, getSize(mods));

        //Creating buttons
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton configure = new JButton("Configure");
        JButton cancel = new JButton("Cancel");
        panel.add(configure);
        panel.add(cancel);

        //Creating the Mod list section
        JPanel modList = new JPanel();
        JPanel modList2 = new JPanel();
        modList2.setLayout(new GridLayout(mods.size() / 2, 3));
        JLabel list = new JLabel("List Of Mods");
        JLabel mod;
        modList.add(list);

        //Adding Components to the frame
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            mod = new JLabel("- " + entry.getKey());
            modList2.add(mod);
        }

        frame.getContentPane().add(BorderLayout.NORTH, modList);
        frame.getContentPane().add(BorderLayout.CENTER, modList2);
        frame.setVisible(true);

        //Adding Button interaction
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        configure.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    configure(mods);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    //getSize is a method to make the window customizable to the needs of diplaying the mods
    public static int getSize(Map<String, String> mods)
    {
        int height = 100;
        for(int  i = 0; i <= mods.size() - 1; i++)
        {
            height += 15;
        }
        return height;
    }

    //configure method is for Configuring the folders and calling the right methods.
    public static void configure(Map<String, String> mods) throws Exception
    {
        clearing();

        //Download
        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            File file = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar");
            File file2 = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + entry.getKey() + ".jar");

            if(!file.exists() && !file2.exists())
            {
                downloadFile(entry.getValue(), entry.getKey() + ".jar");
                Files.move(Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + entry.getKey() + ".jar"), Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar"));
            }
            else
            {
                Files.move(Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + entry.getKey() + ".jar"), Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar"));
            }

        }
    }

    //clears out all mods so only the mods in the modpack will be in the mods folder
    public static void clearing() throws IOException
    {
        File modLocation = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/");

        //Getting all the mods currently in the folder
        FilenameFilter filter = new FilenameFilter() 
        {
            @Override
            public boolean accept(File f, String name) 
            {
                return name.endsWith(".jar");

            }
        };

        String[] contains = modLocation.list(filter);

        //Printing all the mods and moving the mods out of the folder
        //the ensure it is just the mods contained in the modpack
        for(String path : contains)
        {
            System.out.println(path);
            Files.move(Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + path), Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + path));
        }
    }

    //downloadFile is a method to download mods from a provided link
    public static void downloadFile(String urlString, String fileName) throws Exception {
        URL url = new URL(urlString);

        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + fileName));
        }
    }

    //putting method is just to house all the mod links being added to the mods list
    public static void putting(Map<String, String> mods)
    {
        mods.put("JEI", "https://mediafilez.forgecdn.net/files/4239/205/jei-1.19.2-fabric-11.5.0.297.jar");
        //mods.put("Enchancements", "1298");
        //mods.put("Origins", "1877");
        //mods.put("Fabric API", "2001");
    }
}