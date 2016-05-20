package WebSocket.Message;

/**
 * This is a class used to send text data over the websocket protocol
 * @author Brian Parra
 * 
 */
public class WebSocketTextMessage extends WebSocketMessage {

    public String text;

    /**
     * Sets the text received
     * @param _text the text to send
     */
    public void setText(String _text) {

        text = _text;

    }

    /**
     * Returns the current message text in string format
     * @return the string of text to send
     */
    public String getText() {

        return this.text;
    }

}
