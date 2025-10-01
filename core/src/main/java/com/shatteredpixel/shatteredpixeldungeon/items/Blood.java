package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Blood extends Item{
    public static final String AC_DRINK	= "DRINK";
    {
        image = ItemSpriteSheet.BLOOD;

        defaultAction = AC_DRINK;
        bones = false;
    }
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_DRINK );
        return actions;
    }
    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );
        if (action.equals( AC_DRINK )) {
            detach( hero.belongings.backpack );

            hero.spend(Potion.TIME_TO_DROP);
            hero.busy();
            hero.heal(hero.HT/5,false);

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

}
