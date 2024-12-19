package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utils {


    public static void calculateValues(List<DataPoint> released, List<DataPoint> acquired, List<Long> values){

        DataPoint currentRelease= released.get(0);
        DataPoint currentAcquired= acquired.get(1);
        int i=1;
        while( i+1 < acquired.size()){

            if(currentRelease.getThread().equals(currentAcquired.getThread())) {
                i++;
                currentAcquired= acquired.get(i);
                continue;
            }
            long value= (currentAcquired.getValue()- currentRelease.getValue());
            if(value != 0)
                values.add(value);
            System.out.println(i+":release: "+ currentRelease.getThread() +",acquired: "+ currentAcquired.getThread()+" = "+ value);
            i++;

            currentAcquired= acquired.get(i);
            currentRelease= released.get(i-1);
        }
    }


    public static void writeToCSV(List<Long> values, String fileName, int discard){
        if(discard >= values.size())
            throw new IllegalArgumentException("the number of values to discard is higher than the values itself");
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(file);

            for(int y=discard; y< values.size(); y++){
                writer.write(values.get(y)+";");
            }
            writer.close();
        }catch(IOException ignored){

        }
    }
}
