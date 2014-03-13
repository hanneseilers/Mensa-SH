package de.hanneseilers.mensash;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.ProgressBar;
import org.holoeverywhere.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import de.hanneseilers.mensash.activities.ActivityMain;
import de.mensa.sh.core.Meal;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link MenuFragment#newInstance} factory method
 * to create an instance of this fragment.
 * 
 */
public class MenuFragment extends Fragment {

	private ListView mealList;
	private TextView txtMessage;
	private ProgressBar progressBar;
	private Boolean progressBarVisible = false;
	
	public MenuAdapter adapterMeals;
	
	public List<Meal> listMeals = new ArrayList<Meal>();
	public List<String> listCities = new ArrayList<String>();

	private Callback mListener;

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
	public static MenuFragment newInstance(String param1, String param2) {
		MenuFragment fragment = new MenuFragment();
		return fragment;
	}

	public MenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		
		// get the message text view
		txtMessage = (TextView) view.findViewById(R.id.txtMessage);
		
		// get the list for cities and meals
		mealList = (ListView) view.findViewById(R.id.mealList);
		
		// Set adapters and listeners for lists
		mealList.setAdapter(adapterMeals);
		mealList.setOnItemClickListener(new OnItemClickListener(){			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (mListener != null) {
					mListener.showMealDetails(listMeals.get(position));
				}
			}
			
		});
		
		
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		setProgressBar(progressBarVisible);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		activity = (ActivityMain) activity;
		try {
			mListener = (Callback) activity;
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
	public interface Callback {
		
		public void showMealDetails(Meal meal);
		
	}
	
	/**
	 * Sets progress bar visibility
	 * @param visible Show progressbar if {@code visible} = {@code true}.
	 */
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
	
	/**
	 * Shows message on menu
	 * @param message {@link String} message or {@code null} to hide message text view
	 */
	public void setMessage(String message){
		if( message != null ){
			txtMessage.setText(message);
			txtMessage.setVisibility(View.VISIBLE);
		}
		else{
			txtMessage.setVisibility(View.GONE);
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
