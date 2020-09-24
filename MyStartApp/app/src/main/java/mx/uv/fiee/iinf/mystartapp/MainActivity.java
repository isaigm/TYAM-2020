package mx.uv.fiee.iinf.mystartapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        final TextView tvVal1 = findViewById (R.id.tvVal1);
        TextView tvVal2 = findViewById (R.id.tvVal2);
        Button btnPress = findViewById (R.id.next_button);

        btnPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText (getBaseContext(), tvVal1.getText (), Toast.LENGTH_LONG).show ();
            }
        });
    }
}
