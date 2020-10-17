package mx.uv.fiee.iinf.gallerydemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

public class SavingActivity extends Activity {
    public static final int REQUEST_CAMERA_OPEN = 4001;
    public static final int REQUEST_PERMISSION_CAMERA = 3001;
    ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_saving);

        iv = findViewById (R.id.ivSource);


        Button button = findViewById (R.id.btnSave);
        button.setOnClickListener (v -> {

            int perm = checkSelfPermission (Manifest.permission.CAMERA);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (
                        new String [] { Manifest.permission.CAMERA },
                        REQUEST_PERMISSION_CAMERA
                );

                return;
            }

            //abrirCamara ();

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

    void abrirCamara () {
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult (intent, REQUEST_CAMERA_OPEN);
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

    /**
     * Almacena el mapa de bits recibido en el almacenamiento externo, dentro de la carpeta destinada
     * para contener archivos de imagen.
     *
     * @param bitmap imagen en mapa de bits a guardar.
     */
    void saveImage (Bitmap bitmap) {
        ContentResolver resolver = getContentResolver ();

        ContentValues values = new ContentValues ();
        values.put (MediaStore.MediaColumns.DISPLAY_NAME, "myOtherPic.jpg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put (MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            values.put (MediaStore.MediaColumns.IS_PENDING, true);
        } else {
            String pictureDirectory =
                    String.format ("%s/%s", Environment.getExternalStorageDirectory (), Environment.DIRECTORY_PICTURES);
            values.put (MediaStore.MediaColumns.DATA, pictureDirectory);
        }

        Uri targetUri;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else {
            targetUri = MediaStore.Files.getContentUri ("external");
        }

        Uri imageUri =  resolver.insert (targetUri, values);

        try {
            OutputStream fos = resolver.openOutputStream (imageUri);
            bitmap.compress (Bitmap.CompressFormat.JPEG,100, fos);
            fos.flush ();
            fos.close ();

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                values = new ContentValues ();
                values.put (MediaStore.Images.ImageColumns.IS_PENDING, false);
                resolver.update (imageUri, values, null, null);
            }
        } catch (Exception ex) { ex.printStackTrace (); }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                abrirCamara ();
            }
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA_OPEN && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap (bitmap);
        }
    }
}
