/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebSocket.Message;

/**
 *
 * @author mac
 */
public class WebSocketTextMessage extends WebSocketMessage {

    public String text;

    public void setText(String _text) {

        text = _text;

    }

    public String getText() {

        return this.text;
    }

}
