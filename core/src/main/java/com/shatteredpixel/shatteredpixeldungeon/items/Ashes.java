package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;

import java.util.ArrayList;

public class Ashes extends Item{
    public static final String AC_USE	= "USE";
    {
        image = ItemSpriteSheet.ASHES;
        defaultAction = AC_USE;
        bones = false;
    }
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
            GameScene.selectItem(itemSelector);
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

    protected static WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return  Messages.get(Ashes.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return MagicalHolster.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            if (item.cursed) return false;
            return item instanceof Artifact||item instanceof Wand||item instanceof ClassArmor;
        }

        @Override
        public void onSelect( Item item ) {
            if (item == null) return;
            if (item instanceof Artifact){
                ((Artifact) item).charge(curUser,8f);
            }else if (item instanceof Wand){
                ((Wand) item).gainCharge(4f);
            }else if (item instanceof ClassArmor){
                ((ClassArmor) item).gainCharge(25f);
            }
            curItem.detach( curUser.belongings.backpack );
            curUser.spend(Actor.TICK);
            curUser.busy();
            curUser.sprite.operate( curUser.pos );
            ScrollOfRecharging.charge(curUser);
        }
    };
}
