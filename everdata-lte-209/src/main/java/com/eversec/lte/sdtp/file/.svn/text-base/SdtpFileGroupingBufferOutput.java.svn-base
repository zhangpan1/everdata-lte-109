package com.eversec.lte.sdtp.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.eversec.common.constant.CommonConstants;

public class SdtpFileGroupingBufferOutput extends FileBufferOutput {

	private long use_time_all = 0;
	private long use_time_find = 0;

	public SdtpFileGroupingBufferOutput() {
		super(2000);
	}

	public void log(long time) {
		super.log(time);
		logger.info("file output use find rate:"
				+ (use_time_find * 1.0d / use_time_all));
	}

	private DecimalFormat fiveFormat = new DecimalFormat("00000");

	/**
	 * 
	 * @param parameters
	 * @return [0] dir [1] filename
	 */
	private String[] createPreFilename(Object[] parameters) {
		String[] result = new String[2];

		String Interface = parameters[0].toString();
		int apptype = (int) parameters[1];
		long time = (long) parameters[2];
		String dateStr = (String) parameters[3];
		String city = (String) parameters[4];
		String imsiLast = (String) parameters[5];
		String apptypeStr = (String) parameters[6];

		// 024 ->0024
		if (city != null && city.length() < 4) {
			city = "0" + city;
		}

		String preDir;
		String preFilename;
		String day = dateStr.substring(0, 8);
		String hour = dateStr.substring(8);
		if ("s1u".equals(Interface)) {

			// /home/eversec/filled/s1u_HTTP/s1u_2015111112_0024_3_00007.tmp
			// /home/eversec/filled/s1u_HTTP/0024/20151111/12/3/s1u_2015111112_0024_3_00007.tmp
			preDir = city + File.separator + day + File.separator + hour
					+ File.separator + imsiLast;
			preFilename = Interface + CommonConstants.UNDERLINE + dateStr
					+ CommonConstants.UNDERLINE + city
					+ CommonConstants.UNDERLINE + imsiLast;
		} else {
			// s1mme_20150821113533_00000
			preDir = city + File.separator + day + File.separator + hour
					+ File.separator + imsiLast;
			preFilename = Interface + CommonConstants.UNDERLINE + dateStr
					+ CommonConstants.UNDERLINE + city
					+ CommonConstants.UNDERLINE + imsiLast;
		}

		result[0] = preDir;
		result[1] = preFilename;

		return result;
	}

	// 跟文件数量有很大关系
	public File findMaxFilePath(String path, Object[] parameters) {
		long time_find = System.currentTimeMillis();

		try {
			int seq = 0;
			String[] preDirFile = createPreFilename(parameters);
			;
			final String preDir = preDirFile[0];
			final String preFile = preDirFile[1];
			StringBuffer tmp = new StringBuffer(path);
			tmp.append(File.separator).append(preDir).append(File.separator)
					.append(preFile).append(CommonConstants.UNDERLINE)
					.append(fiveFormat.format(seq))
					.append(CommonConstants.TMP_SUFFIX);
			File tmpFile = new File(tmp.toString());

			File lastFile = findLastNameFile(tmpFile.getParentFile(), preFile);
			if (lastFile == null) {
				return tmpFile;
			}
			if (lastFile.getName().endsWith(CommonConstants.TXT_SUFFIX)) {
				String numStr = lastFile.getName().substring(
						lastFile.getName().length()
								- CommonConstants.TXT_SUFFIX.length() - 5,
						lastFile.getName().length()
								- CommonConstants.TXT_SUFFIX.length());
				seq = Integer.parseInt(numStr) + 1;

				StringBuffer newtmp = new StringBuffer(path);
				newtmp.append(File.separator).append(preDir)
						.append(File.separator).append(preFile)
						.append(CommonConstants.UNDERLINE)
						.append(fiveFormat.format(seq))
						.append(CommonConstants.TMP_SUFFIX);
				File newtmpFile = new File(newtmp.toString());

				return newtmpFile;
			} else if (lastFile.getName().endsWith(CommonConstants.TMP_SUFFIX)) {
				return lastFile;
			} else {
				System.out.println("error lastFile:" + lastFile);
				return null;
			}
		} finally {
			use_time_find += (System.currentTimeMillis() - time_find);
		}
	}

	/**
	 * 得到当前目录下文件按某个文件名过滤排列最大的文件
	 * 
	 * @param parentFile
	 * @param preFile
	 * @return
	 */
	public File findLastNameFile(File parentFile, final String preFile) {
		File[] files = parentFile.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(preFile);
			}
		});
		if (files == null || files.length == 0) {
			return null;
		}
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		File lastFile = files[files.length - 1];
		return lastFile;
	}

	@Override
	public void run() {
		while (true) {
			try {
				FileBuffer buffer = fileBufferQueue.take();
				long time_all = System.currentTimeMillis();
				File file = null;
				FileOutputStream out = null;

				file = findMaxFilePath(buffer.getPath(), buffer.getParameters());

				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				out = new FileOutputStream(file, true);

				FileChannel fs = out.getChannel();
				buffer.getBuffer().flip();
				fs.write(buffer.getBuffer().buf());
				out.close();
				fs.close();

				if (buffer.isAppend()) {

				} else {
					file.renameTo(new File(file.getAbsolutePath().replace(
							".tmp", ".txt")));
					logger.info("ready to save file:" + file);
				}

				writeBytes.addAndGet(buffer.getBuffer().limit());

				use_time_all += (System.currentTimeMillis() - time_all);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// public static void main(String[] args) throws IOException {
	// String Interface = "s11";
	// int apptype = 1;
	// long time = new Date().getTime();
	// String dateStr = "2015111010";
	// String city = "024";
	// String imsiLast = "1";
	// String apptypeStr = "2";
	// Object[] p = new Object[] { Interface, apptype, time, dateStr, city,
	// imsiLast, apptypeStr };
	// SdtpFileGroupingBufferOutput output = new SdtpFileGroupingBufferOutput();
	// File file = output.createFilePath("c:/home/test/", p, 0);
	// System.out.println("file:" + file);
	// file.getParentFile().mkdirs();
	// // file.createNewFile();
	// File file1 = output.findMaxFilePath("c:/home/test/", p, 0);
	// System.out.println("file1:" + file1);
	//
	// }

	@Override
	public File createFilePath(String path, Object[] parameters) {
		return null;
	}

}
