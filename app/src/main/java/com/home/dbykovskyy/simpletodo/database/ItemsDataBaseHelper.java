package com.home.dbykovskyy.simpletodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.home.dbykovskyy.simpletodo.Model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dbykovskyy on 8/19/15.
 */

public class ItemsDataBaseHelper extends SQLiteOpenHelper {

    private static ItemsDataBaseHelper sInstance;


    public static synchronized ItemsDataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ItemsDataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    static final String TAG = "SQL problem here >>>";

    // Database Info
    private static final String DATABASE_NAME = "postDb";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_ITEMS = "items";

    // Post Table Columns
    private static final String KEY_ITEM_ID = "_id";
    private static final String KEY_ITEM_NAME = "itemName";


    private ItemsDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


/*    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "create table "
                + TABLE_ITEMS + "(" + KEY_ITEM_ID
                + " integer primary key autoincrement, " + KEY_ITEM_NAME
                + " text not null);";

        db.execSQL(CREATE_ITEMS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }
    // Insert a post into the database
    public void addItem(Item item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_NAME, item.itemName);
            db.insert(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    // Get all posts in the database
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();


        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_ITEMS);


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Item newItem = new Item();
                    newItem.itemName = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME));
                    items.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get all Items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    // Delete Item
    public void deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ITEMS, KEY_ITEM_NAME + "='"+item.itemName+"'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all items");
        } finally {
            db.endTransaction();
        }
    }

    // Update the user's profile picture url
    public int updateIteme(Item item, Item newItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, newItem.itemName);

        // Updating an old item name with new itemName
        return db.update(TABLE_ITEMS, values, KEY_ITEM_NAME + "='"+item.itemName+"'",null);
    }

    // Delete all Items and users in the database
    public void deleteAllItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_ITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all items");
        } finally {
            db.endTransaction();
        }
    }


}
