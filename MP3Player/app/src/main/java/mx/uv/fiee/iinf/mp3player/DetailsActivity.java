package mx.uv.fiee.iinf.mp3player;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

public class DetailsActivity extends Activity {
    MediaPlayer player;
    Thread posThread;
    Uri mediaUri;
    int pos;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_details);

        SeekBar sbProgress = findViewById (R.id.sbProgress);
        sbProgress.setOnSeekBarChangeListener (new MySeekBarChangeListener ());

        player = new MediaPlayer ();
        player.setOnPreparedListener (mediaPlayer -> {
            posThread = new Thread (() -> {
                try {
                    while (player.isPlaying ()) {
                        Thread.sleep (1000);
                        sbProgress.setProgress (player.getCurrentPosition ());
                    }
                } catch (InterruptedException in) { in.printStackTrace (); }
            });

            sbProgress.setMax (mediaPlayer.getDuration ());
            if (pos > -1) mediaPlayer.seekTo (pos);
            mediaPlayer.start ();
            posThread.start ();
        });

        Button btnAudio1 = findViewById (R.id.btnAudio1);
        btnAudio1.setOnClickListener (v -> {

            if (player.isPlaying ()) {
                posThread.interrupt ();
                player.stop ();
                player.seekTo (0);
                sbProgress.setProgress (0);
                pos = -1;
            }

            mediaUri = Uri.parse ("android.resource://" + getBaseContext ().getPackageName () + "/" + R.raw.mr_blue_sky);

            try {
                player.setDataSource(getBaseContext (), mediaUri);
                player.prepare ();
                Toast.makeText (getApplicationContext (), "Now playing: Mr. Blue Sky", Toast.LENGTH_LONG).show ();
            } catch (IOException ex) { ex.printStackTrace (); }

        });

        Button btnAudio2 = findViewById (R.id.btnAudio2);
        btnAudio2.setOnClickListener (v -> {

            if (player.isPlaying ()) {
                posThread.interrupt ();
                player.stop ();
                player.seekTo (0);
                sbProgress.setProgress (0);
                pos = -1;
            }

            mediaUri = Uri.parse ("android.resource://" + getBaseContext ().getPackageName () + "/" + R.raw.lake_shore_drive);

            try {
                player.setDataSource (getBaseContext (), mediaUri);
                player.prepare ();
                Toast.makeText (getApplicationContext (), "Now playing: Lake Shoe Drive", Toast.LENGTH_LONG).show ();
            } catch (IOException ex) { ex.printStackTrace (); }

        });

        Button btnAudio3 = findViewById (R.id.btnAudio3);
        btnAudio3.setOnClickListener (v -> {

            if (player.isPlaying ()) {
                posThread.interrupt ();
                player.stop ();
                player.seekTo (0);
                sbProgress.setProgress (0);
                pos = -1;
            }

            mediaUri = Uri.parse ("android.resource://" + getBaseContext ().getPackageName () + "/" + R.raw.fox_on_the_run);

            try {
                player.setDataSource (getBaseContext(), mediaUri);
                player.prepare ();
                Toast.makeText (getApplicationContext (), "Now playing: Fox On The Run", Toast.LENGTH_LONG).show ();
            } catch (IOException ex) { ex.printStackTrace (); }

        });

    }

    @Override
    protected void onSaveInstanceState (@NonNull Bundle outState) {
        super.onSaveInstanceState (outState);

        outState.putString ("SONG", mediaUri != null ? mediaUri.toString (): "");
        outState.putInt ("PROGRESS", player != null ?  player.getCurrentPosition () : -1);
        outState.putBoolean ("ISPLAYING", player != null && player.isPlaying ());

        if (player.isPlaying ()) {
            posThread.interrupt ();

            player.stop ();
            player.seekTo (0);
            player.release ();
            player = null;
        }
    }

    @Override
    protected void onRestoreInstanceState (@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);

        mediaUri = Uri.parse (savedInstanceState.getString ("SONG"));
        pos = savedInstanceState.getInt ("PROGRESS");
        boolean isPlaying = savedInstanceState.getBoolean ("ISPLAYING");

        if (player == null) return;

        try {
            player.reset ();
            player.setDataSource (getBaseContext (), mediaUri);
            if (isPlaying) player.prepareAsync ();
        } catch (IOException | IllegalStateException ioex) {
            ioex.printStackTrace ();
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        // cleanup

        if (player != null && player.isPlaying ()) {
            player.stop ();
            player.release ();
        }

        player = null;
    }


    @Override
    protected void onResume() {
        super.onResume ();
    }

    @Override
    protected void onStart() {
        super.onStart ();

        Intent intent = getIntent ();
        if (intent != null) {
            String audio = intent.getStringExtra ("AUDIO");
            Toast.makeText (getBaseContext(), audio, Toast.LENGTH_LONG).show ();
        }

    }

    class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged (SeekBar seekBar, int i, boolean b) {
            if (b) {
                player.pause ();
                player.seekTo (i);
                player.start ();
            }
        }

        @Override
        public void onStartTrackingTouch (SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch (SeekBar seekBar) {}

    }
}
