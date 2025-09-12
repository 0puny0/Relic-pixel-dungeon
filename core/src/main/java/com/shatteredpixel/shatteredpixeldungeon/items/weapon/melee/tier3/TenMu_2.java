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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier3;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Spear;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier4.TenMu_3;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class TenMu_2 extends MeleeWeapon {

	{
		image = ItemSpriteSheet.TEN_MU_2;
		hitSound = Assets.Sounds.HIT_STAB;
		hitSoundPitch = 0.9f;

		tier = 3;
		DLY = 1.5f; //0.67x speed
		RCH = 2;    //extra reach
	}
	public int count;
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
		return 25+5*lvl;
	}
	public static final String AC_GROW		="GROW";
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions=super.actions(hero);
		if (isEquipped(hero)&&count>=100){
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
		TenMu_3 growth=new TenMu_3();
		growth.identify();
		growth.cursed=this.cursed;
		growth.level(this.trueLevel());
		growth.enchant(this.enchantment);
		growth. enchantHardened=this.enchantHardened;
		growth. curseInfusionBonus=this.curseInfusionBonus;
		growth. masteryPotionBonus=this.masteryPotionBonus;
		Dungeon.hero.belongings.weapon=growth;
		for (int s = 0; s < QuickSlot.SIZE; s++) {
			if (Dungeon.quickslot.getItem(s) == this) {
				Dungeon.quickslot.setSlot(s,growth);
				break;
			}
		}
		updateQuickslot();
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		// (10+2*lvl),大概+71%伤害，+67%成长。
		int dmgBoost = augment.damageFactor(damageBoost(10 + Math.round(2f*buffedLvl()+2f*hero.weaponMastery)));
		Spear.spikeAbility(hero, target, 1, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		Hero hero=Dungeon.hero;
		int dmgBoost = damageBoost(10 + Math.round(2f*buffedLvl()+2f*hero.weaponMastery));
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(minDamage(hero)+dmgBoost), augment.damageFactor(maxDamage(hero)+dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0)+10, max(0)+10);
		}
	}

	public String upgradeAbilityStat(int level){
		int dmgBoost = 10 + Math.round(2f*level);
		return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
	}

}
