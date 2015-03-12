package fileHandler;

import java.io.*;
import java.util.*;
/*Writing log for each peer at the working directory*/

public class LogFileAccess {

    public static void initializeLog(int peer_ID) {

        myPeerId = peer_ID;
        String fileName = "log_peer_" + myPeerId + ".log";
        file = new File(fileName);

        try {
            logFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void connectToPeer(int peer_ID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " makes a connection to Peer " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void connectedFromPeer(int peer_ID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " is connected from Peer " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /*not finish yet, can the preferred neighbours be set to exactly 2? preferredNeighborIDList is the list of peer IDs seperated by comma*/
    public static void changeOfPreferredNeighbors(String preferredNeighborIDList) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " has the preferred neighbors " + preferredNeighborIDList + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void changeOfOptimisticallyUnchokedNeighbor(int optimisticallyUnchokedNeighborID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " has the optimistically-unchoked neighbor " + optimisticallyUnchokedNeighborID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void unchoking(int peer_ID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " is unchoked by " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void choking(int peer_ID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " is choked by " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void receiveHave(int peer_ID, int pieceIndex) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " received a 'have' message from " + peer_ID + " for the piece " + pieceIndex + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void receiveInterested(int peer_ID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " received an 'interested' message from " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void receiveNotInterested(int peer_ID) {

        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " received a 'not interested' message from Peer " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void downloadPiece(int peer_ID, int pieceIndex) {

        nPieces++;
        try {
            String date = new Date().toString();
            String s = date + " : Peer " + myPeerId + " has downloaded the piece " + pieceIndex + " from Peer " + peer_ID + ".";
            logFile.append(s);
            logFile.newLine();
            s = "Now  the number of pieces it has is " + nPieces;
            logFile.append(s);
            logFile.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void completionOfDownload() {

        if (fFlag == true) {

            try {
                String date = new Date().toString();
                String s = date + " : Peer " + myPeerId + " has downloaded the complete file.";
                logFile.append(s);
                logFile.newLine();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public static void closeLog() {
        try {
            logFile.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    static int myPeerId;
    static File file;
    static BufferedWriter logFile;
    static int nPieces = 0;
    public static boolean fFlag = false;
    public static boolean isFileCompleteFlag = false;
    public static LinkedList<Integer> fWriteOperations = new LinkedList<Integer>();
}
