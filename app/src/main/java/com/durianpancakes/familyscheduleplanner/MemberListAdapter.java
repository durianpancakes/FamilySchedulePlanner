package com.durianpancakes.familyscheduleplanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberHolder> {
    private String groupId;
    private ArrayList<Member> memberArrayList;
    private OnMemberItemClickListener onMemberItemClickListener;

    public MemberListAdapter(String groupId, ArrayList<Member> memberArrayList,
                             OnMemberItemClickListener memberItemClickListener) {
        this.groupId = groupId;
        this.memberArrayList = memberArrayList;
        this.onMemberItemClickListener = memberItemClickListener;
    }

    @NonNull
    @Override
    public MemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_recycler_item,
                parent, false);

        return new MemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberHolder holder, int position) {
        Member member = memberArrayList.get(position);

        holder.userDisplayName.setText(member.getName());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MemberHolder extends RecyclerView.ViewHolder {
        private TextView userDisplayName;
        private TextView userEmail;
        private TextView userMemberStatus;

        public MemberHolder(View itemView) {
            super(itemView);
            userDisplayName = itemView.findViewById(R.id.member_display_name);
            userEmail = itemView.findViewById(R.id.member_email);
            userMemberStatus = itemView.findViewById(R.id.member_status);
        }
    }
}
