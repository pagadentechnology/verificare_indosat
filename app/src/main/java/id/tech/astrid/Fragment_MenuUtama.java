package id.tech.astrid;

import id.tech.astrid.R;
import id.tech.util.Parameter_Collections;
import id.tech.util.RecyclerAdapter_MenuUtama;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragment_MenuUtama extends Fragment{
	RecyclerView rv;
	RecyclerView.Adapter adapter;
	RecyclerView.LayoutManager layoutManager;
	RecyclerView.ItemDecoration decoration;
	ImageView img_logout;
	private SharedPreferences sh;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_menuutama_wp, null);

		sh = getActivity().getSharedPreferences(Parameter_Collections.SH_NAME, Context.MODE_PRIVATE);

		img_logout = (ImageView)v.findViewById(R.id.btn_logout);
		img_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogLogout pDialog = new DialogLogout();
				pDialog.setContext(getActivity());
				pDialog.setSh(sh);
				pDialog.show(getFragmentManager(), "");
			}
		});		
		
		rv = (RecyclerView)v.findViewById(R.id.recycler_view);
		layoutManager = new GridLayoutManager(getActivity(), 1);
		rv.setLayoutManager(layoutManager);		
		adapter = new RecyclerAdapter_MenuUtama(getActivity(), getActivity());		
		rv.setAdapter(adapter);
		
		return v;
	}

}
