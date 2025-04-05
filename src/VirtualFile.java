import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VirtualFile {


    private byte[] content;
    private int blockId;

    // this class exists to house the files as they would on the disk
    // I couldn't figure out a way to reset the files every time I ran the program, so I opted for this structure instead
    public VirtualFile(String filepath, int new_blockId) throws IOException {

        blockId = new_blockId;

        try{
            content = Files.readAllBytes(Paths.get(filepath));
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getBlockId() {
        return blockId;
    }


}
