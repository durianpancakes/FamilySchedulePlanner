package com.durianpancakes.familyscheduleplanner;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddGroupDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddGroupDialog extends DialogFragment implements View.OnClickListener {

    private EditText groupTitle;

    private AddGroupDialogListener mListener;

    public interface AddGroupDialogListener {
        void onAddGroupPressed(String groupTitle);
    }

    public AddGroupDialog() {
        // Required empty public constructor
    }

    public void setAddGroupDialogListener(AddGroupDialogListener listener) {
        this.mListener = listener;
    }

    public static AddGroupDialog newInstance() {
        AddGroupDialog fragment = new AddGroupDialog();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_group_dialog, container, false);
        groupTitle = view.findViewById(R.id.group_title);
        ImageButton close = view.findViewById(R.id.group_add_cancel);
        TextView save = view.findViewById(R.id.group_done_button);

        close.setOnClickListener(this);
        save.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.group_done_button) {
            String groupTitleText = groupTitle.getText().toString();

            assert mListener != null : "Must implement listener in AddGroupDialog!";
            mListener.onAddGroupPressed(groupTitleText);
            dismiss();
        }

        if (id == R.id.group_add_cancel) {
            dismiss();
        }
    }
}