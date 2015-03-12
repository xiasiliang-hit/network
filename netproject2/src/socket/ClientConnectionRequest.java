package socket;

import fileHandler.PeerInfoFileReaderAcess;
import fileHandler.LogFileAccess;
import java.io.*;
import java.net.*;
import java.util.*;
import peer.*;
import msg.BitFieldMsg;

public class ClientConnectionRequest extends Thread {

    public ClientConnectionRequest(int peer_ID, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
        myPeerId = peer_ID;
        this.totalPieces = totalPieces;
        this.haveAllPieces = haveAllPieces;
        this.fSize = fileSize;
        this.pSize = pieceSize;
    }

    @Override
    public void run() {

        PeerInfoFileReaderAcess peerInfo = new PeerInfoFileReaderAcess(myPeerId);
        peerToConnect = peerInfo.getPeerInfo();

        ListIterator<String[]> it = peerToConnect.listIterator();

        /*
         while(it.hasNext()) {
         String[] value = it.next();
         hostName = value[1];
         port = Integer.parseInt(value[2]);
			
         }
         */
        while (it.hasNext()) {
            String[] value = it.next();
            host = value[1];
            port = Integer.parseInt(value[2]);

            try {
                System.out.println("try connection:" + host + ":" + port);
                Socket mySocket = new Socket(host, port);

                //Handshake
                PeerHandshake send = new PeerHandshake(myPeerId);
                sendHandShakeMsg(mySocket, send.handshakeByte);

                byte[] received = receiveHandShake(mySocket);
                byte[] temp = new byte[28];
                for (int i = 0; i < 28; i++) {
                    temp[i] = received[i];
                }

                String header = new String(temp);

                int j = 0;
                byte[] ID_temp = new byte[4];
                for (int i = 28; i < 32; i++) {
                    ID_temp[j] = received[i];
                    j++;
                }

                String s = new String(ID_temp);
                int ID = Integer.parseInt(s);

                if (header.equals("HELLO00000000000000000000000")) {

                    boolean flag = false;
                    ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();

                    while (iter.hasNext()) {

                        int num = iter.next().intValue();
                        if (num != myPeerId) {
                            if (num == ID) {
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (flag == true) {

                        Peer myPeer = new Peer();
                        myPeer.setMyPeerId(myPeerId);
                        myPeer.setSocket(mySocket);
                        myPeer.setPeerId(Integer.parseInt(value[0]));

                        //receive bitfield
                        byte[] field = receiveBitfieldMsg(mySocket);
                        myPeer.setBitfield(field);

                        //send bitfield
                        sendBitfieldMsg(mySocket);
                        myPeer.setInterested(false);

                        synchronized (PeerProcess.peers) {
                            PeerProcess.peers.add(myPeer);
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                System.err.println(e);
                            }
                        }

                        HasCompleteFile hasCompleteFile = new HasCompleteFile();
                        hasCompleteFile.setSocket(mySocket);
                        hasCompleteFile.setHasDownLoadedCompleteFile(false);

                        PeerProcess.hasDownloadedCompleteFile.add(hasCompleteFile);

                        System.out.println("Connection request sent to " + Integer.parseInt(value[0]));
                        System.out.println();
                        LogFileAccess.connectToPeer(Integer.parseInt(value[0]));

                        MyMessageSender messageSender = new MyMessageSender();
                        messageSender.start();

                        MyPieceRequest pieceReq = new MyPieceRequest(Integer.parseInt(value[0]), totalPieces, haveAllPieces, fSize, pSize);
                        pieceReq.start();

                        MyMessageReceiver messageReceiver = new MyMessageReceiver(mySocket, pSize);
                        messageReceiver.start();

                    } else {
                        System.out.println("Unexpected peer connection");
                    }
                }
            } 
            catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    void sendBitfieldMsg(Socket socket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(BitFieldMsg.myBitfield);

        } catch (IOException e) {
            System.err.println(e);
        }
    }

    byte[] receiveBitfieldMsg(Socket pSocket) {

        byte[] myBitfield = null;
        try {
            ObjectInputStream in = new ObjectInputStream(pSocket.getInputStream());
            myBitfield = (byte[]) in.readObject();
        } catch (Exception e) {
            System.err.println(e);
        } 

        return myBitfield;
    }

    void sendHandShakeMsg(Socket pSocket, byte[] handshakeMsg) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(pSocket.getOutputStream());
            out.writeObject(handshakeMsg);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    byte[] receiveHandShake(Socket socket) {
        byte[] handshakeByte = null;
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            handshakeByte = (byte[]) in.readObject();
        } catch (Exception e) {
            System.err.println(e);
        } 

        return handshakeByte;
    }
    
    String host;
    int port;
    int myPeerId;
    ArrayList<String[]> peerToConnect = new ArrayList<String[]>();
    int totalPieces;
    boolean haveAllPieces;
    long fSize;
    long pSize;
}
