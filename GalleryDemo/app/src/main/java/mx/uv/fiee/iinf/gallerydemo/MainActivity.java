package mx.uv.fiee.iinf.gallerydemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.Objects;

public class MainActivity extends Activity {
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_main);

        Toolbar toolbar = findViewById (R.id.toolbar);
        setActionBar (Objects.requireNonNull (toolbar));

        rv = findViewById (R.id.rvGallery);
        rv.setLayoutManager (new GridLayoutManager (getBaseContext (), 2));

        int perm = checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE);
        if (perm != PackageManager.PERMISSION_GRANTED) {
            requestPermissions (
                    new String [] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    1001
            );

            return;
        }

        loadImages ();
    }

    private void loadImages () {
        String [] columns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME };
        String order = MediaStore.Images.Media.DEFAULT_SORT_ORDER;

        Cursor cursor = getContentResolver ().query (
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                order
        );

        DatabaseUtils.dumpCursor (cursor);
        if (cursor == null) return;

        LinkedList<Uri> imageUris = new LinkedList<> ();

        cursor.moveToFirst ();
        while (cursor.moveToNext ()) {
            int index = cursor.getColumnIndexOrThrow (MediaStore.Images.Media._ID);
            int id = cursor.getInt (index);


            Uri uri = ContentUris.withAppendedId (
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
            );

            imageUris.add (uri);
        }

        cursor.close ();

        GalleryAdapter adapter = new GalleryAdapter (getBaseContext (), imageUris);
        rv.setAdapter (adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                    loadImages ();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate (R.menu.menu, menu);
        return super.onCreateOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.mnuSaveActivity:
                Intent intent = new Intent(this, SavingActivity.class);
                startActivity (intent);
                break;
        }

        return super.onOptionsItemSelected (item);
    }
}
