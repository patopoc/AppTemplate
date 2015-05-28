package app.com.apptemplate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.SubMenu;

import com.facebook.TestUserManager;

import app.com.apptemplate.interfaces.RedirectInterface;
import app.com.apptemplate.modules.ModBlank;
import app.com.apptemplate.modules.ModConf;
import app.com.apptemplate.modules.ModItemsDetail;
import app.com.apptemplate.modules.ModLogin2;
import app.com.apptemplate.modules.ModReport;
import app.com.apptemplate.navigation.NavigationDrawerFragment;


public class AppMain extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        RedirectInterface {

    final String TAG="AppMain";

    public static String SELECT_FRAGMENT="selectFragment";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_main_drawer);

        Bundle extras = getIntent().getExtras();
        int defaultFragment=3;

        if(extras != null){
            defaultFragment= extras.getInt(SELECT_FRAGMENT,defaultFragment);
        }

        SharedPreferences preferences= getSharedPreferences("widgetConf",Context.MODE_PRIVATE);
        boolean widgetEnabled= preferences.getBoolean("widgetEnabled",false);
        if(!widgetEnabled){
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.enable_widget_help))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }

        Log.d(TAG,"DISPLAY DENSITY: "+getResources().getDisplayMetrics().density);

        if(AppConf.navigation == AppConf.NavigationType.DRAWER) {
            setContentView(R.layout.activity_app_main_drawer);
            useNavigationDrawer();
        }
        else if(AppConf.navigation == AppConf.NavigationType.TAB){

        }
        else if(AppConf.navigation == AppConf.NavigationType.VIEWPAGER){

        }
        else if(AppConf.navigation == AppConf.NavigationType.GRID){

        }
        else if(AppConf.navigation == AppConf.NavigationType.NONE){
            setContentView(R.layout.activity_app_main_none);
        }

        setFragment(defaultFragment);

    }

    public void useNavigationDrawer(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public void redirectToLogin(int modPosition){
        Fragment modLogin= new ModLogin2();
        Bundle args= new Bundle();
        args.putInt("modPosition",modPosition);
        modLogin.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container,modLogin)
                .commit();
    }

    public void redirectMod(int pos){
        onNavigationDrawerItemSelected(pos);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        setFragment(position);
    }

    public void setFragment(int position){
        Fragment modFragment= null;
        switch (position){
            case 0:
                modFragment= new ModBlank();
                break;
            case 1:
                modFragment= new ModItemsDetail();
                break;
            case 2:
                modFragment= new ModConf();
                break;
            case 3:
                modFragment= new ModReport();

        }

        if(modFragment != null){
            Bundle args= new Bundle();
            args.putInt("modPosition",position);
            modFragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, modFragment)
                    .addToBackStack("NavStack")
                    .commit();
        }
    }

    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 2)
            super.onBackPressed();
        else
            finish();
    }

    public void onSectionAttached(int number) {
        /*switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }*/
        mTitle= getResources().getStringArray(R.array.main_menu_items)[number];
        Log.d(TAG,"current fragment "+number);
        restoreActionBar();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(AppConf.navigation == AppConf.NavigationType.DRAWER) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.app_main, menu);
                restoreActionBar();
            }
        }
        else if(AppConf.navigation == AppConf.NavigationType.NONE){
            //getMenuInflater().inflate(R.menu.app_main, menu);
            //restoreActionBar();
            return true;
        }
        setShareAction(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d(TAG,"selected: "+item.getItemId());
        switch (id){
            case R.id.action_settings:
                setFragment(2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareAction(Menu menu) {
        MenuItem menuItem= menu.findItem(R.id.action_share);
        mShareActionProvider= (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        Log.d(TAG,"mShared Created");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        Log.d(TAG,"actionProvider: "+mShareActionProvider);
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(sendIntent);
        }
    }

}
