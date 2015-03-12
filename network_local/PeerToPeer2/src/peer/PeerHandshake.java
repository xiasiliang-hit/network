package peer;

import java.io.*;
import java.util.*;

public class PeerHandshake {

    public PeerHandshake(int peer_ID) {
        this.peerId = peer_ID;
        String tempStr = headerMsg + zeroBits + Integer.toString(this.peerId);
        handshakeByte = tempStr.getBytes();
    }

    public byte[] handshakeByte = new byte[32];
    String headerMsg = "HELLO";
    String zeroBits = "00000000000000000000000";
    int peerId;
}
