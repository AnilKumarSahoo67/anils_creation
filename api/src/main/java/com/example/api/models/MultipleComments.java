package com.example.api.models;

import java.util.List;

public class MultipleComments {
    private List<SingleComment> mComments;

    public List<SingleComment> getmComments() {
        return mComments;
    }

    public void setmComments(List<SingleComment> mComments) {
        this.mComments = mComments;
    }
}
