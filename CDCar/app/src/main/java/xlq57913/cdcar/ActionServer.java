package xlq57913.cdcar;

import android.os.Handler;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.SocketException;

import xlq57913.protobuf.Command.command;

import static android.content.ContentValues.TAG;

public class ActionServer extends Thread{


    //IP地址
    private String mAddress;
    //端口
    private int port;
    private DatagramSocket socket;
    private command commandMsg;

    private Handler mHandler;

    public ActionServer(String address, int port,Handler handler) {
        this.mAddress = address;
        this.port = port;
        this.mHandler = handler;
    }


    /**
     * 设置
     */
    private void sendSocket(command commandMsg) {
        byte[] bytes = commandMsg.toByteArray();
        Log.i(TAG, "sendSocket: get msg:"+bytes.length);
        try {
            /*******************发送数据***********************/
            InetAddress address = InetAddress.getByName(mAddress);
            //1.构造数据包
            DatagramPacket packet = new DatagramPacket(bytes,
                    bytes.length, address, port);
            //2.创建数据报套接字并将其绑定到本地主机上的指定端口。
            this.socket = new DatagramSocket(port);
            //3.从此套接字发送数据报包。
            Log.i(TAG, "sendSocket: socket == null? : "+(socket == null));
            if(socket!=null) {
                socket.send(packet);
                Log.i(TAG, "sendSocket: finish sending msg");
                socket.close();
                Log.i(TAG, "sendSocket: close socket");

                sendMsg(0,"done");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            Log.i(TAG, "sendSocket: error when sending command:"+e.toString()+": "+e.getMessage());
            e.printStackTrace();
        }finally {
            if(this.socket != null)this.socket.close();
        }
    }

    public void updateMsg(command cMsg){
        this.commandMsg = cMsg;
    }

    private void sendCommand(){
        sendSocket(this.commandMsg);
    }
    @Override
    public void run() {
        super.run();
        sendCommand();
    }

    private void sendMsg(int what, Object object) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

}