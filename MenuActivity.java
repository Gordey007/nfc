package gordey007.nfc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button competition, dbnfc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        competition = (Button) findViewById(R.id.competition);
        competition.setOnClickListener(this);

        dbnfc = (Button) findViewById(R.id.dbnfc);
        dbnfc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.competition:
                Intent intent = new Intent(this, TimeActivity.class);
                startActivity(intent);
                break;
            case R.id.dbnfc:
                Intent intent2 = new Intent(this, DBNFCActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
