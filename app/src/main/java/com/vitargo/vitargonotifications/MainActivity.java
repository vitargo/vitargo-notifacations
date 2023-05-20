package com.vitargo.vitargonotifications;

import android.app.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.snackbar.Snackbar;
import com.vitargo.vitargonotifications.databinding.ActivityMainBinding;
import com.vitargo.vitargonotifications.databinding.FragmentFirstBinding;
import com.vitargo.vitargonotifications.db.CustomNotification;
import com.vitargo.vitargonotifications.db.NotificationContract;
import com.vitargo.vitargonotifications.db.NotificationDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddNotificationDialog.AddNotificationDialogListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private NotificationDBHelper helper;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "Start onCreate...");
        super.onCreate(savedInstanceState);

        helper = new NotificationDBHelper(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (Build.VERSION.SDK_INT >= 33 || !NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},101);
            }
            else {
                createChannel();
            }
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNotificationDialog d = new AddNotificationDialog();
                d.show(getSupportFragmentManager(), "AddPlayerDialog");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, CustomNotification notification) throws ParseException {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotificationContract.NotificationList.COLUMN_TEXT, notification.getText());
        values.put(NotificationContract.NotificationList.COLUMN_TITLE, notification.getTitle());
        values.put(NotificationContract.NotificationList.COLUMN_DATESTAMP, notification.getDate());
        long newRowId = db.insert(NotificationContract.NotificationList.TABLE_NAME, null, values);
        dialog.getDialog().cancel();

        String myDate = notification.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = sdf.parse(myDate);
        long notificationTime = date.getTime();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("NOTIFICATION_ID" , 0) ;
        intent.putExtra("NOTIFICATION" , getNotification(notification.getText(), notification.getTitle())) ;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
        MainActivity.this.recreate();
    }

    private Notification getNotification (String text, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( title ) ;
        builder.setContentText(text) ;
        builder.setSmallIcon(R.drawable.baseline_book_black_24dp) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private void createChannel(){
        NotificationManager mNotificationManager = (NotificationManager) getBaseContext().getSystemService(
                    NOTIFICATION_SERVICE);
        NotificationChannel infoChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            infoChannel = new NotificationChannel("NOTIFICATION_CHANNEL_ID_INFO",
                    "getString(R.string.notification_channel_name_info)", NotificationManager.IMPORTANCE_DEFAULT);

            infoChannel.setDescription("getString(R.string.notification_channel_description_info)");
            infoChannel.enableLights(false);
            infoChannel.enableVibration(false);
            mNotificationManager.createNotificationChannel(infoChannel);
        }
    }

    @Override
    protected void onStart() {
        Log.d("onStart", "Start onStart...");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy", "Start onDestroy...");
        super.onDestroy();
    }
}