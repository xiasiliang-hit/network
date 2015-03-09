package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class Choke {

    
    public Choke() {
        message_length = ByteBuffer.allocate(4).putInt(0).array();
        int i = 0;
        for (i = 0; i < message_length.length; i++) {
            choke[i] = message_length[i];
        }

        choke[i] = type;
    }

    public byte[] choke = new byte[5];
     byte[] message_length = new byte[4];
     byte type = 0;    
}
