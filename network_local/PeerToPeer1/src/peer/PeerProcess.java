package peer;

import fileHandler.*;
import socket.*;
import java.io.*;
import java.util.*;

import msg.*;

public class PeerProcess {

    public static void main(String[] args) {

        System.out.println("hello world in main");
        PeerProcess self = new PeerProcess();
        //String my_pid = "1002";
        String my_pid = args[0];

        CommonCfgFileReader common = new CommonCfgFileReader();
        common.readCfgFile();

        fName = common.getFName();
        self.fSize = common.getFSize();
        self.pSize = common.getPSize();
        self.nPreferredNeighbors = common.getNPreferredNeighbors();
        self.unchokeInterval = common.getUnchokeInterval();
        self.optimisticUnchokeInterval = common.getOptimisticUnchokeInterval();
        self.totalPiece = (int) Math.ceil((double) self.fSize / self.pSize);

        //read peer info file
        System.out.println("my peer id: " + my_pid + "\n");
        PeerInfoFileReaderAcess peerInfo = new PeerInfoFileReaderAcess(Integer.parseInt(my_pid));

        peerInfo.readFile();
        allPeerID = peerInfo.getAllPeerID();

        self.peerId = peerInfo.getPeerId();
        self.port = peerInfo.getPort();
        self.completeFileFlag = peerInfo.isCompleteFileFlag();
        BitFieldMsg.setBitfield(self.completeFileFlag, self.totalPiece);

        LogFileAccess.initializeLog(self.peerId);

        if (self.completeFileFlag == false) {
            map = new HashMap<Integer, PieceMsg>();

            ServerListener peerListener = new ServerListener(self.port, self.peerId, self.totalPiece, self.completeFileFlag, self.fSize, self.pSize);
            peerListener.start();

            ClientConnectionRequest connect = new ClientConnectionRequest(self.peerId, self.totalPiece, self.completeFileFlag, self.fSize, self.pSize);
            connect.start();
        } 
        
        else if (self.completeFileFlag == true) {
            MyFileReaderAccess reader = new MyFileReaderAccess(self.peerId, self.pSize, fName);
            map = reader.readFile();

            // have complete file, do not init outgoing connection, only work as server
            ServerListener peerListener = new ServerListener(self.port, self.peerId, self.totalPiece, self.completeFileFlag, self.fSize, self.pSize);
            peerListener.start();
        }
    }

    int nPreferredNeighbors;
    int unchokeInterval;
    int optimisticUnchokeInterval;
    public static String fName;
    long fSize;
    long pSize;
    int totalPiece;
    int peerId;
    int port;
    boolean completeFileFlag;

    public static HashMap<Integer, PieceMsg> map;
    public static ArrayList<Peer> peers = new ArrayList<Peer>();
    public static ArrayList<Integer> allPeerID;
    public static LinkedList<MessageList> messageList = new LinkedList<MessageList>();
    public static ArrayList<HasCompleteFile> hasDownloadedCompleteFile = new ArrayList<HasCompleteFile>();
}
