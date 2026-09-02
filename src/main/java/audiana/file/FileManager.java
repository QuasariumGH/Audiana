package  audiana.file;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import audiana.audio.AudioTrack;
import audiana.config.ConfigManager;

public class FileManager {

    public static AudioFile parseAudioFile (File file) 
    {
        AudioFile audioFile = null;
        try { audioFile = AudioFileIO.read(file);}
        catch (IOException | CannotReadException | InvalidAudioFrameException | ReadOnlyFileException | TagException e) {}
        return audioFile;

    }

    public static int lengthOf(File[] itemArray) {

        int arrayLength = 0;
        for (File arrayItem : itemArray) {
            if (arrayItem.isDirectory()) {
                arrayLength++;
            } else if (arrayItem.isFile() && FileManager.parseAudioFile(arrayItem) != null) {
                arrayLength++;
            }
        }

        return arrayLength;

    }

     public static void setupDirectory(Scanner scan) {

        boolean pathIsValid;

        do {

            String path;
            pathIsValid = false;
            if (ConfigManager.getMusicFolderPath() == null) { System.out.print("Enter your Music Folder Path: "); }
            else { System.out.print("Enter a new Music Folder Path: "); }

            path = scan.nextLine();
            File pathObject = new File(path);

            if(pathObject.exists()) {
                if(pathObject.isDirectory()) {
                    path = pathObject.getAbsolutePath();
                    ConfigManager.setSavePath(path);
                    ConfigManager.setMusicFolderPath(path);
                    ConfigManager.save();
                    System.out.println("Path Succcessfully Added! ");
                    pathIsValid = true;
                }
                else { System.err.println("ERROR: Path must be a Folder Directory! "); }
            }
            else { System.err.println("ERROR: Path does not exist! " ); }

        } while (!pathIsValid);
    }

    public static void browseFiles( Scanner scan) {

        boolean keepBrowsing;
        int displayIndex, selection = 0, i;
        File[] directoryFiles, displayFiles;
        File currentFolder, rootFolder;
        String input;

        rootFolder = new File (ConfigManager.getMusicFolderPath());

        do {

            keepBrowsing = false;
            System.out.println("""
             File Selection
            ----------------""");
            System.out.println("Current Path: " + ConfigManager.getSavePath() + "\n");

            currentFolder = new File (ConfigManager.getSavePath());
            if (currentFolder.listFiles() == null) {
                System.err.println("ERROR: Path Does Not Exists, please update your music folder path!");
                return;
            }
            directoryFiles = currentFolder.listFiles();

            displayIndex = 0;
            displayFiles = new File[lengthOf(directoryFiles)];

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
                            ConfigManager.setSavePath(currentFolder.getParent());
                            keepBrowsing = true;
                            ConfigManager.save();
                        }
                        else { break; }
                    }

                    else {
                        try { selection = Integer.parseInt(input);}
                        catch (NumberFormatException e) {
                            System.err.println("ERROR: Selection must be a number! ");
                            selection = 0;
                        }
                        if (selection < 1 || selection > displayFiles.length) { System.err.println("ERROR: Selection out of bounds! "); }
                        else if (displayFiles[selection - 1].isDirectory()) {
                            ConfigManager.setSavePath(displayFiles[selection - 1].getPath());
                            keepBrowsing = true;
                            ConfigManager.save();
                        }
                    }
                } while ((selection < 1 || selection > displayFiles.length) && !keepBrowsing);
            }

            if (selection > 0 && displayFiles[selection - 1].isFile()) { // File is Selected //
                
                do {
                    System.out.print("Selected File: " + displayFiles[selection - 1].getName() + "\n" + "Z - Analysis Mode, B - Back: ");
                    input = scan.nextLine();
                    /* if("Q".equalsIgnoreCase(input)) {
                        System.out.print("Playback not implemented yet!");
                    } */
                    if ("Z".equalsIgnoreCase(input)) {
                        try {
                            AudioTrack selectedTrack = new AudioTrack( AudioFileIO.read(displayFiles[selection - 1]));
                            selectedTrack.DisplayInfo();
                        } catch (IOException | CannotReadException | InvalidAudioFrameException | ReadOnlyFileException | TagException e) {}
                    }
                    else if ("B".equalsIgnoreCase(input)) {
                        keepBrowsing = true;
                        break;
                    }
                    else { System.err.println("ERROR: Invalid Selection! "); }
                } while (!"Q".equalsIgnoreCase(input) && !"Z".equalsIgnoreCase(input));

            }

        } while(keepBrowsing);
    }
}
