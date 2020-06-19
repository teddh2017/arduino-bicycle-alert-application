package bike.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {

    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothAdapter mBTAdapter;
    private BluetoothService mBTService;


    private String mConnectedDeviceName;

    private Button btn_pairng;

    private Button blue, blue2, blue3;
    private Button yellow, yellow2, yellow3;
    private Button red, red2, red3;

    private byte blue_led;
    private byte yellow_led;
    private byte red_led;

    private int led_switch;

    private MediaPlayer yellow_player;
    private MediaPlayer red_player;
    private Vibrator vib;

    private SeekBar sb_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        setTitle("SABA");

        led_switch = 0;
        blue_led = 7;
        yellow_led = 4;
        red_led = 1;

        yellow_player = MediaPlayer.create(getApplicationContext(), R.raw.sound1);
        red_player = MediaPlayer.create(getApplicationContext(), R.raw.sound2);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBTAdapter == null) {
            //블루투스가 지원되지 않음을 알려주기 위한 방법 즉, 공지 toast로 하는 것
            Toast.makeText(getApplicationContext(), "블루투스가 지원되지 않습니다", Toast.LENGTH_LONG).show();
            finish();
        } else {
            mBTService = new BluetoothService(getApplicationContext(), mHandler);
        }

        sb_sound = (SeekBar) findViewById(R.id.sound);
        sb_sound.setProgress(100);
        sb_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (yellow_player.isPlaying()) {
                    yellow_player.setVolume(seekBar.getProgress() / 100.0f, seekBar.getProgress() / 100.0f);
                } else if (red_player.isPlaying()) {
                    red_player.setVolume(seekBar.getProgress() / 100.0f, seekBar.getProgress() / 100.0f);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_pairng = (Button) findViewById(R.id.btn_paring);
        btn_pairng.setOnClickListener(new View.OnClickListener() {
            //버튼 관련 작업을 위해서 하는 곳
            //블루투스의 연결을 하고 끊기 위한 작업을 하는 곳

            @Override
            public void onClick(View view) {
                Button btn = (Button) findViewById(view.getId());
                if (btn.getText().equals("페어링")) {
                    Intent intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_SELECT_DEVICE);
                }
                if (btn.getText().equals("연결끊기")) {
                    if (mBTService != null)
                        mBTService.stop();
                }
            }
        });

        blue = (Button) findViewById(R.id.blue);
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환  --- 기기 연결 안 되었을 때 예외 처리 .....
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 12;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해
            }
        });

        blue2 = (Button) findViewById(R.id.blue2);
        blue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환  --- 기기 연결 안 되었을 때 예외 처리 .....
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 11;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해
            }
        });

        blue3 = (Button) findViewById(R.id.blue3);
        blue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환  --- 기기 연결 안 되었을 때 예외 처리 .....
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 10;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해
            }
        });

        yellow = (Button) findViewById(R.id.yellow);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 22;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해
            }
        });

        yellow2 = (Button) findViewById(R.id.yellow2);
        yellow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 21;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해
            }
        });

        yellow3 = (Button) findViewById(R.id.yellow3);
        yellow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 20;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해
            }
        });

        red = (Button) findViewById(R.id.red);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 32;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해  안드로이드에서 아두이노로 데이터 송신 방법
            }
        });

        red2 = (Button) findViewById(R.id.red2);
        red2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 31;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해  안드로이드에서 아두이노로 데이터 송신 방법
            }
        });

        red3 = (Button) findViewById(R.id.red3);
        red3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTService.getState() == BluetoothService.STATE_NONE) {
                    // getState--블루투스 통신 상태 반환
                    return;
                }
                byte[] data = new byte[1];
                data[0] = 30;
                mBTService.write(data); // 연결한 것을 읽고 작동시키기 위해  안드로이드에서 아두이노로 데이터 송신 방법
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBTAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //BluetoothAdapter.ACTION_REQUEST_ENABLE라는 action을 Intent를 통해서 안드로이드 시스템에게 요청을 하면 된다.
            startActivityForResult(intent, REQUEST_ENABLE_BT);
            //사용자가 어떤 선택을 했느냐에 따라서 그에 맞는 어떤 행동을 해야할 필요가 있기때문에 startActivityForResult()를 사용했다.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:  //  블루투스 동작
                if (resultCode == Activity.RESULT_OK) {

                } else {
                    Toast.makeText(getApplicationContext(), "블루투스를 켜주십시오", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case REQUEST_SELECT_DEVICE: // 디바이스 선택
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                } else {
                    Toast.makeText(getApplicationContext(), "기기 선택이 취소되었습니다", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void connectDevice(Intent data) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        BluetoothDevice device = mBTAdapter.getRemoteDevice(address);
        mBTService.connect(device);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    //아두이노와의 통신 상태가 변경되었을 때 확인하는 곳
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            btn_pairng.setText("연결끊기");
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            btn_pairng.setText("연결중");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            btn_pairng.setText("페어링");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    break;

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if (readMessage.equals("1")) {
                        if (yellow_player.isPlaying()) {
                            yellow_player.stop();
                            yellow_player.release();
                            yellow_player = MediaPlayer.create(getApplicationContext(), R.raw.sound1);
                        }
                        if (red_player.isPlaying()) {
                            red_player.stop();
                            red_player.release();
                            red_player = MediaPlayer.create(getApplicationContext(), R.raw.sound2);
                        }
                        vib.cancel();
                    }
                    if (readMessage.equals("2")) {
                        if (red_player.isPlaying()) {
                            red_player.stop();
                            red_player.release();
                            red_player = MediaPlayer.create(getApplicationContext(), R.raw.sound1);
                        }
                        if (!yellow_player.isPlaying()) {
                            yellow_player.release();
                            yellow_player = MediaPlayer.create(getApplicationContext(), R.raw.sound2);
                            yellow_player.setVolume(sb_sound.getProgress() / 100.0f, sb_sound.getProgress() / 100.0f);
                            yellow_player.start();
                        }
                        if (vib.hasVibrator()) {
                            vib.vibrate(8500); //1초동안 진동이 울리게 한다는 의미
                        }
                    }
                    if (readMessage.equals("3")) {
                        if (yellow_player.isPlaying()) {
                            yellow_player.stop();
                            yellow_player.release();
                            yellow_player = MediaPlayer.create(getApplicationContext(), R.raw.sound2);
                        }
                        if (!red_player.isPlaying()) {
                            red_player.release();
                            red_player = MediaPlayer.create(getApplicationContext(), R.raw.sound1);
                            red_player.setVolume(sb_sound.getProgress() / 100.0f, sb_sound.getProgress() / 100.0f);
                            red_player.start();
                        }
                        if (vib.hasVibrator()) {
                            vib.vibrate(9000); //1초동안 진동이 울리게 한다는 의미
                        }
                    }
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (yellow_player.isPlaying()) {
            yellow_player.stop();
            yellow_player.release();
            yellow_player = MediaPlayer.create(getApplicationContext(), R.raw.sound1);
        }
        if (red_player.isPlaying()) {
            red_player.stop();
            red_player.release();
            red_player = MediaPlayer.create(getApplicationContext(), R.raw.sound2);
        }
        vib.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBTService != null) {
            mBTService.stop();
        }
    }
}


