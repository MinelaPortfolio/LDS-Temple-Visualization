package edu.byuh.cis.templevis;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ResourceCache {

//    Integer testIdentifier;
    private ArrayList<String> templeDrawableNames = new ArrayList<>();
    public ArrayList<String> templeNames = new ArrayList<>();
    public  ArrayList<Integer> smallImageIdentifiers = new ArrayList<>();
    public  ArrayList<Temple> templeObjects = new ArrayList<>();

    public ResourceCache(Context context, float w2) {

//        testIdentifier = context.getResources().getIdentifier("antofagasta_chile_temple", "drawable", "edu.byuh.cis.templevis");
//        Log.d("identifier 11111", testIdentifier + "");
//        Log.d("identifier 22222", R.drawable.antofagasta_chile_temple + "");

        readNameFile(context);

        Log.d("temples count", templeDrawableNames.size() + "");

        for (String s: templeDrawableNames) {
            Integer identifier = context.getResources().getIdentifier(s.substring(0,s.length()-1), "drawable", "edu.byuh.cis.templevis");
            if (identifier != 0) {
                smallImageIdentifiers.add(identifier);
            } else {
                smallImageIdentifiers.add(context.getResources().getIdentifier("no_image", "drawable", "edu.byuh.cis.templevis"));
            }
//            Log.d("identifier", identifier + " is " + s);

            String[] templeNameList = s.split("_");
            String templeName = "";
            for(int i = 0; i < templeNameList.length; i++) {
                String word = templeNameList[i];
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
                templeName = templeName + " " + word;
            }
            templeNames.add(templeName.substring(1, templeName.length()-1));
        }

        Log.d("identifiers", smallImageIdentifiers.toString());
        Log.d("temple names", templeNames.toString());

        float w = w2 / 4;

        for (int i:smallImageIdentifiers) {
            Bitmap temple = loadAndScale(context.getResources(),i, w);
            templeObjects.add(new Temple(temple, 0f, 0f, 0f));
        }
        Log.d("templeObjects size", templeObjects.size() + "");


    }

    public void readNameFile(Context context) {
        try {
            InputStream templeNamesFile = context.getResources().openRawResource(R.raw.temple_names);
            if (templeNamesFile != null)
            {
                InputStreamReader ir = new InputStreamReader(templeNamesFile);
                BufferedReader br = new BufferedReader(ir);
                String line;
                //read each line
                while (( line = br.readLine()) != null) {
                    templeDrawableNames.add(line+"\n");
                }
                templeNamesFile.close();
            }
        }
        catch (FileNotFoundException e)
        {
            Log.d("File", "The File doesn't not exist.");
        }
        catch (IOException e)
        {
            Log.d("File", e.getMessage());
        }

    }

    private static Bitmap loadAndScale(Resources res, int id, float newWidth) {
        Bitmap original = BitmapFactory.decodeResource(res, id);
        float aspectRatio = (float)original.getHeight()/(float)original.getWidth();
        float newHeight = newWidth * aspectRatio;
        return Bitmap.createScaledBitmap(original, (int)newWidth, (int)newHeight, true);
    }

}
