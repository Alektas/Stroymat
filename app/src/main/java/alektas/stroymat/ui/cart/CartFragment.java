package alektas.stroymat.ui.cart;

import androidx.lifecycle.ViewModelProviders;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.auth.AuthManager;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.utils.StringUtils;

public class CartFragment extends Fragment {
    private CartViewModel mViewModel;
    private CartAdapter mAdapter;
    private TextView mPriceText;
    private static final String EMAIL_ADDRESS = "alektas@inbox.ru";
    private static final String EMAIL_SUBJECT = "Заказ товаров";

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_fragment, container, false);

        RecyclerView rv = view.findViewById(R.id.cart_list);
        LinearLayoutManager mng = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(mng);

        mPriceText = view.findViewById(R.id.order_price);

        Button orderBtn = view.findViewById(R.id.cart_order_btn);
        orderBtn.setOnClickListener(v -> {
            String body = makeOrderBody(mAdapter.getItems());
            sendEmail(EMAIL_ADDRESS, EMAIL_SUBJECT, body);
//            mViewModel.clearCart();
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AuthManager auth = new AuthManager(requireContext());
        NavController navController = Navigation.findNavController(requireView());
        int curDestId = navController.getCurrentDestination() == null ?
                0 : navController.getCurrentDestination().getId();
        if (curDestId == R.id.cartFragment && !auth.isLogin()) {
            navController.navigate(R.id.loginFragment);
        }

        mViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        mAdapter = new CartAdapter(mViewModel);
        RecyclerView rv = requireView().findViewById(R.id.cart_list);
        rv.setAdapter(mAdapter);
        mViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            mAdapter.setItems(items);
            mViewModel.recalcPrice();
        });
        mViewModel.getPrice().observe(getViewLifecycleOwner(), price -> {
            mPriceText.setText(StringUtils.formatPrice(price));
        });
    }

    private String makeOrderBody(List<CartItem> items) {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences(getString(R.string.PREFS_LOGIN_KEY), Context.MODE_PRIVATE);
        String surname = prefs.getString(getString(R.string.LOGIN_SURNAME_KEY), "");
        String name = prefs.getString(getString(R.string.LOGIN_NAME_KEY), "");
        String phone = prefs.getString(getString(R.string.LOGIN_PHONE_KEY), "");

        StringBuilder sb = new StringBuilder();
        sb.append("------ Покупатель ------")
                .append("\nФамилия: ").append(surname)
                .append("\nИмя: ").append(name)
                .append("\nНомер: ").append(phone)
        .append("\n---------------------------------\n\n");
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            sb.append(i+1).append(". ").append(item.getName())
                    .append("\nАртикул: ").append(item.getArticle())
                    .append("\nКоличество: ").append(item.getQuantity())
                    .append("\nЦена: ")
                    .append(StringUtils.formatOrderPrice(item.getQuantity() * item.getPrice()))
                    .append("\n\n");
        }
        sb.append("---------------------------------\n")
        .append("Итого: ").append(mPriceText.getText()).append(" руб\n");

        return sb.toString();
    }

    private boolean sendEmail(String email, String subject, String body) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Отправка заказа..."));
            return true;
        } catch (ActivityNotFoundException e) {
            // Handle case where no email app is available
            Toast.makeText(getContext(),
                    "Для заказа товаров установите приложение электронной почты",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
