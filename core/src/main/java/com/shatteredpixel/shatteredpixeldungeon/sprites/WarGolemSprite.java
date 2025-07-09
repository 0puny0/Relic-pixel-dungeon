package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Golem;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

public class WarGolemSprite extends GolemSprite{
    private Emitter teleParticles;

    public WarGolemSprite() {
        super();

        texture( Assets.Sprites.GOLEM );

        TextureFilm frames = new TextureFilm( texture, 17, 19 );

        idle = new Animation( 4, true );
        idle.frames( frames, 0+15, 1+15 );

        run = new Animation( 12, true );
        run.frames( frames, 2+15, 3+15, 4+15, 5 +15);

        attack = new Animation( 10, false );
        attack.frames( frames, 6+15, 7+15, 8+15 );

        zap = attack.clone();

        die = new Animation( 15, false );
        die.frames( frames, 9+15, 10+15, 11+15, 12+15, 13+15 );

        play( idle );
    }
    @Override
    public int blood() {
        return 0xFF9a9799;
    }
}
