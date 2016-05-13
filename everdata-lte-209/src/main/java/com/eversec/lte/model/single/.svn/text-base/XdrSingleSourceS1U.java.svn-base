package com.eversec.lte.model.single;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.model.single.s1u.XdrSingleS1UApp;
import com.eversec.lte.model.single.s1u.XdrSingleS1UBusinessCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1UMobileCommon;
import com.eversec.lte.model.single.s1u.XdrSingleS1USignallingGn;

@SuppressWarnings("serial")
public class XdrSingleSourceS1U extends XdrSingleSource {
	XdrSingleS1UCommon s1uCommon;
	XdrSingleS1UMobileCommon mobileCommon;
	XdrSingleS1UBusinessCommon businessCommon;
	XdrSingleS1UApp app;
	XdrSingleS1USignallingGn signallingGn;

	public XdrSingleSourceS1U(XdrSingleS1UCommon s1uCommon,
			XdrSingleS1UMobileCommon mobileCommon,
			XdrSingleS1UBusinessCommon businessCommon, XdrSingleS1UApp app) {
		super();
		this.s1uCommon = s1uCommon;
		this.mobileCommon = mobileCommon;
		this.businessCommon = businessCommon;
		this.app = app;
	}

//	public XdrSingleSourceS1U(XdrSingleS1UCommon s1uCommon,
//			XdrSingleS1UBusinessCommon businessCommon, XdrSingleS1UApp app) {
//		super();
//		this.s1uCommon = s1uCommon;
//		this.businessCommon = businessCommon;
//		this.app = app;
//	}
//
//	public XdrSingleSourceS1U(XdrSingleS1UCommon s1uCommon,
//			XdrSingleS1USignallingGn signallingGn) {
//		super();
//		this.s1uCommon = s1uCommon;
//		this.signallingGn = signallingGn;
//	}

	public XdrSingleS1UCommon getS1uCommon() {
		return s1uCommon;
	}

	public void setS1uCommon(XdrSingleS1UCommon s1uCommon) {
		this.s1uCommon = s1uCommon;
	}

	public XdrSingleS1UMobileCommon getMobileCommon() {
		return mobileCommon;
	}

	public void setMobileCommon(XdrSingleS1UMobileCommon mobileCommon) {
		this.mobileCommon = mobileCommon;
	}

	public XdrSingleS1UBusinessCommon getBusinessCommon() {
		return businessCommon;
	}

	public void setBusinessCommon(XdrSingleS1UBusinessCommon businessCommon) {
		this.businessCommon = businessCommon;
	}

	public XdrSingleS1UApp getApp() {
		return app;
	}

	public void setApp(XdrSingleS1UApp app) {
		this.app = app;
	}

	public XdrSingleS1USignallingGn getSignallingGn() {
		return signallingGn;
	}

	public void setSignallingGn(XdrSingleS1USignallingGn signallingGn) {
		this.signallingGn = signallingGn;
	}

	@Override
	public String[] toStringArr() {
		if(app == null){
			return new String[] { s1uCommon.toString(), mobileCommon.toString(),
					businessCommon.toString()  };
		}else{
			return new String[] { s1uCommon.toString(), mobileCommon.toString(),
					businessCommon.toString(),app.toString()  };
		}
	}

	@Override
	public byte[] toByteArray() {
		return toIobuffer().array();
	}

	@Override
	public String toString() {
		String appStr = ((app == null) ? "" :  "|" + app.toString());
		return s1uCommon + "|" + mobileCommon + "|" + businessCommon 
				+ appStr;
	}

	@Override
	public IoBuffer toIobuffer() {
		IoBuffer buffer = IoBuffer.allocate(getBodyLength());
		toS1UCommon(buffer);
		toS1UmobileCommon(buffer);
		toS1UBusinessCommon(buffer);
		toS1UApp(buffer);
		buffer.flip();
		return buffer;
	}

	private void toS1UCommon(IoBuffer buffer) {
		buffer.put(s1uCommon.toByteArray());
	}

	private void toS1UmobileCommon(IoBuffer buffer) {
		buffer.put(mobileCommon.toByteArray());
	}

	private void toS1UBusinessCommon(IoBuffer buffer) {
		buffer.put(businessCommon.toByteArray());
	}

	private void toS1UApp(IoBuffer buffer) {
		if (app != null) {
			buffer.put(app.toByteArray());
		}
	}

//	@Override
//	@Deprecated
//	public XdrSingleCommon getCommon() {
//		return super.getCommon();
//	}
//
//	@Override
//	@Deprecated
//	public void setCommon(XdrSingleCommon common) {
//		super.setCommon(common);
//	}

	@Override
	public int getBodyLength() {
		return s1uCommon.getBodyLength() + mobileCommon.getBodyLength()
				+ businessCommon.getBodyLength()
				+ ((app == null) ? 0 : app.getBodyLength());
	}

	@Override
	public int getMemoryBytes() {
		return toString().getBytes().length;
	}

}
