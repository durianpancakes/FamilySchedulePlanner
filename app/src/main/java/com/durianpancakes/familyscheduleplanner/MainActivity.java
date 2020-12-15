package com.durianpancakes.familyscheduleplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnHomeGroupItemClickListener {
    public static final int RC_SIGN_IN = 1231;

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    private NavigationView navigationView;
    private View headerView;
    private DrawerLayout drawer;
    private TextView navBarName;
    private TextView navBarEmail;

    private GroupViewModel groupViewModel;

    // Temporary implementations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bindUiItems();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_calendar, R.id.nav_signin, R.id.nav_signout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setupNavItemListeners();

        mAuth = FirebaseAuth.getInstance();
        updateUI(mAuth.getCurrentUser());

        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        groupViewModel.getSelectedGroup().observe(this, group -> {
            Intent intent = new Intent(this, GroupActivity.class);
            String groupId = group.getId();
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });
    }

    private void bindUiItems() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navBarName = headerView.findViewById(R.id.name);
        navBarEmail = headerView.findViewById(R.id.email);
    }

    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void setupNavItemListeners() {
        navigationView.getMenu().findItem(R.id.nav_signin).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                signIn();
                return true;
            }
        });

        navigationView.getMenu().findItem(R.id.nav_signout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                signOut();
                return true;
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Signed in
            navigationView.getMenu().findItem(R.id.nav_signin)
                    .setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signout)
                    .setVisible(true);
            navBarName.setText(user.getDisplayName());
            navBarEmail.setText(user.getEmail());
        } else {
            // Signed out
            navigationView.getMenu().findItem(R.id.nav_signin)
                    .setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signout)
                    .setVisible(false);
            navBarName.setText(R.string.sign_in_prompt);
            navBarEmail.setText("");
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();
                boolean isNewUser = response.isNewUser();
                updateUI(user);
                if (isNewUser) {
                    String email = user.getEmail();
                    String name = user.getDisplayName();
                    String uid = user.getUid();
                    User newUser = new User(new ArrayList<>(), uid, name);
                    DatabaseHelper databaseHelper = new DatabaseHelper();
                    databaseHelper.addUser(uid, newUser);
                }
            } else {
                updateUI(null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(Group group) {

    }
}