package de.hanneseilers.mensash.versions.hints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class VersionHintDialog_2_1_0 extends Hint {
	
	public VersionHintDialog_2_1_0(Activity activity) {
		super();
		name = getClass().getName();
		revision = 6;
		VersionHintDialog_2_1_0.activity = activity;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
    	// Set version info
    	String title = "UPDATE TO VERSION 2.1.0";
    	String text = "In dieser Version wurde die Oberfläche grundlegend verändert."
    			+ " Die Auswahl deiner Stadt sowie deiner Mensa erfolgt jetzt über das Seitenmenü.\n\n"
    			+ "Die Bewertungen wurden optimiert und werden jetzt schneller angezeigt."
    			+ " Wenn du über eine Daten-Flatrate verfügst kannst du die Bewertungen ohne Bedenken einschalten."
    			+ " Das kannst du selbstverständlich auch jederzeit in den Einstellungen ändern.\n\n"
    			+ "Möchtest du die Bewertungen aktivieren?";
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle( title );
        builder.setMessage( text )
               .setPositiveButton("Aktivieren", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   // OK
                	   SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                	   sharedPref.edit().putBoolean( "SHOW_RATING" , true ).commit();
                	   setInactive();
                   }
               })
               .setNegativeButton("Deaktivieren", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                	   sharedPref.edit().putBoolean( "SHOW_RATING" , false ).commit();
                	   sharedPref.edit().apply();
                	   setInactive();
                   }
               });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
	
}
