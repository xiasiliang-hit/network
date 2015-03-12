package socket;

import fileHandler.LogFileAccess;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ListIterator;

import peer.*;
import msg.*;

public class MyMessageReceiver extends Thread {
	public MyMessageReceiver(Socket socket, long pSize) {
		this.socket = socket;
		this.pieceSize = pSize;
		
		ListIterator<Peer> it = PeerProcess.peers.listIterator();

		while(it.hasNext()) {
			Peer myPeer = (Peer)it.next();
			
			if(myPeer.getSocket().equals(socket)) {
				remotePeerID = myPeer.getPeerId();
			}
		}
	}
	
	
	@Override
	public void run() {
		while(true) {
			
			byte[] message = receiveMessage();
			int type = message[4];
			
			if(type == 0) {
				//choke
			}
			
			else if(type == 1) {
				//unchoke
			}
			
			else if(type == 2) {
				//interested
				
				System.out.println("Interested message received from " + remotePeerID);
				System.out.println();
				LogFileAccess.receiveInterested(remotePeerID);
			}
			
			else if(type == 3) {
				//not interested
				
				System.out.println("Not Interested message received from " + remotePeerID);
				System.out.println();
				LogFileAccess.receiveNotInterested(remotePeerID);
			}
			
			else if(type == 4) {
				//have
				
				byte[] temp = new byte[4];
				
				int x = 5;
				for (int i = 0; i < temp.length; i++) {
					temp[i] = message[x];
					x++;
				}
				
				int pieceNum = ByteBuffer.wrap(temp).getInt();
				
				ListIterator<Peer> it = PeerProcess.peers.listIterator();

				while(it.hasNext()) {
					Peer p = (Peer)it.next();

					if(p.getSocket().equals(socket)) {
							byte[] field = p.getBitfield();

							try {
								synchronized(field) {
									field = updateBitField(field, pieceNum);
									p.setBitfield(field);
								}
							} catch (Exception e) {
								System.err.println(e);
							}
					}
				}

				System.out.println("Have message received from " + remotePeerID + " for piece " + pieceNum);
				System.out.println();
				LogFileAccess.receiveHave(remotePeerID, pieceNum);
			}
			
			else if(type == 6) {
				//request
				
				byte[] temp = new byte[4];
				
				int x = 5;
				for (int i = 0; i < temp.length; i++) {
					temp[i] = message[x];
					x++;
				}
				int pieceNum = ByteBuffer.wrap(temp).getInt();
				Integer i = new Integer(pieceNum);
				
				//send piece
				PieceMsg piece = PeerProcess.map.get(i);
				
				//debugging start
				System.out.println("piece " + pieceNum + " requested from " + remotePeerID);
				System.out.println();
				//debugging end
				
				synchronized (PeerProcess.messageList) {
					MessageList m = new MessageList();
					m.setSocket(socket);
					m.setMessage(piece.piece);
					PeerProcess.messageList.add(m);
//					System.out.println();
//					
//					Iterator<byte[]> itr = PeerProcess.messageList.iterator();
//					while(itr.hasNext()) {
//						byte[] temp1 = (byte[]) itr.next(); 
//						for (int i1 = 0; i1 < temp1.length; i1++) {
//							System.out.print(temp1[i1]);
//						}
//						System.out.println();
//					}
				}
				//sendMessage(p.piece);
			}
			
			else if(type == 7) {
				//piece
				
				byte index[] = new byte[4];
				
				int x = 5;
				for (int i = 0; i < index.length; i++) {
					index[i] = message[x];
					x++;
				}
				int pieceIndex = ByteBuffer.wrap(index).getInt();
				Integer num = new Integer(pieceIndex);
				byte[] piece = new byte[message.length - 9];
				for (int i = 0; i < piece.length; i++) {
					piece[i] = message[x];
					x++;
				}
			
				if(piece.length == pieceSize && !PeerProcess.map.containsKey(num)) {
					
					PieceMsg p1 = new PieceMsg(pieceIndex, piece);
					
					try {
						synchronized(PeerProcess.map) {
							PeerProcess.map.put(num, p1);
							Thread.sleep(30);
						}
					} catch (Exception e) {
						System.err.println(e);
					}
					
					
					System.out.println("piece " + pieceIndex + " received from " + remotePeerID);
					System.out.println();
					LogFileAccess.downloadPiece(remotePeerID, pieceIndex);
					
					//update bitfield
					
					try {
						synchronized(BitFieldMsg.myBitfield) {					
							BitFieldMsg.updateBitField(pieceIndex);
							Thread.sleep(20);
						}
					} 
					catch (InterruptedException e) {
						System.err.println(e);
					}
					
					//send have to all peers
					HaveMsg have = new HaveMsg(pieceIndex);
					
					ListIterator<Peer> it = PeerProcess.peers.listIterator();
					
					while(it.hasNext()) {
						Peer peer = (Peer)it.next();
						//Socket s = peer.getSocket();
						
						//if(s.equals(socket))
						//sendMessage(have.have);
						synchronized (PeerProcess.messageList) {
							MessageList m = new MessageList();
							m.setSocket(peer.getSocket());
							m.setMessage(have.have);
							PeerProcess.messageList.add(m);
						}
						//else
						//sendMessage(s, have.have);
					}
				}
				
			}
			
			else if(type == 8) {
				
				synchronized(PeerProcess.hasDownloadedCompleteFile) {
					
					ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();
					
					while(it.hasNext()) {
						HasCompleteFile peer = (HasCompleteFile)it.next();
						
						if(peer.getSocket().equals(socket)) {
							peer.setHasDownLoadedCompleteFile(true);
							break;
						}
					}
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						System.err.println(e);
					}
				}
			}
		}
	}

	
//	 void sendMessageAll(Socket s, byte[] have) {
//		
//		try {
//			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
//			synchronized (s) {
//				out.writeObject(have);
//				Thread.sleep(100);
//			}
//		} catch (IOException e) {
//			System.err.println(e);
//		} catch (InterruptedException e) {
//			System.err.println(e);
//		}
//	}
//
//
//	 void sendMessage(byte[] message) {
//		
//		try {
//			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//			synchronized (socket) {
//				out.writeObject(message);
//				Thread.sleep(100);
//			}
//		} catch (IOException e) {
//			System.err.println(e);
//		} 
//		catch (InterruptedException e) {
//			System.err.println(e);
//		}
//	}


//	public static void updateListOfPeersConnected(Socket newSocket) {
//		
//		synchronized (listOfPeersConnected) {
//			listOfPeersConnected.add(newSocket);
//		}
//	}
	
	
	 byte[] receiveMessage() {
		
		byte[] message = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			message = (byte[]) in.readObject();
		} 
//		catch (IOException e) {
//			System.err.println(e);
//		} catch (ClassNotFoundException e) {
//			System.err.println(e);
//		} 
		catch (Exception e) {
			System.exit(0);
		}
		
		return message;
	}
	
	public byte[] updateBitField(byte[] field, int pieceIndex) {
		int i = (pieceIndex - 1) / 8;
		int k = 7 - ((pieceIndex - 1) % 8);
		field[i + 5] = (byte) (field[i + 5] | (1<<k));
		return field;
	}
	Socket socket;
	 int remotePeerID;
	 long pieceSize;
	
}
