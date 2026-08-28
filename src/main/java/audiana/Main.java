package audiana;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

/** Contains the script for navigating through Audiana.
 * 
 * Written By Dillon Michael Jones
 * Project Started on Wed, Aug 26, 2026, 12:02 PM
 * Affiliated with Columbus State Univeristy - CPSC 2108 
 * 
 * Note to Janace Canedo: I have decided to turn this into a personal project once I complete and submit version 1, which will be 
 * simple enough to satisfy the objectives of the assignment. Feel free to provide feedback, as I plan to port this to a TUI 
 * layer using Lanterna. Thank You!
 * 
 * How to Run:
 * -----------
 * cd ~/Audiana/
 * //mvn clean compile 
 * //mvn exec:java -Dexec.mainClass="audiana.Main"
 * 
 */

public class Main {

    static String musicFolderPath = "";
    static String currentPath = "";

    static Properties settings = new Properties();

    public static void saveConfig() {

        settings.setProperty("MusicFolderPath", musicFolderPath);
        settings.setProperty("SavedPath", currentPath);

        try (FileOutputStream propOut = new FileOutputStream("config.properties")) {
            settings.store(propOut, "Audiana Settings");
        }
        catch (IOException e) { System.out.println("ERROR: Could not save Config!"); }
    }

    public static void loadConfig() {

        try (FileInputStream propIn = new FileInputStream("config.properties")) {
            settings.load(propIn);

            musicFolderPath = settings.getProperty("MusicFolderPath", "");
            currentPath = settings.getProperty("SavedPath", "");
        }
        catch (IOException e) { System.out.println("ERROR: Config does not exist!"); }
    }

    public static AudioFile parseAudioFile (File file) {
        AudioFile audioFile = null;
        try { audioFile = AudioFileIO.read(file);}
        catch (Exception e) {}
        return audioFile;
    }

    public static int itemsToDisplay(File[] fileArray) {

        int arrayLength = 0;
        for( int i = 0; i < fileArray.length; i++ ) {  
            if(fileArray[i].isDirectory()) { arrayLength++; }
            else if(fileArray[i].isFile() && parseAudioFile(fileArray[i]) != null) {  arrayLength++; }
        }
        return arrayLength;

    }

    public static void setupDirectory(Scanner scan) {

        boolean pathIsValid;

        do {
            if (musicFolderPath.isEmpty()) { System.out.print("Enter your Music Folder Path: "); }
            else { System.out.print("Enter a new Music Folder Path: "); }
            musicFolderPath = scan.nextLine();
            File folder = new File(musicFolderPath);
            
            if(folder.isDirectory()) {
                musicFolderPath = folder.getAbsolutePath(); 
                currentPath = musicFolderPath;

                System.out.println("Path Succcessfully Added! ");
                pathIsValid = true; 
            }
            else {
                System.out.println("ERROR: Cannot chose a file as your Folder Path! ");
                pathIsValid = false;
            }
        } while (!pathIsValid);

    }

    public static void browseFiles( Scanner scan) { 
        
        boolean folderIsSelected;
        File[] directoryFiles;
        File[] displayFiles;
        File currentFolder;
        File rootFolder;
        String input;

        int displayIndex;
        int selection;
        int i;
        
        selection = 0;
        rootFolder = new File (musicFolderPath);

        do {
            folderIsSelected = false;
            if(currentPath.isEmpty()) { currentPath = musicFolderPath; }
            System.out.println("\n" + " File Selection " + "\n" + "----------------");
            
            currentFolder = new File (currentPath);
            directoryFiles = currentFolder.listFiles();

            if (directoryFiles == null) {
                System.out.println("ERROR: Path No Longer Exists, please change your music folder path!");
                return;
            }
            
            System.out.println("Current Path: " + currentPath + "\n");

            displayIndex = 0;
            displayFiles = new File[itemsToDisplay(directoryFiles)];

            for( i = 0; i < directoryFiles.length; i++ ) { // Displays audio files and folders, then stores them in Display Files
                if(directoryFiles[i].isDirectory()) {
                    displayFiles[displayIndex] = directoryFiles[i]; 
                    System.out.println((displayIndex + 1) + ": " + displayFiles[i].getName());
                    displayIndex++;
                }
                else if(directoryFiles[i].isFile()) { 

                    AudioFile audioFileInDirectory = parseAudioFile(directoryFiles[i]);

                    if(audioFileInDirectory != null) {
                        displayFiles[displayIndex] = audioFileInDirectory.getFile();
                        System.out.println((displayIndex + 1) + ": " + displayFiles[displayIndex].getName());
                        displayIndex++;
                    }
                }
            }

            if(displayFiles.length == 0) { System.out.println("No Audio Files Found!"); } 
            else {
                do {

                    selection = 0;
                    System.out.print("\n" + "( 1 - " + (displayFiles.length) + " ) Select, B - Back : " );
                    input = scan.nextLine();

                    if("B".equalsIgnoreCase(input)) { // B will take the current path up a folder, only if its not in the setup Directory //
                        if (!currentFolder.equals(rootFolder)) { 
                            currentPath = currentFolder.getParent();
                            folderIsSelected = true;
                        } 
                        else { break; }
                    }
                    
                    else {
                        try { selection = Integer.parseInt(input);}       
                        catch (NumberFormatException e) {
                            System.out.println("ERROR: Selection must be a number! ");
                            selection = 0;
                        }   
                        if (selection < 1 || selection > displayFiles.length) { System.out.println("ERROR: Selection out of bounds! "); } 
                        else if (displayFiles[selection - 1].isDirectory()) { 
                            currentPath = displayFiles[selection - 1].getPath();
                            folderIsSelected = true;
                        }
                    }
                } while ((selection < 1 || selection > displayFiles.length) && !folderIsSelected); 
            } 
        } while(folderIsSelected); 
             
        if (selection > 0 && displayFiles[selection - 1].isFile()) { // File is Selected //
            do { 
                System.out.print("Selected File: " + displayFiles[selection - 1].getName() + "\n" + "Q - Playback Mode, Z - Analysis Mode: "); 
                input = scan.nextLine(); 
                if("Q".equalsIgnoreCase(input)) { System.out.print("Playback not implemented yet!"); }
                else if ("Z".equalsIgnoreCase(input)) { 
                    try {
                        AudioTrack selectedTrack = new AudioTrack( AudioFileIO.read(displayFiles[selection - 1])); 
                        selectedTrack.DisplayInfo();
                    } catch (Exception e) {}
                }
                else { 
                    System.out.println("ERROR: Selection must either be Q or Z! "); 
                }
            } while (!"Q".equalsIgnoreCase(input) && !"Z".equalsIgnoreCase(input));
        }
    }

    public static void main(String[] args) {
        
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
        String versionNumber = "v0.9.0"; // Current Version //
        Scanner scanner = new Scanner(System.in);
        boolean audianaIsRunning = true;
        loadConfig();
        // ASCII Art Generated by ChatGPT //
        String audianaLogo = """ 
            _              _ _
           / §   _   _  __| (_) __ _ _ __   __ _
          / _ § | | | |/ _` | |/ _` | '_ § / _` |
         / ___ §| |_| | (_| | | (_| | | | | (_| |
        /_/   §_§§__,_|§__,_|_|§__,_|_| |_|§__,_| """.replace("§", "\\"); 

        System.out.println("\n" + audianaLogo + " " + versionNumber);

        do {

            System.out.print("\n" +  "D - Directory Setup, F - File Selection,  E - Exit: ");
            String menuSelection = scanner.nextLine();
            switch(menuSelection.toUpperCase()) {
            
                case "D": setupDirectory(scanner); saveConfig(); break;// D - Directory Setup //
        
                case "F": browseFiles(scanner); break; // F - File Selection //
                
                case "E": saveConfig(); audianaIsRunning = false; break; // Exit - Will Terminate Application //

                default: System.out.print("ERROR: Invalid Selection! "); break; // Any Other Input //
            }

        } while (audianaIsRunning);
    }
}


