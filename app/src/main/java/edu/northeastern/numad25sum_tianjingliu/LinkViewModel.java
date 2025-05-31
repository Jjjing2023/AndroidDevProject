package edu.northeastern.numad25sum_tianjingliu;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class LinkViewModel extends ViewModel {
    private final List<LinkItem> links = new ArrayList<>();

    public List<LinkItem> getLinks(){
        return links;
    }


}
