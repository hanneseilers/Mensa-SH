package de.hanneseilers.mensash.versions.hints;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Disclaimer extends Hint {
	
	public Disclaimer(Activity activity) {
		super();
		name = "disclaimer";
		Disclaimer.activity = activity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Set disclaimer text
    	String title = "Datenschutzerklärung";
    	String text = "Du hast die Bewertungsfunktion aktiviert."
    			+ " Diese sendet zum Schutz vor Missbrauch neben deiner Bewertung die Android-ID deines Mobilgerätes an den Mensa-SH-Bewertungsserver."
    			+ " Die Android-ID ist beim ersten Start deines Gerätes automatisch generiert worden und enthält keinerlei persönliche Informationen.\n\n"
    			+ " Auf dem Mensa-SH-Bewertungsserver wird deine Android-ID mit einer Einwegverschlüsselung verschlüsselt und gespeichert."
    			+ " So kann deine Android-ID nicht mehr nachträglich ermittelt werden."
    			+ " Die gespeicherten Bewertungen werden nicht personen- oder Android-ID-bezogen ausgewertet und die einzelnen Datensätze werden zu keinem Zeitpunkt and Dritte weitergegeben.\n\n"
    			+ "Bist du mit dieser Datenschutzerklärung einverstanden?";
    	
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle( title );
        builder.setMessage( text )
               .setPositiveButton("Einverstanden", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   // OK
                	   SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                	   sharedPref.edit().putBoolean( "ACK_DISCLAIMER" , true ).commit();
                	   setInactive();
                   }
               })
               .setNegativeButton("NICHT einverstanden", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
                	   sharedPref.edit().putBoolean( "ACK_DISCLAIMER" , false ).commit();
                	   sharedPref.edit().putBoolean( "SHOW_RATING" , false ).commit();
                	   setInactive();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
	}
	
}
