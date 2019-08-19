package alektas.stroymat.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import alektas.stroymat.R;
import alektas.stroymat.utils.ResourcesUtils;
import alektas.stroymat.utils.StringUtils;

public class AboutAppDialog extends DialogFragment {
    private View mView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.about_app_dialog, null);

        initAboutMenuLinks();

        TextView versionText = mView.findViewById(R.id.version_text);
        String versionName = ResourcesUtils.getVersionName();
        versionText.setText(getResources().getString(R.string.about_app_version, versionName));

        builder.setTitle(R.string.about_app_title)
                .setView(mView)
                .setNeutralButton(android.R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                });
        return builder.create();
    }

    private void initAboutMenuLinks() {
        TextView tv = mView.findViewById(R.id.google_link);
        StringUtils.makeLinkable(tv, getString(R.string.google_material_link));
        tv = mView.findViewById(R.id.license_link);
        StringUtils.makeLinkable(tv, getString(R.string.about_app_license_link));
        tv = mView.findViewById(R.id.apache_link);
        StringUtils.makeLinkable(tv, getString(R.string.apache_license_link));
    }



}