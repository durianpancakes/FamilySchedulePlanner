package com.durianpancakes.familyscheduleplanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroupViewModel extends ViewModel {
    private final MutableLiveData<Group> selectedGroup = new MutableLiveData<Group>();

    public void setSelectedGroup(Group group) {
        selectedGroup.setValue(group);
    }

    public LiveData<Group> getSelectedGroup() {
        return selectedGroup;
    }
}
