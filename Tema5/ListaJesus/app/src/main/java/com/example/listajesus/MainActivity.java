package com.example.listajesus;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        //Los juegos
        ArrayList<Videojuego> videojuegos = new ArrayList<>();
        videojuegos.add(new Videojuego("Zelda", "Nintendo Switch", R.drawable.zelda));
        videojuegos.add(new Videojuego("Mario Kart", "Nintendo Switch", R.drawable.mario));
        videojuegos.add(new Videojuego("Sonic", "Sega Mega Drive", R.drawable.sonic));
        videojuegos.add(new Videojuego("Pac-Man", "Arcade", R.drawable.pacman));
        videojuegos.add(new Videojuego("Tetris", "Game Boy", R.drawable.zelda));
        videojuegos.add(new Videojuego("FIFA", "PlayStation", R.drawable.mario));
        videojuegos.add(new Videojuego("Minecraft", "PC", R.drawable.sonic));
        videojuegos.add(new Videojuego("Fortnite", "PC", R.drawable.pacman));

        // orden de la lista
        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setAdapter(new AdaptadorVideojuegos(videojuegos));
    }
}