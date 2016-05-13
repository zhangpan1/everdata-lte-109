//package com.eversec.lte.tmp;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//
//import com.eversec.lte.model.single.XdrSingleSourceUu;
//import com.eversec.lte.output.FilledXdrFileOutput;
//
//public class UUWork {
//	public static void main(String[] args) throws Exception, Exception {
//		FilledXdrFileOutput.initOutputTask();
//		String dir =  args[0];
//		Collection<File> files = FileUtils.listFiles(new File(dir),
//				new String[] { "csv", "txt" }, true);
//		System.out.println(files.size());
//		for (File f : files) {
//			try {
//				List<XdrSingleSourceUu> uus = XdrSingleSourceUu.getUuXdrList(f
//						.getAbsolutePath());
//				for (XdrSingleSourceUu uu : uus) {
//					FilledXdrFileOutput.output(uu);
//				}
//				System.out.println(uus.size());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
