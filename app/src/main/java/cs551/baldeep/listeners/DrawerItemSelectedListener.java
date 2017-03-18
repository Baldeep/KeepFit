package cs551.baldeep.listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.R;
import cs551.baldeep.keepfit.SettingsPage;

public class DrawerItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;
    private DrawerLayout drawer;
    private GoalDAO goalDAO;

    public DrawerItemSelectedListener(Activity activity, GoalDAO goalDAO, DrawerLayout drawer){
        this.activity = activity;
        this.goalDAO = goalDAO;
        this.drawer = drawer;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_settings){
            drawer.closeDrawer(GravityCompat.START);
            Intent settingsIntent = new Intent(activity, SettingsPage.class);
            activity.startActivity(settingsIntent);

        } else if(item.getItemId() == R.id.nav_view_home){

        }
        return true;
    }
}
