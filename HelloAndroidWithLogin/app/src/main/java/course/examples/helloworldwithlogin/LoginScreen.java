package course.examples.helloworldwithlogin;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginScreen extends Activity {
    String regUName = null;
    String regUPass = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        final EditText uname = (EditText) findViewById(R.id.username_edittext);
        final EditText passwd = (EditText) findViewById(R.id.password_edittext);

        final Button registerButton = (Button) findViewById(R.id.buttonRegister);
        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (checkPassword(uname.getText(), passwd.getText())) {

                    // Create an explicit Intent for starting the HelloAndroid
                    // Activity
                    Intent helloAndroidIntent = new Intent(LoginScreen.this,
                            HelloAndroid.class);

                    // Use the Intent to start the HelloAndroid Activity
                    startActivity(helloAndroidIntent);
                } else {
                    uname.setText("");
                    passwd.setText("");
                }
            }
        });
        registerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent registerUserIntent = new Intent(LoginScreen.this, RegistrationScreen.class);
                startActivityForResult(registerUserIntent, 100);
            }
        });
    }

    private boolean checkPassword(Editable uname, Editable passwd) {
        // Just pretending to extract text and check password
        if(regUPass != null && regUPass != null){
            if(uname.toString() == regUName && passwd.toString() == regUPass){
                return true;
            }else {
                return false;
            }
        }else {
            return new Random().nextBoolean();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK && requestCode == 100){
            regUName = data.getStringExtra("username");
            regUPass = data.getStringExtra("password");
        }
    }
}
