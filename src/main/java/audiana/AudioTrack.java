package audiana;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;

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
        System.out.println(name);
        System.out.println("---------------------------------------------------- ");
        System.out.println("Duration:    " + header.getTrackLength() + " seconds");
        System.out.println("File Size:   " + audioFile.getFile().length() + " bytes");
        System.out.println("Bitrate:     " + header.getBitRate());
        System.out.println("Sample Rate: " + header.getSampleRateAsNumber() + " Hz");
        System.out.println("Channels:    " + header.getChannels());
    }
}