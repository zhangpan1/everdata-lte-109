package com.eversec.lte.processor.decoder;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.constant.SdtpConstants;
import com.eversec.lte.model.raw.XdrRawPayData;
import com.eversec.lte.model.raw.XdrRawPayload;
import com.eversec.lte.model.raw.XdrRawScaPayload;
import com.eversec.lte.model.raw.XdrRawSigPayload;
import com.eversec.lte.utils.FormatUtils;

/**
 * 原始码流解码
 * 
 * @author bieremayi
 * 
 */
public class RawBytesDecoder implements IDecoder {

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
		while (buffer.hasRemaining()) {
			short rat = buffer.getUnsigned();
			short Interface = buffer.getUnsigned();
			byte[] xdrID = FormatUtils.getBytes(buffer, 16);
			int num = buffer.getUnsignedShort();
			// long lengthTotal = buffer.getUnsignedInt();207
			long lengthTotal = buffer.getUnsignedShort();// 209

			List<XdrRawPayload> payloads = new ArrayList<>();

			if (Interface == 0x30 || Interface == 0x31) {// 软采接口
				decodeSca(buffer, num, payloads);// 采用文件字节序
			} else {
				decodeSig(buffer, num, payloads);// 其他硬采接口
			}

			XdrRawPayData raw = new XdrRawPayData(rat, Interface, xdrID, num,
					lengthTotal, payloads);
			raws.add(raw);
		}
		return raws;
	}

	// 硬采接口
	public void decodeSig(IoBuffer buffer, int num, List<XdrRawPayload> payloads) {
		for (int i = 0; i < num; i++) {
			int len = buffer.getUnsignedShort();
			short ver = buffer.getUnsigned();// 1
			short linkType = buffer.getUnsigned();// 1
			short cardType = buffer.getUnsigned();// 1
			short cardID = buffer.getUnsigned();// 1
			long time = buffer.getUnsignedInt();// 4
			long time2 = buffer.getUnsignedInt();// 4
			byte[] load1 = FormatUtils.getBytes(buffer, len - 12);
			XdrRawSigPayload payload = new XdrRawSigPayload(len, ver, linkType,
					cardType, cardID, time, time2, load1);
			payloads.add(payload);
		}

	}

	// 软采接口
	// 注意软采的payload采用的是正常的文件字节序
	// 采用文件字节序解析
	public void decodeSca(IoBuffer buffer, int num, List<XdrRawPayload> payloads) {
		buffer.order(ByteOrder.BIG_ENDIAN);
		for (int i = 0; i < num; i++) {
			int len = buffer.getUnsignedShort();// 2
			// head1部分
			short ver = buffer.getUnsigned();// 1
			short cardType = buffer.getUnsigned();// 1
			long time = buffer.getUnsignedInt();// 4
			long time2 = buffer.getUnsignedInt();// 4
			short netElement = buffer.getUnsigned();// 1
			short Interface = buffer.getUnsigned();// 1
			short direction = buffer.getUnsigned();// 1
			// head2部分 +load部分:
			byte[] load1 = FormatUtils.getBytes(buffer, len - 13);
			XdrRawScaPayload payload = new XdrRawScaPayload(len, ver, cardType,
					time, time2, netElement, Interface, direction, load1);
			payloads.add(payload);
		}
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}

	// 硬采接口
	public void decodeSig(IoBuffer buffer, List<XdrRawPayload> payloads) {
		while (buffer.hasRemaining()) {
			int len = buffer.getUnsignedShort();
			short ver = buffer.getUnsigned();// 1
			short linkType = buffer.getUnsigned();// 1
			short cardType = buffer.getUnsigned();// 1
			short cardID = buffer.getUnsigned();// 1
			long time = buffer.getUnsignedInt();// 4
			long time2 = buffer.getUnsignedInt();// 4
			byte[] load1 = FormatUtils.getBytes(buffer, len - 12);
			XdrRawSigPayload payload = new XdrRawSigPayload(len, ver, linkType,
					cardType, cardID, time, time2, load1);
			payloads.add(payload);
		}

	}

	// 软采接口
	// 注意软采的payload采用的是正常的文件字节序
	// 采用文件字节序解析
	public void decodeSca(IoBuffer buffer, List<XdrRawPayload> payloads) {
		buffer.order(ByteOrder.BIG_ENDIAN);
		while (buffer.hasRemaining()) {
			int len = buffer.getUnsignedShort();// 2
			// head1部分
			short ver = buffer.getUnsigned();// 1
			short cardType = buffer.getUnsigned();// 1
			long time = buffer.getUnsignedInt();// 4
			long time2 = buffer.getUnsignedInt();// 4
			short netElement = buffer.getUnsigned();// 1
			short Interface = buffer.getUnsigned();// 1
			short direction = buffer.getUnsigned();// 1
			// head2部分 +load部分:
			byte[] load1 = FormatUtils.getBytes(buffer, len - 13);
			XdrRawScaPayload payload = new XdrRawScaPayload(len, ver, cardType,
					time, time2, netElement, Interface, direction, load1);
			payloads.add(payload);
		}
		buffer.order(ByteOrder.LITTLE_ENDIAN);
	}
}
