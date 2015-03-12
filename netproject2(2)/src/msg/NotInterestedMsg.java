package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class NotInterestedMsg {

	
	
	public NotInterestedMsg() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i;
		for (i = 0; i < message_length.length; i++) {
			not_interested[i] = message_length[i];
		}
		
		not_interested[i] = type;
		
	}
	public byte[] not_interested = new byte[5];
	 byte[] message_length = new byte[4];
	 byte type = 3;/*3 means msg type is not interested*/
}
