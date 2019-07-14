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

import alektas.stroymat.R;

public class PricelistFragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "PricelistFragment";
    private FragmentListener mFragmentListener;
    private PricelistViewModel mViewModel;
    private PricelistAdapter pricelistAdapter;

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
        NavigationView slideMenu = view.findViewById(R.id.slide_nav_view);
        slideMenu.setNavigationItemSelectedListener(this);

        mViewModel = ViewModelProviders.of(requireActivity()).get(PricelistViewModel.class);
        pricelistAdapter = new PricelistAdapter(mViewModel);
        RecyclerView pricelistRv = view.findViewById(R.id.pricelist);
        pricelistRv.setLayoutManager(new LinearLayoutManager(getContext()));
        pricelistRv.setHasFixedSize(true);
        pricelistRv.setAdapter(pricelistAdapter);
        mViewModel.getItemsLoading().observe(getViewLifecycleOwner(), isLoaded -> {
            requireActivity().findViewById(R.id.loading_bar)
                    .setVisibility(isLoaded ? View.GONE : View.VISIBLE);
        });
        mViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            pricelistAdapter.setItems(items);
            pricelistRv.smoothScrollToPosition(0);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            setCategory(0);
        } else if (id == R.id.nav_kirpich) {
            setCategory(1);
        } else if (id == R.id.nav_gaz_bloki) {
            setCategory(2);
        } else if (id == R.id.nav_keram_bloki) {
            setCategory(3);
        } else if (id == R.id.nav_utepl) {
            setCategory(4);
        } else if (id == R.id.nav_cement) {
            setCategory(5);
        } else if (id == R.id.nav_keramzit) {
            setCategory(6);
        } else if (id == R.id.nav_plenki) {
            setCategory(7);
        } else if (id == R.id.nav_teploizol) {
            setCategory(8);
        } else if (id == R.id.nav_osb_fanera_gkl) {
            setCategory(9);
        } else if (id == R.id.nav_krepej) {
            setCategory(11);
        } else if (id == R.id.nav_krovel_mat) {
            setCategory(12);
        } else if (id == R.id.nav_profil_kompl) {
            setCategory(13);
        } else if (id == R.id.nav_npvh_tubes) {
            setCategory(14);
        } else if (id == R.id.nav_grunt_shutakurka) {
            setCategory(15);
        } else if (id == R.id.nav_lenty_skotchi) {
            setCategory(16);
        } else if (id == R.id.nav_kley_pena) {
            setCategory(17);
        } else if (id == R.id.nav_raznoe) {
            setCategory(18);
        } else if (id == R.id.nav_plita_trotuar) {
            setCategory(19);
        } else if (id == R.id.nav_bordury) {
            setCategory(36);
        } else if (id == R.id.nav_profnastil) {
            setCategory(20);
        } else if (id == R.id.nav_metallocher) {
            setCategory(37);
        } else if (id == R.id.nav_metalloizd) {
            setCategory(21);
        } else if (id == R.id.nav_plast_vodostok) {
            setCategory(22);
        } else if (id == R.id.nav_lakokras_mat) {
            setCategory(23);
        } else if (id == R.id.nav_hoz_tov) {
            setCategory(24);
        } else if (id == R.id.nav_ankera) {
            setCategory(25);
        } else if (id == R.id.nav_gvozdi_str) {
            setCategory(26);
        } else if (id == R.id.nav_dubeli) {
            setCategory(27);
        } else if (id == R.id.nav_instr_abraz) {
            setCategory(28);
        } else if (id == R.id.nav_metr_krep) {
            setCategory(29);
        } else if (id == R.id.nav_perfor) {
            setCategory(30);
        } else if (id == R.id.nav_other) {
            setCategory(31);
        } else if (id == R.id.nav_samorez) {
            setCategory(32);
        } else if (id == R.id.nav_samorez_list_metal) {
            setCategory(33);
        } else if (id == R.id.nav_upakovka) {
            setCategory(34);
        } else if (id == R.id.nav_upakovka1) {
            setCategory(35);
        } else if (id == R.id.nav_zaglushki) {
            setCategory(38);
        } else if (id == R.id.nav_electrody) {
            setCategory(39);
        } else if (id == R.id.nav_smesi_pechi) {
            setCategory(40);
        } else if (id == R.id.nav_litye_pechi) {
            setCategory(41);
        } else if (id == R.id.nav_utepl_jut) {
            setCategory(42);
        } else if (id == R.id.nav_siding) {
            setCategory(50);
        } else if (id == R.id.nav_ankera1) {
            setCategory(51);
        } else if (id == R.id.nav_bity) {
            setCategory(52);
        } else if (id == R.id.nav_bolt_santeh) {
            setCategory(53);
        } else if (id == R.id.nav_bury_sverla) {
            setCategory(54);
        } else if (id == R.id.nav_ventil) {
            setCategory(55);
        } else if (id == R.id.nav_gvozdi_spec) {
            setCategory(56);
        } else if (id == R.id.nav_zaklepki) {
            setCategory(57);
        } else if (id == R.id.nav_instr_abraz1) {
            setCategory(58);
        } else if (id == R.id.nav_instr_otdel) {
            setCategory(59);
        } else if (id == R.id.nav_instr_izmer) {
            setCategory(60);
        } else if (id == R.id.nav_instr_malyar) {
            setCategory(61);
        } else if (id == R.id.nav_instr_slesar) {
            setCategory(62);
        } else if (id == R.id.nav_instr_stolyar) {
            setCategory(63);
        } else if (id == R.id.nav_homut) {
            setCategory(64);
        } else if (id == R.id.nav_shurupy_beton) {
            setCategory(65);
        } else if (id == R.id.nav_elec_roz) {
            setCategory(66);
        } else if (id == R.id.nav_elec_nakon) {
            setCategory(67);
        } else if (id == R.id.nav_raznoe1) {
            setCategory(68);
        } else if (id == R.id.nav_profnastil1) {
            setCategory(69);
        } else if (id == R.id.nav_kabel) {
            setCategory(70);
        } else if (id == R.id.nav_dobor) {
            setCategory(71);
        } else if (id == R.id.nav_plast_panel) {
            setCategory(80);
        } else if (id == R.id.nav_dobor1) {
            setCategory(81);
        } else if (id == R.id.nav_decor) {
            setCategory(82);
        } else if (id == R.id.nav_santeh) {
            setCategory(90);
        } else if (id == R.id.nav_cki) {
            setCategory(91);
        } else if (id == R.id.nav_teplicy) {
            setCategory(92);
        }

        DrawerLayout drawer = requireView().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setCategory(int category) {
        mViewModel.setCategory(category);
    }

}
