package ru.google.studyjam.bestweather.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.roger.catloadinglibrary.CatLoadingView;

import butterknife.ButterKnife;
import io.realm.Realm;
import ru.google.studyjam.bestweather.R;
import ru.google.studyjam.bestweather.network.BestWeatherRest;
import ru.google.studyjam.bestweather.network.SessionRestManager;

public class BaseActivity extends AppCompatActivity {

    private MaterialDialog dialog;
    private CatLoadingView catLoadingView;
    protected Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }


    protected void showProgressBar() {
//        if (dialog == null) {
//            dialog = new MaterialDialog.Builder(BaseActivity.this)
//                    .content("Загрузка...")
//                    .progress(true, 0)
//                    .cancelable(false)
//                    .show();
//        } else {
//            dialog.show();
//        }
        if (catLoadingView == null) {
            catLoadingView = new CatLoadingView();
            catLoadingView.show(getSupportFragmentManager(), "");
        }
    }

    protected void setupToolbar(@StringRes int title, boolean setDisplayHomeAsUpEnabled) {
        setupToolbar(BaseActivity.this.getText(title), setDisplayHomeAsUpEnabled);
    }

    protected void setupToolbar(@NonNull CharSequence title, boolean setDisplayHomeAsUpEnabled) {
        Toolbar toolbar = ButterKnife.findById(BaseActivity.this, R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (setDisplayHomeAsUpEnabled) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void setupToolbar(@StringRes int title) {
        setupToolbar(title, true);
    }

    protected void setupToolbar(@NonNull CharSequence title) {
        setupToolbar(title, true);
    }

    protected void onBackButtonPressed() {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackButtonPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideProgressBar() {
//        if (dialog != null)
//            dialog.dismiss();
        if (catLoadingView != null) catLoadingView.dismiss();
    }

    protected BestWeatherRest getRest() {
        return SessionRestManager.getInstance().getRest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
