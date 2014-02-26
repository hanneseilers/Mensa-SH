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
    	String text = "Die Mensa-SH Applikation bietet die Möglichkeit Bewertungen von Mahlzeiten einer Mensa abzugeben."
    			+ " Die Bewertungen werden auf externern Servern gespeichert."
    			+ " Zur Abgabe einer Bewertung werden Informationen über die Mahlzeit, die Mensa, sowie eine gerätespezifische Identifikationsnnummer (ID) zum Schutz vor Missbrauch übertragen."
    			+ " Die übertragenen Daten werden für einen unbestimmten Zeitraum gespeichert."
    			+ " Die gerätespezifische ID wird ausschließlich mit einer Einwegverschlüsselung verschlüsselt gespeichert und enthält keine persönlichen Daten."
    			+ " Der Betrieber der Mensa-SH Applikation garantiert, dass die ID nur zur Sicherstellung des Bewertungsservice gespeichert wird."
    			+ " Abgegebene Bewertungen werden nicht auf Basis der gerätespezifischen ID ausgewertet."
    			+ " Die Bewertungen werden ggf. nur in allgemein ausgewerteter Form an Dritte weitergegeben."
    			+ " Ist der Benutzer der Mensa-SH-Applikation mit der Datenschutzerklärung nicht einverstanden, ist die Nutzung der Bewertungsfunktion ausgeschlossen.\n\n"
    			+ "Sind Sie mit dieser Datenschutzerklärung einverstanden?";
    	
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
