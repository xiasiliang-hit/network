package socket;

import fileHandler.ReceivedFileReassembler;
import fileHandler.LogFileAccess;
import java.util.*;
import java.net.*;
import peer.*;
import msg.*;

public class MyPieceRequest extends Thread {

    public MyPieceRequest(int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
        this.peer_ID = peer_ID;
        this.totalpieces = totalPieces;
        this.haveAllPieces = haveAllPieces;
        this.fileSize = fileSize;
        this.pieceSize = pieceSize;
    }

    @Override
    public void run() {

        if (haveAllPieces == false) {

            Peer myPeer = null;
            byte[] field;
            int getPiece;

            synchronized (PeerProcess.peers) {

                ListIterator<Peer> it = PeerProcess.peers.listIterator();

                while (it.hasNext()) {
                    myPeer = (Peer) it.next();

                    if (myPeer.getPeerId() == peer_ID) {
                        myPeer_ID = myPeer.getMyPeerId();
                        socket = myPeer.getSocket();
                        break;
                    }
                }

//				try {
//					Thread.sleep(1);
//				} catch (InterruptedException e) {
//					System.err.println(e);
//				}
            }

//			field = p.getBitfield();
//			getPiece = getPieceInfo(field, BitField.bitfield);
//			
//			if(getPiece == 0) {
//				p.setInterested(false);
//
//				//send Not interested
//				NotInterested not = new NotInterested();
//				sendMessage(p.getSocket(), not.not_interested);
//			}
//			else {
//				p.setInterested(true);
//				
//				//send interested
//				Interested interested = new Interested();
//				sendMessage(p.getSocket(), interested.interested);
//				
//				//send request
//				Request req = new Request(getPiece);
//				sendMessage(p.getSocket(), req.request);
//				
//			}
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }

                boolean completeFile = hasCompleteFile();

                if (completeFile == true) {

                    if (LogFileAccess.fFlag == false) {
                        LogFileAccess.fFlag = true;

                        System.out.println("Download complete");
                        LogFileAccess.completionOfDownload();

                        ReceivedFileReassembler assemble = new ReceivedFileReassembler();
                        assemble.reassemblePiece(totalpieces, myPeer_ID, fileSize, pieceSize);

                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            System.err.println(e);
                        }
                    }

                    break;
                } else {

                    if (myPeer.isInterested() == true) {

                        field = myPeer.getBitfield();
                        getPiece = getPieceInfo(field, BitFieldMsg.myBitfield);

                        if (getPiece == 0) {
                            myPeer.setInterested(false);

                            //send Not interested
                            NotInterestedMsg not = new NotInterestedMsg();

//							messageSender.add(not.not_interested);
                            synchronized (PeerProcess.messageList) {
                                MessageList m = new MessageList();
                                m.setSocket(socket);
                                m.setMessage(not.not_interested);
                                PeerProcess.messageList.add(m);
                            }
                            //sendMessage(p.getSocket(), not.not_interested);
                            flag = 1;
                        } else {
                            RequestMsg req = new RequestMsg(getPiece);

                            synchronized (PeerProcess.messageList) {
                                MessageList m = new MessageList();
                                m.setSocket(socket);
                                m.setMessage(req.request);
                                PeerProcess.messageList.add(m);
                            }
                            //sendMessage(p.getSocket(), req.request);
                        }
                    } else {

                        field = myPeer.getBitfield();
                        getPiece = getPieceInfo(field, BitFieldMsg.myBitfield);

                        if (getPiece == 0) {
                            //send Not interested
                            if (flag == 0) {
                                NotInterestedMsg not = new NotInterestedMsg();

                                synchronized (PeerProcess.messageList) {
                                    MessageList m = new MessageList();
                                    m.setSocket(socket);
                                    m.setMessage(not.not_interested);
                                    PeerProcess.messageList.add(m);
                                }
                                //sendMessage(p.getSocket(), not.not_interested);
                            }
                        } else {
                            myPeer.setInterested(true);
                            flag = 0;

                            //send interested
                            InterestedMsg interested = new InterestedMsg();

                            synchronized (PeerProcess.messageList) {
                                MessageList m = new MessageList();
                                m.setSocket(socket);
                                m.setMessage(interested.interested);
                                PeerProcess.messageList.add(m);
                            }
							//sendMessage(p.getSocket(), interested.interested);

                            //send request
                            RequestMsg req = new RequestMsg(getPiece);
                            synchronized (PeerProcess.messageList) {
                                MessageList m = new MessageList();
                                m.setSocket(socket);
                                m.setMessage(req.request);
                                PeerProcess.messageList.add(m);
//								System.out.println();
//								
//								Iterator<byte[]> itr = PeerProcess.messageList.iterator();
//								while(itr.hasNext()) {
//									byte[] temp = (byte[]) itr.next(); 
//									for (int i = 0; i < temp.length; i++) {
//										System.out.print(temp[i]);
//									}
//									System.out.println();
//								}
                            }
                            //sendMessage(p.getSocket(), req.request);

                        }

                    }
                }

            }

//			if(Logger.fileCompleteFlag == false)
//				{
//					Logger.fileCompleteFlag = true;
//							
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						System.err.println(e);
//					}
//				
//					Logger.closeLogger();
//				}
//	
        }

        byte[] downLoadedCompleteFile = new byte[5];

        for (int i = 0; i < downLoadedCompleteFile.length - 1; i++) {
            downLoadedCompleteFile[i] = 0;
        }
        downLoadedCompleteFile[4] = 8;

        sendHasDownloadedCompleteFile(downLoadedCompleteFile);

        while (true) {
            boolean check = checkAllPeerFileDownloaded();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(e);
            }

            if (check == true && PeerProcess.messageList.isEmpty()) {
                break;
            }
        }

        if (LogFileAccess.isFileCompleteFlag == false) {
            LogFileAccess.isFileCompleteFlag = true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e);
            }

            LogFileAccess.closeLog();
        }

        System.exit(0);

    }

    boolean checkAllPeerFileDownloaded() {

        boolean flag1 = true;

        ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();

        while (it.hasNext()) {

            HasCompleteFile peer = (HasCompleteFile) it.next();
            if (peer.isHasDownLoadedCompleteFile() == false) {
                flag1 = false;
                break;
            }
        }

        return flag1;
    }

    void sendHasDownloadedCompleteFile(byte[] downLoadedCompleteFile) {

        ListIterator<HasCompleteFile> it = PeerProcess.hasDownloadedCompleteFile.listIterator();

        while (it.hasNext()) {

            HasCompleteFile peer = (HasCompleteFile) it.next();

            synchronized (PeerProcess.messageList) {
                MessageList m = new MessageList();
                m.setSocket(peer.getSocket());
                m.setMessage(downLoadedCompleteFile);
                PeerProcess.messageList.add(m);
            }
        }

    }

    boolean hasCompleteFile() {

        int flag2 = 1;

        byte[] field = BitFieldMsg.myBitfield;

        for (int i = 5; i < field.length - 1; i++) {
            if (field[i] != -1) {
                flag2 = 0;
                break;
            }
        }

        if (flag2 == 1) {

            int remaining = totalpieces % 8;
            int a = field[field.length - 1];
            String a1 = Integer.toBinaryString(a & 255 | 256).substring(1);
            char[] a2 = a1.toCharArray();
            int[] a3 = new int[8];

            for (int j = 0; j < a2.length; j++) {
                a3[j] = a2[j] - 48;
            }

            for (int j = 0; j < remaining; j++) {
                if (a3[j] == 0) {
                    flag2 = 0;
                    break;
                }
            }
        }

        if (flag2 == 1) {
            return true;
        } else {
            return false;
        }
    }

//	 void sendMessage(Socket socket, byte[] message) {
//		
//		try {
//			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//			synchronized (socket) {
//				out.writeObject(message);
//				//Thread.sleep(10);
//			}
//		} catch (IOException e) {
//			System.err.println(e);
//		} 
////		catch (InterruptedException e) {
////			System.err.println(e);
////		}
//	}
    int getPieceInfo(byte[] field, byte[] bitfield) {

        int[] temp = new int[totalpieces];
        int k = 0;
        int total_missing_pieces = 0;
        int remaining = totalpieces % 8;

//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		try {
//			String message = reader.readLine();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			Thread.sleep(1);
//		} catch (InterruptedException e) {
//			System.err.println(e);
//		}
        for (int i = 5; i < bitfield.length; i++) {

            int a = bitfield[i];
            int b = field[i];

            String a1 = Integer.toBinaryString(a & 255 | 256).substring(1);
            char[] a2 = a1.toCharArray();
            int[] a3 = new int[8];

            for (int j = 0; j < a2.length; j++) {
                a3[j] = a2[j] - 48;
            }

            String b1 = Integer.toBinaryString(b & 255 | 256).substring(1);
            char[] b2 = b1.toCharArray();
            int[] b3 = new int[8];

            for (int j = 0; j < b2.length; j++) {
                b3[j] = b2[j] - 48;
            }

//			for (int j = 0; j < a3.length; j++) {
//				System.out.print(a3[j]);
//				System.out.print(" ");
//			}
//			System.out.println();
//			for (int j = 0; j < b3.length; j++) {
//				System.out.print(b3[j]);
//				System.out.print(" ");
//			}
//			
//			System.out.println();
//			System.out.println();
            if (i < bitfield.length - 1) {

                for (int j = 0; j < b3.length; j++) {
                    if (a3[j] == 0 && b3[j] == 1) {
                        temp[k] = 0;
                        k++;
                        total_missing_pieces++;
                    }

                    if (a3[j] == 0 && b3[j] == 0) {
                        temp[k] = 1;
                        k++;
                    }

                    if (a3[j] == 1) {
                        temp[k] = 1;
                        k++;
                    }
                }
            } else {
                for (int j = 0; j < remaining; j++) {
                    if (a3[j] == 0 && b3[j] == 1) {
                        temp[k] = 0;
                        k++;
                        total_missing_pieces++;
                    }

                    if (a3[j] == 0 && b3[j] == 0) {
                        temp[k] = 1;
                        k++;
                    }

                    if (a3[j] == 1) {
                        temp[k] = 1;
                        k++;
                    }
                }
            }

        }

//		long time1 = System.nanoTime();
//		System.out.println(total_missing_pieces);
//		long time2 = System.nanoTime();
//		System.out.println("time = " + (time2 - time1));
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

//		for (int i = 0; i < temp.length; i++) {
//			System.out.print(temp[i]);
//			System.out.print(" ");
//		}
//		System.out.println();
        if (total_missing_pieces == 0) {
            return 0;
        }

        int[] selectFrom = new int[total_missing_pieces];

        int x = 0;
        for (int l = 0; l < temp.length; l++) {
            if (temp[l] == 0) {
                selectFrom[x] = l;
                x++;
            }
        }
//		System.out.println();
//		System.out.println("Index : ");
//		for (int i = 0; i < selectFrom.length; i++) {
//			System.out.print(selectFrom[i]);
//			System.out.print(" ");
//		}
        int index = select_random_piece(total_missing_pieces);
        int piece = selectFrom[index];

//		int x = 0;
//		int j = 0;
//
//		for (j = 0; j < temp.length; j++) {
//			if(temp[j] == 0) {
//				x++;
//			}
//			else
//				continue;
//			
//			if(x == piece)
//				break;
//		}
//		System.out.println("piece = " + piece);
        return (piece + 1);
    }

    int select_random_piece(int total_missing_pieces) {

        Random rand = new Random();
        int randomNum = rand.nextInt(total_missing_pieces);

        return randomNum;

//	    Random rand = new Random(); 
//	    Set<Integer> generated = new LinkedHashSet<Integer>();
//	    Integer randomNum = null;
//	    
//	    while (generated.size() <= 360)
//	    {
//	    	randomNum = rand.nextInt(360) + 1;
//	    	generated.add(randomNum);
//	    }
//	    
//	    System.out.println(randomNum.intValue());
//	    return randomNum.intValue();
    }

    int peer_ID;
    int myPeer_ID;
    int totalpieces;
    long fileSize;
    long pieceSize;
    boolean haveAllPieces;
    int flag = 0;
    Socket socket = null;
}
