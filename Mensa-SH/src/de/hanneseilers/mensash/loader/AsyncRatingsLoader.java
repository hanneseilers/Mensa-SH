package de.hanneseilers.mensash.loader;

import java.util.Hashtable;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import de.hanneseilers.mensash.activities.ActivityMain;
import de.hanneseilers.mensash.enums.LoadingProgress;
import de.mensa.sh.core.Meal;
import de.mensa.sh.core.Mensa;
import de.mensa.sh.core.Settings;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class AsyncRatingsLoader extends AsyncTask<String, Void, String> {

	private ActivityMain ctx;
	public static AsyncTask<String, Void, String> task = null;
	
	public AsyncRatingsLoader(ActivityMain ctx){
		super();
		this.ctx = ctx;
	}
	
	@Override
	protected String doInBackground(String... params){
		// set runnign task
		task = this;
		
		// get html data
		String html = params[1];
		
		// insert ratings into html
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		boolean showRating = sharedPref.getBoolean("SHOW_RATING", false);
		boolean ackDisclaimer = sharedPref.getBoolean("ACK_DISCLAIMER", false);
		
		// get mensa and add ratings
		for( Mensa mensa : ctx.getLocations() ){
			if( mensa.getName().equals(params[0]) ){
				// check if to add ratings
				if( ackDisclaimer && showRating ){
					return addRatingsToHTML(mensa, html);
				}
				break;
			}
		}		
		
		return html;
	}
	
	/**
	 * Adds cities to list
	 */
	@Override
	protected void onPostExecute(String result) {		
		// check if result is empty
		if( result != "" ){
			// load menue
			ctx.loadWebsiteHtml(result);
			ctx.setLoadingProgress(LoadingProgress.MENUE_LOADED);
		}
		
		// reset running task
		task = null;		
	}		
	
	/**
	 * Adds ratings from ratings database to html
	 * @param mensa
	 * @param html
	 * @return
	 */
	private String addRatingsToHTML(Mensa mensa, String html){
		
		// parse html to document
		html = html.replace("&#x20ac;", "EUR");
		Document doc;
		
		List<Meal> meals = mensa.getMeals();
		Hashtable<String, Integer> ratings = mensa.getRatings(meals);
		
		for( Meal meal : meals ){
			
			doc = Jsoup.parse( html );
			
			// defining querys
			String mealname = meal.getMealName()
					.replace("(", "\\(")
					.replace(")", "\\)")
					.replace(";", "\\;")
					.replace("&", "\\&");
			String query1 = "td:contains(" + mealname + ")";
			String query2 = ".mensa-sh-rating";
			
			// select elements
			Element td =  doc.select(query1).not(query2).first();
			
			
			if( td != null ){
				// set vertical top alignment, class and link to rating function				
				td.attr("valign", "top")
					.attr("onClick", "Android.addMealRating('" + Mensa.serialize(mensa) + "', '" + Meal.serialize(meal) + "');")
					.attr("class", td.attr("class") + " mensa-sh-rating");
				
				
				// get rating
				String ratingTxt = "";
				String key = meal.getKey();
				int rating = -1;
				if( ratings.containsKey(key) )
					rating = ratings.get(key);
				
				// gerenate rating html
				if( rating > -1){
					for( int i=0; i<5; i++ ){
						if( i < rating ){
							ratingTxt += "<img src='" + Settings.sh_mensa_rating_ico_full_url
									+ "' width='" + Settings.sh_mensa_rating_ico_size + "' />";
						}
						else{
							ratingTxt += "<img src='" + Settings.sh_mensa_rating_ico_empty_url
									+ "' width='" + Settings.sh_mensa_rating_ico_size + "' />";
						}
					}
					ratingTxt += "<br />";
				}
				
				// set rating html
				td.prepend( ratingTxt );
				html = doc.outerHtml();
			}
		}
		
		return html;
	}

}
