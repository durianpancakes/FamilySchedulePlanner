package com.durianpancakes.familyscheduleplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeGroupListAdapter extends RecyclerView.Adapter<HomeGroupListAdapter.GroupHolder> {
    private Context context;
    private ArrayList<Group> groupArrayList;
    private OnHomeGroupItemClickListener onHomeGroupItemClickListener;

    public HomeGroupListAdapter(Context context, ArrayList<Group> groupArrayList,
                                OnHomeGroupItemClickListener onHomeGroupItemClickListener) {
        this.context = context;
        this.groupArrayList = groupArrayList;
        this.onHomeGroupItemClickListener = onHomeGroupItemClickListener;
    }

    private String getMembersString(long numMembers) {
        if (numMembers == 1) {
            return numMembers + " member";
        } else {
            return numMembers + " members";
        }
    }

    @NonNull
    @Override
    public HomeGroupListAdapter.GroupHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_group_item,
                parent, false);

        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeGroupListAdapter.GroupHolder holder,
                                 int position) {
        Group group = groupArrayList.get(position);

        holder.mGroupTitle.setText(group.getGroupTitle());
        holder.mNumMembers.setText(getMembersString(group.getNumMembers()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeGroupItemClickListener.onClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    public class GroupHolder extends RecyclerView.ViewHolder {
        private TextView mGroupTitle;
        private TextView mNumMembers;
        private TextView mEvents;
        private TextView mPackages;

        public GroupHolder(View itemView) {
            super(itemView);
            mGroupTitle = itemView.findViewById(R.id.group_name);
            mNumMembers = itemView.findViewById(R.id.card_num_members);
            mEvents = itemView.findViewById(R.id.card_num_events_today);
            mPackages = itemView.findViewById(R.id.card_num_packages);
        }
    }
}
