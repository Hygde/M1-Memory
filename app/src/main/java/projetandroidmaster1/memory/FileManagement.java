package projetandroidmaster1.memory;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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
        AppSettings = InitFile(ctx, AppSettingsFileName);
        FScore = InitFile(ctx, ScoreFileName);
        FGameState = InitFile(ctx, GameStateFile);
    }

    //create file if not exist
    private File InitFile(Context ctx, String FName){
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
    private int WriteLn(File file,String str){
        Log.e("MEMORY : ", "FileManagement.WriteLn() : "+str+" into "+file.getName());
        int result = 0;
        try{
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter wfout = new OutputStreamWriter(fout);
            BufferedWriter bwfout = new BufferedWriter(wfout);
            bwfout.write(str);
            bwfout.newLine();
            bwfout.flush();
            bwfout.close();
        }catch(IOException e){
            Log.e("MEMORY : ", "FileManagement.WriteLn() : Error while writing in file "+file.getName());
            result = ERROR;
        }
        return result;
    }

    //Read a file line by line and return it in an arraylist
    private ArrayList<String>ReadFile(File file){
        ArrayList<String>result = new ArrayList<>();
        try {
            FileInputStream fin = new FileInputStream(file);
            InputStreamReader rfin = new InputStreamReader(fin);
            BufferedReader brfin = new BufferedReader(rfin);
            String str;
            while((str = brfin.readLine()) != null)result.add(str);
            brfin.close();
        }catch(IOException e){
            Log.e("MEMORY : ", "FileManagement.ReadLn() : Error while reading file "+file.getName());
            result.clear();
        }
        return result;
    }

    //write value into FScore
    public int WriteScore(String score){
        Log.e("MEMORY : ","FileManagement.WriteScore() : "+score);
        return WriteLn(FScore,score);
    }

    //read FScore
    public ArrayList<Double> ReadScoreFile(){
        Log.e("MEMORY : ","FileManagement.ReadScoreFile()");
        ArrayList<Double> result = null;
        ArrayList<String> temp = ReadFile(FScore);
        try{
           for(String str : temp) result.add(Double.valueOf(str));
        }catch(NullPointerException e){
            Log.e("MEMORY : ","FileManagement.ReadScoreFile() : Error while converting str to double");
            result.clear();
            result = null;
        }
        return result;
    }

    //Read FGamestate
    public int LoadGameState(){
        int result = ERROR;
        Log.e("MEMORY : ","FileManagement.LoadGameState() : Error not yet implemented !");
        return result;
    }

    //Save game state in FGamestate
    public int SaveGameState(int nb_coup, int mat[][]){
        int result = ERROR;
        Log.e("MEMORY : ","FileManagement.SaveGameState() : Error not yet implemented !");
        return result;
    }
}