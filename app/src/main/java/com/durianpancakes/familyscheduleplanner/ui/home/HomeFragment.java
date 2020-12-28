package com.durianpancakes.familyscheduleplanner.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.durianpancakes.familyscheduleplanner.AddGroupDialog;
import com.durianpancakes.familyscheduleplanner.DatabaseHelper;
import com.durianpancakes.familyscheduleplanner.Group;
import com.durianpancakes.familyscheduleplanner.GroupViewModel;
import com.durianpancakes.familyscheduleplanner.HomeGroupListAdapter;
import com.durianpancakes.familyscheduleplanner.OnGroupAddListener;
import com.durianpancakes.familyscheduleplanner.OnHomeGroupItemClickListener;
import com.durianpancakes.familyscheduleplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private Context context;
    private HomeViewModel homeViewModel;
    private GroupViewModel groupViewModel;
    private RecyclerView groupRecyclerView;
    private TextView homeGreeting;
    private TextView homeSignInStatus;
    private FloatingActionButton addGroupFab;
    private HomeGroupListAdapter homeGroupListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        groupRecyclerView = root.findViewById(R.id.group_recycler_view);
        homeGreeting = root.findViewById(R.id.home_greeting);
        homeSignInStatus = root.findViewById(R.id.sign_in_status);
        addGroupFab = root.findViewById(R.id.add_group_fab);
        homeViewModel.getGroupLiveData().observe((LifecycleOwner) context,
                groupListUpdateObserver);
        homeViewModel.getGreetingLiveData().observe((LifecycleOwner) context,
                homeGreetingUpdateObserver);
        homeViewModel.getSignInStatusLiveData().observe((LifecycleOwner) context,
                signInStatusUpdateObserver);

        addGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGroupDialog addGroupDialog = AddGroupDialog.newInstance();
                addGroupDialog.setAddGroupDialogListener(new AddGroupDialog.AddGroupDialogListener() {
                    @Override
                    public void onAddGroupPressed(String groupTitle) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(new OnGroupAddListener() {
                            @Override
                            public void onSuccess() {
                                Snackbar.make(requireView(), "Group added", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
                        databaseHelper.addGroup(groupTitle);
                    }
                });
                addGroupDialog.show(requireActivity().getSupportFragmentManager(),
                        "addGroup");
            }
        });

        return root;
    }

    Observer<ArrayList<Group>> groupListUpdateObserver = new Observer<ArrayList<Group>>() {
        @Override
        public void onChanged(ArrayList<Group> groupArrayList) {
            homeGroupListAdapter = new HomeGroupListAdapter(context, groupArrayList,
                    new OnHomeGroupItemClickListener() {
                        @Override
                        public void onClick(Group group) {
                            groupViewModel = new ViewModelProvider(requireActivity()).get(GroupViewModel.class);
                            groupViewModel.setSelectedGroup(group);
                        }
                    });
            groupRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            groupRecyclerView.setAdapter(homeGroupListAdapter);
        }
    };

    Observer<String> signInStatusUpdateObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            homeSignInStatus.setText(s);
        }
    };

    Observer<String> homeGreetingUpdateObserver = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            homeGreeting.setText(s);
        }
    };
}