package com.example.hiteshrexwal.popularmovies.Extras;

import java.util.ArrayList;



public class RetrieveListReviews {
    private ArrayList<Reviews> reviews;

    public RetrieveListReviews(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }
    public ArrayList<String> getcontents(){
        ArrayList<String> contents=new ArrayList<>();
        for (int i=0;i<reviews.size();i++){
            contents.add(reviews.get(i).content);
        }
        return contents;
    }
    public ArrayList<String> getauthors(){
        ArrayList<String> authors=new ArrayList<>();
        for (int i=0;i<reviews.size();i++){
            authors.add(reviews.get(i).author);
        }
        return authors;
    }

}
