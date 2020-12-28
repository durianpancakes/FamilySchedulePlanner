package com.durianpancakes.familyscheduleplanner.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.durianpancakes.familyscheduleplanner.R;

public class GroupSettingsFragment extends Fragment {

    private GroupSettingsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(GroupSettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_group_settings, container, false);
        return root;
    }
}