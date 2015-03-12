package socket;

import java.io.*;
import java.net.*;

import peer.*;

public class MyMessageSender extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println(e);
            }

            if (!PeerProcess.messageList.isEmpty()) {

                synchronized (PeerProcess.messageList) {
                    MessageList m = PeerProcess.messageList.poll();
                    Socket socket = m.getSocket();
                    byte[] message = m.getMessage();
                    sendMessage(socket, message);
                }
            }
        }
    }

    public void sendMessage(Socket pSocket, byte[] messageReceive) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(pSocket.getOutputStream());
            synchronized (pSocket) {
                out.writeObject(messageReceive);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
