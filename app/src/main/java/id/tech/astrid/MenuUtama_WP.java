package id.tech.astrid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import id.tech.astrid.R;
import id.tech.util.GPSTracker;
import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_MenuUtama;
import id.tech.util.RecyclerAdapter_Slider;
import id.tech.util.RecyclerItemClickListener;
import id.tech.util.RowData_Produk;
import id.tech.util.ServiceHandlerJSON;
import id.tech.util.SimpleDividerItemDecoration;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.support.v7.widget.RecyclerView.State;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuUtama_WP extends ActionBarActivity {
	RecyclerView rv, rv_slider;
	RecyclerView.Adapter adapter, adapter_slider;
	RecyclerView.LayoutManager layoutManager, layoutManager_slider;
	RecyclerView.ItemDecoration decoration;
	ImageView img_logout;
	private SharedPreferences sp;

	ActionBarDrawerToggle mDrawerToggle;
	DrawerLayout Drawer;
	
	Bundle currentState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_menuutama_wp);
		setContentView(R.layout.main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ActionBar ac = getSupportActionBar();
		ac.hide();

		sp = getSharedPreferences(Parameter_Collections.SH_NAME,
				Context.MODE_PRIVATE);

		rv_slider = (RecyclerView) findViewById(R.id.slider_content);
		Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

		layoutManager_slider = new LinearLayoutManager(this);
		rv_slider.setLayoutManager(layoutManager_slider);

		if (savedInstanceState == null) {
			currentState = savedInstanceState;
			FragmentManager fm = getSupportFragmentManager();
			Fragment fragment = new Fragment_MenuUtama();
			fm.beginTransaction().replace(R.id.frame_container, fragment)
					.commit();

			new AsyncTask_LoadProfile().execute();

		}

		Drawer.openDrawer(Gravity.START);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (currentState == null) {
			new AsyncTask_LoadProfile().execute();
		}

//		Drawer.openDrawer(Gravity.START);
	}
	

	private class AsyncTask_LoadProfile extends AsyncTask<Void, Void, Void> {
		DialogFragmentProgress pDialog;
		String result, cCode, cNama_Pegawai, cUrl_ImgProfilePic, cTarget,
				cAchievement;

		int total_D_300, total_D_616, total_D_816, total_D_C, total_flyer,
				total_O_820D, total_O_M7C, total_O_E8, total_O_M8, total_O_M9,
				total_O_Max, total_O_Mini, total_O_V, total_WP,
				total_WP_8X = 0;

		boolean available_D_300, available_D_616, available_D_816,
				available_D_C, available_flyer, available_O_820D,
				available_O_M7C, available_O_E8, available_O_M8,
				available_O_M9, available_O_Max, available_O_Mini,
				available_O_V, available_WP, available_WP_8X = false;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog = new DialogFragmentProgress();
//			pDialog.show(getSupportFragmentManager(), "");
			Toast.makeText(getApplicationContext(), "Loading Data", Toast.LENGTH_SHORT).show();
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				ServiceHandlerJSON sh = new ServiceHandlerJSON();
				JSONObject jObj = sh.json_get_pic_profile(sp.getString(
						Parameter_Collections.SH_ID_PEGAWAI, ""));
//				JSONObject jObj_Info = sh.json_get_target_info(sp.getString(
//						Parameter_Collections.SH_ID_PEGAWAI, ""));

				cCode = jObj.getString(Parameter_Collections.TAG_JSON_CODE);

				if (cCode.equals("1")) {
					JSONObject jObj_Data = jObj
							.getJSONObject(Parameter_Collections.TAG_DATA);
					cNama_Pegawai = jObj_Data
							.getString(Parameter_Collections.TAG_NAMA_PEGAWAI);

					String img_no_data = jObj_Data.getString(Parameter_Collections.TAG_ARRAY_IMAGES);
					
					if(img_no_data.equals("no data")){
						cUrl_ImgProfilePic = "";
						
					}else{
						JSONArray jArray_Data = jObj_Data
								.getJSONArray(Parameter_Collections.TAG_ARRAY_IMAGES);
						for (int i = 0; i < jArray_Data.length(); i++) {
							JSONObject c = jArray_Data.getJSONObject(i);

							cUrl_ImgProfilePic = Parameter_Collections.URL_GAMBAR_THUMB
									+ c.getString(Parameter_Collections.TAG_NAMA_IMAGE);
						}
					}
					
					
				} else {

				}

//				String cCode_Info = jObj_Info
//						.getString(Parameter_Collections.TAG_JSON_CODE);
//				if (cCode_Info.equals("1")) {
//					String cCode_Message = jObj_Info.getString(Parameter_Collections.TAG_DATA);
//					if(!cCode_Message.equals("no data")){
//						JSONArray jArray_Info = jObj_Info
//								.getJSONArray(Parameter_Collections.TAG_DATA);
//						for (int i = 0; i < jArray_Info.length(); i++) {
//							JSONObject c = jArray_Info.getJSONObject(i);
//
//							cTarget = c.getString(Parameter_Collections.TAG_TARGET);
//							cAchievement = c
//									.getString(Parameter_Collections.TAG_TOTAL_PENJUALAN);
//						}
//					}
//
//				}

//				if (sp.getBoolean(Parameter_Collections.SH_ABSENTED, false)) {
//					JSONObject jObj_Stok = sh.json_cek_stok(sp.getString(
//							Parameter_Collections.SH_KODE_TOKO, ""));
//
//					Log.e("result cek produk", jObj_Stok.toString());
//
//					cCode = jObj_Stok.getString(Parameter_Collections.TAG_DATA);
//					if (!cCode.equals("no data")) {
//						JSONArray jArray = jObj_Stok
//								.getJSONArray(Parameter_Collections.TAG_DATA);
//
//						for (int i = 0; i < jArray.length(); i++) {
//							JSONObject c = jArray.getJSONObject(i);
//							String id_produk_toko = c
//									.getString(Parameter_Collections.TAG_ID_PRODUK_TOKO);
//
//							String nama_produk = c
//									.getString(Parameter_Collections.TAG_NAMA_PRODUK);
//							String harga_produk = c
//									.getString(Parameter_Collections.TAG_KODE_TOKO);
//							String imei_produk = c
//									.getString(Parameter_Collections.TAG_IMEI);
//							String imei2_produk = c
//									.getString(Parameter_Collections.TAG_IMEI2);
//							String status_produk = c
//									.getString(Parameter_Collections.TAG_STATUS_PRODUK);
//
//							if (status_produk.equals("1")) {
//								if (nama_produk.contains(Parameter_Collections.TIPE_D_300)) {
//									available_D_300 = true;
//									total_D_300 += 1;
//								} else if (nama_produk.contains(Parameter_Collections.TIPE_D_616)) {
//									available_D_616 = true;
//									total_D_616 += 1;
//								} else if (nama_produk.contains(Parameter_Collections.TIPE_D_816)) {
//									available_D_816 = true;
//									total_D_816 += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_D_C)) {
//									available_D_C = true;
//									total_D_C += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_FLYER)) {
//									available_flyer = true;
//									total_flyer += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_820D)) {
//									available_O_820D = true;
//									total_O_820D += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_E8)) {
//									available_O_E8 = true;
//									total_O_E8 += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_M7C)) {
//									available_O_M7C = true;
//									total_O_M7C += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_M8)) {
//									available_O_M8 = true;
//									total_O_M8 += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_M9)) {
//									available_O_M9 = true;
//									total_O_M9 += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_Max)) {
//									available_O_Max = true;
//									total_O_Max += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_O_Mini)) {
//									available_O_Mini = true;
//									total_O_Mini += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_WP)) {
//									available_WP = true;
//									total_WP += 1;
//								}else if (nama_produk.contains(Parameter_Collections.TIPE_WP_8X)) {
//									available_WP_8X = true;
//									total_WP_8X += 1;
//								}
//
//
//							}
//						}
//					}
//
//				}

//				adapter_slider = new RecyclerAdapter_Slider(cNama_Pegawai,
//						cTarget, cAchievement, cUrl_ImgProfilePic,
//						available_D_300, total_D_300, available_D_616, total_D_616,
//						available_D_816, total_D_816, available_D_C, total_D_C,
//						available_flyer, total_flyer, available_O_820D, total_O_820D,
//						available_O_M7C, total_O_M7C, available_O_E8, total_O_E8,
//						available_O_M8, total_O_M8,available_O_M9, total_O_M9,
//						available_O_Max, total_O_Max, available_O_Mini, total_O_Mini, available_O_V, total_O_V,
//						available_WP, total_WP, available_WP_8X, total_WP_8X);

				//indosat
				
				adapter_slider = new RecyclerAdapter_Slider(cNama_Pegawai, cUrl_ImgProfilePic);


//				else if (nama_produk.contains(Parameter_Collections.TIPE_D_C)) {
//					available_D_C = true;
//					total_D_C += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_FLYER)) {
//					available_flyer = true;
//					total_flyer += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_820D)) {
//					available_O_820D = true;
//					total_O_820D += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_E8)) {
//					available_O_E8 = true;
//					total_O_E8 += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_M7C)) {
//					available_O_M7C = true;
//					total_O_M7C += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_M8)) {
//					available_O_M8 = true;
//					total_O_M8 += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_M9)) {
//					available_O_M9 = true;
//					total_O_M9 += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_Max)) {
//					available_O_Max = true;
//					total_O_Max += 1;
//				}else if (nama_produk.contains(Parameter_Collections.TIPE_O_Mini)) {
//					available_O_Mini = true;
//					total_O_Mini += 1;
//				}

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			pDialog.dismiss();

//			FragmentManager fm = getSupportFragmentManager();
//			Fragment fragment = new Fragment_MenuUtama();
//			fm.beginTransaction().replace(R.id.frame_container, fragment)
//					.commit();
			rv_slider.setAdapter(adapter_slider);
		}
	}

}
