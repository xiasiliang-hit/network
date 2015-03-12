package msg;

import java.io.*;
import java.util.*;
import java.nio.*;

public class BitFieldMsg {

    public static void setBitfield(boolean hasFile, int num) {

        isCompleteFileFlag = hasFile;
        totalPiece = num;
        
        int payload_length = (int) Math.ceil((double) totalPiece / 8);
        int remaining = totalPiece % 8;
        
        msgLength = ByteBuffer.allocate(4).putInt(payload_length).array();
        payload = new byte[payload_length];
        myBitfield = new byte[payload_length + 5];/*plus 5 bytes, 4 bytes for msg length and 1 byte for msg type*/
        /*4 bytes for msg length*/
        int i;
        for (i = 0; i < msgLength.length; i++) {
            myBitfield[i] = msgLength[i];
        }
        /*1 byte for msg type*/
        myBitfield[i] = type;
        /*if the peer does not have any pieces yet, set the payload field to all 0s*/
        if (isCompleteFileFlag == false) 
        {

            for (int j = 0; j < payload.length; j++) 
            {
                i++;
                myBitfield[i] = 0;
            }
        } 
        else /*if the peer has some pieces*/
        {
            for (int j = 0; j < payload.length - 1; j++) 
            {

                i++;

                for (int k = 0; k < 8; k++) {
                    myBitfield[i] = (byte) (myBitfield[i] | (1 << k));
                }
            }

            i++;
            for (int j = 0; j < remaining; j++) /*for the last byte of the payload filed*/
            {
                myBitfield[i] = (byte) (myBitfield[i] | (1 << (7 - j)));/* << ,Signed left shift*/
            }
        }

//		for (int j = 0; j < bitfield.length; j++) {
//			System.out.print(bitfield[j]);
//			System.out.print(" ");
//		}
    }

    public static void updateBitField(int pieceIndex) {
        int i = (pieceIndex - 1) / 8;
        int k = 7 - ((pieceIndex - 1) % 8);
        myBitfield[i + 5] = (byte) (myBitfield[i + 5] | (1 << k));/*Bitwise inclusive OR按位同或*/
    }

    static int totalPiece;
    static boolean isCompleteFileFlag = false;
    static byte[] payload;
    static byte[] msgLength = new byte[4];
    static byte type = 5;
    public static byte[] myBitfield;
}
