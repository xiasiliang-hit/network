package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class InterestedMsg {

	
	
	public InterestedMsg() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i;
		for (i = 0; i < message_length.length; i++) {
			interested[i] = message_length[i];
		}
		
		interested[i] = type;
		
	}
	public byte[] interested = new byte[5];
	byte[] message_length = new byte[4];
	byte type = 2; /*2 mean msg type is interested*/
}
