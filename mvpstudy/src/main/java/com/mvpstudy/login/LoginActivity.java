package com.mvpstudy.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.mvpstudy.R;
import com.mvpstudy.login.presentation.presenters.LoginPresenter;
import com.mvpstudy.login.presentation.presenters.LoginPresenterImpl;
import com.mvpstudy.login.presentation.views.LoginView;
import com.mvpstudy.main.MainActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * LoginActivity
 *
 * @author Jian Yang
 * @date 9/12/2015
 */
public class LoginActivity extends Activity implements LoginView, View.OnClickListener {
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.progress)
    private ProgressBar progressBar;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        button.setOnClickListener(this);

        presenter = new LoginPresenterImpl(this);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUsernameError() {
        username.setError(getString(R.string.username_error));
    }

    @Override
    public void setPasswordError() {
        password.setError(getString(R.string.password_error));
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        presenter.validateCredentials(username.getText().toString(), password.getText().toString());
    }
}
