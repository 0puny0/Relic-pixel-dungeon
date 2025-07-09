package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class DevilEyeSprite extends MobSprite {
    private int zapPos;

    private Animation charging;
    private Emitter chargeParticles;

    public DevilEyeSprite() {
        super();

        texture( Assets.Sprites.EYE );

        TextureFilm frames = new TextureFilm( texture, 16, 18 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0+16, 1+16, 2+16 );

        charging = new Animation( 12, true);
        charging.frames( frames, 3+16, 4+16 );

        run = new Animation( 12, true );
        run.frames( frames, 5+16, 6+16 );

        attack = new Animation( 8, false );
        attack.frames( frames, 4+16, 3+16 );
        zap = attack.clone();

        die = new Animation( 8, false );
        die.frames( frames, 7+16, 8+16, 9+16 );

        play( idle );
    }

    @Override
    public void link(Char ch) {
        super.link(ch);

        chargeParticles = centerEmitter();
        chargeParticles.autoKill = false;
        chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
        chargeParticles.on = false;

        if (((Eye)ch).beamCharged) play(charging);
    }

    @Override
    public void update() {
        super.update();
        if (chargeParticles != null){
            chargeParticles.pos( center() );
            chargeParticles.visible = visible;
        }
    }

    @Override
    public void die() {
        super.die();
        if (chargeParticles != null){
            chargeParticles.on = false;
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (chargeParticles != null){
            chargeParticles.killAndErase();
        }
    }

    public void charge( int pos ){
        turnTo(ch.pos, pos);
        play(charging);
        if (visible) Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
    }

    @Override
    public void play(Animation anim) {
        if (chargeParticles != null) chargeParticles.on = anim == charging;
        super.play(anim);
    }

    @Override
    public void zap( int pos ) {
        zapPos = pos;
        super.zap( pos );
    }

    @Override
    public void onComplete( Animation anim ) {
        super.onComplete( anim );

        if (anim == zap) {
            idle();
            if (Actor.findChar(zapPos) != null){
                parent.add(new Beam.DeathRay(center(), Actor.findChar(zapPos).sprite.center()));
            } else {
                parent.add(new Beam.DeathRay(center(), DungeonTilemap.raisedTileCenterToWorld(zapPos)));
            }
            ((Eye)ch).deathGaze();
            ch.next();
        } else if (anim == die){
            chargeParticles.killAndErase();
        }
    }
}
