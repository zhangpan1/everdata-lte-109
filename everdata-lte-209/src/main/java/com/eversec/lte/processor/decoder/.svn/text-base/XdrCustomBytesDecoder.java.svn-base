package com.eversec.lte.processor.decoder;

import static com.eversec.lte.constant.SdtpConstants.XDRInterface.SIMPLE_UEMR;
import static com.eversec.lte.utils.FormatUtils.TBCDFormat;
import static com.eversec.lte.utils.FormatUtils.getBytes;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;

import cern.colt.Arrays;

import com.eversec.lte.model.compound.XdrUEMRSimple;
import com.eversec.lte.vo.compound.CompMessage;

/**
 * 
 * @author bieremayi
 * 
 */
public class XdrCustomBytesDecoder implements IDecoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1014330299289342766L;
	XdrSingleBytesDecoder xdrSingleBytesDecoder = new XdrSingleBytesDecoder();

	@SuppressWarnings("unchecked")
	@Override
	public CompMessage decode(byte[] load) {
		CompMessage compMessage = new CompMessage();
		IoBuffer buffer = IoBuffer.wrap(load);
		short type = buffer.getUnsigned();
		if (type == SIMPLE_UEMR) {
			XdrUEMRSimple uemrSimple = processUEMRSimple(buffer);
			compMessage.getMrInfos().put(
					uemrSimple.getTime(),
					new CompMessage.MrInfo(uemrSimple.getLatitude(), uemrSimple
							.getLongitude()));
		} else {
			compMessage.getXdrs().addAll(xdrSingleBytesDecoder.decode(buffer));
		}

		return compMessage;
	}

	/**
	 * 
	 * @param buffer
	 * @return
	 */
	protected XdrUEMRSimple processUEMRSimple(IoBuffer buffer) {
		String imsi = "";
		try {
			imsi = TBCDFormat(getBytes(buffer, 8));
		} catch (Exception e) {
		}
		double longitude = buffer.getDouble();
		double latitude = buffer.getDouble();
		long time = buffer.getLong();
		return new XdrUEMRSimple(imsi, longitude, latitude, time);
	}

	public static void main(String[] args) throws DecoderException {
		XdrCustomBytesDecoder decoder = new XdrCustomBytesDecoder();
		String xdr = "643436303030353137313536393830300000000000000000000000000000000000000148a908df3a00000000000000000000000000000000000000000000000000";
		System.out.println(Arrays.toString(Hex.decodeHex(xdr.toCharArray())));
		decoder.decode(Hex.decodeHex(xdr.toCharArray()));
	}
}
