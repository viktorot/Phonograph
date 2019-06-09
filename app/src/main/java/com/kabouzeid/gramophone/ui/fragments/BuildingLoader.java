package com.kabouzeid.gramophone.ui.fragments;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.loader.content.Loader;

import java.util.ArrayList;

public class BuildingLoader extends Loader<ArrayList<Integer>> {
    private final String[] mBuildingNames;
    private ArrayList<Integer> mBuildingArrayList;

    public BuildingLoader(Context context, String[] buildingNames) {
        super(context);
        mBuildingNames = buildingNames;
    }

    @Override
    protected void onStartLoading() {
        if (mBuildingArrayList != null) {
            deliverResult(mBuildingArrayList);
        } else {
            forceLoad();
        }

    }

    @Override
    protected void onForceLoad() {
        if (mBuildingArrayList != null) {
            deliverResult(mBuildingArrayList);
        }

        loadInBackground();
    }

    @Override
    protected void onReset() {
        mBuildingArrayList = null;
    }

    private void loadInBackground() {
        for (int x = 0; x < mBuildingNames.length; x++) {
            onGetDataForBuilding(mBuildingNames[x]);
        }

        deliverResult(mBuildingArrayList);
    }

    @VisibleForTesting
    public void onGetDataForBuilding(String buildingName) {
        // Pretend this is a blocking call to a third party REST API that
        // accepts a building name and returns the building address.
        Integer resultBuilding = null;

        onSuccess(resultBuilding);
    }

    public void onSuccess(final Integer data) {
        if (mBuildingArrayList == null) {
            mBuildingArrayList = new ArrayList<>();
        }
        mBuildingArrayList.add(data);
    }
}


