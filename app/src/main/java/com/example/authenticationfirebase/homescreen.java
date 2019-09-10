package com.example.authenticationfirebase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class homescreen extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
EditText email,password;
Button login;
TextView Register;

private FirebaseAuth auth;
FirebaseUser user;
    public homescreen() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    auth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homescreen, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email=view.findViewById(R.id.email);
        password=view.findViewById(R.id.password);
     login=view.findViewById(R.id.login);
     Register=view.findViewById(R.id.register);


     login.setOnClickListener(this);
     Register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.login){
            if (TextUtils.isEmpty(email.getText().toString())){
                email.setError("Email cannot be blank");
                email.requestFocus();
            }else if(TextUtils.isEmpty(password.getText().toString())){
                password.setError("password cannot be blank");
                password.requestFocus();
            }else {
                String userEmail=email.getText().toString();
                String userPass=password.getText().toString();
                loginUser(userEmail,userPass);
            }

        }else if(id==R.id.register){
            NavController navController= Navigation.findNavController(getActivity(),R.id.host_fragment);
            navController.navigate(R.id.registerfragment);
        }
    }

    public void loginUser(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    user=auth.getCurrentUser();
                    Toast.makeText(getActivity().getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();

                updateUI(user);
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user=auth.getCurrentUser();
        if (user!=null){
            updateUI(user);
            Toast.makeText(getActivity().getApplicationContext(),"User Logged in!",Toast.LENGTH_LONG).show();
        }
    }

    public void updateUI(FirebaseUser user){
        NavController navController=Navigation.findNavController(getActivity(),R.id.host_fragment);
        
        Bundle b =new Bundle();
        b.putParcelable("user",user);
        navController.navigate(R.id.authenticationSuccess);
    }
}
