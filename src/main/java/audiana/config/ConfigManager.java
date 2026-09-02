package audiana.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static String musicFolderPath = "";
    private static String savePath = "";
    private static Properties config; 

    public static String getMusicFolderPath () { return musicFolderPath; }
    public static String getSavePath () { return savePath; }

    public static void setSavePath (String path) { savePath = path; }
    public static void setMusicFolderPath (String path) { musicFolderPath = path; }

    public static void save() 
    {
        config = new Properties(); 
        config.setProperty("MusicFolderPath", musicFolderPath);
        config.setProperty("SavePath", savePath);
        try (FileOutputStream propOut = new FileOutputStream("config.properties")) { config.store(propOut, "Audiana config"); }
        catch (IOException e) { System.err.println("ERROR: Could not save Config!"); }

    }

    public static void load() {

        try (FileInputStream propIn = new FileInputStream("config.properties")) {
            config = new Properties();
            config.load(propIn);
            musicFolderPath = config.getProperty("MusicFolderPath", "");
            savePath = config.getProperty("SavePath", "");
        }
        catch (IOException e) { System.err.println("ERROR: Config Does Not Exist, please update your music folder path!"); }
    
    }
}