package com.demp.trip.timeline.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.demp.trip.timeline.classes.LocationHistoryData;
import com.demp.trip.timeline.classes.SingleLocationHistoryData;

import java.util.ArrayList;
import java.util.List;

public class SqliteLocationTimeline
{
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "mylocationhistory.db";

    public static final String LOCATION_HISTORY_TABLE = "location_history";

    public static final String KEY_LOCATION_ROW_ID = "row_id";
    public static final String KEY_LOCATION_PLACE_NAME = "location_name";
    public static final String KEY_LOCATION_LATITUDE = "location_latitude";
    public static final String KEY_LOCATION_LONGITUDE = "location_longitude";
    public static final String KEY_LOCATION_ADDRESS = "location_address";
    public static final String KEY_LOCATION_CITY = "location_city";
    public static final String KEY_LOCATION_STATE = "location_state";
    public static final String KEY_LOCATION_COUNTRY = "location_country";
    public static final String KEY_LOCATION_PINCODE = "location_pincode";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_DATE = "date";

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    private Context context;

    private static final String CREATE_LOCATION_HISTORY_TABLE =
            "create table " + LOCATION_HISTORY_TABLE + " ("
                    + KEY_LOCATION_ROW_ID + " integer primary key autoincrement, "
                    + KEY_LOCATION_PLACE_NAME + " varchar(500), "
                    + KEY_LOCATION_LATITUDE + " varchar(200),"
                    + KEY_LOCATION_LONGITUDE + " varchar(200),"
                    + KEY_LOCATION_ADDRESS + " varchar(500),"
                    + KEY_LOCATION_CITY + " varchar(200),"
                    + KEY_LOCATION_STATE + " varchar(200),"
                    + KEY_LOCATION_COUNTRY + " varchar(200),"
                    + KEY_LOCATION_PINCODE + " varchar(200),"
                    + KEY_START_TIME + " varchar(200),"
                    + KEY_END_TIME + " varchar(200),"
                    + KEY_DATE + " varchar(50));";

    public SqliteLocationTimeline(Context c)
    {
        context = c;
    }

    public SqliteLocationTimeline open() throws SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public SqliteLocationTimeline openToRead() throws SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SqliteLocationTimeline openToWrite() throws SQLException
    {
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        sqLiteHelper.close();
    }

    public class SQLiteHelper extends SQLiteOpenHelper
    {

        public SQLiteHelper(Context context, String name,CursorFactory factory, int version)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // TODO Auto-generated method stub
            db.execSQL(CREATE_LOCATION_HISTORY_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // TODO Auto-generated method stub

        }

    }

    public long InsertLocationData(String placeName,String latitude,String longitude,
                                   String address,String city,String state,String country,String pincode,
                                   String startTime,String endTime,String savedDate)
    {
        ContentValues initialvalues = new ContentValues();
        initialvalues.put(KEY_LOCATION_PLACE_NAME, placeName);
        initialvalues.put(KEY_LOCATION_LATITUDE, latitude);
        initialvalues.put(KEY_LOCATION_LONGITUDE, longitude);
        initialvalues.put(KEY_LOCATION_ADDRESS, address);
        initialvalues.put(KEY_LOCATION_CITY, city);
        initialvalues.put(KEY_LOCATION_STATE, state);
        initialvalues.put(KEY_LOCATION_COUNTRY, country);
        initialvalues.put(KEY_LOCATION_PINCODE, pincode);
        initialvalues.put(KEY_START_TIME, startTime);
        initialvalues.put(KEY_END_TIME, endTime);
        initialvalues.put(KEY_DATE, savedDate);
        return sqLiteDatabase.insertOrThrow(LOCATION_HISTORY_TABLE, null, initialvalues);
        // TODO Auto-generated method stub
    }

    public long UpdateLocationData(int rowID,String placeName,String latitude,String longitude,
                                   String address,String city,String state,String country,String pincode,
                                   String startTime,String endTime,String savedDate)
    {
        ContentValues initialvalues = new ContentValues();
        initialvalues.put(KEY_LOCATION_PLACE_NAME, placeName);
        initialvalues.put(KEY_LOCATION_LATITUDE, latitude);
        initialvalues.put(KEY_LOCATION_LONGITUDE, longitude);
        initialvalues.put(KEY_LOCATION_ADDRESS, address);
        initialvalues.put(KEY_LOCATION_CITY, city);
        initialvalues.put(KEY_LOCATION_STATE, state);
        initialvalues.put(KEY_LOCATION_COUNTRY, country);
        initialvalues.put(KEY_LOCATION_PINCODE, pincode);
        initialvalues.put(KEY_START_TIME, startTime);
        initialvalues.put(KEY_END_TIME, endTime);
        initialvalues.put(KEY_DATE, savedDate);
        return sqLiteDatabase.update(LOCATION_HISTORY_TABLE,initialvalues, KEY_LOCATION_ROW_ID + "=" + rowID, null);
        // TODO Auto-generated method stub
    }
	
	LocationHistoryData location_data;
	public List getLocationHistoryList()
    {
    	ArrayList arraylist = new ArrayList();
    	sqLiteDatabase = sqLiteHelper.getReadableDatabase();
    	
        String Query = "SELECT * FROM " + LOCATION_HISTORY_TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        
        if (cursor != null)
        {
        	if(cursor.getCount() > 0)
            {
            	if (cursor.moveToFirst())
                {
                    do
                    {
                        location_data = new LocationHistoryData();
                        location_data.row_id = cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_ROW_ID));
                        location_data.location_name = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_PLACE_NAME));
                        location_data.location_latitude = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_LATITUDE));
                        location_data.location_longitude = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE));
                        location_data.location_address = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_ADDRESS));
                        location_data.location_city = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_CITY));
                        location_data.location_state = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_STATE));
                        location_data.location_country = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_COUNTRY));
                        location_data.location_pincode = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_PINCODE));
                        location_data.start_time = cursor.getString(cursor.getColumnIndex(KEY_START_TIME));
                        location_data.end_time = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));
                        location_data.saved_date = cursor.getString(cursor.getColumnIndex(KEY_DATE));

                        arraylist.add(location_data);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
            	//Log.e("Cursor ::", "Cursor null...");
            }
        }
        
        return arraylist;
    }

    SingleLocationHistoryData single_location_data;
    public List getSingleLocationHistoryList(int rowID)
    {
        ArrayList arraylist = new ArrayList();
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        String Query = "SELECT * FROM " + LOCATION_HISTORY_TABLE + " WHERE " + KEY_LOCATION_ROW_ID + "=" + rowID;
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);

        if (cursor != null)
        {
            if(cursor.getCount() > 0)
            {
                if (cursor.moveToFirst())
                {
                    do
                    {
                        single_location_data = new SingleLocationHistoryData();
                        single_location_data.row_id = cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_ROW_ID));
                        single_location_data.location_name = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_PLACE_NAME));
                        single_location_data.location_latitude = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_LATITUDE));
                        single_location_data.location_longitude = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE));
                        single_location_data.location_address = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_ADDRESS));
                        single_location_data.location_city = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_CITY));
                        single_location_data.location_state = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_STATE));
                        single_location_data.location_country = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_COUNTRY));
                        single_location_data.location_pincode = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_PINCODE));
                        single_location_data.start_time = cursor.getString(cursor.getColumnIndex(KEY_START_TIME));
                        single_location_data.end_time = cursor.getString(cursor.getColumnIndex(KEY_END_TIME));
                        single_location_data.saved_date = cursor.getString(cursor.getColumnIndex(KEY_DATE));

                        arraylist.add(single_location_data);
                    } while (cursor.moveToNext());
                }
            }
            else
            {
                //Log.e("Cursor ::", "Cursor null...");
            }
        }

        return arraylist;
    }

    public void deleteLocationData_byID(int id)
    {
        sqLiteDatabase.delete(LOCATION_HISTORY_TABLE, KEY_LOCATION_ROW_ID+"="+id, null);
    }

    public void deleteAllLocationData()
    {
        sqLiteDatabase.execSQL("delete from "+ LOCATION_HISTORY_TABLE);
    }

    public int GetLastInsertedRowID()
    {
        Cursor mCursor = null;

        int row_id = -1;

        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        String Query = "SELECT * FROM " + LOCATION_HISTORY_TABLE + " ORDER BY " + KEY_LOCATION_ROW_ID + " DESC limit 1";

        mCursor = sqLiteDatabase.rawQuery(Query, null);

        if(mCursor != null)
        {
            if(mCursor.getCount() > 0)
            {
                if (mCursor.moveToFirst())
                {
                    do
                    {
                        single_location_data = new SingleLocationHistoryData();
                        row_id = mCursor.getInt(mCursor.getColumnIndex(KEY_LOCATION_ROW_ID));
                    } while (mCursor.moveToNext());
                }
            }
            else
            {
                //Log.e("Cursor ::", "Cursor null...");
            }
        }
        else
        {
            return -1;
        }

        return row_id;
    }

    public boolean CheckLocationExist(String latitude,String longitude,String place)
    {
        Cursor mCursor = null;

        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        latitude = "'" + latitude + "'";
        longitude = "'" + longitude + "'";
        place = "'" + place + "'";
        String Query = "SELECT * FROM " + LOCATION_HISTORY_TABLE + " WHERE " + KEY_LOCATION_LATITUDE + "=" + latitude + " AND " + KEY_LOCATION_LONGITUDE + "=" + longitude + " OR " + KEY_LOCATION_ADDRESS + "=" + place;

        mCursor = sqLiteDatabase.rawQuery(Query, null);

        if(mCursor == null || mCursor.getCount() == 0)
        {
            return false;
        }
        else
        {
            return true;
        }

    }

    public int GetLocationRowID(String latitude,String longitude,String place)
    {
        Cursor mCursor = null;
        int row_id = -1;

        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        latitude = "'" + latitude + "'";
        longitude = "'" + longitude + "'";
        place = "'" + place + "'";
        String Query = "SELECT * FROM " + LOCATION_HISTORY_TABLE + " WHERE " + KEY_LOCATION_LATITUDE + "=" + latitude + " AND " + KEY_LOCATION_LONGITUDE + "=" + longitude + " OR " + KEY_LOCATION_ADDRESS + "=" + place;

        mCursor = sqLiteDatabase.rawQuery(Query, null);

        if (mCursor != null)
        {
            if(mCursor.getCount() > 0)
            {
                if (mCursor.moveToFirst())
                {
                    do
                    {
                        row_id = mCursor.getInt(mCursor.getColumnIndex(KEY_LOCATION_ROW_ID));

                    } while (mCursor.moveToNext());
                }
            }
            else
            {
                Log.e("Cursor ::", "Cursor null...");
            }
        }

        return row_id;
    }

}
