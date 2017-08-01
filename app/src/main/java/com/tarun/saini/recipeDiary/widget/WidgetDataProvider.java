package com.tarun.saini.recipeDiary.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.tarun.saini.recipeDiary.R;

import static com.tarun.saini.recipeDiary.data.RecipeContract.BASE_CONTENT_URI;
import static com.tarun.saini.recipeDiary.data.RecipeContract.PATH_LIST;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_ID;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_INGREDIENT;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_MEASURE;
import static com.tarun.saini.recipeDiary.data.RecipeContract.ShoppingListEntry.KEY_QUANTITY;

/**
 * Created by Tarun on 27-07-2017.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {


    Context context;
    Intent intent;
    Cursor cursor;

    private static final String[] SHOPPINGLIST_COLUMS = {KEY_ID,
            KEY_QUANTITY,
            KEY_MEASURE,
            KEY_INGREDIENT};


    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        Uri LIST_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LIST);

        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cursor = context.getContentResolver().query(LIST_URI,
                    SHOPPINGLIST_COLUMS,
                    null,
                    null,
                    null,
                    null);
            Binder.restoreCallingIdentity(identityToken);
        }

    }

    @Override
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        if (cursor == null || !cursor.moveToPosition(position)||cursor.getCount()==0) {
            return null;
        }
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),
                R.layout.shopping_list_item);
        String ingredient=cursor.getString(cursor.getColumnIndex(KEY_INGREDIENT));
        String measure=cursor.getString(cursor.getColumnIndex(KEY_MEASURE));
        String quantity=cursor.getString(cursor.getColumnIndex(KEY_QUANTITY));
        remoteViews.setTextViewText(R.id.shopping_list_description,ingredient+" "+quantity+" "+measure);

        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(context.getPackageName(),
                R.layout.shopping_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
