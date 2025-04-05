import java.util.ArrayList;

public class BufferPool {

    private Frame[] buffers;
    //int max_arr_length;


    public BufferPool(int arr_length) {

        buffers = new Frame[arr_length];

        //max_arr_length = arr_length;
    }


    // add a frame to the buffer pool
    public void addFrame(Frame frame, ArrayList<VirtualFile> files) {

        boolean found_space = true;
        int num_frames = 0;


        // first check how many non-null values are in buffers
        for(int i = 0; i < buffers.length; i++) {
            if(buffers[i] != null) {
                num_frames++;
            }
        }

        // if we are at capacity, free up some space
        if(buffers.length == num_frames) {
            found_space = freeSpace(files);
        }

        // if there's free space, find the next empty frame to add the file to
        if(!found_space){
            System.out.println("Not enough free space to add frame");
        }
        else{
            // iterate through the buffers until we see an empty one to add the new frame to
            for(int i = 0; i < buffers.length; i++) {
                if(buffers[i] == null) {
                    buffers[i] = frame;
                    //System.out.println("Added frame " + i + " to buffer pool");
                    break;
                }
            }
            //buffers[num_frames] = frame;
        }
    }

    // finds the frame's index number (which is also its frame number, minus one)
    public int getFrameIndex(Frame frame) {

        for(int i = 0; i < buffers.length; i++) {
            if(buffers[i] == frame && frame != null) {
                return i;
            }
        }

        return -1;
    }


    public Frame getFrameWithID(int id) {

        Frame frame = null;

        for (int i = 0; i < buffers.length; i++) {
            if(buffers[i] != null && buffers[i].getBlockId() == id) {
                frame = buffers[i];
            }
        }

        return frame;
    }

    // frees up space in the buffer pool, if possible
    public boolean freeSpace(ArrayList<VirtualFile> files) {
        // evict a frame from the pool, or return false if we can't (if they're all pinned)
        int block_id_to_evict = -1;
        int index_to_evict = -1;

        for (int i = 0; i < buffers.length; i++) {
            // not null check is unnecessary here but idk just in case
            if(buffers[i] != null && !buffers[i].getPinned()) {
                block_id_to_evict = buffers[i].getBlockId();
                index_to_evict = i;
                break;
            }
        }

        if(block_id_to_evict == -1) {
            return false;
        }

        // evict the frame, writing to (virtual) file if flag is dirty
        if(getFrameWithID(block_id_to_evict).getDirty()) {

            VirtualFile file = null;

            // find the corresponding virtual file (with corresponding block id) so we can write to it
            for(int i=0; i<files.size(); i++) {
                if(files.get(i).getBlockId() == block_id_to_evict) {
                    file = files.get(i);
                    break;
                }
            }

            if(file == null){
                return false;
            }

            // overwrite the virtual file with the updated frame in memory
            file.setContent(getFrameWithID(block_id_to_evict).getContent());

        }

        System.out.println("Evicted file " + getFrameWithID(block_id_to_evict).getBlockId() + " from frame " + (index_to_evict + 1));

        // remove the frame from the buffer pool
        buffers[index_to_evict] = null;

        return true;
    }

    // checks if the buffer pool has any frames that aren't pinned (that can be evicted to free up space)
    public boolean anyUnpinnedFrames() {
        for (int i = 0; i < buffers.length; i++) {
            if(buffers[i] == null || !buffers[i].getPinned()) {
                return true;
            }
        }

        return false;
    }



}
