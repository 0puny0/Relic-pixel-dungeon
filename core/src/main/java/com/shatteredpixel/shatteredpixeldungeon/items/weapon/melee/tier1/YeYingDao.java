package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier1;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Scimitar;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class YeYingDao extends MeleeWeapon {
    {
        image = ItemSpriteSheet.YE_YING_DAO;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.2f;

        tier = 1;
        DLY = 0.8f;
    }
    @Override
    public int max(int lvl) {
        return  8+2*lvl;
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        beforeAbilityUsed(hero, null);
        //1 turn less as using the ability is instant
        Buff.prolong(hero, Scimitar.SwordDance.class, 3+buffedLvl()+hero.weaponMastery);
        hero.sprite.operate(hero.pos);
        hero.next();
        afterAbilityUsed(hero);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 4+buffedLvl()+ Dungeon.hero.weaponMastery);
        } else {
            return Messages.get(this, "typical_ability_desc", 4);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return Integer.toString(4 + level);
    }
}

