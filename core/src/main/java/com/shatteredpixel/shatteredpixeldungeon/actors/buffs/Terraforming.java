package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndJournal;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class Terraforming extends Buff implements ActionIndicator.Action {
    public  int[] doorOpps=new int[27];

    private static final String DOOROPPS="dooropps";
    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DOOROPPS, doorOpps);
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        doorOpps = bundle.getIntArray( DOOROPPS );

    }
    private CellSelector.Listener doorListener = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell == null) return;
            if (!Dungeon.level.adjacent(Dungeon.hero.pos, cell)){
                return;
            }
            if(Dungeon.level.map[cell]!=Terrain.DOOR&&Dungeon.level.map[cell]!=Terrain.OPEN_DOOR){
                GLog.i(Messages.get(Terraforming.class, "door_prompt"));
                return;
            }
            final Char enemy = Actor.findChar( cell );
            Hero hero=Dungeon.hero;
            if(Dungeon.level.map[cell]== Terrain.OPEN_DOOR&&enemy!=null){
                Ballistica trajectory = new Ballistica(hero.pos, enemy.pos, Ballistica.STOP_TARGET);
                trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
                WandOfBlastWave.throwChar(enemy, trajectory, 1, true, true, getClass());
            }
            IronKey key=new IronKey(Dungeon.depth);
            GameScene.pickUpJournal(key, cell);
            WndJournal.last_index = 2;
            Notes.add(key);
            Sample.INSTANCE.play( Assets.Sounds.ITEM );
            Level.set(cell, Terrain.LOCKED_DOOR);
            GameScene.updateKeyDisplay();
            GameScene.updateMap(cell);
            doorOpps[Dungeon.depth]--;
            if (doorOpps[Dungeon.depth]<=0){
                ActionIndicator.clearAction();
            }
            hero.sprite.attack(cell, new Callback() {
                @Override
                public void call() {
                    Dungeon.hero.onOperateComplete();
                }
            }) ;
            hero.spendAndNext(TICK);
            isSneak();
        }

        @Override
        public String prompt() {
            return Messages.get(Terraforming.class, "door_prompt");
        }
    };
    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }
    @Override
    public int actionIcon() {
        return HeroIcon.TERRAFORMING;
    }
    @Override
    public int indicatorColor() {
        return 0xa9a9a9;
    }
    @Override
    public Visual secondaryVisual() {
        BitmapText txt = new BitmapText(PixelScene.pixelFont);
        txt.text( Integer.toString(doorOpps[Dungeon.depth]) );
        txt.hardlight(CharSprite.DEFAULT);
        txt.measure();
        return txt;
    }
    @Override
    public void doAction() {
        if(doorOpps[Dungeon.depth]>0){
            GameScene.selectCell(doorListener);
        }else {
            GLog.n(Messages.get(this,"no_odds"));
        }
    }
    public static boolean isClosed(){
        if(Dungeon.depth%5==0){
            return false;
        }
        PathFinder.buildDistanceMap(Dungeon.level.entrance(), BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (PathFinder.distance[Dungeon.hero.pos] != Integer.MAX_VALUE){
            return false;
        }
        PathFinder.buildDistanceMap(Dungeon.level.exit(), BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (PathFinder.distance[Dungeon.hero.pos] != Integer.MAX_VALUE){
            return false;
        }
        return true;
    }
    public static void isSneak(){
        if (Dungeon.hero.buff(Terraforming.class)==null){
            return;
        }
        if(isClosed()||Dungeon.level.water[Dungeon.hero.pos]||Dungeon.level.map[Dungeon.hero.pos] == Terrain.FURROWED_GRASS){
            for (Mob m : Dungeon.level.mobs){
                if (Dungeon.level.adjacent(m.pos, Dungeon.hero.pos) && m.alignment != Dungeon.hero.alignment){
                    return ;
                }
            }
            if(Dungeon.hero.buff(Sneak.class)==null){
                Buff.affect(Dungeon.hero,Sneak.class);
            }
        }else if
        (Dungeon.hero.buff(Sneak.class)!=null){
            Dungeon.hero.buff(Sneak.class).detach();
        }
    }
    public static class Sneak extends Invisibility {
        {
            announced = false;
        }

        @Override
        public int icon() {
            return BuffIndicator.IMBUE;
        }

        @Override
        public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 1f); }
        @Override
        public float iconFadePercent() {
            return 0;
        }
        @Override
        public boolean act() {
            if (target.isAlive()) {
                spend( TICK );
                for (Mob m : Dungeon.level.mobs){
                    if (Dungeon.level.adjacent(m.pos, target.pos) && m.alignment != target.alignment){
                        detach();
                        return true;
                    }
                }
            } else {
                detach();
            }
            return true;
        }

    }

}
