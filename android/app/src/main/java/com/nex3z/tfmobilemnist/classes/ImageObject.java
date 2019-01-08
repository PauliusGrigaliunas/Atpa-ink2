package com.nex3z.tfmobilemnist.classes;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class ImageObject {


    private String ID;
    private Bitmap image;
    private int prediction;
    private double probability;
    private long time;


    public ImageObject(Bitmap image, int prediction, double probability, long time ){
        this.image = image;
        this.prediction = prediction;
        this.probability = probability;
        this.time = time;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getID(){
        return ID;
    }

    public Bitmap getImage(){ return image; }
    public int getPrediction(){ return prediction; }
    public double getProbability(){ return probability; }
    public long getTime(){
        return time;
    }


}
