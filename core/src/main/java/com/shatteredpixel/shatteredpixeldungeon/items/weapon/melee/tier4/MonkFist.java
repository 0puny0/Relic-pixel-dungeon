package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier4;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cooldown;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sai;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class MonkFist extends MeleeWeapon {
    {
        image = ItemSpriteSheet.MONK_FIST;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 1.2f;

        tier = 4;
        DLY = 0.5f; //2x speed
    }

    @Override
    public boolean doEquip(Hero hero) {
        Buff.affect(hero,FocusCooldown.class,30);
        return super.doEquip(hero);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (hero.buff(FocusCooldown.class)!=null){
            hero.buff(FocusCooldown.class).detach();
        }
        if (hero.buff(Focus.class)!=null){
            hero.buff(Focus.class).detach();
        }
        return super.doUnequip(hero, collect, single);
    }

    @Override
    public int min(int lvl) {
        return 6+2*lvl;
    }

    @Override
    public int max(int lvl) {
        return  10+2*lvl;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(5+lvl) damage, roughly +50% base damage, +50% scaling
        int dmgBoost = augment.damageFactor(damageBoost(5 + buffedLvl()+hero.weaponMastery) );
        Sai.comboStrikeAbility(hero, target, 0, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        Hero hero= Dungeon.hero;
        int dmgBoost =  damageBoost(5 + buffedLvl() +hero.weaponMastery);
        if (levelKnown){
            return Messages.get(this, "ability_desc", augment.damageFactor(dmgBoost));
        } else {
            return Messages.get(this, "typical_ability_desc", augment.damageFactor(5));
        }
    }

    public String upgradeAbilityStat(int level){
        return "+" + augment.damageFactor(5 + level);
    }

    public static class FocusCooldown extends Cooldown{

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.25f, 1.5f, 1f);
        }

        @Override
        public  int duration() {
            return 30;
        }

        @Override
        public boolean act() {
            Buff.affect(target, Focus.class);
            return super.act();
        }
    }
    public static class Focus extends Buff{
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.25f, 1.5f, 1f);
        }
    }
}
