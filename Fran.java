import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.http.WebSocket.Listener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;

public class Fran {

    public static Map<String, String> mods = new LinkedHashMap<String, String>();
    public static void main(String args[]) {

        //Creating a home that the system can hold mods that the user has previously installed 
        //without deleting them so the mods do not have to be downloaded again in the future
        new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/").mkdir();
        new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/").mkdir();

        //Creating HashMap of the mods and their links
        QuickAcess.putting(mods);


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
        for(int  i = 0; i <= Fran.mods.size() - 1; i++)
        {
            height += 15;
        }
        return height;
    }

    //configure method is for Configuring the folders and calling the right methods.
    public static void configure(Map<String, String> mods, JProgressBar bar, JFrame frame) throws Exception
    {
        //Start the progress displaying
        bar.setVisible(true);

        //Clearing the folder
        clearing(bar);

        //Downloading Fabric
        downloadFabric(bar);

        //Download
        for (Map.Entry<String, String> entry : mods.entrySet())
        {
            File file = new File(QuickAcess.modFolder + "/" + entry.getKey() + ".jar");
            File file2 = new File(QuickAcess.franFolder + "/" + entry.getKey() + ".jar");

            if(!file.exists() && !file2.exists())
            {
                //Download
                downloadFile(entry.getValue(), entry.getKey() + ".jar", bar, frame);

            }
            if(file.exists() || file2.exists())
            {
                preDownloaded(mods, bar, frame);
            }
        }
            
    }

    public static void preDownloaded(Map<String, String> mods, JProgressBar bar, JFrame frame) throws IOException
    {
        //Swing Worker so Progress bar can update
        SwingWorker worker = new SwingWorker() 
        {
            @Override
            protected String doInBackground() throws Exception
            {
                for (Map.Entry<String, String> entry : mods.entrySet())
                {
                    float totalFilesMoved = 66;
                    int totalFiles = mods.size();

                    //Moving the mod
                    Files.move(Paths.get(QuickAcess.franFolder + "/" + entry.getKey() + ".jar"), Paths.get(QuickAcess.modFolder + "/" + entry.getKey() + ".jar"));

                    //Printing all the mods and moving the mods out of the folder
                    //the ensure it is just the mods contained in the modpack
                    for(int i = 0; i <= totalFiles; i++)
                    {
                        totalFilesMoved = totalFilesMoved + 1;
                        float Percent = (totalFilesMoved * 100) / totalFiles;
                        bar.setValue((int) Percent);
                    }
                }
                String res = "Finished Execution";
                return res; 
            }

            @Override
            protected void done() 
            {
                        //Close Prompt
                        JDialog closePrompt = new JDialog(frame, "Close Prompt");
                        JLabel completeLabel = new JLabel("Configure Complete.");
                        closePrompt.getContentPane().add(BorderLayout.CENTER, completeLabel);
                        JButton close = new JButton("Close");
                        close.addActionListener(new ActionListener()
                        {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                        closePrompt.getContentPane().add(BorderLayout.SOUTH, close);
                        closePrompt.setSize(175, 100);
                        closePrompt.setVisible(true);
            }
            
        };

        worker.execute();  
    }
    
    public static void downloadFabric(JProgressBar bar) throws IOException
    {
        //Swing Worker so Progress bar can update
        SwingWorker worker = new SwingWorker() 
        {
            @Override
            protected String doInBackground() throws Exception
            {
                URL url = new URL("https://maven.fabricmc.net/net/fabricmc/fabric-installer/0.11.2/fabric-installer-0.11.2.jar");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Getting information on file later used for progress bar
                int filesize = connection.getContentLength();
                float totalDataRead = 33;
                try (java.io.BufferedInputStream in = new java.io.BufferedInputStream(connection.getInputStream())) {                
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(QuickAcess.franFolder + "fabricInstaller.jar");
                    try (java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024)) {
                        byte[] data = new byte[1024];
                        int i;
                        while ((i = in.read(data, 0, 1024)) >= 0) 
                        {
                            totalDataRead = totalDataRead + i;
                            bout.write(data, 0, i);
                            float Percent = (totalDataRead * 66) / filesize;
                            bar.setValue((int) Percent);
                        }
                    }
                }

                //Creating Batch file to be used later.
                try 
                {
			        File file = new File(QuickAcess.franFolder + "fabric.bat");
			        java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
			        java.io.DataOutputStream dos = new java.io.DataOutputStream(fos);
                    dos.writeBytes("TITLE Downloading Fabric");
			        dos.writeBytes("\n");
			        dos.writeBytes("cd %APPDATA%/.minecraft/Fran");
			        dos.writeBytes("\n");   
			        dos.writeBytes("java -jar fabricInstaller.jar client -dir \"%APPDATA%/.minecraft\" -mcversion " + QuickAcess.minecraftVersion);
			        dos.close();
		        } 
                catch (Exception e) {
                    return "Failed: " + e.getMessage();
		        }

        //Failled Attempts to install Fabric
        /* 
        Process proc = null;
        try {
            String command = "cmd";       
            proc = Runtime.getRuntime().exec(new String[] { "**cmd** exe"//$NON-NLS-1$
            , "-c", command });//$NON-NLS-1$
            if (proc != null) {
                proc.waitFor();
            }
        } catch (Exception e) {
            //Handle
            return;
        }
        Process proc1 = null;
        try {
            String command = "cd %APPDATA%/.minecraft/Fran";      
            proc1 = Runtime.getRuntime().exec(new String[] { "**cmd** exe"//$NON-NLS-1$
            , "-c", command });//$NON-NLS-1$
            if (proc1 != null) {
                proc1.waitFor();
            }
        } catch (Exception e) {
            //Handle
            return;
        }
        Process proc2 = null;
        try {
            String command = "java -jar fabricInstaller.jar client -dir \"%APPDATA%/.minecraft\" -mcversion 1.19.1";       
            proc2 = Runtime.getRuntime().exec(new String[] { "**cmd** exe"//$NON-NLS-1$
            , "-c", command });//$NON-NLS-1$
            if (proc2 != null) {
                proc2.waitFor();
            }
        } catch (Exception e) {
            //Handle
            return;
        }

        String[] commands = {"cmd", "cd %APPDATA%/.minecraft/Fran", "java -jar fabricInstaller.jar client -dir \"%APPDATA%/.minecraft\" -mcversion 1.19.1"};
        Runtime.getRuntime().exec(commands);

        String[] commands = {"cd %APPDATA%/.minecraft/Fran", "java -jar fabricInstaller.jar client -dir \"%APPDATA%/.minecraft\" -mcversion 1.19.1"};
        Runtime.getRuntime().exec(commands);

        Runtime.getRuntime().exec("java -jar fabricInstaller.jar client -dir \"%APPDATA%/.minecraft\" -mcversion 1.19.1", null, new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/")); //new File("%APPDATA%/.minecraft/Fran/");

        Runtime.getRuntime().exec("java -jar fabric-installer-0.11.2.jar client -dir \"%AppData%/Roaming/.minecraft/\" -mcversion 1.19.1");
        
        java -jar fabricInstaller.jar client -dir "%APPDATA%/.minecraft" -mcversion 1.19.1
        */

                //Runs Batch script that runs command that will download Fabric
                Runtime.getRuntime().exec("fabric.bat", null, new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/"));
            
                String res = "Success";
                return res;
            }
        };
    }

    //clears out all mods so only the mods in the modpack will be in the mods folder
    public static void clearing(JProgressBar bar) throws IOException
    {

        //Getting all the mods currently in the folder
        FilenameFilter filter = new FilenameFilter() 
        {
            @Override
            public boolean accept(File f, String name) 
            {
                return name.endsWith(".jar");

            }
        };

        String[] contains = QuickAcess.modFolder.list(filter);
        float totalFilesMoved = 0;
        int totalFiles = contains.length;

        //Printing all the mods and moving the mods out of the folder
        //the ensure it is just the mods contained in the modpack
        for(String path : contains)
        {
            System.out.println(path);
            Files.move(Paths.get(QuickAcess.modFolder + "/" + path), Paths.get(QuickAcess.franFolder + "/" + path));

            totalFilesMoved = totalFilesMoved + 1;
            float Percent = (totalFilesMoved * 33) / totalFiles;
            bar.setValue((int) Percent);
        }

    }

    //throws Exception 

    //downloadFile is a method to download mods from a provided link
    public static void downloadFile(String urlString, String fileName, JProgressBar bar, JFrame frame) 
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
                float totalDataRead = 66;

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
                Files.move(Paths.get(QuickAcess.franFolder + "/" + fileName), Paths.get(QuickAcess.modFolder + "/" + fileName));

                String res = "Finished Execution";
                return res;  
            }

            @Override
            protected void done() 
            {
                //Close Prompt
                JDialog closePrompt = new JDialog(frame, "Close Prompt");
                JLabel completeLabel = new JLabel("Configure Complete.");
                closePrompt.getContentPane().add(BorderLayout.CENTER, completeLabel);
                JButton close = new JButton("Close");
                close.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                closePrompt.getContentPane().add(BorderLayout.SOUTH, close);
                closePrompt.setSize(150, 150);
                closePrompt.setVisible(true);
            }
        };     

        worker.execute();   
    }

}