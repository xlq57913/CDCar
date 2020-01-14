package xlq57913.cdcar;

import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.os.Handler;

import static android.content.ContentValues.TAG;

public class MyOnCamTouch implements View.OnTouchListener {
    private float camUp;
    private float camRight;
    private int[] mImageViewCenter;
    private Handler mHandler;

    public MyOnCamTouch(Handler handler){
        mHandler = handler;
        mImageViewCenter = new int[2];
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.getLocationOnScreen(mImageViewCenter);
        mImageViewCenter[0]+=v.getWidth()/2;
        mImageViewCenter[1]+=(v.getHeight()/2-300);
        //Log.i(TAG, "onTouch: mImageViewCenter: "+mImageViewCenter[0]+","+mImageViewCenter[1]);
        //Log.i(TAG, "onTouch: onTouch: "+event.getX()+","+event.getY());
            switch (event.getAction()){
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_DOWN:
                    if (mImageViewCenter[0]>event.getX()){
                        camRight=-1;
                    }else if(mImageViewCenter[0]<event.getX()){
                        camRight=1;
                    }else{
                        camRight=0;
                    }
                    if(mImageViewCenter[1]>event.getY()){
                        camUp=1;
                    }else if(mImageViewCenter[1]<event.getY()){
                        camUp=-1;
                    }else{
                        camUp=0;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    camRight=0;
                    camUp=0;
                    break;
                default:
                    break;

            }
            sendMsg(0,1);
            return false;
    }

    private void sendMsg(int what, Object object) {
        Log.i(TAG, "sendMsg: start sending image");
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }
    public float getCamUp(){return camUp;}
    public float getCamRight(){return camRight;}
}
