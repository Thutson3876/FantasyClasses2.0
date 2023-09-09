package me.thutson3876.fantasyclasses.status;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;

public class StatusContainer {

	private LivingEntity host;
	private List<Status> statuses = new ArrayList<>();
	
	public StatusContainer(LivingEntity host, Status status) {
		this.host = host;
		statuses.add(status);
	}
	
	public StatusContainer(LivingEntity host, List<Status> statuses) {
		this.host = host;
		this.statuses.addAll(statuses);
	}

	public LivingEntity getHost() {
		return host;
	}

	public List<Status> getStatuses() {
		return statuses;
	}
	
	public void addStatus(Status status) {
		statuses.add(status);
	}
	
	public void addAll(List<Status> statuses) {
		this.statuses.addAll(statuses);
	}
	
	public boolean removeStatus(Status status) {
		return statuses.remove(status);
	}
	
	public boolean removeAll(List<Status> statuses) {
		return this.statuses.removeAll(statuses);
	}
	
	public boolean isEmpty() {
		return statuses.isEmpty();
	}

	
}
