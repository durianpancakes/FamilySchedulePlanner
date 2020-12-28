package com.durianpancakes.familyscheduleplanner.ui.tracking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrackingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrackingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}