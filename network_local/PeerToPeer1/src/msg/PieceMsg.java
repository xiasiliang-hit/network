package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class PieceMsg {
	
	
	
	public PieceMsg(int index, byte[] data) {
		
		msg = data;
		int data_len = msg.length;
		pieceIndex = ByteBuffer.allocate(4).putInt(index).array();
		msg_length = ByteBuffer.allocate(4).putInt(4 + data_len).array();
		piece = new byte[9 + data_len];
		
		int i = 0;
		for (i = 0; i < msg_length.length; i++) {
			piece[i] = msg_length[i];
		}
		
		piece[i] = type;
		
		for (int j = 0; j < pieceIndex.length; j++) {
			i++;
			piece[i] = pieceIndex[j];
		}
		
		for (int j = 0; j < msg.length; j++) {
			i++;
			piece[i] = msg[j];
		}
		
	}
	public byte[] piece;
	byte[] msg_length = new byte[4];
	byte type = 7;
	byte[] pieceIndex = new byte[4];
	byte[] msg;
}
