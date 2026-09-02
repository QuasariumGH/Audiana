package audiana.audio;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;

/* Contains the AudioTrack object, used to retrieve and organize Audio File Information. */

public class AudioTrack {
    private AudioFile audioFile;
    private AudioHeader header;
    public String name;

    public AudioTrack(AudioFile audioFile) {
        this.audioFile = audioFile;
        this.header = audioFile.getAudioHeader();
        this.name = audioFile.getFile().getName();
    }

    public void DisplayInfo() {

        System.out.println("\n" + name);
        System.out.println("---------------------------------------------------- ");
        System.out.println("Duration:    " + (header.getTrackLength() / 60) + " Minutes, " + (header.getTrackLength() % 60) + " Seconds");
        System.out.println("File Size:   " + audioFile.getFile().length() + " bytes");
        System.out.println("Bitrate:     " + header.getBitRate());
        System.out.println("Sample Rate: " + header.getSampleRateAsNumber() + " Hz");
        System.out.println("Channels:    " + header.getChannels());
        
    }
}