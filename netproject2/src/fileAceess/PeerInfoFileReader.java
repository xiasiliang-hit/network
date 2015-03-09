package fileAceess;
import java.io.*;
import java.util.*;


public class PeerInfoFileReader {
	
	 
	public PeerInfoFileReader(int myPeer_ID) {
		this.myPeer_ID = myPeer_ID;
	}
	
	public void readFile() {
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("PeerInfo.cfg"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				
				if(myPeer_ID == Integer.parseInt(parts[0])) {
					peer_ID = Integer.parseInt(parts[0]);
					hostName = parts[1];
					port = Integer.parseInt(parts[2]);
					
					if(parts[3].equals("1"))
						hasCompleteFile = true;
					else
						hasCompleteFile = false;
				}
					
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	
	public ArrayList<Integer> getAllPeerID() {
		
		ArrayList<Integer> arr = new ArrayList<Integer>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("PeerInfo.cfg"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				arr.add(Integer.parseInt(parts[0]));
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return arr;
	}
	
	
	public ArrayList<String[]> getPeerInfo() {
		
		ArrayList<String[]> arr = new ArrayList<String[]>(); 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("PeerInfo.cfg"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				
				if(myPeer_ID != Integer.parseInt(parts[0])) {
					arr.add(parts);
				}
				else
                                {
                                   continue;
                                }
					//break;
			}
			reader.close();
		
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		
		return arr;
	}
	
	public int getPeer_ID() {
		return peer_ID;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return port;
	}

	public boolean isHasCompleteFile() {
		return hasCompleteFile;
	}
        
        
        int peer_ID;
	 int myPeer_ID;
	 String hostName;
	 int port;
	 boolean hasCompleteFile;
	
}
