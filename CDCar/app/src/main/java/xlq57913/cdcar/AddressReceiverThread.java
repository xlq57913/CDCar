package xlq57913.cdcar;

import android.os.Handler;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.content.Context;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.SocketException;
import java.net.MulticastSocket;

import xlq57913.protobuf.MessageImage;

public class AddressReceiverThread extends Thread {


    private String mAddress;

    private int port;

    private Handler mHandler;
    private Context mContext;

    private MulticastSocket socket;
    private InetAddress group;

    public AddressReceiverThread(Handler handler,Context mContext){
        this.mHandler = handler;
        this.mAddress = "224.5.23.2";
        this.port = 23333;
        this.mContext = mContext;
        Log.i("AddressReceiver", "AddressReceiver:compete init! ");
    }

    @Override
    public void run() {
        super.run();
        getAddress();
    }

    public void getAddress(){
        while (true){
            try {
                WifiManager wifiManager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);

                MulticastLock multicastLock = wifiManager.createMulticastLock("multicast.test");
                multicastLock.acquire();

                this.group = InetAddress.getByName(this.mAddress);
                Log.i("AddressReceiver", "setup: init group");
                this.socket = new  MulticastSocket(this.port);
                Log.i("Socket", "setup: socket = null? : "+(this.socket == null));
                Log.i("AddressReceiver", "setup: init socket");
                this.socket.joinGroup(this.group);
                Log.i("AddressReceiver", "setup: join group");


                byte[] bytes = new byte[512];
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                this.socket.setSoTimeout(5000);
                this.socket.receive(packet);
                if(packet.getLength()<1){
                    multicastLock.release();
                    this.socket.leaveGroup(this.group);
                    this.socket.close();
                    continue;
                }
                String ip = packet.getAddress().getHostAddress();

                Log.i("AddressReceiver", "getAddress: get target ip: " + ip);


                multicastLock.release();
                this.socket.leaveGroup(this.group);
                this.socket.close();

                sendMsg(0,ip);
                return;
            }catch (Exception e){
                Log.i("Error", "AddressReceiver: something going wrong:"+e.toString()+": "+e.getCause());
            }
        }
    }
    private void sendMsg(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }
}
