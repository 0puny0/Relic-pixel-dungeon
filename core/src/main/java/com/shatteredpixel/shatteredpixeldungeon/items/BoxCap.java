package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Glaive;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;


public class BoxCap extends Artifact {
    {
        image = ItemSpriteSheet.BOX_CAP;
        exp = 0;
        charge = 10;
        chargeCap =  10;
        defaultAction = AC_USE;
        unique = true;
        bones = false;
    }
    public static final String AC_USE	= "USE";
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped(hero)&&!cursed&&charge>1){
            actions.add( AC_USE );
        }
        return actions;
    }
    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );
        if (action.equals( AC_USE )) {
            if (charge<1){
                GLog.i(Messages.get(this, "no_charge"));
                return;
            }
            Buff.affect(hero, Focus.class);
            hero.busy();
            hero.spendAndNext(1f);
            Talent.onArtifactUsed(Dungeon.hero);
            charge-=2;
            updateQuickslot();
        }
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new CapDR();
    }
    @Override
    public void charge(Hero target, float amount) {
        if (cursed || target.buff(MagicImmune.class) != null) return;

        if (charge < chargeCap) {
            partialCharge += 0.25f*amount;
            while (partialCharge >= 1f) {
                charge++;
                partialCharge--;
            }
            if (charge >= chargeCap){
                partialCharge = 0;
                charge = chargeCap;
            }
            updateQuickslot();
        }
    }
    @Override
    public boolean isIdentified() {
        return true;
    }
    public static class Focus extends Buff {
        @Override
        public boolean act() {
            detach();
            return super.act();
        }

    }
    public  class CapDR extends ArtifactBuff{}
}
