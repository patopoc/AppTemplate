package app.com.apptemplate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
    
private static String DB_PATH = "/data/data/app.com.apptemplate/databases/";
 
private static String DB_NAME = "SmilesDB";
 
private SQLiteDatabase myDataBase;
 
private final Context myContext;
 
public DataBaseHelper(Context context) {
 
super(context, DB_NAME, null, 1);
this.myContext = context;
}	
 

public void createDataBase() throws IOException{
 
Log.d("DBH","checking database existence");	
boolean dbExist = checkDataBase();
 
	if(!dbExist){
	 
		
		this.getReadableDatabase();
	 
		try {
	 		Log.d("DBH","database doesn't exist, creating now...");
			copyDataBase();
	 
		} catch (IOException e) {
	 
			throw new Error("Error copying database: "+e.getMessage());
	 
		}
	}
}
 
private boolean checkDataBase(){
	File dbFile= myContext.getDatabasePath(DB_NAME);
	return dbFile.exists();
}
 
private void copyDataBase() throws IOException{
 
InputStream myInput = myContext.getAssets().open("database/"+DB_NAME);
String outFileName = DB_PATH + DB_NAME;
OutputStream myOutput = new FileOutputStream(outFileName);

byte[] buffer = new byte[1024];
int length;
	while ((length = myInput.read(buffer))>0){
		myOutput.write(buffer, 0, length);
	}

myOutput.flush();
myOutput.close();
myInput.close();
 
}
 
public void openDataBase() throws SQLiteException{
 

String myPath = DB_PATH + DB_NAME;
myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
}
 
@Override
public synchronized void close() {
 
if(myDataBase != null)
myDataBase.close();
 
super.close();
 
}
 
@Override
public void onCreate(SQLiteDatabase db) {
 
}
 
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
}
 
}