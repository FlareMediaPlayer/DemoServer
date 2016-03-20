/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author mac
 */
public class WebSocket extends Socket {
    
    private String webSocketKey;
    private String GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private String socketResponseKey;


    public WebSocket() throws IOException {

        super();

    }
    
    //For now, later make a handshake protocol
    public void handshake ()throws IOException{
        
        InputStream stream = getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        try {
            // Redo this, save all header data for later use
            String line = in.readLine();

            while (line != null && line.trim().length() > 0) {

                StringTokenizer st = new StringTokenizer(line);
                String args = st.nextToken();

                if (args.equals("Sec-WebSocket-Key:")) {
                    webSocketKey = st.nextToken().trim();
                }

                line = in.readLine();
            }

            socketResponseKey = Base64.encodeBase64String(DigestUtils.sha1(webSocketKey + GUID));

        } catch (IOException e) {
            System.out.println("Error reading body");

        }

        //Now We Respond!
        BufferedOutputStream out = new BufferedOutputStream(getOutputStream());
        PrintWriter writer = new PrintWriter(out, true);  // char output to the client

        writer.println("HTTP/1.1 101 Switching Protocols");
        writer.println("Upgrade: websocket");
        writer.println("Connection: Upgrade");
        writer.println("Sec-WebSocket-Accept: " + socketResponseKey);

        writer.println("");
    }

}
