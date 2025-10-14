package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Whip;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ScorpioTail extends MeleeWeapon {

    {
        image = ItemSpriteSheet.SCORPIO_TAIL;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.1f;

        tier = 5;
        RCH = 3;    //lots of extra reach
    }

    @Override
    public int max(int lvl) {
        return  20+4*lvl;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (Random.Int( 5 ) == 0) {
            Buff.prolong( defender, Cripple.class, Cripple.DURATION );
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Whip.lashAbility(hero,target,1,0,this);
    }
    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(minDamage(Dungeon.hero)), augment.damageFactor(maxDamage(Dungeon.hero)));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0), max(0));
        }
    }

    public String upgradeAbilityStat(int level){
        return augment.damageFactor(min(level)) + "-" + augment.damageFactor(max(level));
    }
}
