package jdc.kings.options;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PreferencesLoader {
	
	private static final String path = System.getProperty("user.dir") + "//preferences";
	
	public static void savePreferences(Preferences preferences) {
		try {
			FileOutputStream fileOutput = new FileOutputStream(path);
			ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput);
			objOutput.writeObject(preferences);
			objOutput.close();
			fileOutput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static Preferences loadPreferences() {
		Preferences preferences = null;
		try {
			FileInputStream fileInput = new FileInputStream(path);
			ObjectInputStream objInput = new ObjectInputStream(fileInput);
			preferences = (Preferences) objInput.readObject();
			objInput.close();
			fileInput.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return preferences;
		
	}

}
