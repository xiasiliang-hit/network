package fileHandler;

import java.io.*;
import java.util.*;
/*class CommonCfgFileReader reads "Common.cfg" and return the configuration parameters*/
public class CommonCfgFileReader {
    public void readCfgFile() {
        String commonCfgFile = "Common.cfg";
        Properties propertyFile = new Properties();
        
        try {         
            BufferedInputStream bs = new BufferedInputStream(new FileInputStream(commonCfgFile));
            propertyFile.load(bs);/*Properties object "file" load the CfgFile as (key, value) pairs, the first column is the key and the second column is the value, both are handled as string*/
            fName = propertyFile.getProperty("FileName");
            fSize = Long.parseLong(propertyFile.getProperty("FileSize"));
            pSize = Long.parseLong(propertyFile.getProperty("PieceSize"));
            nPreferredNeighbors = Integer.parseInt(propertyFile.getProperty("NumberOfPreferredNeighbors"));
            unchokeInterval = Integer.parseInt(propertyFile.getProperty("UnchokingInterval"));
            optimisticUnchokeInterval = Integer.parseInt(propertyFile.getProperty("OptimisticUnchokingInterval"));
            
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public String getFName() {
        return fName;
    }

    public long getFSize() {
        return fSize;
    }

    public long getPSize() {
        return pSize;
    }
    public int getNPreferredNeighbors() {
        return nPreferredNeighbors;
    }

    public int getUnchokeInterval() {
        return unchokeInterval;
    }

    public int getOptimisticUnchokeInterval() {
        return optimisticUnchokeInterval;
    }

    String fName;
    long fSize;
    long pSize;
    int nPreferredNeighbors;
    int unchokeInterval;
    int optimisticUnchokeInterval;
}
