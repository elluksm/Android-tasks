package com.weatherpal.weatherpal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HomeFragment.UpdateNotifListener, ServiceConnection {
    private NotificationService s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewPager);
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.menu_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.menu_message);
        tabLayout.getTabAt(2).setIcon(R.drawable.menu_location);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent= new Intent(this, NotificationService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        NotificationService.MyBinder b = (NotificationService.MyBinder) binder;
        s = b.getService();
        Toast.makeText(MainActivity.this, "NotificationService Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        s = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateNotifications(String message) {

        // use this to start and trigger a service
        Intent i= new Intent(getApplicationContext(), NotificationService.class);

        // potentially add data to the intent
        i.putExtra("Location", "Value for location");
        this.startService(i);

        if (s != null) {
            s.updateAllNotifications();
        }

    }

}
