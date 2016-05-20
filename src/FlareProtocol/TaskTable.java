/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlareProtocol;

import FlareProtocol.FlareOpCode;
import Core.FlareClient;
import FlareTask.OpenVideoTask;
import WebSocket.WebSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  Class for holding a hash of the flare tasks
 * @author Brian Parra
 */
public class TaskTable {
        
    
    public static final Map<Byte, Class> taskTable = initializeTable();

    /**
     * Sets up task table
     * @return finalized task table
     */
    private static Map<Byte, Class> initializeTable() {

        Map<Byte, Class> table = new HashMap<Byte, Class>();

        try {


            table.put(FlareOpCode.OPEN_VIDEO, OpenVideoTask.class);


        } catch (Exception e) {

            System.out.println("couldnt add " + e.getMessage());
        }

        return Collections.unmodifiableMap(table);

    }
    
    
}
