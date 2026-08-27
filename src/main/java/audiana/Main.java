package audiana;
import java.io.File;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        String versionNumber = "v0.1.0";
        String audianaLogo = """
            _              _ _
           / §   _   _  __| (_) __ _ _ __   __ _
          / _ § | | | |/ _` | |/ _` | '_ § / _` |
         / ___ §| |_| | (_| | | (_| | | | | (_| |
        /_/   §_§§__,_|§__,_|_|§__,_|_| |_|§__,_| """.replace("§", "\\");

        System.out.println(audianaLogo + " " + versionNumber + "\n");
        Scanner scanner = new Scanner(System.in);
        File file;

        do { 
            System.out.print("Enter The File Path (mp3, wav, ogg): ");
            String path = scanner.nextLine();
            file = new File(path);

            System.out.println("Loading file from " + path + "...");

            if(file.exists() && file.isFile()) { 
                System.out.println("File Loaded! "); 
            }
            else { 
                System.out.println("ERROR: File at PATH is not compatible or doesnt exist "); 
            }
        } while (!file.exists() || !file.isFile());
    }
}

//java -cp target/classes audiana.Main