package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Flesh  extends Food {
    {
        image = ItemSpriteSheet.FLESH;
        energy = Hunger.HUNGRY/3f;
        bones = false;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    public int value() {
        return 5 * quantity;
    }

    public static void effect(Hero hero){

        switch (Random.Int( 5 )) {
            case 0:
                GLog.i( Messages.get(FrozenCarpaccio.class, "invis") );
                Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
                break;
            case 1:
                GLog.i( Messages.get(FrozenCarpaccio.class, "hard") );
                Barkskin.conditionallyAppend( hero, hero.HT / 4, 1 );
                break;
            case 2:
                GLog.w( Messages.get(MysteryMeat.class, "not_well") );
                Buff.affect( hero, Poison.class ).set( hero.HT / 5 );
                break;
            case 3:
                GLog.i( Messages.get(FrozenCarpaccio.class, "refresh") );
                PotionOfHealing.cure(hero);
                break;
            case 4:
                GLog.i(Messages.get(MeatPie.class, "eat_msg") );
                Buff.affect(hero, WellFed.class).setLeft((int)(Hunger.STARVING/3));
                break;
        }
    }
}
