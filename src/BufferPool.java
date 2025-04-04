
public class BufferPool {

    private Frame[] buffers;


    public BufferPool(int arr_length) {

        buffers = new Frame[arr_length];


    }


    public void addFrame(Frame frame) {
        buffers[buffers.length - 1] = frame;
    }

    public Frame getFrameWithIndex(int index) {
        return buffers[index];
    }

    public Frame getFrameWithID(int id) {

        Frame frame = null;

        for (int i = 0; i < buffers.length; i++) {
            if(buffers[i].getBlockId() == id) {
                frame = buffers[i];
            }
        }

        return frame;
    }



}
