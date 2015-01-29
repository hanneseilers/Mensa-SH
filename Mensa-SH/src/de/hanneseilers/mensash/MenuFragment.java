package de.hanneseilers.mensash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MenuFragment extends Fragment {
	
	private int mDay = 0;
	
	public MenuFragment(){}
	
	public MenuFragment(int position) {
		mDay = position;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View vView = inflater.inflate(R.layout.fragment_menu, container,
				false);
		
		((TextView) vView.findViewById(R.id.txtDay)).setText( "Day: " + Integer.toString(mDay) );
		
		return vView;
	}
	
	public void cancelBackgroundTasks(){
		// TODO: cancel background tasks
	}
	
}
