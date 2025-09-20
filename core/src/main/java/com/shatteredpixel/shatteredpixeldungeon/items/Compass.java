package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfAbility;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.RegularLevel;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.secret.SecretRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.special.SpecialRoom;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndUpgrade;

import java.util.ArrayList;

public class Compass extends Item{
    public static final String AC_VIEW	= "VIEW";
    {
        image = ItemSpriteSheet.COMPASS;

        defaultAction = AC_VIEW;

        unique = true;
    }
    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_VIEW );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );
        if (action.equals( AC_VIEW )) {
            View();
        }
    }
    private void View(){
        if (!(Dungeon.level instanceof RegularLevel) ||Dungeon.branch != 0){
            GLog.n(Messages.get(this,"nothing"));
            return;
        }
        SpecialRoom room=null;
        Level l=Dungeon.level;
        int heroPos=Dungeon.hero.pos;
        //找到最近的隐藏房间
        for (Room r : ((RegularLevel) l).rooms()){
            if (r instanceof SecretRoom){
                int doorPointNew=((SecretRoom) r).entrance().x+((SecretRoom) r).entrance().y*l.width();
                if (!l.secret[doorPointNew]){
                    continue;
                }else if (room!=null){
                    int doorPointOld=room.entrance().x+room.entrance().y*l.width();
                    if (l.secret[doorPointOld] && l.distance(heroPos,doorPointOld)<l.distance(heroPos,doorPointNew)) {
                        continue;
                    }
                }
                room=(SpecialRoom)r;
            }
        }
        if (room!=null){
            int doorPoint=room.entrance().x+room.entrance().y*l.width();
            int dis=l.distance(doorPoint,heroPos);
            if (dis>9){
                GLog.w(Messages.get(this,"far"));
            }else if (dis>3){
                GLog.p(Messages.get(this,"near"));
            }else {
                GLog.p(Messages.get(this,"close"));
            }
        }else {
            GLog.n(Messages.get(this,"nothing"));
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


}
