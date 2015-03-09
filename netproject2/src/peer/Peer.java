package peer;

import java.net.*;
import java.io.*;
import java.util.*;

public class Peer {
	
	 
	
	public int getPeer_ID() {
		return peer_ID;
	}
	
	public void setPeer_ID(int peer_ID) {
		this.peer_ID = peer_ID;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public byte[] getBitfield() {
		return bitfield;
	}

	public void setBitfield(byte[] bitfield) {
		this.bitfield = bitfield;
	}

	public boolean isInterested() {
		return interested;
	}

	public void setInterested(boolean interested) {
		this.interested = interested;
	}

	public int getMyPeer_ID() {
		return myPeer_ID;
	}

	public void setMyPeer_ID(int myPeer_ID) {
		this.myPeer_ID = myPeer_ID;
	}
	int myPeer_ID;
	 int peer_ID;
	 Socket socket;
	 byte[] bitfield;
	 boolean interested;
}
