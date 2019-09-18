package com.jf.mylinphone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

public class CallActivity extends AppCompatActivity {

    private CoreListenerStub mCoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn_call);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Core core = LinphoneService.getCore();
                Address addressToCall = core.interpretUrl("18183279932");
                CallParams params = core.createCallParams(null);
                params.enableVideo(false);
                if (addressToCall != null) {
                    core.inviteAddressWithParams(addressToCall, params);
                    Toast.makeText(CallActivity.this,"已发出拨打信号",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                if (state == Call.State.End || state == Call.State.Released) {
                    // Once call is finished (end state), terminate the activity
                    // We also check for released state (called a few seconds later) just in case
                    // we missed the first one
                    Toast.makeText(CallActivity.this,"Linphone已释放",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinphoneService.getCore().addListener(mCoreListener);
    }

    @Override
    protected void onPause() {
        LinphoneService.getCore().removeListener(mCoreListener);
        super.onPause();
    }
}