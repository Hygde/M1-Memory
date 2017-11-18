package projetandroidmaster1.memory;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Samuel on 13/11/2017.
 */

public class FileManagement {

    private final String AppSettingsFileName = "AppSettings.txt";
    private final String ScoreFileName = "score.txt";
    private final String GameStateFile = "gamestate.txt";
    private final int ERROR = -1;

    private static File AppSettings;
    private static File FScore;
    private static File FGameState;

    private Context ctx;

    //constructor
    public FileManagement(Context ctx){
        this.ctx = ctx;
        AppSettings = initFile(ctx, AppSettingsFileName);
        FScore = initFile(ctx, ScoreFileName);
        FGameState = initFile(ctx, GameStateFile);
    }

    //create file if not exist
    private File initFile(Context ctx, String FName){
        File f;
        try{
            f = new File(ctx.getFilesDir(), FName);
            if(!f.exists())f.createNewFile();
        }catch(IOException e){
            Log.e("MEMORY : ", "FileManagement.InitFile() : Error while creating "+FName);
            f = null;
        }
        return f;
    }

    //Write a line in File
    private int writeLn(File file,String str, boolean append){
        Log.e("MEMORY : ", "FileManagement.WriteLn() : "+str+" into "+file.getName());
        int result = 0;
        try{
            FileOutputStream fout = new FileOutputStream(file,append);//if append then add arg true
            OutputStreamWriter wfout = new OutputStreamWriter(fout);
            BufferedWriter bwfout = new BufferedWriter(wfout);
            bwfout.write(str+"\n");
            bwfout.flush();
            bwfout.close();
        }catch(IOException e){
            Log.e("MEMORY : ", "FileManagement.WriteLn() : Error while writing in file "+file.getName());
            result = ERROR;
        }
        return result;
    }

    //Read a file line by line and return it in an arraylist
    private ArrayList<String>readFile(File file){
        Log.e("MEMORY : ", "FileManagement.readFile() : "+file.getName());
        ArrayList<String>result = new ArrayList<>();
        try {
            FileInputStream fin = new FileInputStream(file);
            InputStreamReader rfin = new InputStreamReader(fin);
            BufferedReader brfin = new BufferedReader(rfin);
            String str = "B";
            while((str = brfin.readLine()) != null)result.add(str);
            brfin.close();
        }catch(IOException e){
            Log.e("MEMORY : ", "FileManagement.readFile() : Error while reading file "+file.getName());
            result.clear();
        }
        return result;
    }

    //write value into FScore
    public int saveScore(Double score){
        Log.e("MEMORY : ","FileManagement.WriteScore() : "+score);
        int result = 0;
        ArrayList<Double> tmp = readScoreFile();
        if(!tmp.contains(score))tmp.add(score);
        Collections.sort(tmp);
        Collections.reverse(tmp);
        result = writeLn(FScore,tmp.get(0).toString(),false);
        for(int i = 1; (i<10) && (i < tmp.size()); i++){
            if(writeLn(FScore,tmp.get(i).toString(),true) != 0) result = ERROR;
        }
        return result;
    }

    //read FScore
    public ArrayList<Double> readScoreFile(){
        Log.e("MEMORY : ","FileManagement.ReadScoreFile()");
        ArrayList<Double> result = new ArrayList();
        ArrayList<String> temp = readFile(FScore);
        Toast.makeText(ctx,"NB_ITEM_READ = "+Integer.toString(temp.size()),Toast.LENGTH_SHORT).show();
        int i = 0;
        try{
           for(i = 0; i < temp.size(); i++) {
               result.add(Double.valueOf(temp.get(i)));
           }
        }catch(NullPointerException e){
            Log.e("MEMORY : ","FileManagement.ReadScoreFile() : Error while converting str to double : "+temp.get(i)+"\nError = "+e.getMessage());
        }
        return result;
    }

    //Read FGamestate
    public int readGameState(){
        int result = ERROR;
        Log.e("MEMORY : ","FileManagement.LoadGameState() : Error not yet implemented !");
        return result;
    }

    //Save game state in FGamestate
    public int saveGameState(int nb_coup, int mat[][]){
        int result = ERROR;
        Log.e("MEMORY : ","FileManagement.SaveGameState() : Error not yet implemented !");
        return result;
    }

    //Save the app settings
    public int saveAppSettings(String settings){
        Log.e("MEMORY : ", "FileManagement.saveAppSettings()");
        return writeLn(AppSettings, settings, false);
    }

    //return settings of the app
    public HashMap<String,Double> readAppSettings(String[] Settings){
        HashMap<String,Double> result = new HashMap();
        ArrayList<String>config = new ArrayList();
        config = readFile(AppSettings);
        for(String str : config){
            String tmp[] = str.split("=");
            result.put(tmp[0],Double.valueOf(tmp[1]));
        }
        return result;
    }
}