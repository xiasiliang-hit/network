package msg;

import java.nio.*;

public class UnchokeMsg {

	
	
	public UnchokeMsg() {
		msg_length = ByteBuffer.allocate(4).putInt(0).array();
		
		int i;
		for (i = 0; i < msg_length.length; i++) {
			unchoke[i] = msg_length[i];
		}
		
		unchoke[i] = type;

	}
	public byte[] unchoke = new byte[5];/*4 bytes msg length and 1 byte msg type*/
	byte[] msg_length = new byte[4];
	byte type = 1;/*1 means msg type is unchoke*/
}
