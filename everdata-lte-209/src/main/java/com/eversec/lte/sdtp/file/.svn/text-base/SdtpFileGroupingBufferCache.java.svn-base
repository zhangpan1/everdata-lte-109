package com.eversec.lte.sdtp.file;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.mina.core.buffer.IoBuffer;

public class SdtpFileGroupingBufferCache extends FileBufferCache {
	protected String Interface;
	protected int apptype;
	protected String apptypeStr;

	public SdtpFileGroupingBufferCache(String path, String maxlimit,
			String expire, ArrayBlockingQueue<FileBuffer> fileBufferQueue,
			String Interface, int apptype, String apptypeStr) {
		super(path, maxlimit, expire, fileBufferQueue);
		this.Interface = Interface;
		this.apptype = apptype;
		this.apptypeStr = apptypeStr;
	}

	public SdtpFileGroupingBufferCache(String path, String maxlimit,
			String expire, ArrayBlockingQueue<FileBuffer> fileBufferQueue,
			int queueSize, String Interface, int apptype, String apptypeStr) {
		super(path, maxlimit, expire, fileBufferQueue, queueSize);
		this.Interface = Interface;
		this.apptype = apptype;
		this.apptypeStr = apptypeStr;
	}

	protected class GroupKey {
		String dateStr;
		String city;
		String imsiLast;

		public GroupKey(String dateStr, String city, String imsiLast) {
			this.dateStr = dateStr;
			this.city = city;
			this.imsiLast = imsiLast;
		}

		@Override
		public String toString() {
			return "GroupKey [dateStr=" + dateStr + ", city=" + city
					+ ", imsiLast=" + imsiLast + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((city == null) ? 0 : city.hashCode());
			result = prime * result
					+ ((dateStr == null) ? 0 : dateStr.hashCode());
			result = prime * result
					+ ((imsiLast == null) ? 0 : imsiLast.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GroupKey other = (GroupKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (city == null) {
				if (other.city != null)
					return false;
			} else if (!city.equals(other.city))
				return false;
			if (dateStr == null) {
				if (other.dateStr != null)
					return false;
			} else if (!dateStr.equals(other.dateStr))
				return false;
			if (imsiLast == null) {
				if (other.imsiLast != null)
					return false;
			} else if (!imsiLast.equals(other.imsiLast))
				return false;
			return true;
		}

		private SdtpFileGroupingBufferCache getOuterType() {
			return SdtpFileGroupingBufferCache.this;
		}

	}

	protected class BufferItem {
		IoBuffer buffer = IoBuffer.allocate((int) BUFFER_SIZE, false)
				.setAutoExpand(true);
		long lasttime = System.currentTimeMillis();
		long totalSize = 0;
	}

	protected HashMap<GroupKey, BufferItem> bufferItemMap = new HashMap<GroupKey, SdtpFileGroupingBufferCache.BufferItem>();

	protected BufferItem findBufferItem(GroupKey key) {

		if (bufferItemMap.containsKey(key)) {
			return bufferItemMap.get(key);
		} else {
			BufferItem item = new BufferItem();
			bufferItemMap.put(key, item);
			return item;
		}
	}

	protected GroupKey createKey(SimpleDateFormat format, long time,
			String city, String imsi) {
		String dateStr = format.format(new Date(time));
		String imsilast = "x";
		if (imsi != null && imsi.length() > 0) {
			imsilast = imsi.charAt(imsi.length() - 1) + "";
		}
		GroupKey key = new GroupKey(dateStr, city, imsilast);
		return key;
	}

	public Object[] getParameters(SdtpFileGroupingOutputItem item,
			String dateStr, String city, String imsiLast) {
		return new Object[] { Interface, apptype, item.time, dateStr, city,
				imsiLast, apptypeStr };
	}

	@Override
	public Object[] getParameters(FileOutputItem item) {
		return null;
	}

	private void putBufferItem(BufferItem bufferItem, String line) {
		byte[] bytes = (line + "\n").getBytes();
		bufferItem.buffer.put(bytes);
		bufferItem.totalSize += bytes.length;
	}

	@Override
	public void run() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
		while (true) {
			try {
				Collection<FileOutputItem> list = new ArrayList<>();
				int count = queue.drainTo(list);
				if (count > 0) {
					for (FileOutputItem item : list) {
						SdtpFileGroupingOutputItem gitem = (SdtpFileGroupingOutputItem) item;
						GroupKey key = createKey(format, gitem.time,
								gitem.city, gitem.imsi);

						BufferItem bufferItem = findBufferItem(key);
						String line = item.line;
						bufferItem.lasttime = System.currentTimeMillis();

						if (line != null) {
							putBufferItem(bufferItem, line);
						}
						if (bufferItem.buffer.position() > BUFFER_SIZE
								|| bufferItem.totalSize >= maxlimit
								|| line == null) {
							if (bufferItem.totalSize > maxlimit || line == null) {
								fileBufferQueue.put(new FileBuffer(path,
										bufferItem.buffer, getParameters(gitem,
												key.dateStr, key.city,
												key.imsiLast), false));
								bufferItem.totalSize = 0;
								synchronized (bufferItemMap) {
									bufferItemMap.remove(key);
								}
							} else {
								fileBufferQueue.put(new FileBuffer(path,
										bufferItem.buffer, getParameters(gitem,
												key.dateStr, key.city,
												key.imsiLast), true));

								bufferItem.buffer = IoBuffer.allocate(
										(int) BUFFER_SIZE).setAutoExpand(true);
							}

						}
					}
				} else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
					}
				}
				list.clear();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reflush() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
		try {
			Set<GroupKey> set = new HashSet<>(bufferItemMap.keySet());
			for (GroupKey key : set) {
				BufferItem bufferItem = null;
				synchronized (bufferItemMap) {
					bufferItem = bufferItemMap.get(key);
				}
				if (bufferItem != null
						&& (System.currentTimeMillis() - bufferItem.lasttime) >= expire
						&& bufferItem.buffer.position() > 0) {
					logger.info("expire to flush :" + key);
					queue.put(new SdtpFileGroupingOutputItem(null, format
							.parse(key.dateStr).getTime(), (short) 0, 0,
							key.city, key.imsiLast));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		long size = 0;
		Set<GroupKey> set = new HashSet<>(bufferItemMap.keySet());
		for (GroupKey key : set) {
			BufferItem bufferItem = bufferItemMap.get(key);
			if (bufferItem != null) {
				size += bufferItem.buffer.position();
			}
		}
		return Interface + "-" + apptype + ":" + queue.size() + ":"
				+ (size / 1024 / 1024) + "MB";
	}

}
