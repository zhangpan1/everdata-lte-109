package com.eversec.lte.sdtp.file;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import com.eversec.common.constant.CommonConstants;
import com.eversec.common.timeutil.DateUtil;
import com.eversec.lte.config.SdtpConfig;

public class ExamineFileBufferOutput extends FileBufferOutput {

	private DecimalFormat fiveFormat = new DecimalFormat("00000");
	private int seq = 0;// 递归用

	private String city = SdtpConfig.getExamineCity();

	private String createPreFilename(Object[] parameters) {
		String Interface = parameters[0].toString();
		int apptype = (int) parameters[1];
		long time = (long) parameters[2];
		String preFilename;
		// /home/eversec/filled/s1u/s1u_19700101_02062.tmp
		if ("s1u".equals(Interface)) {
			// s1u_20150823181059_00000
			// .append(DateUtil.DateToString(item.time,
			// DateUtil.COMPACT_DAY_FORMAT));
			preFilename = Interface
					+ CommonConstants.UNDERLINE
					+ DateUtil.DateToString(new Date(time),
							DateUtil.COMPACT_DAY_FORMAT);
		} else {
			// s1mme_20150821113533_00000
			preFilename = Interface
					+ CommonConstants.UNDERLINE
					+ DateUtil.DateToString(new Date(time),
							DateUtil.COMPACT_DAY_FORMAT);
		}
		return preFilename;
	}

	private String getInterface(Object[] parameters) {
		String Interface = parameters[0].toString();
		int apptype = (int) parameters[1];
		long time = (long) parameters[2];
		String preFilename;
		if ("s1u".equals(Interface)) {
			preFilename = apptype + "";
		} else if ("s1mme".equals(Interface)) {
			preFilename = "MME";
		} else {
			preFilename = Interface;
		}
		return preFilename;
	}

	/**
	 * 1、原始信令采集
	 * 每200MB保存为一个文件；单文件命名原则：城市（城市全拼）+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.pcap
	 * （例如:chengdu2013111300001
	 * .pcap）。原始数据必须存储为PCAP格式，S1-U和S11/S1-MME接口原始数据应汇入同一链路存储。<br>
	 * 2、S1-U XDR采集文件命名规范 每200M保存为一个文件；单文件命名：城市（城市全拼）+
	 * XDR类型编码+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.txt（例如
	 * :chengdu1002013111300001.txt）； <br>
	 * 3、S1-MME XDR采集文件命名规范 每200M保存为一个文件；单文件命名：城市（城市全拼）+
	 * MME+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.txt（例如:chengduMME2013111300001.txt）；
	 */
	@Override
	public File createFilePath(String path, Object[] parameters) {
		String Interface = parameters[0].toString();
		int apptype = (int) parameters[1];
		long time = (long) parameters[2];

		StringBuffer tmp = new StringBuffer(path);
		tmp.append(File.separator)
				.append(city)
				.append(File.separator)//加一层目录
				.append(DateUtil.DateToString(new Date(time),
						DateUtil.COMPACT_DAY_FORMAT))
				.append(File.separator)
				.append("xdr")
				.append(File.separator)
				.append(city)
				.append(getInterface(parameters))
				.append(DateUtil.DateToString(new Date(time),
						DateUtil.COMPACT_DAY_FORMAT));
		tmp.append(fiveFormat.format(seq));
		tmp.append(CommonConstants.TMP_SUFFIX);

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
