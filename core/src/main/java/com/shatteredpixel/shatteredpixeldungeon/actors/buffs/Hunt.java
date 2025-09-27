package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.EtherealChains;
import com.shatteredpixel.shatteredpixeldungeon.levels.MiningLevel;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Tag;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class Hunt extends Buff implements ActionIndicator.Action {
    private static final String CAN_USE   = "can_use";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CAN_USE, target.buff(HuntCooldown.class)==null);
    }
    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.getBoolean(CAN_USE)) ActionIndicator.setAction(this);
    }
    @Override
    public String actionName() {
        return Messages.get(this, "action_name");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.HUNT;
    }

    @Override
    public int indicatorColor() {
        return 0x181818;
    }

    @Override
    public void doAction() {
        GameScene.selectCell(caster);
    }
    public CellSelector.Listener caster = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer target) {
            if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){

                //chains cannot be used to go where it is impossible to walk to
                PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
                if (!(Dungeon.level instanceof MiningLevel) && PathFinder.distance[Dungeon.hero.pos] == Integer.MAX_VALUE){
                    GLog.w( Messages.get(EtherealChains.class, "cant_reach") );
                    return;
                }

                final Ballistica chain = new Ballistica(Dungeon.hero.pos, target, Ballistica.PROJECTILE);
                if (!target.equals(chain.collisionPos)){
                    GLog.i("你的锁链够不到目标");
                }
                if (target.equals(chain.collisionPos)&&Actor.findChar( chain.collisionPos ) != null){
                    chainEnemy( chain, Dungeon.hero, Actor.findChar( chain.collisionPos ));
                }

            }

        }

        @Override
        public String prompt() {
            return Messages.get(EtherealChains.class, "prompt");
        }
    };
    private void chainEnemy(Ballistica chain, final Hero hero, final Char enemy ){
        //免疫的敌人无法拉取
        if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
            GLog.w( Messages.get(EtherealChains.class, "cant_pull") );
            return;
        }
        final int extraDMG=Dungeon.gold/500*hero.pointsInTalent(Talent.HE_JIN_ZHUANG_BEI);
        //相邻的敌人直接攻击
        if (Dungeon.level.adjacent(hero.pos,enemy.pos)){
            hero.busy();
            Invisibility.dispel(hero);
            target.sprite.attack(enemy.pos, new Callback() {
                @Override
                public void call() {
                    hero.attack(enemy,1.5f,extraDMG,Hero.INFINITE_ACCURACY);
                    hero.spendAndNext(hero.attackDelay());
                    if (hero.hasTalent(Talent.SHU_FU_DA_JI)){
                        Buff.affect(enemy, Cripple.class,2f*hero.pointsInTalent(Talent.SHU_FU_DA_JI));
                    }
                    int cd=6;
                    if (!enemy.isAlive()){
                        cd-=2*hero.pointsInTalent(Talent.SHOU_LIE_JIE_ZOU);
                    }
                    if (cd>0){
                        Buff.affect(hero,HuntCooldown.class,cd);
                        ActionIndicator.clearAction();
                    }

                }
            });
        }else {
            //不相邻的敌人尝试拉取
            int bestPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                //prefer to the earliest point on the path
                if (!Dungeon.level.solid[i]
                        && Actor.findChar(i) == null
                        && (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])){
                    bestPos = i;
                    break;
                }
            }
            if (bestPos == -1) {
                GLog.i(Messages.get(EtherealChains.class, "does_nothing"));
                return;
            }
            //确定拉取位置
            final int pulledPos = bestPos;
            hero.busy();
            Sample.INSTANCE.play(Assets.Sounds.CHAINS);
            hero.sprite.parent.add(new Chains(hero.sprite.center(),
                    enemy.sprite.center(),
                    Effects.Type.ETHEREAL_CHAIN,
                    new Callback() {
                        public void call() {
                            Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
                                public void call() {
                                    enemy.pos = pulledPos;
                                    Invisibility.dispel(hero);
                                    target.sprite.attack(enemy.pos, new Callback() {
                                        @Override
                                        public void call() {
                                            AttackIndicator.target(enemy);
                                            hero.attack(enemy,1.5f,extraDMG,Hero.INFINITE_ACCURACY);
                                            if (hero.hasTalent(Talent.SHU_FU_DA_JI)){
                                                Buff.affect(enemy, Cripple.class,2f*hero.pointsInTalent(Talent.SHU_FU_DA_JI));
                                            }
                                            hero.spendAndNext(hero.attackDelay());
                                            int cd=6;
                                            if (!enemy.isAlive()){
                                                cd-=2*hero.pointsInTalent(Talent.SHOU_LIE_JIE_ZOU);
                                            }
                                            if (cd>0){
                                                Buff.affect(hero,HuntCooldown.class,cd);
                                                ActionIndicator.clearAction();
                                            }
                                        }
                                    });
                                    Dungeon.level.occupyCell(enemy);
                                    Dungeon.observe();
                                    GameScene.updateFog();
                                }
                            }));
                            hero.next();
                        }
                    }));
        }
    }
    public static class HuntCooldown extends FlavourBuff {
        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void detach() {
            Hunt h=target.buff(Hunt.class);
            if (h!=null){
                ActionIndicator.setAction(h);
            }
            super.detach();
        }

        public void tintIcon(Image icon) { icon.hardlight(0.3f, 0.1f, 0.3f); }
        public float iconFadePercent() { return Math.max(0, visualcooldown() / 6); }
    }
}
