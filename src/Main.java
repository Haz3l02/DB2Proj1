import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        // max number of frames allowed in the buffer pool - from input
        int numFrames = Integer.parseInt(args[0]);

        // buffer pool that holds all the frames
        BufferPool bufferPool = new BufferPool(numFrames);

        Scanner scan = new Scanner(System.in);

        String input;

        // read files into virtual file objects - as not to have to deal with changing files on disk back and forth every time I run the program
        ArrayList<VirtualFile> virtualFiles = new ArrayList<>();



        // this loop is the bulk of the program, which commands get read and executed
        while(true){

            System.out.println("Awaiting next argument.\n");

            // read the user-provided input
            input = scan.nextLine();

            // ----------------------------------------------- GET -------------------------------------------------
            // perform some string manipulation to see if the command starts with "GET"
            if(input.substring(0,3).equalsIgnoreCase("get")){

                // everything after "GET " is the argument for it (the record number to get)
                int record_num = Integer.parseInt(input.substring(4));

                int block_num = (int) ((record_num-1) / 100) + 1;

                Frame tempFrame = bufferPool.getFrameWithID(block_num);

                // if the file the record is on is not loaded into memory, go find it and load it
                if(tempFrame == null){

                    // if there are no frames that can be freed up (all pinned)
                    if(!bufferPool.anyUnpinnedFrames()){
                        System.out.println("The file " + block_num + " cannot be accessed from disk because the memory buffers are full");
                        continue;
                    }

                    // load the file into a virtual representation for much easier testing
                    VirtualFile newFile = new VirtualFile("src/Project1/F" + block_num + ".txt", block_num);
                    virtualFiles.add(newFile);

                    // need to load the file into a frame in the buffer pool
                    Frame newFrame = new Frame(newFile.getContent(), block_num);
                    bufferPool.addFrame(newFrame, virtualFiles);

                    System.out.println("File " + block_num + " successfully loaded into memory from disk");

                }
                else{
                    System.out.println("File " + block_num + " was already loaded in memory");
                }

                // try again now that the block has been loaded into memory
                tempFrame = bufferPool.getFrameWithID(block_num);


                // the frame number for the current frame
                int frame_num = bufferPool.getFrameIndex(tempFrame) + 1;


                // convert the byte array into a nice, readable string
                String str = new String(tempFrame.getRecord(record_num), StandardCharsets.UTF_8);

                // print the record that it fetched
                System.out.println(str);
                System.out.println("Record fetched from frame " + frame_num);



            }

            // ----------------------------------------------- SET -------------------------------------------------

            else if(input.substring(0,3).equalsIgnoreCase("set")){

                int record_num;
                String record;


                // splits the string with "record_num record_text" into an array with  [record_num, record_text(in multiple entries)]
                // the replaceAll() removes the quotations present in the test cases, but not needed in the actual records
                String[] rest_of_input = input.substring(4).replaceAll("\"", "").split(" ");

                record_num = Integer.parseInt(rest_of_input[0]);
                record = input.substring(4 + 1 + rest_of_input[0].length()).replaceAll("\"", "");


                int block_num = (int) ((record_num-1) / 100) + 1;

                Frame tempFrame = bufferPool.getFrameWithID(block_num);

                if(tempFrame == null){

                    // if there are no frames that can be freed up (all pinned)
                    if(!bufferPool.anyUnpinnedFrames()){
                        System.out.println("The file " + block_num + " cannot be accessed from disk because the memory buffers are full");
                        continue;
                    }

                    // load the file into a virtual representation for much easier testing
                    VirtualFile newFile = new VirtualFile("src/Project1/F" + block_num + ".txt", block_num);
                    virtualFiles.add(newFile);

                    // need to load the file into a frame in the buffer pool
                    Frame newFrame = new Frame(newFile.getContent(), block_num);
                    bufferPool.addFrame(newFrame, virtualFiles);

                    System.out.println("File " + block_num + " successfully loaded into memory from disk");

                }
                else{
                    System.out.println("File " + block_num + " was already loaded in memory");
                }



                tempFrame = bufferPool.getFrameWithID(block_num);
                tempFrame.setDirty(true);

                tempFrame.setRecord(record_num, record.getBytes());

                // the frame number for the current frame
                int frame_num = bufferPool.getFrameIndex(tempFrame) + 1;

                System.out.println("Successfully wrote to File " + block_num + "; located in frame " + frame_num);


            }

            // ----------------------------------------------- PIN -------------------------------------------------

            else if(input.substring(0,3).equalsIgnoreCase("pin")){

                int block_num = Integer.parseInt(input.substring(4));

                Frame tempFrame = bufferPool.getFrameWithID(block_num);

                if(tempFrame == null){

                    // if there are no frames that can be freed up (all pinned)
                    if(!bufferPool.anyUnpinnedFrames()){
                        System.out.println("The file " + block_num + " cannot be pinned because the memory buffers are full");
                        continue;
                    }

                    // load the file into a virtual representation for much easier testing
                    VirtualFile newFile = new VirtualFile("src/Project1/F" + block_num + ".txt", block_num);
                    virtualFiles.add(newFile);

                    // need to load the file into a frame in the buffer pool
                    Frame newFrame = new Frame(newFile.getContent(), block_num);
                    bufferPool.addFrame(newFrame, virtualFiles);
                    System.out.println("File " + block_num + " successfully loaded into memory from disk");

                }
                else{
                    System.out.println("File " + block_num + " was already loaded in memory");
                }

                tempFrame = bufferPool.getFrameWithID(block_num);

                // the frame number for the current frame
                int frame_num = bufferPool.getFrameIndex(tempFrame) + 1;

                if(tempFrame.getPinned()){
                    System.out.println("The file " + block_num + ", was already pinned, in frame " + frame_num);
                }
                else{
                    System.out.println("The file " + block_num + ", was pinned successfully (it was previously unpinned), in frame " + frame_num);
                }

                tempFrame.setPinned(true);


            }

            // ----------------------------------------------- UNPIN -------------------------------------------------

            else if(input.substring(0,5).equalsIgnoreCase("unpin")){

                int block_num = Integer.parseInt(input.substring(6));

                Frame tempFrame = bufferPool.getFrameWithID(block_num);

                // this command doesn't care about fetching files from disk
                if(tempFrame == null){
                    System.out.println("The file " + block_num + " cannot be unpinned because it is not loaded into memory");
                    continue;
                }

                tempFrame = bufferPool.getFrameWithID(block_num);

                // the frame number for the current frame
                int frame_num = bufferPool.getFrameIndex(tempFrame) + 1;

                if(tempFrame.getPinned()){
                    System.out.println("The file " + block_num + ", was unpinned successfully (it was previously pinned), in frame " + frame_num);
                }
                else{
                    System.out.println("The file " + block_num + ", was already unpinned, in frame " + frame_num);
                }

                tempFrame.setPinned(false);



            }
            else{
                System.out.println("Unknown command.");
                //continue;
            }


        }

    }



}