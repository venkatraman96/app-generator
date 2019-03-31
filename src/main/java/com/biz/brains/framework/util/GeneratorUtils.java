package com.biz.brains.framework.util;

public class GeneratorUtils {
	
	public static String generateDtoPackageName(String basePath) {
		String dtoBasePath= NameUtils.getSourcePath(basePath).replaceAll("/", ".").toLowerCase();
		if(!dtoBasePath.endsWith(".dto")) {
			dtoBasePath=dtoBasePath+".dto";
		}
		return dtoBasePath;
		
	}
	
	public static String generateBSPackageName(String basePath) {
		String dtoBasePath= NameUtils.getSourcePath(basePath).replaceAll("/", ".").toLowerCase();
		if(!dtoBasePath.endsWith(".batch")) {
			dtoBasePath=dtoBasePath+".batch";
		}
		return dtoBasePath;
		
	}
	
	public static String generateEntityPackageName(String basePath) {
		String dtoBasePath= NameUtils.getSourcePath(basePath).replaceAll("/", ".").toLowerCase();
		if(!dtoBasePath.endsWith(".entity")) {
			dtoBasePath=dtoBasePath+".entity";
		}
		return dtoBasePath;
		
	}
	
	public static String generateMapperPackageName(String basePath) {
		String dtoBasePath= NameUtils.getSourcePath(basePath).replaceAll("/", ".").toLowerCase();
		if(!dtoBasePath.endsWith(".mapper")) {
			dtoBasePath=dtoBasePath+".mapper";
		}
		return dtoBasePath;
		
	}
	
	public static String generateDaoPackageName(String basePath) {
		String dtoBasePath= NameUtils.getSourcePath(basePath).replaceAll("/", ".").toLowerCase();
		if(!dtoBasePath.endsWith(".dao")) {
			dtoBasePath=dtoBasePath+".dao";
		}
		return dtoBasePath;
		
	}
	public static String indent(String lineContent,int length) {
		StringBuilder sb=new StringBuilder();
		for (int count = 0; count <= length; count++) {
			sb.append("\t");
		}
		sb.append(lineContent);
		 return sb.toString();
	}

}
