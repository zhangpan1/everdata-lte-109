package com.eversec.lte.model.single;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.XdrData;

@SuppressWarnings("serial")
public abstract class XdrSingleSource extends XdrData {

	protected AtomicLong tryTimeLeft = new AtomicLong(SdtpConfig.MAX_TRY_NUM);
	protected XdrSingleCommon common;//公共部分
	protected long produceStartTime;// 流程开始时间
	protected long produceEndTime;// 流程开始时间
	protected boolean isComped;// 话单是否被合成

	public static Comparator<XdrSingleSource> XDR_SINGLE_SOURCE_COMPARATOR = new Comparator<XdrSingleSource>() {
		@Override
		public int compare(XdrSingleSource o1, XdrSingleSource o2) {
			return (int) (o1.getProduceStartTime() - o2.getProduceStartTime());
		}
	};

	public XdrSingleCommon getCommon() {
		return common;
	}

	public void setCommon(XdrSingleCommon common) {
		this.common = common;
	}

	public long getProduceStartTime() {
		return produceStartTime;
	}

	public void setProduceStartTime(long produceStartTime) {
		this.produceStartTime = produceStartTime;
	}

	public long getProduceEndTime() {
		return produceEndTime;
	}

	public void setProduceEndTime(long produceEndTime) {
		this.produceEndTime = produceEndTime;
	}

	public boolean isComped() {
		return isComped;
	}

	public void setComped(boolean isComped) {
		this.isComped = isComped;
	}

	@Override
	public boolean isXdrSingle() {
		return true;
	}

	@Override
	public String[] toStringArr() {
		return new String[] { common.toString(), toString() };
	}

	@Override
	public int getMemoryBytes() {
		return toString().getBytes().length + common.getMemoryBytes() + 1;
	}

	public AtomicLong getTryTimeLeft() {
		return tryTimeLeft;
	}

}
