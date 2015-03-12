package peer;

import java.net.*;
import java.io.*;
import java.util.*;

public class Peer {

    public int getPeerId() {
        return peerId;
    }

    public void setPeerId(int peer_ID) {
        this.peerId = peer_ID;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public byte[] getBitfield() {
        return bitfield;
    }

    public void setBitfield(byte[] bitfield) {
        this.bitfield = bitfield;
    }

    public boolean isInterested() {
        return interested;
    }

    public void setInterested(boolean interested) {
        this.interested = interested;
    }

    public int getMyPeerId() {
        return myPeerId;
    }

    public void setMyPeerId(int myPeer_ID) {
        this.myPeerId = myPeer_ID;
    }
    int myPeerId;
    int peerId;
    Socket socket;
    byte[] bitfield;
    boolean interested;
}