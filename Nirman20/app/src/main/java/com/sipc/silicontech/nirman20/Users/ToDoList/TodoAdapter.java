package com.sipc.silicontech.nirman20.Users.ToDoList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sipc.silicontech.nirman20.R;
import com.sipc.silicontech.nirman20.Users.SessionManagerParticipant;

import java.util.Calendar;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private final Context mContext;
    private final List<TodoModel> todoList;

    public TodoAdapter(Context context, List<TodoModel> todoModels) {
        mContext = context;
        todoList = todoModels;

    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_todo, parent, false);
        return new TodoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {

        TodoModel model = todoList.get(position);

        holder.tv_title.setText(model.getTitle());
        holder.tv_description.setText(model.getDescription());


        SessionManagerParticipant managerUser;
        managerUser = new SessionManagerParticipant(mContext.getApplicationContext());
        String phoneNumber = managerUser.getPhone();

        holder.btn_delete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?");

            builder.setPositiveButton("YES", (dialog, which) -> {

                String idUpdate = model.getId();
                FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("Todo").child(idUpdate)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().removeValue();
                                notifyDataSetChanged();
                                Toast.makeText(mContext, "Item Deleted..", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                dialog.dismiss();
            });

            builder.setNegativeButton("NO", (dialog, which) -> {

                // Do nothing
                dialog.dismiss();
            });

            AlertDialog alert = builder.create();
            alert.show();

        });

        holder.btn_edit.setOnClickListener(v -> {


            Calendar calendar = Calendar.getInstance();
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);

            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.todo_add_and_update);

            TextView tv_update = dialog.findViewById(R.id.tv_update);
            EditText et_title = dialog.findViewById(R.id.et_title);
            EditText et_desc = dialog.findViewById(R.id.et_description);
            Button btn_add = dialog.findViewById(R.id.bt_ok);
            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

            btn_cancel.setOnClickListener(v1 -> dialog.dismiss());


            btn_add.setText("Update");
            tv_update.setText("Update");


            et_title.setText(model.getTitle());
            et_desc.setText(model.getDescription());

            btn_add.setOnClickListener(v13 -> {
                String nTitle = et_title.getText().toString();
                String nDesc = et_desc.getText().toString();

                String idUpdate = model.getId();
                final DatabaseReference db;
                db = FirebaseDatabase.getInstance().getReference("Users").child(phoneNumber).child("Todo").child(String.valueOf(idUpdate));
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (et_title.getText().toString().trim().isEmpty() | et_desc.getText().toString().trim().isEmpty()) {
                            Toast.makeText(mContext, "Do not empty Title and Description", Toast.LENGTH_SHORT).show();
                        } else {

                            db.child("title").setValue(nTitle);
                            db.child("description").setValue(nDesc);

                            Toast.makeText(mContext, "Item Updated..", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.BottomDialog);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_description;
        Button btn_delete;
        LinearLayout btn_edit;

        public TodoViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);


            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
