package fileAceess;

import java.io.*;
import java.util.*;

public class CommonConfigFileReader {
    public void readFile() {
        String commonCfgFile = "Common.cfg";
        Properties file = new Properties();
        
        try {
            file.load(new BufferedInputStream(new FileInputStream(commonCfgFile)));
            numberOfPreferredNeighbors = Integer.parseInt(file.getProperty("NumberOfPreferredNeighbors"));
            unchokingInterval = Integer.parseInt(file.getProperty("UnchokingInterval"));
            optimisticUnchokingInterval = Integer.parseInt(file.getProperty("OptimisticUnchokingInterval"));
            fileName = file.getProperty("FileName");
            fileSize = Long.parseLong(file.getProperty("FileSize"));
            pieceSize = Long.parseLong(file.getProperty("PieceSize"));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getPieceSize() {
        return pieceSize;
    }
    public int getNumberOfPreferredNeighbors() {
        return numberOfPreferredNeighbors;
    }

    public int getUnchokingInterval() {
        return unchokingInterval;
    }

    public int getOptimisticUnchokingInterval() {
        return optimisticUnchokingInterval;
    }

    String fileName;
    long fileSize;
    long pieceSize;
    int numberOfPreferredNeighbors;
    int unchokingInterval;
    int optimisticUnchokingInterval;
}
