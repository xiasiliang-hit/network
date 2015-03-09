package msg;

import java.nio.*;

public class Unchoke {

	
	
	public Unchoke() {
		message_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			unchoke[i] = message_length[i];
		}
		
		unchoke[i] = type;

	}
	public byte[] unchoke = new byte[5];
	 byte[] message_length = new byte[4];
	 byte type = 1;
}
