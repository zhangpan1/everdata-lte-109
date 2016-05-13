package com.eversec.lte.sdtp.infinispan;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listener
public class ClusterListener {

	public volatile List<Address> members = null;
	private volatile Address localMembers;
	private volatile List<Address> oldMembers;
	private volatile List<Address> newMembers;

	public ClusterListener() {
	}

	@ViewChanged
	public void viewChanged(ViewChangedEvent event) {
		this.members = event.getCacheManager().getMembers();
		this.localMembers = event.getLocalAddress();
		this.oldMembers = event.getOldMembers();
		this.newMembers = event.getNewMembers();

		System.out.println("ClusterListener viewChangedview  now cache member:" + members
				+ ",local member:" + localMembers + ",new member:" + oldMembers
				+ ",old member:" + newMembers);
	}

	public List<Address> getMembers() {
		return members;
	}

	public Address getLocalMembers() {
		return localMembers;
	}

	public List<Address> getOldMembers() {
		return oldMembers;
	}

	public List<Address> getNewMembers() {
		return newMembers;
	}

}
