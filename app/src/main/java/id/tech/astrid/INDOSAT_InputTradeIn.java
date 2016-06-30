package id.tech.astrid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import id.tech.util.Parameter_Collections;
import id.tech.util.Public_Functions;
import id.tech.util.ServiceHandlerJSON;

/**
 * Created by RebelCreative-A1 on 12/11/2015.
 */
public class INDOSAT_InputTradeIn extends ActionBarActivity {
    private EditText ed_Nama, ed_Nomor_Iden, ed_Perangkat_Merk, ed_Perangkat_IMEI, ed_Perangkat_Harga,
            ed_Perangkat_Update, ed_Plan;
    //revisi
    private EditText ed_Totalcashout, ed_Bank, ed_Approvalcode,ed_Salesperson,ed_Newimei, ed_Newmsisdn;
    private Spinner spinner_Plan, spinner_Kontrak;
    private Button btn;
    private HashMap<String, String> data_plan;
    SharedPreferences spf;
    private String  id_pegawai, kode_toko, cPlan, cKontrak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat_input_tradein);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trade In");

        spf = getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);
		kode_toko = spf.getString(Parameter_Collections.SH_KODE_TOKO, "");
        id_pegawai = spf.getString(Parameter_Collections.SH_ID_PEGAWAI, "");

        new AsyncTas_GetPlan().execute();

        initView();
    }

    private class AsyncTas_GetPlan extends AsyncTask<Void, Void, Void> {
        private String cCode;
        ArrayAdapter<String> adapter;
        DialogFragmentProgress dialogProgress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data_plan = new HashMap<>();
            dialogProgress = new DialogFragmentProgress();
            dialogProgress.show(getSupportFragmentManager(), "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();
            JSONObject jObj = sh.json_get_trade_plan();
            try {
                cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

                if (cCode.equals("1")) {
                    JSONArray jArray = jObj
                            .getJSONArray(Parameter_Collections.TAG_DATA);
                    String[] spinnerArray = new String[jArray.length()];

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject c = jArray.getJSONObject(i);
                        String id_plan = c.getString(Parameter_Collections.TAG_ID_PLAN);
                        String plan_name = c.getString(Parameter_Collections.TAG_PLAN_NAME);

                        data_plan.put(id_plan, plan_name);
                        spinnerArray[i] = plan_name;
                    }



                    adapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, spinnerArray);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
                ArrayAdapter<CharSequence> adapter_kontrak = ArrayAdapter.createFromResource(getApplicationContext(), R.array.kontrak, R.layout.spinner_item);

                spinner_Plan.setAdapter(adapter);
                spinner_Kontrak.setAdapter(adapter_kontrak);

                spinner_Plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String txt = parent.getSelectedItem().toString();

                        Set set = data_plan.entrySet();
                        Iterator iterator = set.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry m_entry = (Map.Entry) iterator.next();

                            if (m_entry.getValue().equals(txt)) {
                                cPlan = m_entry.getKey().toString();
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinner_Kontrak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position == 0){
                            cKontrak = "12";
                        }else{
                            cKontrak = "24";
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }else{
                Toast.makeText(getApplicationContext(), "Something Wrong, Please check Internet or Web Admin",
                        Toast.LENGTH_LONG).show();
            }


        }
    }

    private class AsyncTask_Input_TradeIn extends AsyncTask<Void, Void, Void> {
        private String cNama, cIdentitas, cOldBrand, cHarga, cImei, cNewBrand, cId_Pegawai,
                cLati, cLongi, cId_Toko;
        private String latitude,longitude, cCode;
        private String cTotalcashout, cBank, cApprovalcode,cSalesperson,cNewimei, cNewmsisdn;
        DialogFragmentProgress dialogProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress = new DialogFragmentProgress();
            dialogProgress.show(getSupportFragmentManager(), "");


            cNama = ed_Nama.getText().toString();
            cIdentitas = ed_Nomor_Iden.getText().toString();
            cOldBrand = ed_Perangkat_Merk.getText().toString();
            cHarga = ed_Perangkat_Harga.getText().toString();
            cImei = ed_Perangkat_IMEI.getText().toString();
            cNewBrand = ed_Perangkat_Update.getText().toString();

            cTotalcashout= ed_Totalcashout.getText().toString();
            cBank= ed_Bank.getText().toString();
            cApprovalcode= ed_Approvalcode.getText().toString();
            cSalesperson= ed_Salesperson.getText().toString();
            cNewimei= ed_Newimei.getText().toString();
            cNewmsisdn= ed_Newmsisdn.getText().toString();

            cLati = spf.getString(Parameter_Collections.TAG_LATITUDE_NOW, "0.0");
            cLongi = spf.getString(Parameter_Collections.TAG_LONGITUDE_NOW, "0.0");

            cId_Toko = spf.getString(Parameter_Collections.SH_ID_TOKO, "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            ServiceHandlerJSON sh = new ServiceHandlerJSON();

            JSONObject jObj = sh.trade_in(cPlan,cId_Toko, cKontrak,id_pegawai,cNama,cOldBrand,cNewBrand,cImei,cIdentitas,cHarga,cLati,cLongi,
                    cTotalcashout, cBank, cApprovalcode,cSalesperson,cNewimei, cNewmsisdn);
            try {
                cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);
            }catch (JSONException e){

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialogProgress.dismiss();

            if(cCode.equals("1")){
                Toast.makeText(getApplicationContext(), "Input Trade In Success",
                        Toast.LENGTH_LONG).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Something Wrong, Please check Internet or Web Admin",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        ed_Nama = (EditText) findViewById(R.id.ed_nama);
        ed_Nomor_Iden = (EditText) findViewById(R.id.ed_identitas);
        ed_Perangkat_Merk = (EditText) findViewById(R.id.ed_perangkat_merk);
        ed_Perangkat_IMEI = (EditText) findViewById(R.id.ed_perangkat_imei);
        ed_Perangkat_Harga = (EditText) findViewById(R.id.ed_perangkat_harga);
        ed_Perangkat_Update = (EditText) findViewById(R.id.ed_perangkat_baru);
//        ed_Plan = (EditText) findViewById(R.id.ed_nama);
        ed_Totalcashout = (EditText) findViewById(R.id.ed_totalcashout);
        ed_Bank = (EditText) findViewById(R.id.ed_bank);
        ed_Approvalcode = (EditText) findViewById(R.id.ed_approvalcode);
        ed_Salesperson = (EditText) findViewById(R.id.ed_salesperson);
        ed_Newimei = (EditText) findViewById(R.id.ed_newimei);
        ed_Newmsisdn = (EditText) findViewById(R.id.ed_newmsisdn);

        spinner_Plan = (Spinner) findViewById(R.id.spinner_plan);
        spinner_Kontrak = (Spinner) findViewById(R.id.spinner_kontrak);

        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask_Input_TradeIn().execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
