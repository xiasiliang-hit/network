package peer;

import java.io.*;
import java.util.*;

public class Handshake {
	
	
	
	public Handshake(int peer_ID) {
		this.peer_ID = peer_ID;
		String array = header + zero_bits + Integer.toString(this.peer_ID);
		handshake = array.getBytes();
	}
        
        public byte[] handshake = new byte[32];
	 String header = "CNT5106C2013SPRING";
	 String zero_bits = "0000000000";
	 int peer_ID;
	
}
