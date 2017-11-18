package projetandroidmaster1.memory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScoreActivity extends AppCompatActivity {

    private FileManagement FM;
    private ListView ListViewScore;
    private ArrayList<HashMap<String,String>> ListScoreListViewScore;
    private SimpleAdapter SimpleAdapterLVScore;

    /***********************************************************************************************
     * ****************************** Activity Life Circle *****************************************
     * *********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        FM = new FileManagement(this);
        initView();
    }

    /***********************************************************************************************
     * *********************************** functions ***********************************************
     * ********************************************************************************************/

    //display score on listView
    private void initView(){
        ListViewScore = (ListView)findViewById(R.id.ListViewScore);
        ListScoreListViewScore = new ArrayList<>();
        SimpleAdapterLVScore = new SimpleAdapter(this, ListScoreListViewScore,android.R.layout.activity_list_item,new String[]{"text1"}, new int[]{android.R.id.text1});
        ListViewScore.setAdapter(SimpleAdapterLVScore);

        ArrayList<Double> tmp = FM.readScoreFile();
        Collections.sort(tmp);
        Collections.reverse(tmp);
        Toast.makeText(this,"Integer.toString(tmp.size()) = "+Integer.toString(tmp.size()),Toast.LENGTH_SHORT).show();
        for(int i = 0; i < tmp.size(); i++){
            HashMap<String,String> Container = new HashMap<>();
            Container.put("text1",tmp.get(i).toString());
            ListScoreListViewScore.add(Container);
        }
        Toast.makeText(this,"ListScoreListViewScore.size() = "+Integer.toString(ListScoreListViewScore.size()),Toast.LENGTH_SHORT).show();
        SimpleAdapterLVScore.notifyDataSetChanged();
    }
}
