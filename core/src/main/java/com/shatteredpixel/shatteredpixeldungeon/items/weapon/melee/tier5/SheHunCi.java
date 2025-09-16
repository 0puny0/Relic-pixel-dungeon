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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class SheHunCi extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SHE_HUN_CI;
		hitSound = Assets.Sounds.HIT_STAB;
		hitSoundPitch = 0.9f;

		tier = 5;
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
		return  20+5*lvl+count/3;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (attacker instanceof Hero&&defender instanceof Mob&&defender.surprisedBy(attacker)){
			//TODO 伏击回合时造成生物死亡就会触发
			Buff.affect(attacker, SheHunTracker.class,0.0f);
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	public int damageRoll(Char owner) {
		if (owner instanceof Hero) {
			Hero hero = (Hero)owner;
			Char enemy = hero.enemy();
			if (enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
				int diff = maxDamage(owner) - minDamage(owner);
				int damage = augment.damageFactor(Hero.heroDamageIntRange(
						minDamage(owner) + Math.round(diff*0.34f),
						maxDamage(owner)));
				return damage;
			}
		}
		return super.damageRoll(owner);
	}

	public String statsInfo(){
		if (isIdentified()){
			return Messages.get(this, "stats_desc",  count);
		} else {
			return Messages.get(this, "typical_stats_desc");
		}
	}
	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	public boolean useTargeting(){
		return false;
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Dagger.sneakAbility(hero, target, 3, 2+buffedLvl()+hero.weaponMastery, this);
	}

	@Override
	public String abilityInfo() {
		if (levelKnown){
			return Messages.get(this, "ability_desc", 2+buffedLvl()+ Dungeon.hero.weaponMastery);
		} else {
			return Messages.get(this, "typical_ability_desc", 2);
		}
	}

	@Override
	public String upgradeAbilityStat(int level) {
		return Integer.toString(2+level);
	}
	public static class SheHunTracker extends FlavourBuff {}
}