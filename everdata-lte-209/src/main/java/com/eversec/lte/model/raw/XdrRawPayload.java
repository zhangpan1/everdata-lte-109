package com.eversec.lte.model.raw;

import org.apache.mina.core.buffer.IoBuffer;

public abstract class XdrRawPayload {
	public abstract long getTime() ;
	public abstract long getTime2() ;
	public abstract IoBuffer toBuffer();
	public abstract int getTotalLength();
}
