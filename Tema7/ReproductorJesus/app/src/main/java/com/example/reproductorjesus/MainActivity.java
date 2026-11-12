package com.example.reproductorjesus;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SeekBar seekBar;
    private TextView tvActual, tvTotal;
    private boolean arrastrando = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable actualizador = new Runnable() {
        @Override
        public void run() {
            if (!arrastrando) {
                long pos = player.getCurrentPosition();
                seekBar.setProgress((int) pos);
                tvActual.setText(formatear(pos));
            }
            handler.postDelayed(this, 500); // se repite cada medio segundo
        }
    };

    private String formatear(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // --- Reproductor ---
        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.puntalon);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();
        seekBar = findViewById(R.id.seekBar);
        tvActual = findViewById(R.id.tvActual);
        tvTotal = findViewById(R.id.tvTotal);

// La duración solo se conoce cuando el reproductor está listo
        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvTotal.setText(formatear(duracion));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progreso, boolean delUsuario) {
                if (delUsuario) tvActual.setText(formatear(progreso));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {
                arrastrando = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                player.seekTo(sb.getProgress()); // salta al soltar
                arrastrando = false;
            }
        });

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);

        btnPlay.setOnClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0); // si terminó, empieza desde el principio
            }
            player.play();

        });
        btnPausa.setOnClickListener(v -> player.pause());

    }
    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizador);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
        handler.removeCallbacks(actualizador);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(actualizador);
        player.release();
    }
}