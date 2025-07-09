package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class HiddenWarlockSprite extends MobSprite{
    public HiddenWarlockSprite() {
        super();

        texture( Assets.Sprites.WARLOCK );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0+21, 0+21, 0+21, 1+21, 0+21, 0+21, 1+21, 1+21 );

        run = new Animation( 15, true );
        run.frames( frames, 0+21, 2+21, 3+21, 4+21 );

        attack = new Animation( 12, false );
        attack.frames( frames, 0+21, 5+21, 6+21 );

        zap = attack.clone();

        die = new Animation( 15, false );
        die.frames( frames, 0+21, 7+21, 8+21, 8+21, 9+21, 10+21 );

        play( idle );
    }

    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((Warlock)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }
}
