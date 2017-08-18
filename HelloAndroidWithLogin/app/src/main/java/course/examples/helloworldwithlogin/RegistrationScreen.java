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
    EditText newUName = null;
    EditText newUPass = null;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registrationscreen);

        final Button register = (Button) findViewById(R.id.register_user_button);


        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                enterClicked();

            }
        });
    }
    private void enterClicked(){
        Intent save = new Intent(RegistrationScreen.this, LoginScreen.class);
        newUName = (EditText) findViewById(R.id.editTextUsername);
        newUPass = (EditText) findViewById(R.id.editTextPassword);
        save.putExtra("username", newUName.getText().toString());
        save.putExtra("password", newUPass.getText().toString());
        Bundle b = new Bundle();
        setResult(RESULT_OK);
        startActivity(save);

    }

}

