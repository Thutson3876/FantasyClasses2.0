package me.thutson3876.fantasyclasses.status;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import me.thutson3876.fantasyclasses.FantasyClasses;

public class StatusManager {

	private List<StatusContainer> statusList = new ArrayList<>();

	private int taskId;

	public StatusManager() {
		FantasyClasses plugin = FantasyClasses.getPlugin();
		this.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new UplinkReducingTask(), 1L, 1L);
	}

	public void deInit() {
		List<StatusContainer> toRemove = new ArrayList<>();
		toRemove.addAll(statusList);

		for (StatusContainer container : toRemove) {

			for (Status s : container.getStatuses()) {
				s.getType().remove(container.getHost(), RemoveCause.DISCONNECT);
			}
		}

		Bukkit.getScheduler().cancelTask(this.taskId);
	}

	protected void tick() {
		if (this.statusList.isEmpty())
			return;
		synchronized (this.statusList) {
			List<Status> toRemove = new ArrayList<>();
			for (StatusContainer container : statusList) {
				if (container == null || container.getStatuses() == null)
					continue;

				for (Status s : container.getStatuses()) {
					s.tick();

					if (s.getRemainingDuration() <= 0)
						toRemove.add(s);
				}
			}

			for (Status s : toRemove) {
				s.getType().remove(s.getHost(), RemoveCause.EXPIRED);
			}

		}
	}

	public Status get(LivingEntity host, Class<? extends StatusType> statusTypeClass) {
		if (!contains(host))
			return null;

		for (Status s : getAll(host)) {
			if (s.getType().getClass().equals(statusTypeClass))
				return s;
		}

		return null;
	}

	public Status get(LivingEntity host, StatusType statusType) {
		if (!contains(host))
			return null;

		for (Status s : getAll(host)) {
			if (s.getType().equals(statusType))
				return s;
		}

		return null;
	}

	public List<Status> getAll(LivingEntity host) {
		for (StatusContainer container : statusList)
			if (container.getHost().equals(host))
				return container.getStatuses();

		return null;
	}

	public boolean has(LivingEntity host, Status status) {
		return getAll(host).contains(status);
	}

	// make sure this works
	public void add(Status status) {
		LivingEntity host = status.getHost();
		StatusContainer container = getContainer(host);
		if (container == null) {
			container = new StatusContainer(host, status);
			statusList.add(container);
		} else {
			getContainer(host).addStatus(status);
		}
	}

	public boolean remove(LivingEntity host, Status status) {
		return getContainer(host).removeStatus(status);
	}

	public boolean remove(LivingEntity host, StatusType statusType) {
		StatusContainer container = getContainer(host);

		if (container == null)
			return false;

		Status toRemove = null;
		for (Status s : getContainer(host).getStatuses()) {
			if (s.getType().equals(statusType)) {
				toRemove = s;
				break;
			}
		}

		if (toRemove != null) {
			container.removeStatus(toRemove);
			return true;
		}

		return false;
	}

	public boolean removeAll(LivingEntity host) {
		return statusList.remove(getContainer(host));
	}

	public boolean contains(LivingEntity host) {
		for (StatusContainer container : statusList)
			if (container.getHost().equals(host))
				return true;

		return false;
	}

	public boolean contains(LivingEntity host, Status status) {
		if (!contains(host))
			return false;

		return getAll(host).contains(status);
	}

	public boolean contains(LivingEntity host, StatusType statusType) {
		for (StatusContainer container : statusList)
			if (container.getHost().equals(host)) {
				for (Status s : container.getStatuses()) {
					if (s.getType().equals(statusType)) {
						return true;
					}
				}
			}

		return false;
	}

	protected StatusContainer getContainer(LivingEntity host) {
		for (StatusContainer container : statusList)
			if (container.getHost().equals(host))
				return container;

		return null;
	}

	protected class UplinkReducingTask implements Runnable {
		public void run() {
			StatusManager.this.tick();
		}
	}

}
