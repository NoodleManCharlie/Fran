import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import javax.swing.SwingWorker;

public class Fran {

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

        //Creating the Mod list section
        JPanel modList = new JPanel();
        JPanel modList2 = new JPanel();
        modList2.setLayout(new GridLayout(mods.size() / 2, 3));
        JLabel list = new JLabel("List Of Mods");
        JLabel mod;
        modList.add(list);

        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            mod = new JLabel("- " + entry.getKey());
            modList2.add(mod);
        }

        //Creating buttons
        JPanel panel = new JPanel(); // the panel is not visible in output
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JButton configure = new JButton("Configure");
        JButton cancel = new JButton("Cancel");
        c.gridx = 0;
        c.gridy = 1;
        panel.add(configure, c);
        c.gridx = 3;
        c.gridy = 1;
        panel.add(cancel, c);

        //Adding the progress bar
        JPanel loading = new JPanel();
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setSize(100, 200);
        bar.setValue(0);
        bar.setStringPainted(true);
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(bar, c);
        bar.setVisible(false);

        //Putting it all together
        frame.getContentPane().add(BorderLayout.NORTH, modList);
        frame.getContentPane().add(BorderLayout.WEST, modList2);
        frame.getContentPane().add(BorderLayout.PAGE_END, panel);
        //frame.getContentPane().add(BorderLayout.CENTER, loading);
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
                    loading.setVisible(true);
                    configure(mods, bar, frame);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        
    }

    //getSize is a method to make the window customizable to the needs of the mod list
    public static int getSize(Map<String, String> mods)
    {
        int height = 125;
        for(int  i = 0; i <= mods.size() - 1; i++)
        {
            height += 15;
        }
        return height;
    }

    //configure method is for Configuring the folders and calling the right methods.
    public static void configure(Map<String, String> mods, JProgressBar bar, JFrame frame) throws Exception
    {
        clearing();

        //Download
        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            File file = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + entry.getKey() + ".jar");
            File file2 = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + entry.getKey() + ".jar");

            if(!file.exists() && !file2.exists())
            {
                //Download
                bar.setVisible(true);
                downloadFile(entry.getValue(), entry.getKey() + ".jar", bar);

                //Close Prompt
                JDialog closePrompt = new JDialog(frame, "Close Prompt");
                JLabel completeLabel = new JLabel("Configure Complete.");
                closePrompt.add(completeLabel);
                JButton close = new JButton("Close");
                //close.addActionListener(close);
                closePrompt.setSize(150, 150);
                closePrompt.setVisible(true);
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

    //throws Exception 

    //downloadFile is a method to download mods from a provided link
    public static void downloadFile(String urlString, String fileName, JProgressBar bar) 
    {
        //Swing Worker so Progress bar can update
        SwingWorker worker = new SwingWorker() 
        {
            @Override
            protected String doInBackground() throws Exception
            {

                //Setting up connection to url
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Getting information on file later used for progress bar
                int filesize = connection.getContentLength();
                float totalDataRead = 0;

                //Downloading
                try (java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream())) {                
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + fileName);
                    try (java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024)) {
                        byte[] data = new byte[1024];
                        int i;
                        while ((i = in.read(data, 0, 1024)) >= 0) 
                        {
                            totalDataRead = totalDataRead + i;
                            bout.write(data, 0, i);
                            float Percent = (totalDataRead * 100) / filesize;
                            bar.setValue((int) Percent);
                        }
                    }
                }
                Files.move(Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/" + fileName), Paths.get(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/" + fileName));

                String res = "Finished Execution";
                return res;  
            }

        };     

        worker.execute();

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