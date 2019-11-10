package alektas.stroymat.ui.cart;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import alektas.stroymat.R;

public class OrderCompleteDialog extends DialogFragment {
    private CartViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getParentFragment() != null) {
            viewModel = ViewModelProviders.of(getParentFragment()).get(CartViewModel.class);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.order_sucess)
                .setMessage(R.string.clear_cart_question)
                .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    if (viewModel != null) viewModel.clearCart();
                })
                .setNegativeButton(android.R.string.no, (dialog, id) -> {
                    // User cancelled the dialog
                });
        return builder.create();
    }
}
