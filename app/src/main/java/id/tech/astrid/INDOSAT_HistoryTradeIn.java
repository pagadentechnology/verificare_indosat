package id.tech.astrid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.tech.util.CustomerAdapter_History_TradeIn;
import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.RowData_History_TradeIn;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 17/11/2015.
 */
public class INDOSAT_HistoryTradeIn extends AppCompatActivity {
    ListView lv;
    private SharedPreferences spf;
    private String id_pegawai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat_history_tradein);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("History Trade-In");

        spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
        id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

        lv = (ListView)findViewById(R.id.ls);

        new AsyncTask_GetHistory_TradeIn().execute();
    }

    private class AsyncTask_GetHistory_TradeIn extends AsyncTask<Void,Void, Void>{
        private String cCode, temp;
        private List<RowData_History_TradeIn> objects;
        DialogFragmentProgress dialogProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            objects = new ArrayList<>();
            dialogProgress = new DialogFragmentProgress();
            dialogProgress.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            JSONObject jObj = sh.json_get_history_tradein(id_pegawai);
            temp = jObj.toString();

            try {
                cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

                if (cCode.equals("1")) {
                    JSONArray jArray = jObj.getJSONArray(Parameter_Collections.TAG_DATA);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject c = jArray.getJSONObject(i);
                        String nama = c.getString(Parameter_Collections.TAG_TRADE_CUSTOMER_NAME);
                        String old_phone = c.getString(Parameter_Collections.TAG_TRADE_CUSTOMER_OLD_BRAND);
                        String new_phone = c.getString(Parameter_Collections.TAG_TRADE_CUSTOMER_NEW_BRAND);
                        String plan = c.getString(Parameter_Collections.TAG_PLAN_NAME);

                        objects.add(new RowData_History_TradeIn(nama,old_phone,new_phone,plan));
                    }

                } else {
                    cCode = "0";
                }
            } catch (JSONException e) {
                cCode = "0";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogProgress.dismiss();
            if(cCode.equals("1")){
                CustomerAdapter_History_TradeIn adapter = new CustomerAdapter_History_TradeIn(getApplicationContext(),0,objects);

                lv.setAdapter(adapter);
            }else{
                Toast.makeText(getApplicationContext(), "Something Wrong, Please check Internet or Web Admin", Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "Something Wrong = " + temp, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
