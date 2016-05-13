package com.eversec.lte.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.common.output.IOutput;
import com.eversec.lte.config.SdtpConfig;

@SuppressWarnings("serial")
public abstract class XdrData implements IOutput {
	

	protected static String _DELIMITER_ = SdtpConfig.getListDelimiter();

	protected int xdrType = 2; // 默认是2 single
	protected int fillType;

	public abstract boolean isXdrSingle();

	public abstract byte[] toByteArray();

	public abstract IoBuffer toIobuffer();

	public abstract int getBodyLength();

	public int getXdrType() {
		return xdrType;
	}

	public void setXdrType(int xdrType) {
		this.xdrType = xdrType;
	}

	public int getFillType() {
		return fillType;
	}

	public void setFillType(int fillType) {
		this.fillType = fillType;
	}

}
