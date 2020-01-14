package xlq57913.cdcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;

import android.graphics.Bitmap;

import android.annotation.SuppressLint;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inuker.bluetooth.library.utils.ByteUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xlq57913.protobuf.Command.command;
import xlq57913.protobuf.Command.armCommand;
import xlq57913.protobuf.Command.carCommand;
import xlq57913.protobuf.Command.cameraCommand;
import xlq57913.protobuf.Command.lightCommand;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageViewMain;
    private LinearLayout mMainLayout;
    private command.Builder commandMsg;

    private float carVelocity;
    private float carDegree;
    private boolean armUp;
    private boolean armDown;
    private float camUp;
    private float camRight;

    private MyOnCamTouch mOnTouch;
    private SeekBar mSeekBar;
    private ImageButton mLightButton;
    private boolean isOn = false;

    private final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBtAdapter;
    private BleService mBleService;
    private BroadcastReceiver mBleReceiver;
    private List<BluetoothDevice> mBluetoothDeviceList;
    private ActionServer mActionServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate", "onCreate: start init");

        verifyStoragePermissions(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initData();
        //initBle();
        //btAddressReceiverThread mbtAddressReceiverThread = new btAddressReceiverThread(mbtAddressHandler,this);
        //mbtAddressReceiverThread.start();


        carVelocity = 0;
        carVelocity = 0;
        armUp = false;
        armDown = false;
        camUp = 0;
        camRight = 0;

        mImageViewMain = findViewById(R.id.imageView_main);
        mImageViewMain.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mMainLayout = findViewById(R.id.whole_layout);
        mSeekBar = findViewById(R.id.mSeekBar);
        mLightButton = findViewById(R.id.light_button);


        mOnTouch = new MyOnCamTouch(mImageViewHandler);

        GraphicClientThread mGraphicThread = new GraphicClientThread(mGraphicHandler, mImageViewMain);
        Log.i("MainActivity", "onCreate: try to run graphicThread!");
        mGraphicThread.start();

        final JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onMove(int angle, int strength) {
                carDegree = angle;
                carVelocity = strength;
                Log.i("main", "onMove: update angle and velocity");
                makeAndSend();
            }
        });

        mLightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isOn){
                    mLightButton.setImageResource(R.drawable.light_off);
                    isOn = false;
                }else{
                    mLightButton.setImageResource(R.drawable.light_on);
                    isOn = true;
                }
                makeAndSend();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<100){
                    armDown=true;
                    armUp=false;
                }else if(progress>200){
                    armUp=true;
                    armDown=false;
                }else{
                    armDown=false;
                    armUp=false;
                }
                makeAndSend();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "开始滑动！");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("------------", "停止滑动！");
            }
        });

        mImageViewMain.setOnTouchListener(mOnTouch);
    }

    private Handler mImageViewHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    camUp = mOnTouch.getCamUp();
                    camRight = mOnTouch.getCamRight();
                    makeAndSend();
                    camUp=0;
                    camRight=0;
                }
            }
        }
    };

    private Handler mGraphicHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {

                    String content = msg.obj.toString();

                    try {
                        //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(new File(content))));
                        //mImageViewMain.setImageBitmap(bitmap);
                        //mImageViewMain.setBackground(new BitmapDrawable(getResources(),bitmap));
                        //mMainLayout.setBackground(new BitmapDrawable(getResources(),bitmap));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //Log.i("main", "handleMessage: get image:"+content.getHeight()+"x"+content.getWidth());
                    //mImageViewMain.setImageURI(Uri.fromFile(new File(content)));




                    break;
                }
            }
        }
    };

    private Handler mbtAddressHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    String ip = (String) msg.obj;

                    break;
                }
            }
        }
    };

    private Handler mActionHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    //Log.i("main", "handleMessage: "+msg.toString());

                    break;
                }
            }
        }
    };

    private command makeCommand(float carV,float carD,boolean armU,boolean armD,float camU,float camR){

        armCommand.Builder armMsg = armCommand.newBuilder();
        cameraCommand.Builder cameraMsg = cameraCommand.newBuilder();
        carCommand.Builder carMsg = carCommand.newBuilder();
        lightCommand.Builder lightMsg = lightCommand.newBuilder();

        armMsg.setGoDown(armD);
        armMsg.setGoUp(armU);
        cameraMsg.setRight(camR);
        cameraMsg.setUp(camU);
        carMsg.setVelocity(carV);
        carMsg.setDegree(carD);
        lightMsg.setOn(isOn);

        commandMsg = command.newBuilder();
        commandMsg.setArm(armMsg.build());
        commandMsg.setCamera(cameraMsg.build());
        commandMsg.setCar(carMsg.build());
        commandMsg.setLight(lightMsg.build());
        return commandMsg.build();
    }

    private void makeAndSend(){
        mActionServer = new ActionServer("192.168.43.18",20005,mActionHandler);
        mActionServer.updateMsg(makeCommand(carVelocity,carDegree,armUp,armDown,camUp,camRight));
        mActionServer.start();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"};

    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
