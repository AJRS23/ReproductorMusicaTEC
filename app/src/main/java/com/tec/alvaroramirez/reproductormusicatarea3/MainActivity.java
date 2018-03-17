package com.tec.alvaroramirez.reproductormusicatarea3;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;  //Controlar el audio
    AudioManager audioManager;
    boolean isPause = false;
    Button controlButton;
    SeekBar volumeSeekBar;
    SeekBar advanceSeekbar;
    int progress=0;
    int duration=0;
    int maxVolume=0;
    int currentVolume =0;
    TextView lyrics;
    boolean newSong=true;
    ListView listView;
    int actualSong=0;



    public void controlClick(View view){
        if (!isPause){
            if(newSong){
                init();
                listView.performItemClick(view,actualSong,R.id.listView_songs);
            }

            mediaPlayer.start();
            isPause = true;
            lyrics.animate().translationYBy(-5900f).setDuration(duration + 10000);

            controlButton.setBackgroundResource(android.R.drawable.ic_media_pause);
            newSong=false;

        }
        else {
            mediaPlayer.pause();
            isPause = false;

            controlButton.setBackgroundResource(android.R.drawable.ic_media_play);
            lyrics.animate().cancel();
        }

    }

    public void nextClick(View view){
        if (actualSong!=4)
            actualSong++;
        else {
            actualSong = 0;

        }


        mediaPlayer.pause();
        newSong =false;
        isPause=true;
        listView.performItemClick(view,actualSong,R.id.listView_songs);
        controlButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        init();
        mediaPlayer.start();
        lyrics.animate().translationYBy(-5900f).setDuration(duration + 10000);

    }

    public void previousClick(View view){
        if(actualSong != 0)
            actualSong--;
        else{
            actualSong=4;

        }

        mediaPlayer.pause();
        newSong =false;
        isPause=true;

        listView.performItemClick(view,actualSong,R.id.listView_songs);

        init();
        mediaPlayer.start();
        lyrics.animate().translationYBy(-5900f).setDuration(duration + 10000);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listView_songs);
        volumeSeekBar = findViewById(R.id.seekBar_Volume);
        advanceSeekbar = findViewById(R.id.seekBar_progress);
        controlButton = findViewById(R.id.btn_Control);
        lyrics = findViewById(R.id.txt_Lyrics);


        init();


        //inicializar coleccion (bajarla de internet,etc.)
        final ArrayList<String> elementos = new ArrayList<String>(asList("Can't Stop The Feeling - Justin Timberlake", "Closer - The Chainsmokers","Don't Let Me Down - The Chainsmokers",
                                                                        "En El Cielo No Hay Hospital - Juan Luis Guerra","Good Feeling - Flo Rida"/*,
                                                                        "Happy","I Took A Pill In Ibiza"*/));  //asList: convertir strings en lista

        //ArrayAdapter: convertir tipos de datos en vistas
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,elementos); //Segundo parametro destino a convertir //Tarea se puede usar two_line_list_item

        listView.setAdapter(arrayAdapter);

        //Que quiero que pase cuando le doy click a un elemento
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (int j = 0; j < listView.getChildCount(); j++) {
                    if(i == j ){

                        listView.getChildAt(j).setBackgroundColor(Color.GRAY);

                    }else{
                        listView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    }
                }


                actualSong = i;
                newSong=true;
                //Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_SHORT).show();
            }
        });





        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Log.d("volume",Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        //Manejo de avance



        advanceSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //mediaPlayer.seekTo(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lyrics.animate().cancel();
                if(seekBar.getProgress()/ 45000>=1)
                    lyrics.setY((float) ((Math.ceil(seekBar.getProgress()/ 45000))* -550));
                //Toast.makeText(getApplicationContext(),Integer.toString(seekBar.getProgress()),Toast.LENGTH_SHORT).show();
                mediaPlayer.seekTo(seekBar.getProgress());
                lyrics.animate().translationYBy(-6500f).setDuration(duration + 10000);


            }
        });

        //Mover seekbar automatico conforme avanza la cancion
        new Timer().scheduleAtFixedRate(new TimerTask() {  //Funcion anonima
            @Override
            public void run() {
                advanceSeekbar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);






    }

    public int songToPlayId(int i){
        String letra="";
        int id=R.raw.cantstopthefeeling;

        switch (i){
            case 0:
                letra = "- CAN'T STOP THE FEELING - \n I got this feeling inside my bones \n" +
                        "It goes electric, wavey when I turn it on \n \n" +
                        "All through my city, all through my home \n" +
                        "We're flying up, no ceiling, when we in our zone \n" +
                        "\n" +
                        "I got that sunshine in my pocket \n" +
                        "Got that good song in my feet \n" +
                        "I feel that hot blood in my body when it drops \n \n" +
                        "I can't take my eyes up off it, moving so phenomenally \n" +
                        "You gon' like the way we rock it, so don't stop \n" +
                        "\n \n" +
                        "Under the lights when everything goes \n \n" +
                        "Nowhere to hide when I'm getting you close \n \n \n" +
                        "When we move, well, you already know \n" +
                        "So just imagine, just imagine, just imagine \n \n \n " +
                        "Nothing I can see but you when you dance, dance, dance \n" +
                        "Feeling good, good, creeping up on you \n" +
                        "So just dance, dance, dance, come on \n \n \n" +
                        "All those things I should do to you \n" +
                        "But you dance, dance, dance \n" +
                        "And ain't nobody leaving soon, so keep dancing \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "I can't stop the feeling \n \n " +
                        "So just dance, dance, dance \n \n" +
                        "I can't stop the feeling \n \n"  +
                        "So just dance, dance, dance, come on \n \n" +
                        "\n" +
                        "Ooh, it's something magical \n \n" +
                        "It's in the air, it's in my blood, it's rushing on \n \n" +
                        "I don't need no reason, don't need control \n \n" +
                        "I fly so high, no ceiling, when I'm in my zone \n" +
                        "\n \n" +
                        "Cause I got that sunshine in my pocket \n" +
                        "Got that good song in my feet \n" +
                        "I feel that hot blood in my body when it drops \n \n" +
                        "I can't take my eyes up off it, moving so phenomenally \n \n" +
                        "You gon' like the way we rock it, so don't stop \n" +
                        "\n" +
                        "\n" +
                        "\n \n " +
                        "Under the lights when everything goes \n \n" +
                        "Nowhere to hide when I'm getting you close \n \n \n \n" +
                        "When we move, well, you already know \n \n" +
                        "So just imagine, just imagine, just imagine \n \n \n \n \n " +
                        "Nothing I can see but you when you dance, dance, dance \n" +
                        "Feeling good, good, creeping up on you \n" +
                        "So just dance, dance, dance, come on \n \n \n" +
                        "All those things I should do to you \n" +
                        "But you dance, dance, dance \n" +
                        "And ain't nobody leaving soon, so keep dancing \n" +
                        "\n \n \n" +
                        "I can't stop the feeling \n \n" +
                        "So just dance, dance, dance \n" +
                        "I can't stop the feeling \n \n" +
                        "So just dance, dance, dance \n" +
                        "I can't stop the feeling \n \n" +
                        "So just dance, dance, dance \n" +
                        "I can't stop the feeling \n \n" +
                        "So keep dancing, come on \n" +
                        "\n \n \n \n \n \n \n \n \n \n" +
                        "I can't stop the, I can't stop the \n \n \n \n" +
                        "I can't stop the, I can't stop the \n \n \n \n" +
                        "I can't stop the feeling \n \n \n \n" +
                        "\n" +
                        "Nothing I can see but you when you dance, dance, dance \n" +
                        "(I can't stop the feeling) \n" +
                        "Feeling good, good, creeping up on you \n" +
                        "So just dance, dance, dance, come on \n" +
                        "(I can't stop the feeling) \n" +
                        "All those things I should do to you \n" +
                        "But you dance, dance, dance \n" +
                        "(I can't stop the feeling) \n" +
                        "And ain't nobody leaving soon, so keep dancing \n" +
                        "\n" +
                        "Everybody sing \n" +
                        "(I can't stop the feeling) \n" +
                        "Got this feeling in my body \n" +
                        "(I can't stop the feeling) \n" +
                        "Got this feeling in my body \n" +
                        "(I can't stop the feeling) \n" +
                        "Wanna see you move your body \n" +
                        "(I can't stop the feeling) \n" +
                        "Got this feeling in my body \n" +
                        "Break it down \n" +
                        "Got this feeling in my body \n" +
                        "Can't stop the feeling \n" +
                        "Got this feeling in my body, come on";
                id= R.raw.cantstopthefeeling;
                break;

            case 1:
                letra = " - CLOSER - \n \n Hey, I was doing just fine before I met you \n \n" +
                        "I drank too much and that’s an issue \n \n" +
                        "But I’m okay \n \n \n" +
                        "Hey, you tell your friends \n \n" +
                        "It was nice to meet them \n \n" +
                        "But I hope I never see them again \n \n" +
                        "\n" +
                        "I know it breaks your heart \n" +
                        "Moved to the city in a broke down car and \n \n" +
                        "Four years, no calls \n" +
                        "Now you’re looking pretty \n" +
                        "In a hotel bar and I can’t stop \n \n" +
                        "No, I can’t stop \n" +
                        "\n" +
                        "So baby pull me closer \n" +
                        "In the backseat of your Rover \n \n" +
                        "That I know you can’t afford \n" +
                        "Bite that tattoo on your shoulder \n \n \n" +
                        "Pull the sheets right off the corner \n" +
                        "Of the mattress that you stole \n \n" +
                        "From your roommate back in Boulder \n" +
                        "We ain’t ever getting older \n \n" +
                        "\n" +
                        "\n" +
                        "\n \n \n" +
                        "We ain’t ever getting older \n \n \n \n " +
                        "We ain’t ever getting older \n \n " +
                        "\n \n" +
                        "You, look as the good as the day as I met you \n \n" +
                        "I forget just why I left you, I was insane \n \n \n \n" +
                        "Stay, and play that Blink-182 song \n \n" +
                        "That we beat to death in Tuscon, okay? \n \n \n" +
                        "\n" +
                        "I know it breaks your heart \n" +
                        "Moved to the city in a broke down car and \n \n \n" +
                        "Four years, no calls \n" +
                        "Now you’re looking pretty \n" +
                        "In a hotel bar and I can’t stop \n \n \n \n" +
                        "No, I can’t stop \n \n \n \n" +
                        "\n" +
                        "So baby pull me closer \n \n" +
                        "In the backseat of your Rover \n \n \n " +
                        "That I know you can’t afford \n" +
                        "Bite that tattoo on your shoulder \n \n \n" +
                        "Pull the sheets right off the corner \n" +
                        "Of the mattress that you stole \n \n" +
                        "From your roommate back in Boulder \n" +
                        "We ain’t ever getting older \n \n" +
                        "\n" +
                        "\n" +
                        "\n \n \n \n" +
                        "We ain’t ever getting older \n\n \n \n" +
                        "We ain’t ever getting older \n \n " +
                        "\n \n" +
                        "So baby pull me closer \n \n" +
                        "In the backseat of your Rover \n \n \n " +
                        "That I know you can’t afford \n" +
                        "Bite that tattoo on your shoulder \n \n \n" +
                        "Pull the sheets right off the corner \n" +
                        "Of the mattress that you stole \n \n" +
                        "From your roommate back in Boulder \n" +
                        "We ain’t ever getting older \n \n" +
                        "We ain’t ever getting older \n \n" +
                        "We ain’t ever getting older \n \n" +
                        "We ain’t ever getting older \n \n" +
                        "We ain’t ever getting older \n \n" +
                        "We ain’t ever getting older \n" +
                        "\n \n \n \n " +
                        "We ain’t ever getting older \n" +
                        "No we ain’t ever getting older";
                id= R.raw.closer;
                break;
            case 2:
                letra = "- DON'T LET ME DOWN - \n \n \n \n \n Crashing, hit a wall \n" +
                        "Right now I need a miracle \n \n" +
                        "Hurry up now, I need a miracle \n \n \n \n \n" +
                        "Stranded, reaching out \n \n \n" +
                        "I call your name but you're not around \n \n" +
                        "I say your name but you're not around \n" +
                        "\n \n " +
                        "I need you, I need you, I need you right now \n \n" +
                        "Yeah, I need you right now \n \n" +
                        "So don't let me, don't let me, don't let me down \n \n" +
                        "I think I'm losing my mind now \n \n" +
                        "It's in my head, darling I hope \n" +
                        "That you'll be here, when I need you the most \n \n" +
                        "So don't let me, don't let me, don't let me down \n" +
                        "D-Don't let me down \n \n \n \n " +
                        "\n" +
                        "Don't let me down \n \n \n " +
                        "Don't let me down, down, down \n \n \n" +
                        "Don't let me down, don't let me down, down, down \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "R-r-running out of time \n" +
                        "I really thought you were on my side \n" +
                        "But now there's nobody by my side \n \n \n" +
                        "\n \n \n" +
                        "I need you, I need you, I need you right now \n \n" +
                        "Yeah, I need you right now \n \n" +
                        "So don't let me, don't let me, don't let me down \n \n" +
                        "I think I'm losing my mind now \n \n" +
                        "It's in my head, darling I hope \n" +
                        "That you'll be here, when I need you the most \n \n" +
                        "So don't let me, don't let me, don't let me down \n" +
                        "D-Don't let me down \n \n \n \n " +
                        "\n" +
                        "Don't let me down \n \n \n \n" +
                        "Don't let me down, down, down \n \n \n \n \n" +
                        "Don't let me down, down, down \n \n \n \n" +
                        "Don't let me down, down, down \n \n \n \n \n" +
                        "Don't let me down, don't let me down, down, down \n \n \n \n" +
                        "\n" +
                        "Oh, I think I'm losing my mind now, yeah, yeah, yeah \n \n \n \n" +
                        "Oh, I think I'm losing my mind now, yeah, yeah \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "I need you, I need you, I need you right now \n \n" +
                        "Yeah, I need you right now \n \n" +
                        "So don't let me, don't let me, don't let me down \n \n" +
                        "I think I'm losing my mind now \n \n" +
                        "It's in my head, darling I hope \n" +
                        "That you'll be here, when I need you the most \n \n" +
                        "So don't let me, don't let me, don't let me down \n" +
                        "D-Don't let me down \n \n \n \n " +
                        "\n" +
                        "Yeah, don't let me down \n \n \n" +
                        "Yeah, don't let me down \n \n \n" +
                        "Don't let me down, oh no \n \n \n" +
                        "Said don't let me down \n \n" +
                        "Don't let me down \n \n \n \n " +
                        "\n" +
                        "Don't let me down \n \n \n \n" +
                        "Don't let me down, down, down \n \n \n \n \n" ;
                id = R.raw.dontletmedown;
                break;
            case 3:
                letra = "- EN EL CIELO NO HAY HOSPITAL - \n \n \n \n \n" +
                        "Gracias al Dios Bendito yo fui sanado de todo stress \n" +
                        "Me curó de la sinusitis y la migraña que bueno es El \n" +
                        "Me sacó de la depresión y ahora yo le bailo en un solo pie \n" +
                        "y no me duele la cinturita, ay! que rico. \n \n \n" +
                        "\n" +
                        "En el cielo no hay hospital ! te aseguro que te quiere sanar \n \n" +
                        "En su nombre te vas a levantar. En el cielo no hay hospital ! \n" +
                        "\n \n" +
                        "En el cielo no hay hospital ! te aseguro que te quiere sanar \n \n" +
                        "En su nombre te vas a levantar. En el cielo no hay hospital ! \n" +
                        "\n \n \n \n \n \n" +
                        "Gracias a Jesucristo yo fui sanado de un gran dolor \n \n" +
                        "Para El no hay nada imposible todo lo puede que gran doctor ! (que doctor !) \n \n \n" +
                        "Me sanó del ojo derecho y de un colapso en el corazón. \n" +
                        "Y no me duele la espalda baja, ay! que rico. \n \n" +
                        "\n" +
                        "\n" +
                        "\n \n" +
                        "En el cielo no hay hospital ! te aseguro que te quiere sanar \n \n" +
                        "En su nombre te vas a levantar. En el cielo no hay hospital ! \n \n" +
                        "\n" +
                        "En el cielo no hay hospital ! te aseguro que te quiere sanar \n \n" +
                        "En su nombre te vas a levantar. En el cielo no hay hospital ! \n \n \n \n \n \n " +
                        "\n" +
                        "No hay hospital no ! ,\n \n no hay hospital,\n \n \n \n no hay hospital no no ! \n" +
                        "\n \n \n \n \n" +
                        "Te lo dije (No hay hospital) \n \n" +
                        "No hay Ben Gay (No hay hospital) \n \n" +
                        "y te sana (No hay hospital) \n \n" +
                        "Bueno y Fiel (No hay hospital) \n \n" +
                        "\n" +
                        "No es un cuento (No hay hospital) \n \n" +
                        "Ni es palé (No hay hospital) \n \n" +
                        "Te levanta (No hay hospital) \n \n" +
                        "Con tu Fé (No hay hospital) \n \n" +
                        "\n \n \n" +
                        "Si ! eeeeje ! \n \n \n \n \n \n \n" +
                        "Mira que te levanta ... \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Te repito (No hay hospital) \n \n" +
                        "Clementina (No hay hospital) \n \n" +
                        "El te añoña (No hay hospital) \n \n" +
                        "y El te cuida (No hay hospital) \n \n" +
                        "\n" +
                        "Yo le canto (No hay hospital) \n \n" +
                        "Como es (No hay hospital) \n \n" +
                        "y le bailo (No hay hospital) \n \n" +
                        "En un pie (Pero mira que bonito !) \n \n" +
                        "\n" +
                        "En un pie ! \n" +
                        "En un pie ! \n" +
                        "En un pie ! (Ay mira como bailo !) \n" +
                        "En un pie ! \n" +
                        "En un pie ! \n" +
                        "En un pie ! (Ojooooye !) \n" +
                        "En un pie ! \n" +
                        "En un pie ! \n" +
                        "\n \n" +
                        "En el cielo no hay hospital ! te aseguro que te quiere sanar \n \n" +
                        "En su nombre te vas a levantar. En el cielo no hay hospital ! \n \n" +
                        "\n \n" +
                        "En el cielo no hay hospital ! te aseguro que te quiere sanar \n \n" +
                        "En su nombre te vas a levantar. En el cielo no hay hospital !";


                id = R.raw.enelcielonohayhospital;
                break;
            case 4:
                letra = "- GOOD FEELING - \n \n \n \n Oh, sometimes I get a good feeling, yeah \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n" +
                        "Oh, sometimes I get a good feeling, yeah \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n" +
                        "\n" +
                        "Yes I can, doubt to believe what I know what’s his plan \n" +
                        "Pull me, grab me, grab till the bucket can’t have me \n" +
                        "I’ll be the president one day \n" +
                        "January first, oh, you like that gossip \n" +
                        "Like you the one drinking that god sip dot com \n" +
                        "Now I gotta work with your tone \n" +
                        "How many rolling stones you want \n" +
                        "Yeah I got a brand new spirit, \n" +
                        "Speak it and it’s done \n" +
                        "Woke up on the side of the bed like I won \n" +
                        "Top like a winner \n" +
                        "G5 dealer, U.S to Taiwan \n" +
                        "Now who can say that, I wanna play back \n" +
                        "Mama knew I was a needle in a hay stack \n" +
                        "A bugatti boy, plus maybach \n" +
                        "I got a feeling it’s around asap \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Oh, sometimes I get a good feeling, yeah \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n \n \n \n" +
                        "Oh, sometimes I get a good feeling, yeah \n \n \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n" +
                        "\n \n \n \n \n" +
                        "The mountain top, walk on water \n" +
                        "I got power, feel so royal \n" +
                        "One second, I’ma strike oil \n" +
                        "Diamond, platinum, no more for you \n" +
                        "Gotta drill a land, never giving in \n" +
                        "Giving up’s not an option and gotta get it in \n" +
                        "Witness I got the heart of 20 men \n" +
                        "No fear go to sleep in the lion’s den \n" +
                        "That flow, that funk that crown \n" +
                        "You looking at the king of the jungle now \n" +
                        "Stronger than ever can’t hold me down \n" +
                        "A hundred miles going till them bitches smile \n" +
                        "Straight game face, it’s game day \n" +
                        "See me wanna do the drop from may lay? \n" +
                        "No trick plays, I’m Bill Gates, take a genius to understand me \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Oh, sometimes I get a good feeling, yeah \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n \n \n \n" +
                        "Oh, sometimes I get a good feeling, yeah \n \n \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n \n \n \n \n \n \n \n \n \n \n \n " +
                        "\n" +
                        "\n" +
                        "\n \n \n" +
                        "Oh, sometimes I get a good feeling, yeah \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n \n \n \n" +
                        "Oh, sometimes I get a good feeling, yeah \n \n \n" +
                        "Get a feeling that I never never never never had before, no no \n" +
                        "I get a good feeling, yeah \n \n \n \n \n \n \n \n \n \n \n \n " ;
               id = R.raw.goodfeeling;
               break;
            /*case 5:
                letra="- HAPPY - \n It might seem crazy what I'm about to say \n" +
                        "Sunshine she's here, you can take a break \n" +
                        "I'm a hot air balloon that could go to space \n" +
                        "With the air, like I don't care baby by the way \n" +
                        "\n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like a room without a roof \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like happiness is the truth \n" +
                        "Because I'm happy \n" +
                        "Clap along if you know what happiness is to you \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like that's what you want to do \n" +
                        "\n" +
                        "Here come bad news talking this and that \n" +
                        "Well, give me all you got and don't hold back \n" +
                        "Well, I should probably warn you \n" +
                        "I'll be just fine \n" +
                        "No offense to you, don't waste your time \n" +
                        "Here's why: \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like a room without a roof \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like happiness is the truth \n" +
                        "Because I'm happy \n" +
                        "Clap along if you know what happiness is to you \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like that's what you want to do \n" +
                        "\n" +
                        "-Happy- bring me down, can't nothing \n" +
                        "-Happy- bring me down \n" +
                        "My level's too high -happy- to bring me down \n" +
                        "Can't nothing -happy- bring me down I said \n" +
                        "-Happy- bring me down, can't nothing \n" +
                        "-Happy- bring me down \n" +
                        "My level's too high -happy- to bring me down \n" +
                        "Can't nothing -happy- bring me down I said \n" +
                        "\n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like a room without a roof \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like happiness is the truth \n" +
                        "Because I'm happy \n" +
                        "Clap along if you know what happiness is to you \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like that's what you want to do \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like a room without a roof \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like happiness is the truth \n" +
                        "Because I'm happy \n" +
                        "Clap along if you know what happiness is to you \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like that's what you want to do \n" +
                        "\n" +
                        "-Happy- bring me down, can't nothing \n" +
                        "-Happy- bring me down \n" +
                        "My level's too high -happy- to bring me down \n" +
                        "Can't nothing -happy- bring me down I said \n" +
                        "\n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like a room without a roof \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like happiness is the truth \n" +
                        "Because I'm happy \n" +
                        "Clap along if you know what happiness is to you \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like that's what you want to do \n" +
                        "\n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like a room without a roof \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like happiness is the truth \n" +
                        "Because I'm happy \n" +
                        "Clap along if you know what happiness is to you \n" +
                        "Because I'm happy \n" +
                        "Clap along if you feel like that's what you want to do \n" +
                        "\n" +
                        "Come on!";
                id = R.raw.happy;
                break;
            case 6:
                letra="- I TOOK A PILL IN IBIZA - \n I took a pill in Ibiza \n" +
                        "To show Avicii I was cool \n" +
                        "And when I finally got sober, felt ten years older \n" +
                        "But f*** it, it was something to do \n" +
                        "I'm living out in LA \n" +
                        "I drive a sports car just to prove \n" +
                        "I'm a real big baller cause I made a million dollars \n" +
                        "And I spend it on girls and shoes \n" +
                        "\n" +
                        "But you don't wanna be high like me \n" +
                        "Never really knowing why like me \n" +
                        "You don't ever wanna step off that roller coaster and be all alone \n" +
                        "You don't wanna ride the bus like this \n" +
                        "Never knowing who to trust like this \n" +
                        "You don't wanna be stuck up on that stage singing \n" +
                        "Stuck up on that stage singing \n" +
                        "All I know are sad songs, sad songs \n" +
                        "Darling, all I know are sad songs, sad songs \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "I'm just a singer who already blew his shot \n" +
                        "I get along with old timers \n" +
                        "Cause my name's a reminder of a pop song people forgot \n" +
                        "And I can't keep a girl, no \n" +
                        "Cause as soon as the sun comes up \n" +
                        "I cut em all loose and work's my excuse \n" +
                        "But the truth is I can't open up \n" +
                        "\n" +
                        "But you don't wanna be high like me \n" +
                        "Never really knowing why like me \n" +
                        "You don't ever wanna step off that roller coaster and be all alone \n" +
                        "You don't wanna ride the bus like this \n" +
                        "Never knowing who to trust like this \n" +
                        "You don't wanna be stuck up on that stage singing \n" +
                        "Stuck up on that stage singing \n" +
                        "All I know are sad songs, sad songs \n" +
                        "Darling, all I know are sad songs, sad songs \n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "I took a plane to my home town \n" +
                        "I brought my pride and my guitar \n" +
                        "All my friends are all gone but there's manicured lawns \n" +
                        "And the people still think I'm a star \n" +
                        "I walked around downtown \n" +
                        "I met some fans on Lafayette \n" +
                        "They said tell us how to make it cause we're getting real impatient \n" +
                        "So I looked em in the eye and said \n" +
                        "\n" +
                        "You don't wanna be high like me \n" +
                        "Never really knowing why like me \n" +
                        "You don't ever wanna step off that roller coaster and be all alone \n" +
                        "You don't wanna ride the bus like this \n" +
                        "Never knowing who to trust like this \n" +
                        "You don't wanna be stuck up on that stage singing \n" +
                        "Stuck up on that stage singing \n" +
                        "All I know are sad songs, sad songs \n" +
                        "Darling, all I know are sad songs, sad songs";
                id = R.raw.ibiza;
                break;*/

        }
        lyrics.setText(letra);
        return id;

    }

    public void init(){
        lyrics.setY(0);
        mediaPlayer = MediaPlayer.create(this,songToPlayId(actualSong));
        //Devolverme servicio del sistema
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);



         duration = mediaPlayer.getDuration();
         progress = mediaPlayer.getCurrentPosition();
         advanceSeekbar.setMax(duration);
         advanceSeekbar.setProgress(progress);



    }
}
