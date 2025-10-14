package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greataxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class DmDrill extends MeleeWeapon {

    {
        image = ItemSpriteSheet.DM_DRILL;
        hitSound = Assets.Sounds.HIT_STRONG;
        hitSoundPitch = 1f;
        DLY = 1.5f;
        tier = 5;
    }

    @Override
    public int max(int lvl) {
        return  40+8*lvl;
    }

    @Override
    public int min(int lvl) {
        return 20+4*lvl;
    }

    @Override
    public int STRReq(int lvl) {
        int req = STRReq(tier+1, lvl); //20 base strength req, up from 18
        if (masteryPotionBonus){
            req -= 2;
        }
        return req;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(15+(2*lvl)) damage, roughly +60% base damage, +55% scaling
        int dmgBoost = augment.damageFactor(damageBoost(15 + 2*buffedLvl()+2*hero.weaponMastery) );
        Greataxe.RetributionAbility(hero,target,dmgBoost,this);
    }
}
