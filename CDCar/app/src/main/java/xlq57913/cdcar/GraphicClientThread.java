package xlq57913.cdcar;

import xlq57913.protobuf.MessageImage;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.SocketException;
import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import static android.content.ContentValues.TAG;

public class GraphicClientThread extends Thread {

    //IP地址
    private String mAddress;
    //端口
    private int port;
    //发送内容
    private String msg;
    private Handler mHandler;

    //private DatagramSocket socket;
    private MessageImage.message_image myImage;
    Bitmap myBitmap;

    private Socket socket;
    private InetAddress group;
    private ImageView mImageView;
    OutputStream os;
    InputStream in;

    //private MessageImage myImage;

    public GraphicClientThread(Handler handler, ImageView imageView) {
        this.mHandler = handler;
        this.mAddress = "192.168.43.18";
        this.port = 23223;
        this.mImageView=imageView;
        Log.i("GraphicClientThread", "GraphicClientThread:compete init! ");
    }



    @Override
    public void run() {
        super.run();
        while (true) {

            try{
                getImage();
                sleep(20);
            }
            catch (InterruptedException e){
                Log.i(TAG, "run: sleep is interrupted");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置
     */
    private void setup() {
        Log.i("GraphicClientThread", "setup: do setup");
        try {
            this.socket = new Socket(mAddress,port);
            if (socket.isConnected()) {
                Log.i(TAG, "setup: connect to Server success");
            }
            socket.setSoTimeout(8000);
            in=this.socket.getInputStream();
            os=this.socket.getOutputStream();



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
        Log.i("Error", "getImage: something going wrong:"+e.toString()+": "+e.getMessage());
    }
    }

    /**
     * 接受图片
     */

    private void getImage()throws IOException{
        setup();
        //Log.i("Socket", "getImage: socket = null? : "+(this.socket == null));
        try {

            byte[] sendData = "get length".getBytes(Charset.forName("UTF-8"));
            os.write(sendData, 0, sendData.length);
            os.flush();
            //Log.i(TAG, "getImage: finish sending msg");


            byte[] lenMsg = new byte[5];
            if(this.socket != null) {
                int len = 0;
                while (len < 1) {
                    len = in.read(lenMsg,0,5 );
                }
            }

            int dataLen = Integer.valueOf(new String(lenMsg,"UTF-8"));
            //Log.i(TAG, "getImage: dataLen = "+dataLen);

            sendData = "get image".getBytes(Charset.forName("UTF-8"));
            os.write(sendData, 0, sendData.length);
            os.flush();




            final byte[] bytes = new byte[dataLen];
            if(this.socket != null) {
                int len = 0;
                int leftLen = dataLen;
                while(leftLen>0) {
                    if(leftLen>2400) {
                        len = in.read(bytes, dataLen - leftLen, 2400);
                        leftLen -= len;
                    }
                    else{
                        len = in.read(bytes, dataLen - leftLen, leftLen);
                        leftLen-=len;
                    }

                    os.write(sendData, 0, sendData.length);
                    os.flush();
                }
                //Log.i(TAG, "getImage: get Image: "+(dataLen-leftLen));

                myImage = MessageImage.message_image.parseFrom(bytes);

                //byte[] tmp = Base64.decode(bytes,Base64.DEFAULT);

                myBitmap = BitmapFactory.decodeByteArray(myImage.getImageData().toByteArray(), 0,myImage.getImageData().toByteArray().length);
                //myBitmap = BitmapFactory.decodeByteArray(Base64.decode(bytes,Base64.DEFAULT), 0, 640);
                //Log.i(TAG, "getImage: myBitmap==null? : "+ (myBitmap==null));
                mImageView.setImageBitmap(myBitmap);
                //saveBitmap(myBitmap);
                //Log.i(TAG, "getImage: image shape:"+myImage.getHeight()+"x"+myImage.getWidth());

                sendMsg(0, sdCardDir+"tmp.png");


            }
            this.socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            Log.i("Error", "getImage: something going wrong: "+e.toString()+": "+e.getMessage());
        }
        finally {
            if(in!=null)in.close();
            if(os!=null)os.close();
            if(socket!=null)socket.close();
        }
    }


    /**
     * 发送消息
     */
    private void sendMsg(int what, Object object) {
        Log.i(TAG, "sendMsg: start sending image");
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

    // 指纹图片存放路径
    public String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CDCarImages/";

    private boolean saveBitmap(Bitmap bitmap) {
        try {
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                Log.i(TAG, "saveBitmap: try to make dir:"+dirFile.mkdirs());
            }
            File file = new File(sdCardDir, "tmp.png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
