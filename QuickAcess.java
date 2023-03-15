import java.io.File;
import java.util.Map;

public class QuickAcess {
    public static String minecraftVersion = "1.19.2";
    public static File franFolder = new File(System.getProperty("user.home") + "/AppData/Roaming/.minecraft/Fran/");
    public static File modFolder = new File (System.getProperty("user.home") + "/AppData/Roaming/.minecraft/mods/");


    //putting method justs adds the mods to the list containing all of the information needed to download each mod
    public static void putting(Map<String, String> mods)
    {
        Fran.mods.put("JEI_" + QuickAcess.minecraftVersion, "https://mediafilez.forgecdn.net/files/4239/205/jei-1.19.2-fabric-11.5.0.297.jar");
        //Fran.mods.put("Enchancements", "1298");
        //Fran.mods.put("Origins", "1877");
        //Fran.mods.put("Fabric API", "2001");
        for(Map.Entry<String,String>it:mods.entrySet())
            System.out.print(it.getKey()+", ");
    }
}