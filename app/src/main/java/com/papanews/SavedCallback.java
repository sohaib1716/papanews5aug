package com.papanews;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class SavedCallback extends DiffUtil.Callback {

    private List<savePost_Model> old, baru;

    public SavedCallback(List<savePost_Model> old, List<savePost_Model> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getImage() == baru.get(newItemPosition).getImage();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
