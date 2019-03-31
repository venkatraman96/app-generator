package com.biz.brains.framework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.biz.brains.framework.generator.BatchSetterGenerator;
import com.biz.brains.framework.generator.DaoGenerator;
import com.biz.brains.framework.generator.DaoImplGenerator;
import com.biz.brains.framework.generator.DtoGenerator;
import com.biz.brains.framework.generator.EntityGenerator;
import com.biz.brains.framework.generator.MapperGenerator;
import com.biz.brains.framework.generator.QueryGenerator;
import com.biz.brains.framework.reader.LayoutReader;
import com.biz.brains.framework.util.NameUtils;

public class AppGenerator {

	
	public static void main(String[] args) {
		Map<String,Object> dataMap=new HashMap<>();
		System.out.println("----------------------------------------");
		System.out.println("|           APP GENERATOR (V1.0)        |");
		System.out.println("----------------------------------------");
		System.out.println("| Arguments to be given :               |");
		System.out.println("| 1.Table Layout File Path              |");
		System.out.println("| 2.SQL Generation File Path            |");
		System.out.println("| 3.DTO/Model Generation File Path      |");
		System.out.println("| 4.DAO/Repository Generation File Path |");
		System.out.println("----------------------------------------");
		System.out.println("eg.) java -jar AppGenerator.jar /Users/venkatraman/Documents/customer_detail_mst.xlsx /Users/venkatraman/Develop/Workspace/biz-brains-invoice-project/biz-brains-invoice-project-test-data/src/main/resources/sql /Users/venkatraman/Develop/Workspace/biz-brains-invoice-project/biz-brains-invoice-project-dto/src/main/java/com/biz/brains/project /Users/venkatraman/Develop/Workspace/biz-brains-invoice-project/biz-brains-invoice-project-bizcore/src/main/java/com/biz/binary/project/bizcore");
		System.out.println("\nAny queries contact author at madurai.mvr@gmail.com");
		System.out.println("----------------------------------------");
		System.out.println("Layout File Path given is : \n\t"+args[0]);
		System.out.println("SQL File Path given is : \n\t"+args[1]);
		System.out.println("DTO/Model File Path given is : \n\t"+args[2]);
		System.out.println("DAO/Repository File Path given is : \n\t"+args[3]);
		
		long start=System.currentTimeMillis();
		System.out.println("Generating ...");
		
		String fileName=args[0];
		dataMap=LayoutReader.read(fileName);
		String filePath=args[1];
		generateCreateSQL(dataMap, filePath);
		
		//DTO generation logic
		String dtoProjectPath=args[2];
		generateDto(dataMap, dtoProjectPath);
		
		//Entity generation logic
		generateEntity(dataMap, dtoProjectPath);
		
		//Mapper Generation Logic
		generateMapper(dataMap, dtoProjectPath);
		
		//Batch Setter Generation Logic
		generateBatchSetter(dataMap, dtoProjectPath);
		
		//Dao Inteface Generation Logic
		String daoProjectpath=args[3];
		generateDao(dataMap, daoProjectpath, dtoProjectPath);
		generateDaoImpl(dataMap, daoProjectpath, dtoProjectPath);
		
		long end=System.currentTimeMillis();
		
		System.out.println("Generation Completed in "+(end-start)+" seconds.");

	}

	private static void generateBatchSetter(Map<String, Object> dataMap, String dtoProjectPath) {
		String batchPackagePath;
		if(!dtoProjectPath.endsWith("/")) {
			batchPackagePath=dtoProjectPath+"/batch";
		}else {
			batchPackagePath=dtoProjectPath+"batch";
		}
		
		String className=NameUtils.getBatchSetterClassName(dataMap.get("tableName").toString());
		Path bsJavaPath = Paths.get(batchPackagePath, className + ".java");
		try {
		if (Files.exists(bsJavaPath)){
			Files.deleteIfExists(bsJavaPath);
			Files.createFile(bsJavaPath);
		}
		String dtoClassName=NameUtils.getDtoClassName(dataMap.get("tableName").toString());
		dataMap.put("dtoClassName", dtoClassName);
		String entityClassName=NameUtils.getEntityClassName(dataMap.get("tableName").toString());
		dataMap.put("entityClassName", entityClassName);
		String entityPackagePath = null;
		if(batchPackagePath.endsWith("/batch")) {
			entityPackagePath=batchPackagePath.replaceFirst("/batch", "/entity");
		}
		dataMap.put("entityPackage", entityPackagePath);
			StringBuilder bsContentStringBuilder=BatchSetterGenerator.generate(dataMap, batchPackagePath,className);
			Files.write(bsJavaPath, bsContentStringBuilder.toString().getBytes());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private static void generateCreateSQL(Map<String, Object> dataMap, String filePath) {
		LocalDateTime localDateTime=LocalDateTime.now();
		String formatter=localDateTime.getYear()+""+localDateTime.getMonthValue()+""+localDateTime.getDayOfMonth();
		String className="CREATE_"+(dataMap.get("tableName").toString().toUpperCase())+"_"+formatter;
		Path sqlFilePath = Paths.get(filePath, className + ".sql");
		try {
			if (Files.exists(sqlFilePath)){
				Files.deleteIfExists(sqlFilePath);
				Files.createFile(sqlFilePath);
			}
				StringBuilder sqlContentStringBuilder=QueryGenerator.generate(dataMap);
				Files.write(sqlFilePath, sqlContentStringBuilder.toString().getBytes());
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
	}

	private static void generateDto(Map<String, Object> dataMap, String dtoProjectPath) {
		String dtoPackagePath;
		if(!dtoProjectPath.endsWith("/")) {
			dtoPackagePath=dtoProjectPath+"/dto";
		}else {
			dtoPackagePath=dtoProjectPath+"dto";
		}
		
		String className=NameUtils.getDtoClassName(dataMap.get("tableName").toString());
		Path dtoJavaFilePath = Paths.get(dtoPackagePath, className + ".java");
		try {
		if (Files.exists(dtoJavaFilePath)){
			Files.deleteIfExists(dtoJavaFilePath);
			Files.createFile(dtoJavaFilePath);
		}
			StringBuilder dtoContentStringBuilder=DtoGenerator.generate(dataMap, dtoPackagePath,className);
			Files.write(dtoJavaFilePath, dtoContentStringBuilder.toString().getBytes());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private static void generateEntity(Map<String, Object> dataMap, String entityProjectPath) {
		String entityPackagePath;
		if(!entityProjectPath.endsWith("/")) {
			entityPackagePath=entityProjectPath+"/entity";
		}else {
			entityPackagePath=entityProjectPath+"entity";
		}
		
		String className=NameUtils.getEntityClassName(dataMap.get("tableName").toString());
		Path entityJavaFilePath = Paths.get(entityPackagePath, className + ".java");
		try {
		if (Files.exists(entityJavaFilePath)){
			Files.deleteIfExists(entityJavaFilePath);
			Files.createFile(entityJavaFilePath);
		}
		String dtoClassName=NameUtils.getDtoClassName(dataMap.get("tableName").toString());
		dataMap.put("dtoClassName", dtoClassName);
			StringBuilder entityContentStringBuilder=EntityGenerator.generate(dataMap, entityPackagePath,className);
			Files.write(entityJavaFilePath, entityContentStringBuilder.toString().getBytes());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private static void generateMapper(Map<String, Object> dataMap, String mapperProjectpath) {
		String mapperPackagePath;
		if(!mapperProjectpath.endsWith("/")) {
			mapperPackagePath=mapperProjectpath+"/mapper";
		}else {
			mapperPackagePath=mapperProjectpath+"mapper";
		}
		
		String className=NameUtils.getMapperClassName(dataMap.get("tableName").toString());
		Path mapperJavaPath = Paths.get(mapperPackagePath, className + ".java");
		try {
		if (Files.exists(mapperJavaPath)){
			Files.deleteIfExists(mapperJavaPath);
			Files.createFile(mapperJavaPath);
		}
		String dtoClassName=NameUtils.getDtoClassName(dataMap.get("tableName").toString());
		dataMap.put("dtoClassName", dtoClassName);
		String entityClassName=NameUtils.getEntityClassName(dataMap.get("tableName").toString());
		dataMap.put("entityClassName", entityClassName);
		String dtoPackagePath = null;
		if(mapperPackagePath.endsWith("/mapper")) {
			dtoPackagePath=mapperPackagePath.replaceFirst("/mapper", "/dto");
		}
		dataMap.put("dtoPackage", dtoPackagePath);
			StringBuilder mapperContentStringBuilder=MapperGenerator.generate(dataMap, mapperPackagePath,className);
			Files.write(mapperJavaPath, mapperContentStringBuilder.toString().getBytes());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private static void generateDao(Map<String, Object> dataMap, String daoProjectpath,String dtoProjectPath) {
		String daoPackagePath;
		if(!daoProjectpath.endsWith("/")) {
			daoPackagePath=daoProjectpath+"/dao";
		}else {
			daoPackagePath=daoProjectpath+"dao";
		}
		
		String className=NameUtils.getDaoClassName(dataMap.get("tableName").toString());
		Path daoJavaPath = Paths.get(daoPackagePath, className + ".java");
		try {
		if (Files.exists(daoJavaPath)){
			Files.deleteIfExists(daoJavaPath);
			Files.createFile(daoJavaPath);
		}
		
		String dtoClassName=NameUtils.getDtoClassName(dataMap.get("tableName").toString());
		dataMap.put("dtoClassName", dtoClassName);
		String dtoPackagePath=null;
		if(!dtoProjectPath.endsWith("/")) {
			dtoPackagePath=dtoProjectPath+"/dto";
		}else {
			dtoPackagePath=dtoProjectPath+"dto";
		}
		dataMap.put("dtoProjectPath", dtoProjectPath);
		
		
		String entityClassName=NameUtils.getEntityClassName(dataMap.get("tableName").toString());
		dataMap.put("entityClassName", entityClassName);
		String entityPackagePath=null;
		if(!dtoProjectPath.endsWith("/")) {
			entityPackagePath=dtoProjectPath+"/entity";
		}else {
			entityPackagePath=dtoProjectPath+"entity";
		}
		dataMap.put("entityPackagePath", entityPackagePath);
		
			StringBuilder daoContentStringBuilder=DaoGenerator.generate(dataMap, daoPackagePath,className);
			Files.write(daoJavaPath, daoContentStringBuilder.toString().getBytes());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	private static void generateDaoImpl(Map<String, Object> dataMap, String daoProjectpath,String dtoProjectPath) {
		String daoPackagePath;
		if(!daoProjectpath.endsWith("/")) {
			daoPackagePath=daoProjectpath+"/dao";
		}else {
			daoPackagePath=daoProjectpath+"dao";
		}
		
		String className=NameUtils.getDaoClassName(dataMap.get("tableName").toString());
		Path daoJavaPath = Paths.get(daoPackagePath, className + "Impl.java");
		try {
		if (Files.exists(daoJavaPath)){
			Files.deleteIfExists(daoJavaPath);
			Files.createFile(daoJavaPath);
		}
		
		String dtoClassName=NameUtils.getDtoClassName(dataMap.get("tableName").toString());
		dataMap.put("dtoClassName", dtoClassName);
		String dtoPackagePath=null;
		if(!dtoProjectPath.endsWith("/")) {
			dtoPackagePath=dtoProjectPath+"/dto";
		}else {
			dtoPackagePath=dtoProjectPath+"dto";
		}
		dataMap.put("dtoProjectPath", dtoProjectPath);
		
		
		String entityClassName=NameUtils.getEntityClassName(dataMap.get("tableName").toString());
		dataMap.put("entityClassName", entityClassName);
		String entityPackagePath=null;
		if(!dtoProjectPath.endsWith("/")) {
			entityPackagePath=dtoProjectPath+"/entity";
		}else {
			entityPackagePath=dtoProjectPath+"entity";
		}
		dataMap.put("entityPackagePath", entityPackagePath);
		
			StringBuilder daoContentStringBuilder=DaoImplGenerator.generate(dataMap, daoPackagePath,className);
			Files.write(daoJavaPath, daoContentStringBuilder.toString().getBytes());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
