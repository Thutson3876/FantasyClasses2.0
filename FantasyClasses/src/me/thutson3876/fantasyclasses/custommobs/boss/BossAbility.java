package me.thutson3876.fantasyclasses.custommobs.boss;

public abstract class BossAbility implements MobAbility {

	protected final AbstractBoss boss;
	
	protected BossAbility(AbstractBoss boss) {
		this.boss = boss;
	}
	
	public AbstractBoss getBoss() {
		return boss;
	}
	
}
