package domain.com.shoppinglist;

import android.provider.BaseColumns;

/**
 * Created by MrKohvi on 17.10.2017.
 */

public class DbConstants
{
    private DbConstants(){}

    public static class ShoppingItem implements BaseColumns{
        public static final String TABLE_NAME = "shoppinglist";
        public static final String COLUMN_NAME_PRODUCT = "product";
        public static final String COLUMN_NAME_UNIT = "unit";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_MODIFYDATE = "modifydate";
    }
}

