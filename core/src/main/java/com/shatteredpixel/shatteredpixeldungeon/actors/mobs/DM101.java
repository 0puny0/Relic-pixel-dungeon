package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM101Sprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class DM101 extends DM100 {
    private static final float TIME_TO_ZAP	= 1f;
    {
        spriteClass = DM101Sprite.class;
        lootChance = 1f;
    }

    @Override
    public int attackSkill(Char target) {
        return 13;
    }

    @Override
    protected boolean doAttack( Char enemy ) {


        spend( TIME_TO_ZAP );

        Invisibility.dispel(this);
        if (hit( this, enemy, true )) {
            int dmg = Random.NormalIntRange(3, 10);
            dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
            enemy.damage( dmg, new LightningBolt() );

            if (enemy.sprite.visible) {
                enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
                enemy.sprite.flash();
            }

            if (enemy == Dungeon.hero) {

                PixelScene.shake( 2, 0.3f );

                if (!enemy.isAlive()) {
                    Badges.validateDeathFromEnemyMagic();
                    Dungeon.fail( this );
                    GLog.n( Messages.get(this, "zap_kill") );
                }
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }

        if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
            sprite.zap( enemy.pos );
            return false;
        } else {
            return true;
        }
    }

}
