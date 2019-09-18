package com.jf.mylinphone;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.TransportType;

public class LinphoneMananger {

    private static final String TAG = "JF.LINPHONE";
    private static AccountCreator mAccountCreator;
    private static CoreListenerStub mCoreListener;

    public static void configureAccount(String userName,String domain,String password) {

        mAccountCreator = LinphoneService.getCore().createAccountCreator(null);
        // At least the 3 below values are required
        mAccountCreator.setUsername(userName);
        mAccountCreator.setDomain(domain);
        mAccountCreator.setPassword(password);

        // By default it will be UDP if not set, but TLS is strongly recommended
        //mAccountCreator.setTransport(TransportType.Udp);
        mAccountCreator.setTransport(TransportType.Tcp);
        //mAccountCreator.setTransport(TransportType.Tls);

        // This will automatically create the proxy config and auth info and add them to the Core
        ProxyConfig cfg = mAccountCreator.createProxyConfig();
        // Make sure the newly created one is the default
        LinphoneService.getCore().setDefaultProxyConfig(cfg);
    }

    public static void addCoreListener(final Context context){
        if(mCoreListener == null){
            mCoreListener = new CoreListenerStub() {
                @Override
                public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState state, String message) {
                    if (state == RegistrationState.Ok) {
                        Log.d(TAG,"");
                    } else if (state == RegistrationState.Failed) {
                        Toast.makeText(context, "Failure: " + message, Toast.LENGTH_LONG).show();
                    }
                }
            };
        }
    }

    public static void removeCoreListener(){
        if(mCoreListener != null){
            LinphoneService.getCore().removeListener(mCoreListener);
        }
    }

}
