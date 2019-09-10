package com.example.authenticationfirebase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class authenticationSuccess extends Fragment {

TextView name;
Button logout,delete;
FirebaseFirestore db;
FirebaseUser user;

    public authenticationSuccess() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//if(getArguments()!=null)
                 user=getArguments().getParcelable("user");
                db=FirebaseFirestore.getInstance();
           }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authentication_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        name=view.findViewById(R.id.authName);
        logout=view.findViewById(R.id.logout);
        delete=view.findViewById(R.id.delete);


        //name.setText("user logged in");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                NavController navController= Navigation.findNavController(getActivity(),R.id.host_fragment);
                navController.navigate(R.id.homescreen);
            }
        });
        readFireStore();
    }

    public void readFireStore(){
        DocumentReference documentReference=db.collection("users").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snap=task.getResult();
                    if (snap.exists()){
                        System.out.println(snap.getData());
                        name.setText("Welcome "+snap.get("Name") );
                    }
                }
            }
        });


    }
}
