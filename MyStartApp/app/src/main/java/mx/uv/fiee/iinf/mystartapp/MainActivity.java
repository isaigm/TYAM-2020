package mx.uv.fiee.iinf.mystartapp;

import android.app.Activity;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import java.io.ByteArrayOutputStream;

import mx.uv.fiee.iinf.mystartapp.room.AppDatabase;
import mx.uv.fiee.iinf.mystartapp.room.Formulario;

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
        EditText edName = findViewById (R.id.edVal1);
        EditText edLastName = findViewById (R.id.edVal2);

        group = findViewById (R.id.group1);
        check = findViewById (R.id.check1);
        img = findViewById (R.id.image1);
        spinner = findViewById (R.id.spinner1);

        // referencia al botón que añade una imagen a la vista
        Button b = findViewById(R.id.btnPic);
        b.setOnClickListener(v -> img.setImageResource (R.mipmap.ic_launcher_round));


        AppDatabase db = Room.databaseBuilder (getApplicationContext (),
                AppDatabase.class, "myRoomDatabase").build ();

        findViewById (R.id.btnSave).setOnClickListener (v -> {
            Formulario f = new Formulario ();
            f.name = edName.getText ().toString ();
            f.lastName = edLastName.getText ().toString ();
            f.isChecked = check.isChecked ();
            f.radioSelected = group.getCheckedRadioButtonId ();
            f.spinnerSelectedItem = spinner.getSelectedItemId ();

            db.formularioDAO().inserAll (f);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d ("TYAM", "OnSaveInstanceState");

        // se recuperan el estado de cada componente asignándolo
        // a variables locales
        int radioSelected = group.getCheckedRadioButtonId ();
        boolean isChecked = check.isChecked ();
        long spinnerItem = spinner.getSelectedItemId ();

        // obtenemos el arreglo de bytes que componen a la imagen
        byte [] baImage = convertImage2ByteArray (img);

        // persistimos la información a través del objeto bundle
        outState.putInt ("RADIOSELECTED", radioSelected);
        outState.putBoolean ("CHECKED", isChecked);
        outState.putLong ("SPINNERITEM", spinnerItem);
        outState.putByteArray ("IMAGE", baImage);
    }

    /**
     * Convierte la imagen contenida en el componente ImageView en su representación
     * de arreglo de bytes de acuerdo a un formato de imagen determinado.
     *
     * @param imageView componente que contiene la imagen deseada.
     * @return arreglo de bytes que representan a la imagen.
     */
    private byte [] convertImage2ByteArray (ImageView imageView) {
        // obtiene la imagen desde el canvas sobre el que está dibujada
        Drawable drawable = imageView.getDrawable ();
        Bitmap bitmap = getBitmapFromDrawable (drawable); // obtiene un objeto Bitmap a partir del lienzo
        // se requiere un objeto que almacena en memoria los bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        // copia los bytes al flujo indicado, usando compresión (representación) de acuerdo al formato
        // indicado
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray (); // devuelve el arreglo
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
        Canvas canvas = new Canvas (bitmap);
        drble.setBounds (0, 0, canvas.getWidth (), canvas.getHeight ());
        drble.draw (canvas);

        return bitmap;
    }

    /**
     * Convierte un objeto de mapa de bits a su representación base64.
     *
     * @param bitmap objeto a transformar en su representación base64.
     * @return cadena de caracteres con la información codificada de los bytes que conforman
     * al bitmap.
     */
    private String convertBitmap2Base64String (Bitmap bitmap) {
        // flujo de salida usado para almacenar los bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        // copia los bytes al flujo de salida usando la compresión indicada
        bitmap.compress (Bitmap.CompressFormat.PNG, 100, baos);
        byte [] baBitmap = baos.toByteArray (); // obtiene el arreglo de bytes

        return Base64.encodeToString (baBitmap, Base64.DEFAULT);
    }

    /**
     * Convierte la cadena codificada en base64 en un objeto de mapa de bits.
     *
     * @param b64s objeto string con la información codificada.
     * @return objeto de mapa de bits obtenido de decodificar la información de la cadena.
     */
    private Bitmap convertBase64String2Bitmap (String b64s) {
        // la función decode de la clase base64 convierte la cadena en un arreglo de bytes
        byte [] foo = Base64.decode (b64s, Base64.DEFAULT);
        // mediante la función decodeByteArray se obtiene el mapa de bits que representa
        // el arreglo de bytes
        return BitmapFactory.decodeByteArray (foo, 0, foo.length);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Log.d ("TYAM", "OnRestoreInstanceState");

        // sino hay información qué restaurar sale del método

        // se obtienen los valores recibidos en el parámetro savedInstanceState, validando
        // que existan las llaves dentro del diccionario del objeto Bundle
        int radioSelected = savedInstanceState.containsKey ("RADIOSELECTED") ?
                savedInstanceState.getInt ("RADIOSELECTED") : -1;

        boolean isChecked = savedInstanceState.containsKey ("CHECKED") && savedInstanceState.getBoolean ("CHECKED");

        long spinnerItem = savedInstanceState.containsKey ("SPINNERITEM") ?
                savedInstanceState.getLong ("SPINNERITEM") : -1;

        byte [] baImage = savedInstanceState.containsKey ("IMAGE") ?
                savedInstanceState.getByteArray ("IMAGE") : null;

        // el identificador del radiobutton almacenado debe convertirse en una widget
        // al cuál seleccionar
        RadioButton foo = group.findViewById (radioSelected);
        if (foo != null) {
            int rbIndex = group.indexOfChild(foo);
            group.getChildAt(rbIndex).setSelected(true);
        }

        if (spinnerItem > -1) spinner.setSelection ((int) spinnerItem);
        check.setChecked (isChecked);

        // antes de asignar la imagen al imageview, debe ser convertida de arreglo de bytes
        // a bitmap
        if (baImage == null) return;
        Bitmap bitmap = BitmapFactory.decodeByteArray (baImage, 0, baImage.length);
        img.setImageBitmap (bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.d ("TYAM", "OnStart");

        // las preferencias compartidas nos permiten almacenar información y persistirla entre
        // invocaciones de la app
        //
        // aquí se obtienen las referencias a las preferencias compartidas de la app
        SharedPreferences pref = getSharedPreferences ("mySharedPreferences", 0);

        // se recupera la información almacenada en las referencias compartidas
        int radioSelected = pref.getInt ("RADIOSELECTED", -1);
        boolean isChecked = pref.getBoolean ("CHECKED", false);
        long spinnerItem = pref.getLong ("SPINNERITEM", -1);

        RadioButton foo = group.findViewById (radioSelected);
        if (foo != null) {
            int rbIndex = group.indexOfChild(foo);
            group.getChildAt(rbIndex).setSelected(true);
        }

        check.setChecked (isChecked);

        if (spinnerItem > -1) spinner.setSelection ((int) spinnerItem);

        String imageBase64 = pref.getString ("IMAGE", "");
        if (imageBase64 == "") return;

        Bitmap bitmap = convertBase64String2Bitmap (imageBase64);
        if (bitmap != null) img.setImageBitmap (bitmap);
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
        //Log.d ("TYAM", "OnStop");

        // las preferencias compartidas nos permiten almacenar información y persistirla entre
        // invocaciones de la app
        //
        // aquí se obtienen la referencia a las preferencias compartidas de la app y se inicia
        // el editor de las mismas
        SharedPreferences.Editor editor = getSharedPreferences ("mySharedPreferences", MODE_PRIVATE).edit ();

        // se recuperan los estados de los componentes
        int radioSelected = group.getCheckedRadioButtonId ();
        boolean isChecked = check.isChecked ();
        long spinnerItem = spinner.getSelectedItemId ();

        // se almacenan en las referencias compartidas usando una llave determinada
        editor.putInt ("RADIOSELECTED", radioSelected);
        editor.putBoolean ("CHECKED", isChecked);
        editor.putLong ("SPINNERITEM", spinnerItem);

        // para almacenar la imagen, primero se obtiene el objeto bitmap
        // a partir del objeto canvas contenido en el imageview
        // posteriormente se convierte dicho objeto bitmap en su representación
        // base64
        Bitmap b = getBitmapFromDrawable (img.getDrawable ());
        String s = convertBitmap2Base64String (b);
        editor.putString ("IMAGE", s);

        editor.apply (); // se guardan los cambios en el objeto de preferencias compartidas.
    }


}
