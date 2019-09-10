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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class register extends Fragment {
private FirebaseAuth auth;
EditText emailR,nameR,passR,confPassR;
Button registerB;
    public register() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    public boolean checkEmptyField(){

        if(TextUtils.isEmpty(nameR.getText().toString())){
            nameR.setError("Cannot be blank..!");
            nameR.requestFocus();
            return true;
        }
        else if(TextUtils.isEmpty(emailR.getText().toString())){
            emailR.setError("Cannot be blank..!");
            emailR.requestFocus();
            return true;
        }   else if(TextUtils.isEmpty(passR.getText().toString())){
            passR.setError("Cannot be blank..!");
            passR.requestFocus();
            return true;
        }   else if(TextUtils.isEmpty(confPassR.getText().toString())){
            confPassR.setError("Cannot be blank..!");
            confPassR.requestFocus();
            return true;
        }       else {
            String name=nameR.getText().toString();
            String email=emailR.getText().toString();
            String pass=passR.getText().toString();

            createUser(name,email,pass);

        }
        return false;
    }


    public void createUser(final String name, final String email, String pass){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();
                    FirebaseFirestore db=FirebaseFirestore.getInstance();

                    Map<String,Object> userMap=new HashMap<>();
                    userMap.put("Name",name);
                    userMap.put("Email",email);

                    db.collection("users").document(user.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity().getApplicationContext(),"Register Success!",Toast.LENGTH_LONG).show();
                        }
                    });

                }else
                {
                    System.out.println("From register: "+task.getException());
                    Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        FirebaseAuth.getInstance().signOut();
        NavController navController= Navigation.findNavController(getActivity(),R.id.host_fragment);
        navController.navigate(R.id.homescreen);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailR=view.findViewById(R.id.emailadd);
        nameR=view.findViewById(R.id.name);
        passR=view.findViewById(R.id.passwordR);
        confPassR=view.findViewById(R.id.confPass);

        registerB=view.findViewById(R.id.registerButton);
        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkEmptyField()){
                    if (passR.getText().toString().length()<6){
                        passR.setError("More than 6 characters required!");
                        passR.requestFocus();
                    }
                    else {
                        if (!passR.getText().toString().equals(confPassR.getText().toString())){
                            confPassR.setError("Passwords didn't match!");
                            confPassR.requestFocus();
                        }
                    }
                }
            }
        });
    }
// TODO: Rename method, update argument and hook method into UI event

}
