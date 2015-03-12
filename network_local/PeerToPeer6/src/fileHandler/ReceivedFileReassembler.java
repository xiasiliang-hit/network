package fileHandler;

import java.io.*;
import java.util.*;
import java.nio.*;

import peer.*;
import msg.*;

/* */
public class ReceivedFileReassembler {

    public void reassemblePiece(int totalpieces, int peer_ID, long fSize, long pieceSize) {
        System.out.println("reassemble received file pieces");
        String dirName = "peer_" + peer_ID;
        File dirPath = new File(dirName);

        if (!dirPath.exists()) {

            try {
                dirPath.mkdir();
            } catch (SecurityException e) {
                System.err.println(e);
            }
        }

        String fName = "peer_" + peer_ID + "/" + PeerProcess.fName;
        File file = new File(fName);
        try {
            OutputStream out = new FileOutputStream(file);

            for (int i = 1; i <= totalpieces - 1; i++) {

                Integer num = new Integer(i);
                PieceMsg p = PeerProcess.map.get(num);
                byte[] tempByte = new byte[4];

                for (int j = 0; j < 4; j++) {
                    tempByte [j] = p.piece[j];
                }

                int size = ByteBuffer.wrap(tempByte).getInt();/* wrapping an existing byte array into a buffer.*/

                size = size - 4;

                System.out.println(i + " = " + size);

                byte[] buffer = new byte[size];

                for (int j = 0, z = 9; j < buffer.length && z < p.piece.length; j++, z++) {
                    buffer[j] = p.piece[z];
                }
                out.write(buffer);
            }

            Integer num = new Integer(totalpieces);
            PieceMsg peer = PeerProcess.map.get(num);

            int size = (int) (fSize % pieceSize);
            System.out.println(size);

            byte[] bufferByte = new byte[size];
            int j = 0;
            int z = 9;
            for (j = 0, z = 9; j < bufferByte.length && z < peer.piece.length; j++, z++) {
                bufferByte[j] = peer.piece[z];
            }
            out.write(bufferByte);
            out.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
