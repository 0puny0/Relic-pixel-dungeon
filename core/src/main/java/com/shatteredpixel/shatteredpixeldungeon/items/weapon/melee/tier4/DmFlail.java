package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Flail;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class DmFlail extends MeleeWeapon {
    {
        image = ItemSpriteSheet.DM_FLAIL;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 4;
        ACC = 0.8f; //0.8x accuracy
        //also cannot surprise attack, see Hero.canSurpriseAttack
    }

    @Override
    public int max(int lvl) {
        return  Math.round(7*(tier+1)) +        //35 base, up from 25
                lvl*Math.round(1.6f*(tier+1));  //+8 per level, up from +5
    }

    private static int spinBoost = 0;

    @Override
    public int damageRoll(Char owner) {
        int dmg = super.damageRoll(owner) + spinBoost;
        if (spinBoost > 0) Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
        spinBoost = 0;
        return dmg;
    }

    @Override
    public float accuracyFactor(Char owner, Char target) {
        Flail.SpinAbilityTracker spin = owner.buff(Flail.SpinAbilityTracker.class);
        if (spin != null) {
            Actor.add(new Actor() {
                { actPriority = VFX_PRIO; }
                @Override
                protected boolean act() {
                    if (owner instanceof Hero && !target.isAlive()){
                        onAbilityKill((Hero)owner, target);
                    }
                    Actor.remove(this);
                    return true;
                }
            });
            //we detach and calculate bonus here in case the attack misses (e.g. vs. monks)
            spin.detach();
            //+(8+2*lvl) damage per spin, roughly +40% base damage, +45% scaling
            // so +120% base dmg, +135% scaling at 3 spins
            int weaponLvl=buffedLvl();
            if (owner instanceof Hero ){
                weaponLvl+=((Hero) owner).weaponMastery;
            }
            spinBoost = spin.spins * augment.damageFactor(damageBoost(8 + 2*weaponLvl) );
            return Float.POSITIVE_INFINITY;
        } else {
            spinBoost = 0;
            return super.accuracyFactor(owner, target);
        }
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (Dungeon.hero.buff(Flail.SpinAbilityTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Flail.SpinAbility(hero,this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = damageBoost(8 + 2*buffedLvl()+2* Dungeon.hero.weaponMastery);
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", augment.damageFactor(8));
        }
    }

    public String upgradeAbilityStat(int level){
        return "+" + augment.damageFactor(8 + 2*level);
    }

}
