package com.tarun.saini.recipeDiary.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.tarun.saini.recipeDiary.R;
import com.tarun.saini.recipeDiary.UI.RecipeActivity;
import com.tarun.saini.recipeDiary.UI.ShoppingListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class ShoppingListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shopping_list_widget);

        setRemoteAdapter(context, views);
        Intent intent=new Intent(context, ShoppingListActivity.class);
        Intent recipeIntent=new Intent(context, RecipeActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
        PendingIntent pendingIntentRecipe=PendingIntent.getActivity(context,0,recipeIntent,0);
        views.setOnClickPendingIntent(R.id.widget_layout_main,pendingIntent);
        views.setOnClickPendingIntent(R.id.button,pendingIntentRecipe);
        views.setEmptyView(R.id.widget_list,R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            //RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, ShoppingListWidget.class));
        context.sendBroadcast(intent);
        //Toast.makeText(context, "CALLED SERVICE", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, ShoppingListWidget.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_list);
        }
        super.onReceive(context, intent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WidgetService.class));
    }
}

