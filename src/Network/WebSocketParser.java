/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

/**
 *
 * @author mac
 */
public class WebSocketParser {

    public static int getBit(byte input, int position) {
        return (input >> position) & 1;
    }

}
