package peer;

import java.net.Socket;
import java.io.*;
import java.util.*;

public class HasCompleteFile {

	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isHasDownLoadedCompleteFile() {
		return hasDownLoadedCompleteFile;
	}
	
	public void setHasDownLoadedCompleteFile(boolean hasDownLoadedCompleteFile) {
		this.hasDownLoadedCompleteFile = hasDownLoadedCompleteFile;
	}
        
        Socket socket;
	boolean hasDownLoadedCompleteFile;
}
