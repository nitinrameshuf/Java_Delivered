//_____________________________________________________________________________________
//Author: Nitin Ramesh 2023
//Solution: Min,Max,Avg & SD calculated in one pass. File or Data not stored in memory
//_____________________________________________________________________________________

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class taskfile_sd_comments {

    public static void main(String[] args) {

        //Files read as Input-stream to prevent memory overhead.
        FileInputStream inputStream = null;
        Scanner my_scanner = null;
        Map<String,List<Float>> sysMap = new HashMap<>();

        try {
            //Reading each line from the file.
            inputStream = new FileInputStream("./files/CODETEST.CSV");
            my_scanner = new Scanner(inputStream, "UTF-8");
            while (my_scanner.hasNextLine()) {
                String line = my_scanner.nextLine();
                // Breaking the line into array of values seperated by the ","
                String[] csv_component = line.split(",");

                List<Float> list = new ArrayList<Float>();
                //List [0]:Total Execution time [1]: Min Execution time [2]:Max Execution time 
                //     [3]: Exec Count          [4]: Current Mean       [5]:sum of square of differences.

                // Checks if the current job label exists:
                if (sysMap.containsKey(csv_component[1])){
                    list = sysMap.get(csv_component[1]);
                    // Updating the total execution time
                    list.set(0, list.get(0) + Float.valueOf(csv_component[2]));
                    // Updating the Minimum Execution time
                    if(Float.valueOf(csv_component[2]) < list.get(1)){
                        list.set(1, Float.valueOf(csv_component[2]));
                    }
                    // Updating the Maximum Execution time
                    if (Float.valueOf(csv_component[2]) > list.get(2)) {
                        list.set(2, Float.valueOf(csv_component[2]));
                    }
                    // Increasing the execution counter
                    list.set(3, (list.get(3) + 1));
                    //Previous Mean Execution time
                    float prev_mean = list.get(4);
                    //Current Mean execution time:
                    // [0]/[3]
                    list.set(4,(list.get(0)/list.get(3)));
                    //Formula:
                    // M(2,n) = M(2,n-1) + (Xn - X~n)(Xn - X~n-1)
                    // Current SS = Previous SS + (Current Value - Current Mean)(Current Value - Previous Mean)
                    float ssd = list.get(5) + (Float.valueOf(csv_component[2]) - prev_mean) * (Float.valueOf(csv_component[2]) - list.get(4));
                    list.set(5,ssd);
                }
                //Adding a new Job label
                else{
                    //[0] Total exec time
                    list.add(Float.valueOf(csv_component[2]));
                    //[1] Min exec time
                    list.add(Float.valueOf(csv_component[2]));
                    //[2] Max exec time
                    list.add(Float.valueOf(csv_component[2]));
                    //[3] Exec Count
                    list.add(1F);
                    //[4] Current Mean
                    list.add(Float.valueOf(csv_component[2]));
                    //[5] Sum of squares of differences
                    list.add(0F);
                    //Add to the list
                    sysMap.put(csv_component[1],list);
                }
            }

            // The standard deviation formula:
            // Square Root (SSD/(Count -1))
            // SQRT([5]/([4]-1))
            sysMap.forEach((key,value) ->
                    System.out.println("Task Name: " + key + "\nThe minimum execution time: " + value.get(1) + " millis \nThe maximum execution time: " +value.get(2) +" millis\nThe average execution time: "+ value.get(0)/value.get(3) + " millis.\nThe standard deviation is: " + Math.sqrt((value.get(5)/(value.get(3)-1))) +"\n"));

        } catch (Exception e) {
            System.out.println("Exception caught " + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (my_scanner != null) {
                    my_scanner.close();
                }
            }
            catch(Exception e) {
                System.out.println("File close exception caught");

            }
        }

    }
}

