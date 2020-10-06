package mx.uv.fiee.iinf.mp3player;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {
    public static final int REQUEST_CODE = 1001;
    public static final int ACTIVITY_REQUEST_CODE = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnItem).setOnClickListener (v -> {
            Intent intent = new Intent (getBaseContext (), DetailsActivity.class);
            intent.putExtra ("AUDIO", "/sdcard/Music/01.mp3");
            startActivityForResult (intent, ACTIVITY_REQUEST_CODE);


            // verifica si se cuenta con el permiso para realizar llamadas
//            int perm = getBaseContext ().checkSelfPermission (Manifest.permission.CALL_PHONE);
//            if (perm != PackageManager.PERMISSION_GRANTED) { // sino se tiene el permiso
//                requestPermissions ( // se solicita explicitamente
//                        new String [] { Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE },
//                        REQUEST_CODE
//                );
//
//                return;
//            }
//
//            Invoca al marcador telefónico
//            String uri = "tel: 229 2343435";
//            Intent intent = new Intent (Intent.ACTION_CALL);
//            intent.setData (Uri.parse (uri));
//            startActivity (intent);
        });
    }

    /**
     * Callback de la solicitud de permisos realizada en cualquier punto de la actividad.
     *
     * @param requestCode código de verificación de la solicitud
     * @param permissions conjunto de permisos solicitados
     * @param grantResults conjunto de resultados, permisos otorgados o denegados
     */
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);

        switch (REQUEST_CODE) {
            case 1001:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText (getBaseContext(),"Permission Granted!", Toast.LENGTH_LONG).show ();
                }

                break;
        }
    }

    /**
     * Callback invocado después de llamar a startActivityForResult
     *
     * @param requestCode código de verificación de la llamadas al método
     * @param resultCode resultado: OK, CANCEL, etc.
     * @param data información resultante, si existe
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
