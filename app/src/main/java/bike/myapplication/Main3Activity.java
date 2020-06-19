package bike.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Main3Activity extends AppCompatActivity {

    EditText id, password;
    Button loginbutton1;
    TextView registerbutton;

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView userEmailTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main3);
        setTitle("SABA");


        id = (EditText)findViewById(R.id.id);
        password = (EditText)findViewById(R.id.password);
        loginbutton1 =(Button)findViewById(R.id.loginbutton1);
        registerbutton = (TextView)findViewById(R.id.registerbutton);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        //userEmailTv = (TextView) findViewById(R.id.userEmailTv);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();


        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Main3Activity.this,Main2Activity.class);
                startActivityForResult(registerIntent,1000);
                // 버튼을 클릭해서 다음 화면이 실행할 수 있도록 설정한다.
            }
        });

        loginbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Main3Activity.this,Main2Activity.class);
                startActivityForResult(loginIntent,1000);
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("getAccessToken()", String.valueOf(loginResult.getAccessToken()));
                        Log.d("getId()", String.valueOf(Profile.getCurrentProfile().getId()));
                        Log.d("getName()", String.valueOf(Profile.getCurrentProfile().getName())); // 이름
                        Log.d("getProfilePictureUri", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));//프로필 사진
                        getUserEmail(loginResult);

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }
    public void getUserEmail(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = response.getJSONObject().getString("email");
                            Log.d("email", email);
                            userEmailTv.setText(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}








