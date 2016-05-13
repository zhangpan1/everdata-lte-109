package com.eversec.lte.model;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.common.output.IOutput;
import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.single.XdrSingleCommon;
import com.eversec.lte.model.single.XdrSingleSource;
import com.eversec.lte.model.single.XdrSingleSourceS1U;
import com.eversec.lte.model.single.s1u.XdrSingleS1UBusinessCommon;

public class IOutputSingleAdapter extends XdrSingleSource implements IOutput {

	private static final long serialVersionUID = 1L;
	
	private final String strValue;
	private final int memoryBytes;

	private XdrSingleS1UBusinessCommon businessCommon;
	

	public IOutputSingleAdapter(XdrSingleSource source) {
		StringBuilder sb = new StringBuilder();

		String[] arr = source.toStringArr();
		for (String string : arr) {
			sb.append(string);
			sb.append("|");
		}
		strValue = sb.toString();
		memoryBytes = strValue.getBytes().length;
		this.common = source.getCommon();
		
		if(SdtpConstants.XDRInterface.S1U == common.getInterface()){
			XdrSingleSourceS1U s1u = (XdrSingleSourceS1U) source;
			this.businessCommon = s1u.getBusinessCommon();
		}
	}
	
	public XdrSingleS1UBusinessCommon getBusinessCommon() {
		return businessCommon;
	}

	@Override
	public XdrSingleCommon getCommon() {
		return common;
	}

	@Override
	public int getMemoryBytes() {
		return memoryBytes;
	}

	@Override
	public String[] toStringArr() {
		return new String[] { strValue };
	}

	@Override
	public byte[] toByteArray() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public IoBuffer toIobuffer() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public int getBodyLength() {
		throw new java.lang.UnsupportedOperationException();
	}

}
