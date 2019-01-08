package com.nex3z.tfmobilemnist;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class ImageObject {


    private String ID;
    private Bitmap image;
    private int prediction;
    private double probability;
    private String time;


    public ImageObject(Bitmap image, int prediction, double probability, String time ){
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
    public String getTime(){
        return time;
    }


}
