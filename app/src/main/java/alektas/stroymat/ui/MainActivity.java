package alektas.stroymat.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.View;

import alektas.stroymat.R;
import alektas.stroymat.ui.pricelist.PricelistViewModel;

/**
 * Здравствуй, тот, кто решился поглубиться в дебри легаси-кода июля 2019 года.
 *
 * Прости меня.
 * Прости за отсутствие тестов.
 * Прости за отсутсвтвие документации.
 * Прости за транслит с русского.
 * Прости за спаггети-код.
 * Прости за хардкод.
 * Прости за корявое подобие архитектуры.
 * Прости и за многое другое, чего я не припомнил или о чем не ведаю.
 *
 * В оправдание лишь скажу, что у меня было крайне мало времени,
 * минимальный шанс на получение достойной оплаты, отсутствие опыта
 * и уверенность в том, что сюда никто не заглянет.
 *
 * @author Alektas, студент-самоучка
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private PricelistViewModel mPricelistViewModel;
    private Toolbar toolbar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appbarConfig = new AppBarConfiguration
                .Builder(R.id.pricelistFragment, R.id.calculatorsFragment, R.id.galleryFragment)
                        .build();
        toolbar = findViewById(R.id.toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appbarConfig);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);

        mPricelistViewModel = ViewModelProviders.of(this).get(PricelistViewModel.class);
        mPricelistViewModel.getSelectedItem().observe(this, item -> {
            navController.navigate(R.id.action_pricelistFragment_to_itemFragment);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    public void onCalcBtnClick(View view) {
        switch(view.getId()) {
            case R.id.btn_calc_profnastil:
                navController.navigate(R.id.action_calculatorsFragment_to_profnastilFragment);
                break;
            case R.id.btn_calc_siding:
                navController.navigate(R.id.action_calculatorsFragment_to_sidingFragment);
                break;
        }
    }

    // Used for search handle
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSearch(intent);
    }

    private void handleSearch(Intent intent) {
        /* Intent.ACTION_SEARCH - on enter text typed in search view
         * Intent.ACTION_VIEW - on click in search suggestions  */
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            onSearchEnter(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String itemArticle = intent.getDataString();
            onSearchItemSelected(itemArticle);
        }
    }

    private void onSearchEnter(String query) {
        mPricelistViewModel.onSearchEnter(query);
    }

    private void onSearchItemSelected(String articleString) {
        try {
            int article = Integer.valueOf(articleString);
            mPricelistViewModel.onItemSelected(article);
        } catch (NumberFormatException e) {
            Log.e(TAG, "onSearchItemSelected: item article " + articleString + " is not a number", e);
        }
    }

}