package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.BlobImmunity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM100;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Eye;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Shaman;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Warlock;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogDzewa;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.List;

public class Flesh  extends Food {
    {
        image = ItemSpriteSheet.FLESH;
        energy = Hunger.HUNGRY/3f;
        bones = false;
    }
    public enum Effect{
        barrier,
        barkskin,
        haste,
        arcane,
        cure,
        purification
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

        List<Effect> effectList =new ArrayList<>();
        effectList.add(Effect.barrier);
        //添加护盾
        if (hero.buff(Barrier.class)==null){
            effectList.add(Effect.barrier);
        }
        //添加树肤
        for (Mob m :hero.getVisibleEnemies()){
            if (m.distance(hero)<3){
                Barkskin barkskin=hero.buff(Barkskin.class);
                if (barkskin == null || barkskin.level() < hero.HT / 8) effectList.add(Effect.barkskin);
            }
        }
        //添加急速
        for (Mob m :hero.getVisibleEnemies()){
            if (m.distance(hero)>2&&hero.buff(Haste.class)==null){
                effectList.add(Effect.haste);
            }
        }
        //添加秘法护盾
        for (Mob m :hero.getVisibleEnemies()){
            if (m instanceof DM100 ||m instanceof Shaman||m instanceof Elemental
                    ||m instanceof Warlock||m instanceof Eye||m instanceof YogDzewa){
                effectList.add(Effect.arcane);
            }
        }
        //添加净化屏障
        for (Blob blob:Dungeon.level.blobs.values()){
            for (int blobCell : blob.getActiveCells()) {
                if (hero.fieldOfView[blobCell]){
                    effectList.add(Effect.purification);
                    break;
                }
            }
        }
        //添加负面清除
        for (Buff buff:hero.buffs()){
            if (buff instanceof Poison||buff instanceof Weakness||buff instanceof Vulnerable||buff instanceof Bleeding
                    ||buff instanceof Drowsy||buff instanceof Slow||buff instanceof Vertigo){
                effectList.add(Effect.cure);
            }
        }

        Effect effect=effectList.get(Random.Int(effectList.size()));

        GLog.i(effect.toString());
        switch (effect){
            case barrier: default:
                Buff.affect(hero,Barrier.class).setShield(hero.HT/10);
                break;
            case barkskin:
                Barkskin.conditionallyAppend( hero, hero.HT / 4, 1 );
                break;
            case haste:
                Buff.affect(hero,Haste.class,4f);
                break;
            case arcane:
                Buff.affect(hero, ArcaneArmor.class).set(5 + hero.lvl/2,10);
                break;
            case cure:
                PotionOfHealing.cure(hero);
                break;
            case purification:
                Buff.prolong( hero, BlobImmunity.class, BlobImmunity.DURATION );
                break;
        }
    }
}
