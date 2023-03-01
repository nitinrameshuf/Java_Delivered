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

public class taskfile_sd {

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
                String[] csv_component = line.split(",");

                List<Float> list = new ArrayList<Float>();
                //List [0]:Total Execution time [1]: Min Execution time [2]:Max Execution time [3]: Exec Count [4]: Current Mean [5]:sum of square of differences.

                if (sysMap.containsKey(csv_component[1])){
                    list = sysMap.get(csv_component[1]);
                    list.set(0, list.get(0) + Float.valueOf(csv_component[2]));
                    if(Float.valueOf(csv_component[2]) < list.get(1)){
                        list.set(1, Float.valueOf(csv_component[2]));
                    }
                    if (Float.valueOf(csv_component[2]) > list.get(2)) {
                        list.set(2, Float.valueOf(csv_component[2]));
                    }
                    list.set(3, (list.get(3) + 1));
                    float prev_mean = list.get(4);
                    list.set(4,(list.get(0)/list.get(3)));
                    float ssd = list.get(5) + (Float.valueOf(csv_component[2]) - prev_mean) * (Float.valueOf(csv_component[2]) - list.get(4));
                    list.set(5,ssd);
                }
                else{
                    list.add(Float.valueOf(csv_component[2]));
                    list.add(Float.valueOf(csv_component[2]));
                    list.add(Float.valueOf(csv_component[2]));
                    list.add(1F);
                    list.add(Float.valueOf(csv_component[2]));
                    list.add(0F);
                    sysMap.put(csv_component[1],list);
                }
            }

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

