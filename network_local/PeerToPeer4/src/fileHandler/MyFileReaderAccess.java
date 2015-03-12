package fileHandler;

import java.io.*;
import java.util.*;
import msg.PieceMsg;

public class MyFileReaderAccess {

    public MyFileReaderAccess(int ID, long pieceSize, String fileName) {
        this.id = ID;
        this.pSize = pieceSize;
        this.fName = fileName;
    }

    public HashMap<Integer, PieceMsg> readFile() {

        fName = "peer_" + id + "/" + fName;
        File f = new File(fName);

        try {

            InputStream input = new FileInputStream(f);
            byte[] buf = new byte[(int) pSize];

            @SuppressWarnings("unused")
            int length;

            while ((length = input.read(buf)) > 0) {

                myMap.put(pieceNumber, new PieceMsg(pieceNumber, buf));
                pieceNumber++;
            }

            input.close();

        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }

        return myMap;
    }
    int id;
    long pSize;
    HashMap<Integer, PieceMsg> myMap = new HashMap<Integer, PieceMsg>();
    int pieceNumber = 1;
    String fName;
}
