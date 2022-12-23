package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//create object of database reference class to access firebase realtime database
public class Register extends AppCompatActivity {

    public FirebaseAuth mAuth;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://login-activity-d6b7e-default-rtdb.asia-southeast1.firebasedatabase.app");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullname = findViewById(R.id.fullname);
        final EditText phone = findViewById(R.id.phone);
        final EditText email = findViewById(R.id.emailid);
        final EditText password = findViewById(R.id.textpassword);
        final EditText conpassword = findViewById(R.id.confirmpassword);

        final Button registerbutton = findViewById(R.id.registerbutton);
        final TextView signin = findViewById(R.id.signinbuton);


        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get data from edittext of register activity
                final String fullnameTxt = fullname.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conpasswordTxt = conpassword.getText().toString();

                //checking if user filled all the data before sending it to firebase
                if (fullnameTxt.isEmpty() || phoneTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || conpasswordTxt.isEmpty()) {
                    Toast.makeText(Register.this, "Please Fill the above details", Toast.LENGTH_SHORT).show();
                }
                //checking whether passwords are matching
                else if (!passwordTxt.equals(conpasswordTxt)) {
                    Toast.makeText(Register.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //checking if phone not registered
                            if (snapshot.hasChild(phoneTxt)) {
                                Toast.makeText(Register.this, "Phone is already Registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                //phone numbers ko as a unique key use kr davy so that saare details phone number ke under aaye
                                databaseReference.child("users").child(phoneTxt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("users").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                Toast.makeText(Register.this, "User Registered Successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}