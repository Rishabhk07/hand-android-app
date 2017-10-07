package me.rishabhkhanna.the_hand;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by rishabhkhanna on 08/10/17.
 */

public class ChatApi {
    public static Socket getSocket(){
        try{
            return IO.socket("http://192.168.43.164:9999/");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
