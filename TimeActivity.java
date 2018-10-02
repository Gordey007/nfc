package gordey007.nfc;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{

    private Chronometer mChronometer;

    String key = "";

    Button start, stop, reset;

    LinearLayout listResults;

    int wrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;

    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);

        mChronometer = (Chronometer) findViewById(R.id.chronometer);

        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(this);

        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(this);

        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);

        listResults = (LinearLayout) findViewById(R.id.listResults);

        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            showElapsedTime();

            key = (ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            //Создание LayoutParams c шириной и высотой по содержимому
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(wrapContent, wrapContent);
            // переменная для хранения значения выравнивания
            // по умолчанию пусть будет LEFT
            int btnGravity = Gravity.LEFT;
            // переносим полученное значение выравнивания в LayoutParams
            lParams.gravity = btnGravity;

            // создаем TextView, пишем текст и добавляем в LinearLayout
            TextView txtNew = new TextView(this);

            txtNew.setText(key.toString() + " - " + showElapsedTime());
            txtNew.setTextColor(0xff66ff00);
            txtNew.setTextSize(18);
            listResults.addView(txtNew, lParams);
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public void onStartClick(View view) {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void onStopClick(View view) {
        mChronometer.stop();
    }

    public void onResetClick(View view) {
        mChronometer.setBase(SystemClock.elapsedRealtime());
    }

    private String showElapsedTime() {
        String time = null;
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        // elapsedMillis = elapsedMillis / 3600;
      //  Toast.makeText(TimeActivity.this, "Elapsed milliseconds: " + elapsedMillis, Toast.LENGTH_SHORT).show();

        time = String.format("%02d:%02d:%02d", elapsedMillis / 1000 / 3600, elapsedMillis / 1000 / 60 % 60, elapsedMillis / 1000 % 60);

        return time;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                onStartClick(v);
                break;
            case R.id.stop:
                onStopClick(v);
                break;
            case R.id.reset:
                onResetClick(v);
                break;

        }
    }
}