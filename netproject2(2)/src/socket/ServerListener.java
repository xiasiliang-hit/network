package socket;

import java.io.*;
import java.util.*;
import java.net.*;
import msg.BitFieldMsg;
import fileHandler.LogFileAccess;
import peer.*;

public class ServerListener extends Thread {

    public ServerListener(int listenPort, int peerId, int totalPieces, boolean haveAllPieces, long fileSize, long pieceSize) {
        this.listenPort = listenPort;
        myPeerId = peerId;
        this.totalPieces = totalPieces;
        this.haveAllPieces = haveAllPieces;
        this.fSize = fileSize;
        this.pSize = pieceSize;
    }

    @Override
    public void run() {

        try {
            @SuppressWarnings("resource")
            ServerSocket listener = new ServerSocket(listenPort);

            while (true) {
                Socket socket = listener.accept();
                int peer_ID;

                //Handshake
                byte[] received = receiveHandShake(socket);

                PeerHandshake send = new PeerHandshake(myPeerId);
                sendHandShake(socket, send.handshakeByte);

                byte[] temp = new byte[28];
                for (int i = 0; i < 28; i++) {
                    temp[i] = received[i];
                }

                String header = new String(temp);
                int j = 0;
                byte[] ID_temp = new byte[4];
                int i = 28;
                for (i = 28; i < 32; i++) {
                    ID_temp[j] = received[i];
                    j++;
                }

                String str = new String(ID_temp);
                peer_ID = Integer.parseInt(str);

                //waiting for handshake msg
                if (header.equals("HELLO00000000000000000000000")) {

                    boolean flag = false;
                    ListIterator<Integer> iter = PeerProcess.allPeerID.listIterator();

                    while (iter.hasNext()) {

                        int num = iter.next().intValue();
                        if (num != myPeerId) {
                            continue;
                        } else {
                            break;
                        }
                    }

                    while (iter.hasNext()) {

                        int num = iter.next().intValue();
                        if (num == peer_ID) {
                            flag = true;
                        }
                    }

                    if (flag == true) {
                        Peer p = new Peer();
                        p.setMyPeerId(myPeerId);
                        p.setSocket(socket);
                        p.setPeerId(peer_ID);

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

                        System.out.println("connection request from host: " + peer_ID);
                        System.out.println();
                        LogFileAccess.connectedFromPeer(peer_ID);

//						
                        MyMessageSender messageSender = new MyMessageSender();
                        messageSender.start();

                        MyPieceRequest pieceReq = new MyPieceRequest(peer_ID, totalPieces, haveAllPieces, fSize, pSize);
                        pieceReq.start();

                        MyMessageReceiver messageReceiver = new MyMessageReceiver(socket, pSize);
                        messageReceiver.start();
                    } else {
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
            out.writeObject(BitFieldMsg.myBitfield);

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
    int myPeerId;
    int totalPieces;
    boolean haveAllPieces;
    long fSize;
    long pSize;
}
