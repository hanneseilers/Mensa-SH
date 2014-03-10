package de.hanneseilers.mensash;

import java.util.List;

import org.holoeverywhere.LayoutInflater;

import org.holoeverywhere.widget.ArrayAdapter;
import org.holoeverywhere.widget.TextView;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;

public class MenuAdapter extends ArrayAdapter<Meal> {
	  private final Context context;
	  private final List<Meal> values;
	  private boolean showRating;
	  
	  public MenuAdapter(Context context, List<Meal> values) {
		    super(context, R.layout.menu_list_item, values);
		    this.context = context;
		    this.values = values;
		    
		    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			showRating = sharedPref.getBoolean("SHOW_RATING", false) && sharedPref.getBoolean("ACK_DISCLAIMER", false);
	}

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.menu_list_item, parent, false);
	    
	    TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
	    TextView txtInfo = (TextView) rowView.findViewById(R.id.txtInfo);
	    ImageView imgAlcohol = (ImageView) rowView.findViewById(R.id.imgAlcohol);
	    ImageView imgBeef = (ImageView) rowView.findViewById(R.id.imgBeef);
	    ImageView imgPork = (ImageView) rowView.findViewById(R.id.imgPork);
	    ImageView imgVegetarian = (ImageView) rowView.findViewById(R.id.imgVegetarian);
	    ImageView imgVegan = (ImageView) rowView.findViewById(R.id.imgVegan);
	    RatingBar rating = (RatingBar) rowView.findViewById(R.id.ratingBar);
	    
	    txtName.setText(values.get(position).getDate() + values.get(position).getMealName());
	    txtInfo.setText(values.get(position).getPrice());

	    imgAlcohol.setVisibility(values.get(position).isAlc() ? View.VISIBLE : View.GONE);
	    imgBeef.setVisibility(values.get(position).isCow() ? View.VISIBLE : View.GONE);
	    imgPork.setVisibility(values.get(position).isPig() ? View.VISIBLE : View.GONE);
	    imgVegetarian.setVisibility(values.get(position).isVegetarian() ? View.VISIBLE : View.GONE);
	    imgVegan.setVisibility(values.get(position).isVegan() ? View.VISIBLE : View.GONE);
	    
	    if(values.get(position).getRating() < 0 || !showRating) {
	    	rating.setVisibility(View.INVISIBLE);
	    } else {
	    	rating.setVisibility(View.VISIBLE);
	    	rating.setRating(values.get(position).getRating());
	    }
		Log.d("Mensa", "rating "+ String.valueOf(values.get(position).getRating()));

	    return rowView;
	  }
	  
	  @Override
	  public int getCount() {
		  return values.size();
	  }
	  
	} 
