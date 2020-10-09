package mx.uv.fiee.iinf.mp3player;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class MainActivity extends Activity {
    public static final int REQUEST_CODE = 1001;
    public static final int ACTIVITY_REQUEST_CODE = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // origen de datos
        String [] days = { "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", };

        // controlador
        ArrayAdapter<String> adapter = new ArrayAdapter<> (getBaseContext(), android.R.layout.simple_list_item_2, days);

        // recupera los archivos de audio
        LinkedList<String> l = loadAudios ();

        // vista
        RecyclerView lv = findViewById (R.id.list1);
        lv.setLayoutManager (new LinearLayoutManager (getBaseContext(), RecyclerView.VERTICAL, false));
        lv.setAdapter (new MyAdapter (getBaseContext(), days));
    }

    LinkedList<String> loadAudios () {
        String [] columns = { MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Media.ALBUM};
        String order = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        // SELECT MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Media.ALBUM
        // FROM MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ORDER BY MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
        Cursor cursor =  getBaseContext().getContentResolver().query (MediaStore.Audio.Media.INTERNAL_CONTENT_URI, columns, null, null, order);
        if (cursor == null) return null;

        LinkedList<String> artists = new LinkedList<> ();

        for (int i = 0; i < cursor.getCount (); i++) {
            int index = cursor.getColumnIndex (MediaStore.Audio.Media.ARTIST);
            String artist = cursor.getString (index);

            artists.add (artist);
        }

        cursor.close ();
        return artists;
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

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    String [] data;

    public MyAdapter (Context context, String [] data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (context).inflate (R.layout.list_item, parent, false);
        return new MyViewHolder (view);
    }

    @Override
    public void onBindViewHolder (@NonNull MyViewHolder holder, int position) {
        String foo = data [position];
        holder.text1.setText (foo);
    }

    @Override
    public int getItemCount () {
        return data.length;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById (R.id.tvItem);
        }
    }

}