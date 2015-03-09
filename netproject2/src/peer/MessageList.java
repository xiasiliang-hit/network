package peer;
import java.io.*;
import java.util.*;
import java.net.*;

public class MessageList {
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public byte[] getMessage() {
		return message;
	}
	
	public void setMessage(byte[] message) {
		this.message = message;
	}
         Socket socket;
	 byte[] message;
}
