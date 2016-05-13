package com.eversec.lte.vo.compound;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.eversec.lte.model.single.XdrSingleSource;

public class CompMessage {
	private List<XdrSingleSource> xdrs = new ArrayList<>();
	private TreeMap<Long, MrInfo> mrInfos = new TreeMap<>();

	public static class MrInfo {
		public double latitude;
		public double longitude;

		public MrInfo(double latitude, double longitude) {
			super();
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		public double getLongitude() {
			return longitude;
		}
		
		public double getLatitude() {
			return latitude;
		}
	}

	public List<XdrSingleSource> getXdrs() {
		return xdrs;
	}

	public void setXdrs(List<XdrSingleSource> xdrs) {
		this.xdrs = xdrs;
	}

	public TreeMap<Long, MrInfo> getMrInfos() {
		return mrInfos;
	}

	public void setMrInfos(TreeMap<Long, MrInfo> mrInfos) {
		this.mrInfos = mrInfos;
	}
}
