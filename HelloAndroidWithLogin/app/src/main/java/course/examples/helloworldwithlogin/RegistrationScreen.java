package course.examples.helloworldwithlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by 10364498 on 8/18/2017.
 */
public class RegistrationScreen extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registrationscreen);

        final Button register = (Button) findViewById(R.id.register_user_button);
        final EditText newUName = (EditText) findViewById(R.id.editTextUsername);
        final EditText newUPass = (EditText) findViewById(R.id.editTextPassword);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent registered = new Intent(RegistrationScreen.this, LoginScreen.class);

                startActivity(registered);
            }
        });
    }

}

