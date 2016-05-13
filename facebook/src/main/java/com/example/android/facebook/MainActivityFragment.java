package com.example.android.facebook;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    LoginButton loginButton;
    Button btn;
    CallbackManager callbackManager;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
/*
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        */

        btn = (Button) rootView.findViewById(R.id.login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
//                    App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                                           }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
            }
        });
        AccessToken at = AccessToken.getCurrentAccessToken();
        if(at!=null){
            String atStr = at.getToken();
            Set<String> deniedPermission = at.getDeclinedPermissions();
            Set<String> grantedPermission = at.getDeclinedPermissions();
        } else {
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile",
                        "read_custom_friendlist",
                        "user_friends",
                        "email",
                        "user_post",
                        "user_birthday"));
        }
        return rootView;
    }
}
