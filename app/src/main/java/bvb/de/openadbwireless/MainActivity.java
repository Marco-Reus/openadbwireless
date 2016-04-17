package bvb.de.openadbwireless;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import bvb.de.openadbwireless.utils.DeviceInfoUtil;
import bvb.de.openadbwireless.utils.RuntimeUtil;

public class MainActivity extends Activity {

    private static final String TAG = "runtime";

    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    TextView tv;
    ToggleButton toggleButton;
    String ip;
    final String PORT = "5555";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        tv = $(R.id.tv);
        toggleButton = $(R.id.toggleButton);

        if (DeviceInfoUtil.isWifi(this) && RuntimeUtil.hasRootedSilent()) {
            ip = DeviceInfoUtil.getIpv4(this);
            List<String> result = RuntimeUtil.exec("getprop service.adb.tcp.port");
            boolean adbdRunning = RuntimeUtil.isProcessRunning("adbd") && result != null && "5555".equals(result.get(0));
            toggleButton.setChecked(adbdRunning);
            tv.setText(adbdRunning ? ("adb is on \nrun on you pc\nadb connect " + ip + ":" + PORT) : ("adb is off"));

            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toggleButton.setChecked(isChecked);
                    if (isChecked) {
                        RuntimeUtil.execSilent("setprop service.adb.tcp.port " + PORT, "stop adbd", "start adbd");
                    } else {
                        RuntimeUtil.execSilent("setprop service.adb.tcp.port -1", "stop adbd", "start adbd");
                    }
                    tv.setText(isChecked ? ("adb is on \nrun on you pc\nadb connect " + ip + ":" + PORT) : ("adb is off"));
                }
            });
        } else {
            tv.setText("wifi未连接或手机未root");
            toggleButton.setChecked(false);
            toggleButton.setEnabled(false);
        }

    }
}