package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Honey extends Item{
    {
        image = ItemSpriteSheet.HONEY;
        defaultAction = AC_USE;
        stackable=true;
    }
    public static final String AC_USE	= "USE";
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_USE );
        return actions;
    }
    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );
        if (action.equals( AC_USE )) {
            Buff.affect(hero,Health.class).boost(hero.HT*2);

            detach( hero.belongings.backpack );

            hero.spend(Potion.TIME_TO_DROP);
            hero.busy();
            Sample.INSTANCE.play( Assets.Sounds.DRINK );
            hero.sprite.operate( hero.pos );
        }
    }
    @Override
    public boolean isUpgradable() {
        return false;
    }
    @Override
    public boolean isIdentified() {
        return true;
    }



    public static class Health extends Buff {

        private static final float STEP = 1f;
        private int level;
        private int amount;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public boolean act() {
            target.heal(1,false);
            level--;
            if (level <= 0) {
                detach();
            }
            spend( STEP );
            return true;
        }

        public void boost( int amount ){
            this.amount=amount;
            if (target != null) {
                level += amount;
            }
        }

        @Override
        public int icon() {
            return BuffIndicator.HEALTH;
        }
        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 1f, 0f);
        }
        @Override
        public float iconFadePercent() {
            return Math.max(0, (amount -level) / (float)amount);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(level);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", level);
        }

        private static final String LEVEL = "level";
        private static final String AMOUNT = "amount";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEVEL, level);
            bundle.put( AMOUNT, level);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            level = bundle.getInt( LEVEL );
            amount = bundle.getInt( AMOUNT );

        }
    }
}
