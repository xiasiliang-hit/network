package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class HaveMsg {
	
	
	
	public HaveMsg(int index) {
		msg_length = ByteBuffer.allocate(4).putInt(4).array();/*allocate a 4 bytes buffer with value 4, means the msg length for have msg is 4, 4 byte for msg payload*/
		payload = ByteBuffer.allocate(4).putInt(index).array();
		
		int i = 0;
		for (i = 0; i < msg_length.length; i++) {
			have[i] = msg_length[i];
		}
		
		have[i] = type;
		
		for (int j = 0; j < payload.length; j++) {
			i++;
			have[i] = payload[j];
		}
		
	}
	public byte[] have = new byte[9];
	byte[] msg_length = new byte[4];
	byte type = 4;
	byte[] payload = new byte[4];
}
