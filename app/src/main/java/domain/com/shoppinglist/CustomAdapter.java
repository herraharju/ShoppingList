package domain.com.shoppinglist;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MrKohvi on 17.10.2017.
 */

public class CustomAdapter extends BaseAdapter
{
    private ArrayList<ShoppingListItem> items;
    private Context _context;
    private LayoutInflater _inflater;
    private EditText et_prod;
    private EditText et_am;
    private EditText et_unit;

    public CustomAdapter(Context context, final ArrayList<ShoppingListItem> items){
        this._context = context;
        this.items = items;
        this._inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = _inflater.inflate(R.layout.custom_list_item, null);
        et_prod = (EditText) convertView.findViewById(R.id.et_product);
        et_am   = (EditText) convertView.findViewById(R.id.et_amount);
        et_unit = (EditText) convertView.findViewById(R.id.et_unit);

        et_prod.setText(items.get(position).product);
        et_am.setText(Integer.toString(items.get(position).amount));
        et_unit.setText(items.get(position).unit);
        addTextWatchers(position);

        return convertView;
    }

    //Adds listeners for edittext boxes and to get updated values into list
    public void addTextWatchers(final int position){
        et_prod.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                items.get(position).product = s.toString();
            }
        });
        et_am.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                items.get(position).amount = Integer.parseInt(s.toString());
            }
        });
        et_unit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                items.get(position).unit = s.toString();
            }
        });
    }
}
