public class Frame {


    private boolean dirty, pinned;
    private byte[] content;
    private int blockId;

    public Frame(byte[] new_content, int new_blockId) {

        content = new_content;
        blockId = new_blockId;
        pinned = false;
        dirty = false;

    }

    public byte[] getRecord(int index){

        byte[] record = new byte[40];

        // put 40 bytes into the record array to be returned
        // ie. indices 0-40, 41-80, 81-100, etc

        // this convoluted math gives the starting point for where the data is.
        // ex. record num = 430. 430 --> 30 (30th record in the block) * 40 (40 bytes) and finally - 40 (b/c it was off by one)
        int start_pos = ((index - (blockId - 1)*100) * 40) - 40;

        for(int i = 0; i < 40; i++){
            record[i] = content[start_pos + i];
        }


        return record;
    }

    public void setRecord(int index, byte[] record){

        // if a valid record is passed in, flip the dirty flag to true b/c we've changed the content of the frame
        if(record.length != 0 ){
            dirty = true;
        }

        // this convoluted math gives the starting point for where the data is.
        // ex. record num = 430. 430 --> 30 (30th record in the block) * 40 (40 bytes) and finally - 40 (b/c it was off by one)
        int start_pos = ((index - (blockId - 1)*100) * 40) - 40;

        // put the 40 bytes from the new record into the corresponding old record in the block
        // ie. indices 0-40, 41-80, 81-100, etc
        for(int i = 0; i < 40; i++){
            content[start_pos + i] = record[i];
        }

    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public boolean getDirty(){
        return dirty;
    }
    public void setDirty(boolean new_dirty){
        dirty = new_dirty;
    }

    public boolean getPinned(){
        return pinned;
    }
    public void setPinned(boolean new_pinned){
        pinned = new_pinned;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
