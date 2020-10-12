package mx.uv.fiee.iinf.gallerydemo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SavingActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_saving);

        ImageView iv = findViewById (R.id.ivSource);

        Button button = findViewById (R.id.btnSave);
        button.setOnClickListener (v -> {

            Bitmap bitmap = getBitmapFromDrawable (iv.getDrawable ());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveImage (bitmap);
            } else {
                String imageDir = Environment.getExternalStoragePublicDirectory (Environment.DIRECTORY_PICTURES).toString ();
                File file = new File(imageDir, "/mypic.jpg");

                try {
                    OutputStream fos = new FileOutputStream (file);
                    bitmap.compress (Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close ();
                } catch (IOException ex) {
                    ex.printStackTrace ();
                }
            }

        });
    }

    /**
     * Obtiene un objeto de mapa de bits a partir del objeto Drawable (canvas) recibido.
     *
     * @param drble Drawable que contiene la imagen deseada.
     * @return objeto de mapa de bits con la estructura de la imagen.
     */
    private Bitmap getBitmapFromDrawable (Drawable drble) {
        // debido a la forma que el sistema dibuja una imagen en un el sistema gráfico
        // es necearios realzar comprobaciones para saber del tipo de objeto Drawable
        // con que se está trabajando.
        //
        // si el objeto recibido es del tipo BitmapDrawable no se requieren más conversiones
        if (drble instanceof BitmapDrawable) {
            return  ((BitmapDrawable) drble).getBitmap ();
        }

        // en caso contrario, se crea un nuevo objeto Bitmap a partir del contenido
        // del objeto Drawable
        Bitmap bitmap = Bitmap.createBitmap (drble.getIntrinsicWidth (), drble.getIntrinsicHeight (), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);

        return bitmap;
    }

    void saveImage (Bitmap bitmap) {
        ContentResolver resolver = getContentResolver ();

        ContentValues values = new ContentValues ();
        values.put (MediaStore.MediaColumns.DISPLAY_NAME, "myOtherPic.jpg");
        values.put (MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        Uri imageUri = resolver.insert (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream fos = resolver.openOutputStream (imageUri);
            bitmap.compress (Bitmap.CompressFormat.JPEG,100, fos);
            fos.close ();
        } catch (Exception ex) { ex.printStackTrace (); }
    }

}
