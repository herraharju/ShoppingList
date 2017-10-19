package domain.com.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MrKohvi on 17.10.2017.
 */

public class ShoppingListItem
{
    public String product;
    public int amount;
    public String unit;
    public long id;

    public ShoppingListItem(){}
    public ShoppingListItem(String product, int amount, String unit)
    {
        this.product = product;
        this.amount = amount;
        this.unit = unit;
    }

}
