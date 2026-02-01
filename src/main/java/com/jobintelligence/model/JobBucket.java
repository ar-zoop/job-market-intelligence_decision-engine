package com.jobintelligence.model;

import java.util.ArrayList;
import java.util.List;

public class JobBucket {
    private List<String> applyNowList;
    private List<String> prepareThenApplyList;
    private List<String> skipList;

    public JobBucket(List<String> applyNowList, List<String> prepareThenApplyList, List<String> skipList) {
        this.applyNowList = applyNowList;
        this.prepareThenApplyList = prepareThenApplyList;
        this.skipList = skipList;
    }

    public List<String> getApplyNowList() {
        return applyNowList;
    }

    public void setApplyNowList(List<String> applyNowList) {
        this.applyNowList = applyNowList;
    }

    public List<String> getPrepareThenApplyList() {
        return prepareThenApplyList;
    }

    public void setPrepareThenApplyList(List<String> prepareThenApplyList) {
        this.prepareThenApplyList = prepareThenApplyList;
    }

    public List<String> getSkipList() {
        return skipList;
    }

    public void setSkipList(List<String> skipList) {
        this.skipList = skipList;
    }
}
