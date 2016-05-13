package com.eversec.lte.output;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import com.eversec.common.constant.CommonConstants;
import com.eversec.common.output.FileOutput;
import com.eversec.common.timeutil.DateUtil;
import com.eversec.lte.config.SdtpConfig;
import com.eversec.lte.model.XdrData;

@SuppressWarnings("serial")
public class LteFileOutput extends FileOutput<XdrData> {

	private DecimalFormat fiveFormat = new DecimalFormat("00000");
	private String exportDir;
	private String Inteface;
	private int seq = 0;

	public LteFileOutput(String maxMemorySize, String expireTime,
			String exportDir, String inteface) {
		super(maxMemorySize, expireTime);
		this.exportDir = exportDir;
		this.Inteface = inteface;
		this.isCompress = SdtpConfig.isCompress();
	}

	/**
	 * 单接口文件命名为Interface_YYYYMMDDHHmmss_SEQ.txt： 表1-1 分组域日志服务器上报用户上网日志文件命名规则 字段名
	 * 说明 Interface 代表接口名称，如S1_MME、S6a、SGs、S11、S1_U YYYYMMDDHHmmss 代表时间 SEQ
	 * 描述同一个文件顺序号，若结果被分割成多个文件则其流水号必须不同，并且从00001依次递增，如： 00001、00002、00003；
	 */
	@Override
	public String buildTmpPath() {
		StringBuffer tmpPath = new StringBuffer(exportDir);
		tmpPath.append(File.separator)
				.append(Inteface)
				.append(CommonConstants.UNDERLINE)
				.append(DateUtil.DateToString(new Date(),
						DateUtil.COMPACT_SECOND_FORMAT))
				.append(CommonConstants.UNDERLINE)
				.append(fiveFormat.format(seq))
				.append(CommonConstants.TMP_SUFFIX);
		File tmpFile = new File(tmpPath.toString());
		File txtFile = new File(tmpFile.getAbsolutePath().replace(
				CommonConstants.TMP_SUFFIX, CommonConstants.TXT_SUFFIX));
		while (tmpFile.exists() || txtFile.exists()) {
			seq++;
			tmpFile = new File(buildTmpPath());
			txtFile = new File(tmpFile.getAbsolutePath().replace(
					CommonConstants.TMP_SUFFIX, CommonConstants.TXT_SUFFIX));
		}
		seq = 0;
		return tmpFile.toString();
	}

	@Override
	public int getMemoryBytes() {
		// TODO Auto-generated method stub
		return 0;
	}
}
