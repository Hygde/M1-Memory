package projetandroidmaster1.memory;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Samuel on 13/11/2017.
 */

public class FileManagement {

    private final String ScoreFileName = "score.txt";
    private final String GameStateFile = "gamestate.txt";
    private final int ERROR = -1;


    private Context ctx;

    public FileManagement(Context ctx){
        this.ctx = ctx;

        try {
            File f = new File("home/user/Documents/"+ScoreFileName);
            Toast.makeText(ctx,f.getAbsolutePath(),Toast.LENGTH_LONG).show();
            if(!f.exists()) {
                f.createNewFile();
                Log.e("Memory : ", "FileManagement() : Creating file "+ScoreFileName);
            }
        } catch (IOException e) {
            Log.e("Memory : ", "FileManagement() : Error while creating "+ScoreFileName);
        }

        try{
            File f1 = new File(ctx.getFilesDir(), GameStateFile);
            if(!f1.exists()){
                f1.createNewFile();
                Log.e("Memory : ", "FileManagement() : Creating file "+GameStateFile);
            }
        }catch(IOException e){
            Log.e("Memory : ", "FileManagement() : Error while creating "+GameStateFile);
        }
    }

    public int WriteScore(String score){
        int result = 0;
        Log.e("Memory : ","WriteScore() : "+score);
        try{
            OutputStreamWriter out = new OutputStreamWriter(ctx.openFileOutput(ScoreFileName,Context.MODE_PRIVATE));
            BufferedWriter bw = new BufferedWriter(out);
            bw.write(score);
            bw.newLine();
            bw.flush();
            bw.close();
            out.close();
        }catch(IOException e){
            Log.e("Memory : ", "WriteScore() : Error while write score");
            result = ERROR;
        }
        return result;
    }

    public ArrayList<Double> ReadScoreFile(){
        ArrayList<Double> result = new ArrayList<>();
        try {
            InputStream in = ctx.openFileInput(ScoreFileName);
            if(in != null) {
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader br = new BufferedReader(reader);
                String temp = "";
                while ((temp = br.readLine()) != "") {
                    if(temp != null)result.add(Double.valueOf(temp));
                }
                Log.e("Memory : ", "ReadScoreFile() : " + Integer.toString(result.size()));
                br.close();
                reader.close();
                in.close();
            }
            Collections.sort(result);
        }catch(IOException e){
            Log.e("Memory : ","ReadScoreFile() : Error while reading file");
            result = null;
        }
        return result;
    }

    public int LoadGameState(){
        int result = ERROR;
        Log.e("Memory : ","LoadGameState() : Error not yet implemented !");
        return result;
    }

    public int SaveGameState(int nb_coup, int mat[][]){
        int result = ERROR;
        Log.e("Memory : ","SaveGameState() : Error not yet implemented !");
        return result;
    }
}
