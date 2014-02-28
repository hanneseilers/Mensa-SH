package de.hanneseilers.mensash.versions.hints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class VersionHintDialog1 extends Hint {
	
	public VersionHintDialog1(Activity activity) {
		super();
		name = "versionDialog1";
		VersionHintDialog1.activity = activity;
	}
	
	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	// Set version info
    	String title = "UPDATE TO VERSION 2.0.1";
    	String text = "Mit dem Update auf Version 2.0.1 wurde eine Bewertungsfunktion eingeführt.\n\n"
    			+ "Mit ihr kannst du Gerichte deiner Mensa bewerten."
    			+ " Durch das Aktivieren der Funktion werden jedoch mehr Daten aus dem Internet geladen."
    			+ " Ohne Internet-Daten-Flatrate solltest du die Bewertungsfunktion deaktivieren."
    			+ " Du kannst die Funktion auch nachträglich in den Einstellungen aktivieren oder deaktivieren.\n\n"
    			+ "Möchtest du die Bewertungsfunktion aktivieren?";
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle( title );
        builder.setMessage( text )
               .setPositiveButton("Aktivieren", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   // OK
                	   SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                	   sharedPref.edit().putBoolean( "SHOW_RATING" , true ).commit();
                	   sharedPref.edit().putBoolean( "SHOW_VERSION_HINTS", false ).commit();
                	   setInactive();
                   }
               })
               .setNegativeButton("Deaktivieren", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                	   sharedPref.edit().putBoolean( "SHOW_RATING" , false ).commit();
                	   sharedPref.edit().putBoolean( "SHOW_VERSION_HINTS", false ).commit();
                	   sharedPref.edit().apply();
                	   setInactive();
                   }
               });
        
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
