import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URL;


//This was testing for the downloading code
public class Download {

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://mediafilez.forgecdn.net/files/4239/205/jei-1.19.2-fabric-11.5.0.297.jar");

        downloadFile(url, "mod.jar");
    }

    public static void downloadFile(URL url, String fileName) throws Exception {
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL("https://mediafilez.forgecdn.net/files/4239/205/jei-1.19.2-fabric-11.5.0.297.jar").openStream());
        FileOutputStream fileOS = new FileOutputStream("/Downloads/bean.jar")) {
          byte data[] = new byte[1024];
          int byteContent;
          while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
              fileOS.write(data, 0, byteContent);
          }
        }
    }
}
