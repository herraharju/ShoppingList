package domain.com.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class ShowList extends AppCompatActivity implements View.OnClickListener
{
    private Button btn_close;
    private EditText et_items;
    private DbHelper _dbHelper;
    private ArrayList<ShoppingListItem> shoppingListItems;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        shoppingListItems = new ArrayList<ShoppingListItem>();
        _dbHelper = new DbHelper(this);
        ReadValuesFromDataBase();
        et_items = (EditText)findViewById(R.id.et_items);


            for(int i = 0; i<shoppingListItems.size();i++){
                String product = shoppingListItems.get(i).product;
                int amount = shoppingListItems.get(i).amount;
                String unit = shoppingListItems.get(i).unit;

                et_items.setText(et_items.getText()+"\n"+product + " "+amount +" "+unit );
            }
        btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.btn_close){
            finish();
        }
    }
    public void ReadValuesFromDataBase()
    {
        SQLiteDatabase db = _dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
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
}
