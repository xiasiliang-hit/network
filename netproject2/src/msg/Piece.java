package msg;
import java.io.*;
import java.util.*;
import java.nio.*;

public class Piece {
	
	
	
	public Piece(int index, byte[] data) {
		
		message = data;
		int data_len = message.length;
		pieceIndex = ByteBuffer.allocate(4).putInt(index).array();
		message_length = ByteBuffer.allocate(4).putInt(4 + data_len).array();
		piece = new byte[9 + data_len];
		
		int i = 0;
		for (i = 0; i < message_length.length; i++) {
			piece[i] = message_length[i];
		}
		
		piece[i] = type;
		
		for (int j = 0; j < pieceIndex.length; j++) {
			i++;
			piece[i] = pieceIndex[j];
		}
		
		for (int j = 0; j < message.length; j++) {
			i++;
			piece[i] = message[j];
		}
		
	}
	public byte[] piece;
	 byte[] message_length = new byte[4];
	 byte type = 7;
	 byte[] pieceIndex = new byte[4];
	 byte[] message;
}
