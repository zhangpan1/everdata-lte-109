package com.eversec.lte.processor.decoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.raw.XdrRawPayData;
import com.eversec.lte.model.raw.XdrRawPayDataGbiups;
import com.eversec.lte.utils.FormatUtils;

/**
 * 原始码流解码 Gbiups 测试用
 * 
 */
public class RawGbiupsBytesDecoder extends RawBytesDecoder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4688224610539354606L;

	@SuppressWarnings("unchecked")
	@Override
	public List<XdrRawPayData> decode(byte[] load) {
		return decode(load, true);
	}

	public List<XdrRawPayData> decode(byte[] load, boolean decodeHead) {
		List<XdrRawPayData> raws = new ArrayList<>();
		IoBuffer buffer = IoBuffer.wrap(load);
		// System.out.println(Hex.encodeHexString(load));
		if (decodeHead) {
			buffer.skip(SdtpConstants.SDTP_HEADER_LENGTH);
		}
		// while (buffer.hasRemaining()) {

		byte[] cdrid = FormatUtils.getBytes(buffer, 8);// 8

		// if (buffer.hasRemaining()) {
		int len = buffer.getUnsignedShort();// Len 2
		short ver = buffer.getUnsigned();// 1Ver 1
		short linkType = buffer.getUnsigned();// 1LinkType 1
		short cardType = buffer.getUnsigned();// 1CardType 1
		short cardID = buffer.getUnsigned();// 1CardID 1
		long timeOffset = buffer.getUnsignedInt();// 4TimeOffset 4
		long time = buffer.getUnsignedInt();// 4Time 4
		long time2 = buffer.getUnsignedInt();// 4Time2 4
		int sequenceNum = buffer.getUnsignedShort();// Sequence 2
		byte[] load1 = FormatUtils.getBytes(buffer, len - 18);

		// }
		raws.add(new XdrRawPayDataGbiups(cdrid, len, ver, linkType, cardType,
				cardID, timeOffset, time, time2, sequenceNum, load1));
		return raws;
	}

}
