package com.example.chatup;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentRegister extends Fragment {

    private FirebaseAuth mAuth;

    Button  buttonSignUp;
    EditText etEmail, etUsername, etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();

        etEmail = view.findViewById(R.id.etEmail);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);

        buttonSignUp = view.findViewById(R.id.btnSignUp);


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


        return view;
    }

    private void registerUser() {

        String email = etEmail.getText().toString().trim();
        String userName = etUsername.toString().trim();
        String password = etPassword.getText().toString().trim();

        // Checks if boxes are checked and a valid email input.
        if(email.isEmpty()){
            etEmail.setError("Email is required!");
            etEmail.requestFocus();
        }
        if(password.isEmpty()){
            etEmail.setError("Password is required!");
            etEmail.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter valid email-");
            etEmail.requestFocus();
        }

        // Creates Username/password in Firebase Authenticate and saves it to DB.

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(email, userName);

                            FirebaseDatabase.getInstance("https://chat-up-663ab-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getActivity(), "User has been registered!", Toast.LENGTH_LONG).show();
                                        Log.d("This", String.valueOf(user));
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Failed to register!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getActivity(), "Failed to register!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
