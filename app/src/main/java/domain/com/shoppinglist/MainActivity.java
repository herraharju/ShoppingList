package domain.com.shoppinglist;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private ListView listView;
    private Button btn_add, btn_show_list, btn_save;
    private ArrayList<ShoppingListItem> shoppingListItems;
    private CustomAdapter adapter;
    private String prod_name,prod_unit;
    private int prod_amount;
    private boolean wasPaused;
    private DbHelper _dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }
    private void Init(){

        _dbHelper = new DbHelper(getApplicationContext());
        wasPaused = false;

        //find elements from view
        listView = (ListView) findViewById(R.id.lv_main);
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_show_list = (Button)findViewById(R.id.btn_show_list);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setEnabled(false);

        //add click listeners to buttons
        InitOnClickListeners();
        shoppingListItems = new ArrayList<ShoppingListItem>();
        adapter = new CustomAdapter(this,shoppingListItems);
        listView.setAdapter(adapter);
    }

    public void InitOnClickListeners(){
        btn_add.setOnClickListener(this);
        btn_show_list.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.btn_add:
                CreateCustomDialog();
                break;
            case R.id.btn_show_list:
                shoppingListItems.clear();
                ReadValuesFromDataBase();
                adapter.notifyDataSetChanged();
                if(shoppingListItems.size() > 0){
                    btn_save.setEnabled(true);
                }
                else{
                    btn_save.setEnabled(false);
                }
                break;
            case R.id.btn_save:
                SaveChangesToDataBase();
                break;
            default:
                break;
        }
    }
    public void SaveChangesToDataBase(){
        // Create the database
        // Gets the data repository in write mode
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        for (int i = 0; i < shoppingListItems.size(); i++)
        {
            ShoppingListItem item = (ShoppingListItem) adapter.getItem(i);
            ContentValues values = new ContentValues();

            values.put(DbConstants.ShoppingItem.COLUMN_NAME_PRODUCT,item.product);
            values.put(DbConstants.ShoppingItem.COLUMN_NAME_AMOUNT, item.amount);
            values.put(DbConstants.ShoppingItem.COLUMN_NAME_UNIT, item.unit);
            values.put(DbConstants.ShoppingItem.COLUMN_NAME_MODIFYDATE,System.currentTimeMillis());

            //update table columns by row id
            db.update(DbConstants.ShoppingItem.TABLE_NAME,values,DbConstants.ShoppingItem._ID + "=" + shoppingListItems.get(i).id, null);
        }

    }
    public void ReadValuesFromDataBase()
    {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DbConstants.ShoppingItem._ID,
                DbConstants.ShoppingItem.COLUMN_NAME_PRODUCT,
                DbConstants.ShoppingItem.COLUMN_NAME_AMOUNT,
                DbConstants.ShoppingItem.COLUMN_NAME_UNIT
        };

        //Eliminate exception on searching for table without rows
        try
        {
            Cursor cursor = db.query(
                    DbConstants.ShoppingItem.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    null,                                   // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor.moveToNext())
            {
                ShoppingListItem item = new ShoppingListItem();
                item.id = cursor.getInt(cursor.getColumnIndexOrThrow(DbConstants.ShoppingItem._ID));
                item.product = cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.ShoppingItem.COLUMN_NAME_PRODUCT));
                item.amount = cursor.getInt(cursor.getColumnIndexOrThrow(DbConstants.ShoppingItem.COLUMN_NAME_AMOUNT));
                item.unit = cursor.getString(cursor.getColumnIndexOrThrow(DbConstants.ShoppingItem.COLUMN_NAME_UNIT));
                shoppingListItems.add(item);
//                Log.d("Values", "Name=" + item.product + ", amount=" + item.amount + ", unit=" + item.unit);
//                Toast.makeText(getApplicationContext(), ("Name=" + item.product + ", amount=" + item.amount + ", unit=" + item.unit), Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }
        catch (SQLException e)
        {
            e.getStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        wasPaused = true;
    }

     @Override
     protected void onResume()
     {
         super.onResume();
         if(wasPaused){
             shoppingListItems.clear();
             adapter.notifyDataSetChanged();
         }
     }

    private void CreateCustomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        //set view to its own property
        View dialogView = inflater.inflate(R.layout.add_dialog,null);

        //get elements from dialog view
        final EditText et_name = (EditText)dialogView.findViewById(R.id.et_product_name);
        final EditText et_amount = (EditText) dialogView.findViewById(R.id.et_amount);
        final EditText et_unit = (EditText)dialogView.findViewById(R.id.et_unit);
        builder.setView(dialogView)
                .setTitle(getString(R.string.main_add_dialog_title))
                .setPositiveButton(R.string.main_add, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //save this for older versions
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });


        final AlertDialog m_dialog = builder.create();
        m_dialog.show();

        //override positive button to show validation errors on dialog without dismiss
        m_dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean isValid = true;
                prod_name = et_name.getText().toString();
                prod_unit = et_unit.getText().toString();

                prod_amount = -1;
                try{
                    prod_amount = Integer.parseInt(et_amount.getText().toString());
                }
                catch(NumberFormatException e){
                    et_amount.setError(getString(R.string.error_amount));
                    isValid = false;
                }

                if(prod_name.isEmpty() || prod_name.length() < 3){
                    et_name.setError(getString(R.string.error_name));
                    isValid = false;
                }
                if (prod_amount <= 0){
                    et_amount.setError(getString(R.string.error_name));
                    isValid = false;
                }
                if(prod_unit.isEmpty()){
                    et_unit.setError(getString(R.string.error_unit));
                    isValid = false;
                }

                if(isValid){
                    m_dialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content),getString(R.string.snack_success),Snackbar.LENGTH_LONG).show();

                    addToDataBase(new ShoppingListItem(prod_name,prod_amount,prod_unit));
                    //make adapter to refresh listview
                    //adapter.notifyDataSetChanged();
                }
            }
        });

    }
    public void addToDataBase(ShoppingListItem item){
        // Create the database
        // Gets the data repository in write mode
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbConstants.ShoppingItem.COLUMN_NAME_PRODUCT, item.product);
        values.put(DbConstants.ShoppingItem.COLUMN_NAME_AMOUNT, item.amount);
        values.put(DbConstants.ShoppingItem.COLUMN_NAME_UNIT, item.unit);
        values.put(DbConstants.ShoppingItem.COLUMN_NAME_MODIFYDATE,System.currentTimeMillis());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbConstants.ShoppingItem.TABLE_NAME, null, values);
        item.id = newRowId;
        GiveDbTableInformation();
        //Toast.makeText(getApplicationContext(), "Db created", Toast.LENGTH_LONG).show();
    }

    public void GiveDbTableInformation(){
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        int productCnt;
        int totalAmount;
        long date;

        productCnt = _dbHelper.getRowCount(db);
        totalAmount = _dbHelper.getAmountSum(db);
        date = _dbHelper.getLatestModifyDate(db);

        CreateNotification(productCnt,totalAmount,date);
    }

    //builds and shows notification
    public void CreateNotification(int cnt, int amount,long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date date = new Date(millis);
        String s_Date = sdf.format(date);

        String message = "There are "+cnt+" different items.\n" +
                "Total amount of items is "+amount+".\n" +
                "Last modification date: "+s_Date;

        //init values for notification onclick event
        Intent intent = new Intent(this,ShowList.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        //notification build
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Shopping list notification!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message)) // to enlarge notification
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0,mBuilder.build());
    }


}
