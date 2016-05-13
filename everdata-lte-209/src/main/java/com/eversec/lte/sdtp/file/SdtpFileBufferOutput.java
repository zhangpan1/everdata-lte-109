package com.eversec.lte.sdtp.file;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import com.eversec.common.constant.CommonConstants;
import com.eversec.common.timeutil.DateUtil;

public class SdtpFileBufferOutput extends FileBufferOutput {

	private DecimalFormat fiveFormat = new DecimalFormat("00000");
	private int seq = 0;// 递归用

	private String createPreFilename(Object[] parameters) {
		String Interface = parameters[0].toString();
		int apptype = (int) parameters[1];
		long time = (long) parameters[2];
		String preFilename;
		// /home/eversec/filled/s1u/s1u_19700101_02062.tmp
		if ("s1u".equals(Interface) || "s11".equals(Interface)
				|| "s1mme".equals(Interface)) {
			// s1u_20150823181059_00000
			// .append(DateUtil.DateToString(item.time,
			// DateUtil.COMPACT_DAY_FORMAT));
			preFilename = "0"
					+ File.separator
					+ Interface
					+ CommonConstants.UNDERLINE
					+ DateUtil.DateToString(new Date(time),
							DateUtil.COMPACT_SECOND_FORMAT);
		} else {
			// s1mme_20150821113533_00000
			preFilename = Interface
					+ CommonConstants.UNDERLINE
					+ DateUtil.DateToString(new Date(time),
							DateUtil.COMPACT_SECOND_FORMAT);
		}
		return preFilename;
	}

	@Override
	public File createFilePath(String path, Object[] parameters) {
		StringBuffer tmp = new StringBuffer(path);
		tmp.append(File.separator).append(createPreFilename(parameters))
				.append(CommonConstants.UNDERLINE)
				.append(fiveFormat.format(seq))
				.append(CommonConstants.TMP_SUFFIX);
		File tmpFile = new File(tmp.toString());
		File txtFile = new File(tmpFile.getAbsolutePath().replace(
				CommonConstants.TMP_SUFFIX, CommonConstants.TXT_SUFFIX));
		while (tmpFile.exists() || txtFile.exists()) {
			seq++;
			tmpFile = createFilePath(path, parameters);
			txtFile = new File(tmpFile.getAbsolutePath().replace(
					CommonConstants.TMP_SUFFIX, CommonConstants.TXT_SUFFIX));
		}
		seq = 0;
		return tmpFile;
	}

}
