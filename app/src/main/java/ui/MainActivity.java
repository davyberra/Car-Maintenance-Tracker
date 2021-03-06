package ui;

import android.os.Bundle;

import com.example.carmaintenancetracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AppBarConfiguration appBarConfiguration;
    DrawerLayout drawerLayout;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        NavigationView navView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.dashboardFragment, R.id.carSelectFragment, R.id.servicesOverviewFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);





        FloatingActionButton fab = findViewById(R.id.fabPlusIcon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                navController.navigate(R.id.action_global_add_Vehicle_Fragment);
                fab.hide();
            }
        });
        setNavigationViewListener();

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

        switch (item.getItemId()) {
            case R.id.action_about:
                navController.navigate(R.id.action_global_aboutFragment);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_drawer_add_vehicle:
                navController.navigate(R.id.action_global_add_Vehicle_Fragment);
                break;

            case R.id.nav_drawer_select_vehicle:
                navController.navigate(R.id.action_global_carSelectFragment);
                break;

            case R.id.nav_drawer_dashboard:
                navController.navigate(R.id.action_global_dashboardFragment);
                break;

            case R.id.nav_drawer_services_overview:
                navController.navigate(R.id.action_global_servicesOverviewFragment);
                break;

            case R.id.nav_drawer_about:
                navController.navigate(R.id.action_global_aboutFragment);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void hideFabButtons() {
        FloatingActionButton fabAddMain = findViewById(R.id.fabPlusIcon);
        FloatingActionButton fabAddGas = findViewById(R.id.fabAddGas);
        FloatingActionButton fabAddService = findViewById(R.id.fabAddService);
        FloatingActionButton fabAddReminder = findViewById(R.id.fabAddReminder);
        FloatingActionButton fabAddMileage = findViewById(R.id.fabAddMileage);
        fabAddMain.hide();
        fabAddGas.hide();
        fabAddService.hide();
        fabAddReminder.hide();
        fabAddMileage.hide();
    }
}