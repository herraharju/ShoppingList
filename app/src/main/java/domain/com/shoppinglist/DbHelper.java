package domain.com.shoppinglist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by MrKohvi on 17.10.2017.
 */


public class DbHelper extends SQLiteOpenHelper
{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "ShopListItem.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DbConstants.ShoppingItem.TABLE_NAME + " (" +
                    DbConstants.ShoppingItem._ID + " INTEGER PRIMARY KEY," +
                    DbConstants.ShoppingItem.COLUMN_NAME_PRODUCT+ " TEXT," +
                    DbConstants.ShoppingItem.COLUMN_NAME_AMOUNT + " INTEGER,"+
                    DbConstants.ShoppingItem.COLUMN_NAME_UNIT + " TEXT,"+
                    DbConstants.ShoppingItem.COLUMN_NAME_MODIFYDATE + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbConstants.ShoppingItem.TABLE_NAME;

    public static final String SQL_GET_AMOUNT_SUM =
            "SELECT sum("+DbConstants.ShoppingItem.COLUMN_NAME_AMOUNT+") FROM "+
                    DbConstants.ShoppingItem.TABLE_NAME;
    public static final String SQL_GET_MAX_DATE =
            "SELECT max("+DbConstants.ShoppingItem.COLUMN_NAME_MODIFYDATE+") FROM "+
                    DbConstants.ShoppingItem.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public int getRowCount(SQLiteDatabase db)
    {
        int count = 0;
        Cursor cursor = db.query(
                true,
                DbConstants.ShoppingItem.TABLE_NAME,
                new String[] {DbConstants.ShoppingItem._ID},
                null,
                null,
                null,
                null,
                null,
                null
        );

        count = cursor.getCount();
        cursor.close();
        return count;
    }

    //counts total of product amounts in db table
    public int getAmountSum(SQLiteDatabase db)
    {
        int sum = 0;
        Cursor cursor = db.rawQuery(SQL_GET_AMOUNT_SUM, null);

        if(cursor.moveToFirst()){
            sum = cursor.getInt(0);
        }
        cursor.close();
        return sum;
    }

    public long getLatestModifyDate(SQLiteDatabase db){
        long date=0;
        Cursor cursor = db.rawQuery(SQL_GET_MAX_DATE,null);

        if(cursor.moveToFirst()){
            date = cursor.getLong(0);
        }
        cursor.close();
        return date;
    }
}
