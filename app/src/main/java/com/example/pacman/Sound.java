package com.example.pacman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class Sound {
    public int eat,eatfruit,eatghost,intermission,lose,start,siren,pac6,soundbg;
    public SoundPool soundPool;
    private boolean soundPoolLoaded;

    public Sound (Context context)  {
        // With Android API >= 21.
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(100);

            this.soundPool = builder.build();
        }
        // With Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(100, AudioManager.STREAM_MUSIC, 0);
        }

        // When SoundPool load complete.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
            }
        });

        // Load the sound explosion.wav into SoundPool
        this.eat = this.soundPool.load(context, R.raw.pacman_eat,1);
        eatfruit = this.soundPool.load(context, R.raw.pacman_eatfruit,1);
        eatghost = this.soundPool.load(context, R.raw.pacman_eatghost,1);
        pac6 = this.soundPool.load(context, R.raw.pac6,1);
        siren = this.soundPool.load(context, R.raw.siren,1);
        start = this.soundPool.load(context, R.raw.pacman_start,1);
        lose = this.soundPool.load(context, R.raw.pacman_lose,1);
        intermission = this.soundPool.load(context, R.raw.pacman_intermission,1);




    }
    public int playSoundBackground(int id)  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound explosion.wav
            soundbg = id;
            int streamId = this.soundPool.play(id,leftVolumn, rightVolumn, 1, -1, 1f);
            return  streamId;
        }
        return -1;
    }
    public void stop(int id){
       this.soundPool.stop(id);
    }
    public int play(int id)  {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            // Play sound explosion.wav
            int streamId = this.soundPool.play(id,leftVolumn, rightVolumn, 1, 0, 1f);
            return streamId;
        }
        return -1;
    }


}
