package com.example.reproductorjesus;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private SeekBar seekBar;
    private TextView tvActual, tvTotal;
    private boolean arrastrando = false;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private SoundPool soundPool;
    private int idCaca, idCampana, idSilbato, idSiu;
    private int cargados = 0;

    private final Runnable actualizador = new Runnable() {
        @Override
        public void run() {
            if (!arrastrando) {
                long pos = player.getCurrentPosition();
                seekBar.setProgress((int) pos);
                tvActual.setText(formatear(pos));
            }
            handler.postDelayed(this, 500);
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

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvTotal.setText(formatear(duracion));
                }
            }

            // DIAGNÓSTICO: borrar este método cuando funcione
            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
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
                player.seekTo(sb.getProgress());
                arrastrando = false;
            }
        });

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);

        btnPlay.setOnClickListener(v -> {
            // DIAGNÓSTICO: borrar este Toast cuando funcione
            Toast.makeText(this, "Play pulsado, estado " + player.getPlaybackState(),
                    Toast.LENGTH_SHORT).show();

            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });
        btnPausa.setOnClickListener(v -> player.pause());

        // --- Efectos de sonido ---
        Button btnCaca = findViewById(R.id.btnCaca);
        Button btnCampana = findViewById(R.id.btnCampana);
        Button btnSilbato = findViewById(R.id.btnSilbato);
        Button btnSiu = findViewById(R.id.btnSiu);

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        // El listener va ANTES de los load()
        soundPool.setOnLoadCompleteListener((sp, idSonido, estado) -> {
            if (estado == 0) {
                cargados++;
                if (cargados == 4) {
                    btnCaca.setEnabled(true);
                    btnCampana.setEnabled(true);
                    btnSilbato.setEnabled(true);
                    btnSiu.setEnabled(true);
                }
            }
        });

        idCaca    = soundPool.load(this, R.raw.caca, 1);
        idCampana = soundPool.load(this, R.raw.campana, 1);
        idSilbato = soundPool.load(this, R.raw.silbato, 1);
        idSiu     = soundPool.load(this, R.raw.siu, 1);

        btnCaca.setOnClickListener(v -> soundPool.play(idCaca, 1f, 1f, 1, 0, 1f));
        btnCampana.setOnClickListener(v -> soundPool.play(idCampana, 1f, 1f, 1, 0, 1f));
        btnSilbato.setOnClickListener(v -> soundPool.play(idSilbato, 1f, 1f, 1, 0, 1f));
        btnSiu.setOnClickListener(v -> soundPool.play(idSiu, 1f, 1f, 1, 0, 1f));
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
        soundPool.release();
    }
}