package de.hanneseilers.mensash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import de.hanneseilers.mensash.activities.ActivityMain;
import de.mensa.sh.core.Meal;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;



/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MenuFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class MenuFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private ListView mealList;
	private ActivityMain activity;
	private ProgressBar progressBar;
	private Boolean progressBarVisible = false;
	public MenuAdapter adapterMeals;
	
	public List<Meal> listMeals = new ArrayList<Meal>();

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment MenuFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MenuFragment newInstance(String param1, String param2) {
		MenuFragment fragment = new MenuFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (savedInstanceState != null) {
//			ArrayList<ParcelableHashMap> list = savedInstanceState.getParcelableArrayList("listMeals");
//			for (ParcelableHashMap phm:list) {
//				listMeals.add(phm.data_map);
//			}
//		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		mealList = (ListView) view.findViewById(R.id.mealList);
		mealList.setAdapter(adapterMeals);
		mealList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (mListener != null) {
					mListener.onFragmentInteraction(listMeals.get(position));
				}
			}
		});
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		setProgressBar(progressBarVisible);
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activity = (ActivityMain) activity;
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(Meal meal);
	}
	
	public void setProgressBar(Boolean visible) {
		progressBarVisible = visible;
		if (progressBar != null) {			
			if (visible) {				
				crossfade(mealList, progressBar);
			} else {
				crossfade(progressBar, mealList);
			}
		}
	}
	
	private void crossfade(final View fromView, View toView) {

	    // Set the content view to 0% opacity but visible, so that it is visible
	    // (but fully transparent) during the animation.
		ViewHelper.setAlpha(toView,0f);
	    toView.setVisibility(View.VISIBLE);

	    // Animate the content view to 100% opacity, and clear any animation
	    // listener set on the view.
	    ViewPropertyAnimator.animate(toView)
	            .alpha(1f)
	            .setDuration(200)
	            .setListener(null);

	    // Animate the loading view to 0% opacity. After the animation ends,
	    // set its visibility to GONE as an optimization step (it won't
	    // participate in layout passes, etc.)
	    ViewPropertyAnimator.animate(fromView)
	            .alpha(0f)
	            .setDuration(200)
	    		.setListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                	fromView.setVisibility(View.GONE);
	                }
	            });
	}

}
