package com.eversec.lte.sdtp.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class TestNewIO {
	
	public static void main(String[] args) {
		
	}
	public static void main1(String[] args) throws Exception {
		// Path path = Paths.get("C:/home/eversec/filled/s1u/s1u_S11SIGN");
		Path path = Paths.get("C:/home/eversec/filled/s1u/s1u_S11SIGN/" );
		int c = path.getNameCount();// /home/eversec/filled/s1u/s1u_S11SIGN 5ge
		Path i = path.getName(0);
		System.out.println(c);
//
//		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
//			@Override
//			public FileVisitResult visitFile(Path file,
//					BasicFileAttributes attrs) throws IOException {
//			}
//		});

		try {
			Files.readAllLines(path, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
