package fileHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PeerInfoFileReaderAcess {
	 
	public PeerInfoFileReaderAcess(int myPeer_ID) {
		this.myPeerId = myPeer_ID;
	}
	
	public void readFile() {
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("PeerInfo.cfg"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				
				if(myPeerId == Integer.parseInt(parts[0])) {
					peerId = Integer.parseInt(parts[0]);
					host = parts[1];
					port = Integer.parseInt(parts[2]);
					
					if(parts[3].equals("1"))
						completeFileFlag = true;
					else
						completeFileFlag = false;
				}
					
			}
			
			reader.close();
		
		} catch (Exception e) {
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
				
				if(myPeerId != Integer.parseInt(parts[0])) {
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
	
	public int getPeerId() {
		return peerId;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public boolean isCompleteFileFlag() {
		return completeFileFlag;
	}
        
        
        /*
        public void updateCfgFile()
        {
            
        }
        */
        
        int peerId;
	int myPeerId;
	String host;
	int port;
	boolean completeFileFlag;
}
