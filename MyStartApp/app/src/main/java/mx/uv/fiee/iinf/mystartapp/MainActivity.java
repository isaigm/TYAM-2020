package mx.uv.fiee.iinf.mystartapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity {
    // variables de las se obtendrán sus estados para mantenerlo
    // entre cambios de estado de la app o cambios en el ciclo de vida
    // de la actividad
    ImageView img;
    RadioGroup group;
    CheckBox check;
    Spinner spinner;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        Log.d ("TYAM", "OnCreate");

        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        // se obtienen las referencias a los componentes
        group = findViewById (R.id.group1);
        check = findViewById (R.id.check1);
        img = findViewById (R.id.image1);
        spinner = findViewById (R.id.spinner1);

        // referencia al botón que añade una imagen a la vista
        Button b = findViewById(R.id.btnPic);
        b.setOnClickListener(v -> img.setImageResource (R.mipmap.ic_launcher_round));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d ("TYAM", "OnSaveInstanceState");

        int radioSelected = group.getCheckedRadioButtonId ();
        boolean isChecked = check.isChecked ();
        long spinnerItem = spinner.getSelectedItemId ();

        byte [] baImage = convertImage2ByteArray (img);

        outState.putInt ("RADIOSELECTED", radioSelected);
        outState.putBoolean ("CHECKED", isChecked);
        outState.putLong ("SPINNERITEM", spinnerItem);
        outState.putByteArray ("IMAGE", baImage);
    }

    private byte [] convertImage2ByteArray (ImageView imageView) {
        Drawable drawable = imageView.getDrawable ();
        Bitmap bitmap = getBitmapFromDrawable (drawable);
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat.JPEG, 100, baos);

        return baos.toByteArray ();
    }

    private Bitmap getBitmapFromDrawable (Drawable drble) {
        if (drble instanceof BitmapDrawable) {
            return  ((BitmapDrawable) drble).getBitmap ();
        }

        Bitmap bitmap = Bitmap.createBitmap (drble.getIntrinsicWidth (), drble.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);

        return bitmap;
    }

    private String convertBitmap2Base64String (Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat.JPEG, 100, baos);
        byte [] baBitmap = baos.toByteArray ();

        return Base64.encodeToString (baBitmap, Base64.DEFAULT);
    }

    private Bitmap convertBase64String2Bitmap (String b64s) {
        byte [] foo = Base64.decode (b64s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray (foo, 0, foo.length);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d ("TYAM", "OnRestoreInstanceState");

        int radioSelected = savedInstanceState.getInt ("RADIOSELECTED");
        boolean isChecked = savedInstanceState.getBoolean ("CHECKED");
        long spinnerItem = savedInstanceState.getLong ("SPINNERITEM");
        byte [] baImage = savedInstanceState.getByteArray ("IMAGE");

        int rbIndex = group.indexOfChild (group.findViewById (radioSelected));
        group.getChildAt (rbIndex).setSelected (true);
        check.setChecked (isChecked);
        spinner.setSelection ((int) spinnerItem);

        if (baImage == null) return;
        Bitmap bitmap = BitmapFactory.decodeByteArray (baImage, 0, baImage.length);
        img.setImageBitmap (bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d ("TYAM", "OnStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d ("TYAM", "OnResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d ("TYAM", "OnPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d ("TYAM", "OnStop");

    }


}
