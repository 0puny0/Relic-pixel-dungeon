package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class KingSword extends MeleeWeapon {
    {
        image = ItemSpriteSheet.KING_SWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1f;

        tier = 5;
    }

    @Override
    protected int baseChargeUse(Hero hero, Char target){
        if (hero.buff(Sword.CleaveTracker.class) != null){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(7+lvl) damage, roughly +40% base dmg, +30% scaling
        int dmgBoost = augment.damageFactor( damageBoost(7+ buffedLvl()+hero.weaponMastery) );
        Sword.cleaveAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        Hero hero= Dungeon.hero;
        int dmgBoost = damageBoost( 7 + buffedLvl() +hero.weaponMastery) ;
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(minDamage(hero)+dmgBoost), augment.damageFactor(maxDamage(hero)+dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", min(0)+7, max(0)+7);
        }
    }

    public String upgradeAbilityStat(int level){
        int dmgBoost = 7 + level;
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

    @Override
    public int value() {
        int price = 1000;
        if (hasGoodEnchant()) {
            price *= 1.5;
        }
        if (cursedKnown && (cursed || hasCurseEnchant())) {
            price /= 2;
        }
        if (levelKnown && level() > 0) {
            price *= (level() + 1);
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }
}
