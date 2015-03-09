package socket;

import java.io.*;
import java.util.*;
import java.net.*;

import msg.BitField;
import fileAceess.Logger;
import peer.*;


public class IncomingPeerListener extends Thread {
	
	 
	public IncomingPeerListener(int listenPort, int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
		this.listenPort = listenPort;
		myPeer_ID = peer_ID;
		this.totalPieces = totalPieces;
		this.haveAllPieces = haveAllPieces;
		this.fileSize = fileSize;
		this.pieceSize = pieceSize;
	}
	
	@Override
	public void run() {
		
		try {
			@SuppressWarnings("resource")
			ServerSocket listener = new ServerSocket(listenPort);
			
			while(true) {
				Socket socket = listener.accept();				
				int peer_ID;

				//Handshake
				byte[] received = receiveHandShake(socket);
				
				Handshake send = new Handshake(myPeer_ID);
				sendHandShake(socket, send.handshake);
				
				byte[] temp = new byte[28];
				for(int i = 0 ; i < 28 ; i++){
					temp[i] = received[i];
				}
				
				String header = new String(temp);
				
				int j = 0;
				byte[] ID_temp = new byte[4];
				for(int i = 28 ; i < 32 ; i++) {
					ID_temp[j] = received[i];
					j++;
				}
				
				String s = new String(ID_temp);
				peer_ID = Integer.parseInt(s);
				
                                
                                //wait for handshaking msg
				if(header.equals("CNT5106C2013SPRING0000000000")) {
					
					boolean flag = false;
					ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();
					
					while(iter.hasNext()) {
						
						int num = iter.next().intValue(); 
						if(num != myPeer_ID) 
							continue;
						else
							break;
					}
					
					while(iter.hasNext()) {
						
						int num = iter.next().intValue(); 
						if(num == peer_ID) 
							flag = true;
					}
										
					if(flag == true) {	
						Peer p = new Peer();
						p.setMyPeer_ID(myPeer_ID);
						p.setSocket(socket);
						p.setPeer_ID(peer_ID);						
						
						//send bitfield
						sendBitfield(socket);
						
						//receive bitfield
						p.setBitfield(receiveBitfield(socket));
						p.setInterested(false);
						
						PeerProcess.peers.add(p);
						
						HasCompleteFile completeFile = new HasCompleteFile();
						completeFile.setSocket(socket);
						completeFile.setHasDownLoadedCompleteFile(false);
						
						PeerProcess.hasDownloadedCompleteFile.add(completeFile);
						
						System.out.println("Connection request from " + peer_ID);
						System.out.println();
						Logger.madeTCPConnection(peer_ID);
						
//						ListIterator<Peer> list = PeerProcess.peers.listIterator();
//						
//						while(list.hasNext()) {
//							Peer pt = (Peer)list.next();
//							System.out.println(pt.getPeer_ID() + " " + pt.getSocket());
//						}
						
						MessageSender messageSender = new MessageSender();
						messageSender.start();
						
						PieceRequest pieceReq = new PieceRequest(peer_ID, totalPieces, haveAllPieces, fileSize, pieceSize);
						pieceReq.start();
						
						MessageReceiver messageReceiver = new MessageReceiver(socket, pieceSize);
						messageReceiver.start();
					}
					else {
						System.out.println("Unexpected peer connection");
					}
				}
			}
			
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	
	 void sendBitfield(Socket socket) {
		
		try {
		
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(BitField.bitfield);
		
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}


	 byte[] receiveBitfield(Socket socket) {
		
		byte[] bitfield = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			bitfield = (byte[]) in.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		return bitfield;
	}

	 void sendHandShake(Socket socket, byte[] handshake) {
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(handshake);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	

	 byte[] receiveHandShake(Socket socket) {
		
		byte[] handshake = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			handshake = (byte[]) in.readObject();
		} catch (IOException e) {
			System.err.println(e);
		} catch (ClassNotFoundException e) {
			System.err.println(e);
		}
		
		return handshake;
	}
         
         int listenPort;
	 int myPeer_ID;
	 int totalPieces;
	 boolean haveAllPieces;
	 long fileSize;
	 long pieceSize;
	
	
}
