/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Receive;

import Core.FlareClient;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author mac
 */
public abstract class NetworkReceive {
    
    
    protected int receiveCode;
    protected FlareClient client;

    public int getID() {
        return receiveCode;
    }

    public int setID(int receiveCode) {
        return this.receiveCode = receiveCode;
    }

    public FlareClient getGameClient() {
        return client;
    }

    public FlareClient setGameClient(FlareClient client) {
        return this.client = client;
    }

    /**
     * Parse the request from the input stream.
     *
     * @param dataInput
     * @throws IOException
     */
    public abstract void parse(DataInputStream dataInput) throws IOException;

    /**
     * Interpret the information from the request.
     *
     * @throws Exception
     */
    public abstract void process() throws Exception;

    
}
