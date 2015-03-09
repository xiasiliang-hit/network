package peer;


import fileAceess.PeerInfoFileReader;
import fileAceess.Logger;
import fileAceess.CommonConfigFileReader;
import fileAceess.MyFileReader;
import socket.IncomingPeerListener;
import socket.OutgoingConnectionRequest;
import java.io.*;
import java.util.*;

import msg.*;

public class PeerProcess {
	
     
    public static HashMap<Integer, Piece> map;
    public static ArrayList<Peer> peers = new ArrayList<Peer>();
    public static ArrayList<Integer> allPeerID;
    public static LinkedList<MessageList> messageList = new LinkedList<MessageList>(); 
    public static ArrayList<HasCompleteFile> hasDownloadedCompleteFile = new ArrayList<HasCompleteFile>();
    
	public static void main(String[] args) {
    	
            System.out.println("hello world");
            PeerProcess self = new PeerProcess();
            //String my_pid = "1002";
            String my_pid = args[0];
        
		CommonConfigFileReader common = new CommonConfigFileReader(); 
		common.readFile();
		
		self.numberOfPreferredNeighbors = common.getNumberOfPreferredNeighbors();
		self.unchokingInterval = common.getUnchokingInterval();
		self.optimisticUnchokingInterval = common.getOptimisticUnchokingInterval();
		fileName = common.getFileName();
		self.fileSize = common.getFileSize();
		self.pieceSize = common.getPieceSize();
		
		self.totalPieces = (int) Math.ceil((double)self.fileSize/self.pieceSize);
		
		//read peer info file
		PeerInfoFileReader peerInfo = new PeerInfoFileReader(Integer.parseInt(my_pid));
		System.out.println("My peer id: " + my_pid);
		System.out.println();
		peerInfo.readFile();
		allPeerID = peerInfo.getAllPeerID();

		self.peer_ID = peerInfo.getPeer_ID();
		self.port = peerInfo.getPort();
		self.hasCompleteFile = peerInfo.isHasCompleteFile();
		
		BitField.setBitfield(self.hasCompleteFile,self.totalPieces);
		
		Logger.startLogger(self.peer_ID);
		
		if(self.hasCompleteFile == false) {
			map = new HashMap<Integer, Piece>();
			
			IncomingPeerListener peerListener = new IncomingPeerListener(self.port, self.peer_ID, self.totalPieces, self.hasCompleteFile, self.fileSize, self.pieceSize);
			peerListener.start();
			
			OutgoingConnectionRequest connect = new OutgoingConnectionRequest(self.peer_ID, self.totalPieces, self.hasCompleteFile, self.fileSize, self.pieceSize);
			connect.start();
		}
	
		else if(self.hasCompleteFile == true) {
			MyFileReader reader = new MyFileReader(self.peer_ID, self.pieceSize, fileName);
			map = reader.readFile();
			
                        
                        /// have complete file, do not init outgoing connection, only work as server
			IncomingPeerListener peerListener = new IncomingPeerListener(self.port, self.peer_ID, self.totalPieces, self.hasCompleteFile, self.fileSize, self.pieceSize);
			peerListener.start();
//			Set<Integer> keys = self.map.keySet();
//			Iterator<Integer> it = keys.iterator();
//			
//			while(it.hasNext()) {
//				Integer i = it.next();
//				System.out.println(i + " " + self.map.get(i).getMessage());
//			}
		}	
	}
        
        int numberOfPreferredNeighbors;
     int unchokingInterval;
     int optimisticUnchokingInterval;
    public static String fileName;
     long fileSize;
     long pieceSize;
     int totalPieces;
     int peer_ID;
     int port;
     boolean hasCompleteFile;
}
