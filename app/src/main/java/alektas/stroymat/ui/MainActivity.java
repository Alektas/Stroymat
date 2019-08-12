package alektas.stroymat.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import alektas.stroymat.R;
import alektas.stroymat.ui.pricelist.PricelistFragment;
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
 * В оправдание лишь приведу крайне сжатые сроки,
 * минимальный шанс на получение достойной оплаты, отсутствие опыта
 * и уверенность в том, что сюда никто не заглянет.
 *
 * @author Alektas, студент-самоучка
 */
public class MainActivity extends AppCompatActivity implements PricelistFragment.FragmentListener {
    private static final String TAG = "MainActivity";
    private PricelistViewModel mPricelistViewModel;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Убираем сплэш
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigation();

        FirebaseMessaging.getInstance().subscribeToTopic("discounts");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    Log.d(TAG, "token=" + token);
                });

        mPricelistViewModel = ViewModelProviders.of(this).get(PricelistViewModel.class);
        mPricelistViewModel.getSelectedItem().observe(this, item -> {
            navController.navigate(R.id.action_pricelistFragment_to_itemFragment);
        });
    }

    private void setupNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public void onFragmentCreated(String tag) {
        // Установка навигации AppBar с DrawerLayout во фрагменте прайслиста
        if (tag.equals(PricelistFragment.TAG)) {
            Fragment fragment =
                    getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            if (fragment == null) return;
            drawerLayout = fragment.requireView().findViewById(R.id.drawer_layout);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Вызывает обработчик из PricelistFragment#onOptionsItemSelected, который закрывает меню
        if (isDrawerOpen()) return super.onSupportNavigateUp();
        // Открывает меню
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (closeDrawer()) return;
        super.onBackPressed();
    }

    private boolean closeDrawer() {
        if (isDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.action_about_shop == item.getItemId()) {
            DialogFragment dialog = new AboutShopDialog();
            dialog.show(getSupportFragmentManager(), "AboutShopDialog");
            return true;
        } else if (R.id.action_about_app == item.getItemId()) {
            DialogFragment dialog = new AboutAppDialog();
            dialog.show(getSupportFragmentManager(), "AboutAppDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCalcBtnClick(View view) {
        switch(view.getId()) {
            case R.id.btn_calc_profnastil:
                navController.navigate(R.id.action_calculatorsFragment_to_profnastilFragment);
                break;
            case R.id.btn_calc_siding:
                navController.navigate(R.id.action_calculatorsFragment_to_sidingFragment);
                break;
            case R.id.btn_calc_trotuar:
                navController.navigate(R.id.action_calculatorsFragment_to_trotuarFragment);
                break;
            case R.id.btn_calc_stove:
                navController.navigate(R.id.action_calculatorsFragment_to_stoveFragment);
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
            cancelSearch();
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String itemArticle = intent.getDataString();
            onSearchItemSelected(itemArticle);
        }
    }

    private void onSearchEnter(String query) {
        mPricelistViewModel.onSearchEnter(query);
        RecyclerView pricelistRv = findViewById(R.id.pricelist);
        if (pricelistRv != null) pricelistRv.smoothScrollToPosition(0);
    }

    private void cancelSearch() {
        MenuItem menuSearch = toolbar.getMenu().findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuSearch.getActionView();
        searchView.onActionViewCollapsed();
    }

    private void onSearchItemSelected(String articleString) {
        try {
            int article = Integer.valueOf(articleString);
            mPricelistViewModel.onItemSelected(article);
        } catch (NumberFormatException e) {
            Log.e(TAG, "onSearchItemSelected: item article " + articleString + " is not a number", e);
        }
    }

    public void onLinkClick(View view) {
        String link = null;

        switch (view.getId()) {
            case R.id.address: {
                link = getString(R.string.about_shop_address_link);
                break;
            }

            case R.id.apache_link: {
                link = getString(R.string.apache_license_link);
                break;
            }

            case R.id.google_link: {
                link = getString(R.string.google_material_link);
                break;
            }

            case R.id.license_link: {
                link = getString(R.string.about_app_license_link);
                break;
            }
        }

        if (link == null) return;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }

}