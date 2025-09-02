package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfAbility;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUpgrade;

import java.util.ArrayList;

public class Stithy extends Item{
    public static final String AC_STITHY	= "STITHY";
    {
        image = ItemSpriteSheet.STITHY;

        defaultAction = AC_STITHY;

        unique = true;
    }
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_STITHY );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );
        if (action.equals( AC_STITHY )) {
            GameScene.selectItem( itemSelector );
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
    protected Class<?extends Bag> preferredBag = null;

    protected boolean usableOnItem( Item item ){
        return true;
    }


    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(this, "inv_title");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return preferredBag;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return usableOnItem(item);
        }

        @Override
        public void onSelect( Item item ) {
            if (item!=null){
                ScrollOfAbility i=(Dungeon.hero.belongings.backpack.Search(ScrollOfAbility.class));
                GameScene.show(new WndUpgrade(i, item, false));
            }
        }
    };
}
