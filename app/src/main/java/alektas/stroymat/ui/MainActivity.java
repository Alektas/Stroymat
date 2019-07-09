package alektas.stroymat.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.ui.calculators.CalculatorsFragment;
import alektas.stroymat.ui.calculators.profnastil.ProfnastilFragment;
import alektas.stroymat.ui.calculators.siding.SidingFragment;
import alektas.stroymat.ui.calculators.vodostock.VodostockFragment;
import alektas.stroymat.ui.gallery.GalleryFragment;
import alektas.stroymat.ui.pricelist.ItemFragment;
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
 * В оправдание лишь скажу, что у меня было крайне мало времени,
 * минимальный шанс на получение достойной оплаты, отсутствие опыта
 * и уверенность в том, что сюда никто не заглянет.
 *
 * @author Alektas, студент-самоучка
 */
public class MainActivity extends AppCompatActivity
        implements FragmentManager.OnBackStackChangedListener {
    private static final String TAG = "MainActivity";
    private static final String TAG_FRAGMENT_CALCULATORS = "TAG_FRAGMENT_CALCULATORS";
    private static final String TAG_FRAGMENT_CALC_PROFNASTIL = "TAG_FRAGMENT_CALC_PROFNASTIL";
    private static final String TAG_FRAGMENT_CALC_SIDING = "TAG_FRAGMENT_CALC_SIDING";
    private static final String TAG_FRAGMENT_CALC_VODOSTOCK = "TAG_FRAGMENT_CALC_VODOSTOCK";
    private static final String TAG_FRAGMENT_PRICELIST = "TAG_FRAGMENT_PRICELIST";
    private static final String TAG_FRAGMENT_PRICELIST_ITEM = "TAG_FRAGMENT_PRICELIST_ITEM";
    private static final String TAG_FRAGMENT_GALLERY = "TAG_FRAGMENT_GALLERY";

    private List<Fragment> fragments;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_pricelist:
                        switchFragment(0, TAG_FRAGMENT_PRICELIST);
                        return true;
                    case R.id.navigation_calculators:
                        switchFragment(1, TAG_FRAGMENT_CALCULATORS);
                        return true;
                    case R.id.navigation_gallery:
                        switchFragment(2, TAG_FRAGMENT_GALLERY);
                        return true;
                }
                return false;
            };
    private PricelistViewModel mPricelistViewModel;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        initToolbar();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        fragments = buildFragmentsList();
        BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switchFragment(0, TAG_FRAGMENT_PRICELIST);

        mPricelistViewModel = ViewModelProviders.of(this).get(PricelistViewModel.class);
        mPricelistViewModel.getSelectedItem().observe(this, item -> {
            switchBackStackFragment(6, TAG_FRAGMENT_PRICELIST_ITEM);
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
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

    @Override
    public void onBackStackChanged() {
        if (getSupportActionBar() == null) return;
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void switchFragment(int pos, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            int firstId = manager.getBackStackEntryAt(0).getId();
            manager.popBackStack(firstId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragments.get(pos), tag)
                .commit();
    }

    private void switchBackStackFragment(int pos, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragments.get(pos), tag)
                .addToBackStack(null)
                .commit();
    }

    private ArrayList<Fragment> buildFragmentsList() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        PricelistFragment pricelistFrag = PricelistFragment.newInstance();
        ItemFragment itemFrag = ItemFragment.newInstance();

        CalculatorsFragment calculatorsFrag = CalculatorsFragment.newInstance();
        ProfnastilFragment profnastilFrag = ProfnastilFragment.newInstance();
        SidingFragment sidingFrag = SidingFragment.newInstance();
        VodostockFragment vodostockFrag = VodostockFragment.newInstance();

        GalleryFragment galleryFrag = GalleryFragment.newInstance();

        fragments.add(pricelistFrag);
        fragments.add(calculatorsFrag);
        fragments.add(galleryFrag);
        fragments.add(profnastilFrag);
        fragments.add(sidingFrag);
        fragments.add(vodostockFrag);
        fragments.add(itemFrag);

        return fragments;
    }

    public void onCalcBtnClick(View view) {
        switch(view.getId()) {
            case R.id.btn_calc_profnastil:
                switchBackStackFragment(3, TAG_FRAGMENT_CALC_PROFNASTIL);
                break;
            case R.id.btn_calc_siding:
                switchBackStackFragment(4, TAG_FRAGMENT_CALC_SIDING);
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