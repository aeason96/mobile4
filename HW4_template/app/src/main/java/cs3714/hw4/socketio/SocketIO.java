package cs3714.hw4.socketio;

/**
 * Created by Andrey on 3/19/2017.
 */

import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
/**
 * Created by andrey on 12/19/16.
 */

// Singleton containing the SocketIO connection.
public class SocketIO extends Application {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    //this method is called from various Android components that want to use SocketIO
    public Socket getSocket() {
        return mSocket;
    }

}
