import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int numFrames = Integer.parseInt(args[0]);

        BufferPool bufferPool = new BufferPool(numFrames);

        Scanner scan = new Scanner(System.in);

        String input;

        // read files into buffer pool frames



        while(true){

            System.out.println("Awaiting next argument.");

            input = scan.nextLine();

            if(input.substring(0,3).equalsIgnoreCase("get")){

                int record_num = Integer.parseInt(input.substring(4));

                Frame tempFrame = bufferPool.getFrameWithID((int) (record_num / 100));

                System.out.println(Arrays.toString(tempFrame.getRecord(record_num)));

            }
            else if(input.substring(0,3).equalsIgnoreCase("set")){

                int record_num;
                String record;

                // splits the string with "record_num record_text" into an array with length = 2 [record_num, record_text]
                String[] rest_of_input = input.substring(4).split(" ");
                record_num = Integer.parseInt(rest_of_input[0]);
                record = rest_of_input[1];


                Frame tempFrame = bufferPool.getFrameWithID((int) (record_num / 100));

                tempFrame.setRecord(record_num, record.getBytes());


            }
            else if(input.substring(0,3).equalsIgnoreCase("pin")){

                int record_num = Integer.parseInt(input.substring(4));

                Frame tempFrame = bufferPool.getFrameWithID((int) (record_num / 100));

                tempFrame.setPinned(true);


            }
            else if(input.substring(0,5).equalsIgnoreCase("unpin")){

                int record_num = Integer.parseInt(input.substring(6));

                Frame tempFrame = bufferPool.getFrameWithID((int) (record_num / 100));

                tempFrame.setPinned(false);


            }
            else{
                System.out.println("Unknown command.");
                //continue;
            }


        }

    }
}