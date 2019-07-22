package alektas.stroymat.ui.pricelist;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.Category;

public class PricelistFragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "PricelistFragment";
    private FragmentListener mFragmentListener;
    private PricelistViewModel mViewModel;
    private PricelistAdapter pricelistAdapter;
    private NavigationView slideMenu;

    public interface FragmentListener {
        void onFragmentCreated(String tag);
    }

    public static PricelistFragment newInstance() {
        return new PricelistFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mFragmentListener = (FragmentListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: activity must implement " +
                    FragmentListener.class.getSimpleName(), e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pricelist_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slideMenu = view.findViewById(R.id.slide_nav_view);
        slideMenu.setNavigationItemSelectedListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mViewModel = ViewModelProviders.of(requireActivity()).get(PricelistViewModel.class);
        pricelistAdapter = new PricelistAdapter(db, mViewModel);
        RecyclerView pricelistRv = view.findViewById(R.id.pricelist);
        pricelistRv.setLayoutManager(new LinearLayoutManager(getContext()));
        pricelistRv.setHasFixedSize(true);
        pricelistRv.setAdapter(pricelistAdapter);

        mViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            updateDrawerMenu(categories);
        });
        mViewModel.getSelectedCategory().observe(getViewLifecycleOwner(), category -> {
            slideMenu.setCheckedItem(category);
        });
        mViewModel.getItemsLoading().observe(getViewLifecycleOwner(), isLoaded -> {
            requireActivity().findViewById(R.id.loading_bar)
                    .setVisibility(isLoaded ? View.GONE : View.VISIBLE);
        });
        mViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            pricelistAdapter.setItems(items);
            view.requestLayout();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentListener.onFragmentCreated(TAG);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pricelist_toolbar, menu);
        SearchManager searchManager =
                (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
    }

    private void updateDrawerMenu(List<Category> categories) {
        Menu menu = slideMenu.getMenu();
        menu.clear();

        menu.add(R.id.drawer_menu_group, 0, 0, R.string.all);
        for (Category category : categories) {
            menu.add(R.id.drawer_menu_group, category.getCateg(),
                    category.getCateg(), category.getCategName());
        }
        menu.setGroupCheckable(R.id.drawer_menu_group, true, true);

        slideMenu.invalidate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setCategory(item.getItemId());

        DrawerLayout drawer = requireView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        RecyclerView pricelistRv = requireView().findViewById(R.id.pricelist);
        pricelistRv.smoothScrollToPosition(0);

        return true;
    }

    private void setCategory(int category) {
        mViewModel.setCategory(category);
    }

}
