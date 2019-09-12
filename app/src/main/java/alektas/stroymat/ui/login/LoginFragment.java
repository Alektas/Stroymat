package alektas.stroymat.ui.login;

import androidx.activity.OnBackPressedCallback;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import alektas.stroymat.R;
import alektas.stroymat.auth.AuthManager;

public class LoginFragment extends Fragment {

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AuthManager auth = new AuthManager(requireContext());
        NavController navController = Navigation.findNavController(requireView());

        Button completeBtn = requireView().findViewById(R.id.login_complete_btn);
        EditText editSurname = requireView().findViewById(R.id.login_surname_input);
        EditText editName = requireView().findViewById(R.id.login_name_input);
        EditText editPhone = requireView().findViewById(R.id.login_phone_input);
        editPhone.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                completeBtn.performClick();
            }
            return false;
        });

        completeBtn.setOnClickListener(v -> {
            String surname = editSurname.getText().toString();
            String name = editName.getText().toString();
            String phone = editPhone.getText().toString();

            if (TextUtils.isEmpty(surname) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                Toast.makeText(requireContext(),
                        "Заполните все поля для осуществления заказов",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            auth.save(surname, name, phone);

            requireActivity().invalidateOptionsMenu();
            hideKeyboard();

            navController.popBackStack();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.popBackStack(R.id.pricelistFragment, false);
                    }
                });
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getView();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) requireContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
