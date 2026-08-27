package audiana;
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

        System.out.print("Enter The File Path (mp3, wav, ogg): ");
        String input = scanner.nextLine();

        System.out.println("Loading file from " + input + "...");
    }
}
