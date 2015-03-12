package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class ChokeMsg {

    
    public ChokeMsg() {
        msg_length = ByteBuffer.allocate(4).putInt(0).array();/*allocate a 4 byte all 0 buffer*/
        int i;
        for (i = 0; i < msg_length.length; i++) {
            choke[i] = msg_length[i];
        }

        choke[i] = type;
    }

    public byte[] choke = new byte[5];/*4 byte msg length an 1 byte msg type*/
    byte[] msg_length = new byte[4];
    byte type = 0; /*0 means the msg type is choke*/   
}
