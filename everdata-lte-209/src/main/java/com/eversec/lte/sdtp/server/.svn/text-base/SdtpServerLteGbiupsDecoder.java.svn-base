package com.eversec.lte.sdtp.server;

import java.util.Date;

import org.apache.mina.core.buffer.IoBuffer;

import com.eversec.lte.sdtp.model.SdtpHeader;
import com.eversec.lte.sdtp.model.XDRRawDataQueryGbiupsReq;
import com.eversec.lte.sdtp.model.XDRRawDataQueryReq;
import com.eversec.lte.utils.FormatUtils;

/**
 * 仅自定义raw查询功能
 * @author lirongzhi
 *
 */
public class SdtpServerLteGbiupsDecoder extends SdtpServerLteDecoder {
	@Override
	protected XDRRawDataQueryReq processXDRRawDataQueryReq(SdtpHeader header,
			IoBuffer in) {
		short Interface = in.getUnsigned();
		byte[] xdrID = FormatUtils.getBytes(in, 8);
		Date startTime = FormatUtils.getDate(in);
		Date endTime = FormatUtils.getDate(in);
		XDRRawDataQueryGbiupsReq req = new XDRRawDataQueryGbiupsReq(Interface, xdrID,
				startTime,endTime);
		req.setHeader(header);
		return req;
	}
}
