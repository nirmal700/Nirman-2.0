package com.sipc.silicontech.nirman20.Users.ToDoList;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.Evaluators.EvaluatorSignIn;
import com.sipc.silicontech.nirman20.Evaluators.LineFollowerEvaluation;
import com.sipc.silicontech.nirman20.Evaluators.RoboRaceEvaluation;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;
import com.sipc.silicontech.nirman20.Users.UserDashBoard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserToDoList extends AppCompatActivity {

    SessionManagerParticipant managerUser;

    FloatingActionButton btn_add;
    ImageView btn_back;

    private RecyclerView recyclerView;

    private ProgressDialog progressDialog;
    private DatabaseReference todoDb;
    private List<TodoModel> todoModels;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_to_do_list);

        btn_back = findViewById(R.id.btn_backToCd);
        recyclerView = findViewById(R.id.toDoListView);
        btn_add = findViewById(R.id.btn_add);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        todoModels = new ArrayList<>();

        managerUser = new SessionManagerParticipant(getApplicationContext());
        String phoneNumber = managerUser.getPhone();

        if (!isConnected(UserToDoList.this)) {
            showCustomDialog();
        }

        todoDb = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("Todo");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (dy > 0) {
                    btn_add.hide();
                } else {
                    btn_add.show();
                }

                super.onScrolled(recyclerView, dx, dy);

            }
        });

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(UserToDoList.this, UserDashBoard.class));
            finish();
        });

        //Initialize ProgressDialog
        loadProgressDialog();

        list(); // Load all data

        btn_add.setOnClickListener(v -> {


            final Dialog dialog = new Dialog(UserToDoList.this);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.todo_add_and_update);

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.BottomDialog);
            dialog.getWindow().setGravity(Gravity.BOTTOM);

            EditText et_title = dialog.findViewById(R.id.et_title);
            EditText et_desc = dialog.findViewById(R.id.et_description);
            Button btn_new = dialog.findViewById(R.id.bt_ok);

            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(v1 -> dialog.dismiss());




            btn_new.setOnClickListener(v12 -> {

                if (et_title.getText().toString().trim().isEmpty() | et_desc.getText().toString().trim().isEmpty()) {
                    Toast.makeText(UserToDoList.this, "Do not empty Title and Description", Toast.LENGTH_SHORT).show();
                }  else {



                    String nTitle = et_title.getText().toString();
                    String nDesc = et_desc.getText().toString();

                    String id = todoDb.push().getKey();
                    TodoModel model = new TodoModel(id, nTitle, nDesc);

                    if (id != null) {
                        todoDb.child(id).setValue(model);
                    }

                    dialog.dismiss();
                    Toast.makeText(UserToDoList.this, "New item listed", Toast.LENGTH_SHORT).show();
                    loadProgressDialog();
                    list();

                }

            });

        });


    }

    //-----------------------Progress Dialog-------------------
    private void loadProgressDialog() {

        //Initialize ProgressDialog
        progressDialog = new ProgressDialog(UserToDoList.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void list() {

        todoDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                todoModels.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    TodoModel model = postSnapshot.getValue(TodoModel.class);
                    todoModels.add(0, model);
                }

                adapter = new TodoAdapter(UserToDoList.this, todoModels);
                recyclerView.setAdapter(adapter);
                recyclerView.smoothScrollToPosition(0);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserToDoList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomDialog() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserToDoList.this);
        builder.setMessage("Please connect to the internet")
                //.setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))).setNegativeButton("Cancel", (dialog, which) -> {
                    startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
                    finish();
                });
        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //--------------- Check Internet Is Connected -----------
    private boolean isConnected(UserToDoList userLogin) {

        ConnectivityManager connectivityManager = (ConnectivityManager) userLogin.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo bluetoothConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected() || (bluetoothConn != null && bluetoothConn.isConnected())); // if true ,  else false

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), UserDashBoard.class));
        super.onBackPressed();
    }

}