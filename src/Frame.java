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
        for(int i = 0; i < 40; i++){
            record[i] = content[index*40 + i];
        }


        return record;
    }

    public void setRecord(int index, byte[] record){

        if(record.length != 0 ){
            dirty = true;
        }

        // put the 40 bytes from the new record into the corresponding old record in the block
        // ie. indices 0-40, 41-80, 81-100, etc
        for(int i = 0; i < 40; i++){
            content[index*40 + i] = record[i];
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
