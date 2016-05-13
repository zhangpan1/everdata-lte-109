//package com.eversec.lte.output;
//
//import java.io.File;
//import java.text.DecimalFormat;
//import java.util.Date;
//
//import com.eversec.common.constant.CommonConstants;
//import com.eversec.common.output.FileOutput;
//import com.eversec.common.output.IOutput;
//import com.eversec.common.timeutil.DateUtil;
//import com.eversec.lte.config.SdtpConfig;
//import com.eversec.lte.model.XdrData;
//
///**
// * 针对集团考核数据输出
// * 
// * @author lirongzhi
// * 
// */
//@SuppressWarnings("serial")
//public class ExamineLteFileOutput extends FileOutput<IOutput> {
//
//	private DecimalFormat fiveFormat = new DecimalFormat("00000");
//	private DecimalFormat fourFormat = new DecimalFormat("0000");
//	private String city = SdtpConfig.getExamineCity();
//	private String exportDir;
//	private String typeDir;
//	private String Inteface;
//	private int seq = 0;
//	
//	private int startPos = -1;
//
//	public ExamineLteFileOutput(String maxMemorySize, String expireTime,
//			String exportDir, String typeDir, String inteface) {
//		super(maxMemorySize, expireTime);
//		this.exportDir = exportDir;
//		this.typeDir = typeDir;
//		this.Inteface = inteface;
//		this.isCompress = SdtpConfig.isCompress();
////		this.setMaxLineOutput(5000);
//	}
//	
//	public void setStartPos(int startPos) {
//		if(startPos>9 || startPos<0){
//			startPos = 9;
//		}
//		this.startPos = startPos;
//	}
//	
//	@Override
//	public void flush(IOutput access) {
//		// TODO Auto-generated method stub
//		super.flush(access);
//	}
//
//	/**
//	 * 1、原始信令采集
//	 * 每200MB保存为一个文件；单文件命名原则：城市（城市全拼）+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.pcap
//	 * （例如:chengdu2013111300001
//	 * .pcap）。原始数据必须存储为PCAP格式，S1-U和S11/S1-MME接口原始数据应汇入同一链路存储。<br>
//	 * 2、S1-U XDR采集文件命名规范 每200M保存为一个文件；单文件命名：城市（城市全拼）+
//	 * XDR类型编码+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.txt（例如
//	 * :chengdu1002013111300001.txt）； <br>
//	 * 3、S1-MME XDR采集文件命名规范 每200M保存为一个文件；单文件命名：城市（城市全拼）+
//	 * MME+采集时间年月日（格式：YYYYMMDD）+文件编号（五位数字）.txt（例如:chengduMME2013111300001.txt）；
//	 */
//	@Override
//	public String buildTmpPath() {
//		// 城市 /日期/xdr/inderface/
//		StringBuffer tmpPath = new StringBuffer(exportDir);
//		tmpPath.append(File.separator)
//				.append(city)
//				.append(File.separator)
//				.append(DateUtil.DateToString(new Date(),
//						DateUtil.COMPACT_DAY_FORMAT))
//				.append(File.separator)
//				.append("xdr")
//				.append(File.separator)
//				.append(city)
//				.append(Inteface)
//				.append(DateUtil.DateToString(new Date(),
//						DateUtil.COMPACT_DAY_FORMAT));
//				if (startPos<0 || SdtpConfig.IS_EXAMINE_SKIP_START_POS) {
//					tmpPath.append(fiveFormat.format(seq));
//				}else{
//					tmpPath.append(startPos);
//					tmpPath.append(fourFormat.format(seq));
//				}
//				tmpPath.append(CommonConstants.TMP_SUFFIX);
//		File tmpFile = new File(tmpPath.toString());
//		File txtFile = new File(tmpFile.getAbsolutePath().replace(
//				CommonConstants.TMP_SUFFIX, CommonConstants.TXT_SUFFIX));
//		while (tmpFile.exists() || txtFile.exists()) {
//			seq++;
//			tmpFile = new File(buildTmpPath());
//			txtFile = new File(tmpFile.getAbsolutePath().replace(
//					CommonConstants.TMP_SUFFIX, CommonConstants.TXT_SUFFIX));
//		}
//		seq = 0;
//		return tmpFile.toString();
//	}
//
//	@Override
//	public int getMemoryBytes() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//}
