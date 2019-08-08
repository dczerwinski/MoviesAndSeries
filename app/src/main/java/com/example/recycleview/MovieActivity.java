package com.example.recycleview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.recycleview.DataBase.AppDatabase;
import com.example.recycleview.DataBase.DataBase;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {
    @BindView(R.id.collapsing_toolbar_layout_movie)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.rezyser_movie)
    TextView tv_rezyser;
    @BindView(R.id.opis_movie)
    TextView tv_opis;
    @BindView(R.id.recenzja_movie)
    TextView tv_recenzja;
    @BindView(R.id.aktorzy_movie)
    TextView tv_aktorzy;
    @BindView(R.id.gora_movie)
    ImageView imageView;
    @BindView(R.id.app_bar_movie)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar_movie)
    Toolbar mToolbar;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        int uid = getIntent().getExtras().getInt("elo");

        final DataBase movie = AppDatabase.getAppDatabase(this).dataBaseDao().findById(uid);

        //zeby byl jakis opis gdzies
        if (movie.title.equals("Pirates of the Caribbean")) {
            tv_opis.setText(
                    "Zrealizowana z rozmachem awanturnicza opowieść o piratach, miłości, odwadze i przeznaczeniu. Akcja rozgrywa się w XVIII wieku na Karaibach. W Port Royal odbywa się uroczystość nominowania kapitana Norringtona (Jack Davenport) na komodora. W tym wielkim dla siebie dniu Norrington pragnie do swoich sukcesów zawodowych dołączyć także osobisty: zdobyć rękę pięknej córki gubernatora, Elizabeth Swann (Keira Knightley). Nieoczekiwanie na przeszkodzie jego planom staje sławny pirat Jack Sparrow (Johnny Depp). Ów zuchwalec zamierza ukraść najszybszy okręt floty brytyjskiej, ponieważ jego własną Czarną Perłę przejęła zbuntowana załoga. Ci właśnie korsarze zbliżają się do Port Royal, aby odzyskać tajemniczy złoty medalion. Przy jego pomocy będą mogli uwolnić się od straszliwej klątwy, rzuconej na nich niegdyś przez pogańskie bóstwa. Posiadaczką medalionu, części skarbu Azteków, jest właśnie Elizabeth Swann. Dziewczynie grozi wielkie niebezpieczeństwo. Na szczęście ma dzielnego obrońcę w osobie Willa Turnera (Orlando Bloom), młodego płatnerza, w którego żyłach płynie piracka krew...");
        }


        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            if (i == -mCollapsingToolbarLayout.getHeight() + mToolbar.getHeight()) {
                mCollapsingToolbarLayout.setTitle(movie.title);
            } else {
                mCollapsingToolbarLayout.setTitle("");
            }
        });

        tv_rezyser.setText("Rezyser: " + movie.director);
        imageView.setImageResource(this.getResources()
                .getIdentifier(movie.img_file, "drawable", this.getPackageName()));

        tv_rezyser.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DirectorActivity.class);
            intent.putExtra("dir", movie.director);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

}
