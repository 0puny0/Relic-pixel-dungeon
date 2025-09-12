/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier2;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier3.TenMu_2;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TenMu_1 extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.TEN_MU_1;
		hitSound = Assets.Sounds.HIT_CRUSH;
		hitSoundPitch = 1f;
		tier = 2;
	}
	public int count=0;
	private static final String COUNT="count";
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(COUNT, count);
	}
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		count = bundle.getInt( COUNT );

	}
	@Override
	public int max(int lvl) {
		return  10 + lvl*2;
	}
	public static final String AC_GROW		="GROW";
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions=super.actions(hero);
		if (isEquipped(hero)&&count>=50){
			actions.add(AC_GROW);
		}else {
			actions.remove(AC_GROW);
		}
		return actions;
	}
	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_GROW)){
			grow();
		}
	}
	private void grow(){
		if (this!= Dungeon.hero.belongings.weapon()){
			return;
		}
		TenMu_2 growth=new TenMu_2();
		growth.identify();
		growth.level(this.trueLevel());
		growth.enchant(this.enchantment);
		growth.cursed=this.cursed;
		growth. enchantHardened=this.enchantHardened;
		growth. curseInfusionBonus=this.curseInfusionBonus;
		growth. masteryPotionBonus=this.masteryPotionBonus;
		growth.count=this.count-50;
		Dungeon.hero.belongings.weapon=growth;
		for (int s = 0; s < QuickSlot.SIZE; s++) {
			if (Dungeon.quickslot.getItem(s) == this) {
				Dungeon.quickslot.setSlot(s,growth);
				break;
			}
		}
		GLog.p();
		updateQuickslot();
	}


	@Override
	public int defenseFactor( Char owner ) {
		return 2;	//2 extra defence
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		beforeAbilityUsed(hero, null);
		//1 turn less as using the ability is instant
		Buff.prolong(hero, Quarterstaff.DefensiveStance.class, 3 + buffedLvl()+hero.weaponMastery);
		hero.sprite.operate(hero.pos);
		hero.next();
		afterAbilityUsed(hero);
	}

	@Override
	public String abilityInfo() {
		if (levelKnown){
			return Messages.get(this, "ability_desc", 4+buffedLvl()+Dungeon.hero.weaponMastery);
		} else {
			return Messages.get(this, "typical_ability_desc", 4);
		}
	}

	@Override
	public String upgradeAbilityStat(int level) {
		return Integer.toString(4+level);
	}

}
