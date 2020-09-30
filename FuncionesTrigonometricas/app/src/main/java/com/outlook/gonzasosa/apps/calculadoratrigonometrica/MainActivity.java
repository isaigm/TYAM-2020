package com.outlook.gonzasosa.apps.calculadoratrigonometrica;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Locale;

public class MainActivity extends Activity {
    private static final int DEGREE_45 = 45;
    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;

    private static final int FUNC_SIN = 0;
    private static final int FUNC_COS = 1;
    private static final int FUNC_TAN = 2;

    TextView tvResultados;
    int grados = -1, func = -1;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        tvResultados = findViewById (R.id.tvResultados);
        final ImageView imageView = findViewById (R.id.ivGrados);

        RadioGroup rgGrados = findViewById (R.id.rgGrados);
        rgGrados.setOnCheckedChangeListener ((radioGroup, i) -> {
            Bitmap foo = null;

            switch (i) {
                case R.id.rb45:
                    foo = BitmapFactory.decodeResource (getResources (), R.drawable.g45);
                    grados = DEGREE_45;
                    break;
                case R.id.rb90:
                    foo = BitmapFactory.decodeResource (getResources (), R.drawable.g90);
                    grados = DEGREE_90;
                    break;
                case R.id.rb180:
                    foo = BitmapFactory.decodeResource (getResources (), R.drawable.g180);
                    grados = DEGREE_180;
                    break;
            }

            calcular ();
            imageView.setImageBitmap (foo);
        });

        RadioGroup rbFuncs = findViewById (R.id.rbFuncs);
        rbFuncs.setOnCheckedChangeListener ((radioGroup, i) -> {
            switch (i) {
                case R.id.rbSIN:
                    func = FUNC_SIN;
                    break;
                case R.id.rbCOS:
                    func = FUNC_COS;
                    break;
                case R.id.rbTAN:
                    func = FUNC_TAN;
                    break;
            }

            calcular ();
        });
    }

    private void calcular () {
        if (grados > -1 && func > -1) {
            double bar = 0.0;

            switch (func) {
                case 0:
                    bar = Math.sin (Math.toRadians (grados));
                    break;
                case 1:
                    bar = Math.cos (Math.toRadians (grados));
                    break;
                case 2:
                    bar = Math.tan (Math.toRadians (grados));
                    break;
            }

            tvResultados.setText (String.format (Locale.US, "%f", bar));
        }
    }
}
