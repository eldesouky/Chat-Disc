/*
 * Copyright [2020] [Martin Osorio Bugueño]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package cl.ucn.disc.dsm.chatdisc.Fragments;

/**
 * @author Martin Osorio-Bugueño.
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cl.ucn.disc.dsm.chatdisc.Adapter.UserAdapter;
import cl.ucn.disc.dsm.chatdisc.Model.User;
import cl.ucn.disc.dsm.chatdisc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 *  A simple {@link Fragment} sublass
 */

public class UsersFragment extends Fragment {

  private RecyclerView recyclerView;

  private UserAdapter userAdapter;
  private List<User> mUsers;

  EditText search_users;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_users, container, false);

    //init recyclerview
    recyclerView = view.findViewById(R.id.recycler_view);
    //it's properties
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    //init user list
    mUsers = new ArrayList<>();

    //get all users
    readUsers();

    return view;


  }


  private void readUsers() {
    //get current user
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    //get path of database named "Users" containing user info
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
    //get all data from path
    reference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        mUsers.clear();

          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            User user = snapshot.getValue(User.class);
            assert user !=null;
            assert firebaseUser !=null;

            //get all users except currently signed user
            if (!user.getId().equals(firebaseUser.getUid())) {
              mUsers.add(user);
            }

          }

          //Adapter
          userAdapter = new UserAdapter(getContext(), mUsers, false);
          //Set adapter to recycler view
          recyclerView.setAdapter(userAdapter);
        }


      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });
  }
  }


