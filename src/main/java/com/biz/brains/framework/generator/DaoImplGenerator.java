package com.biz.brains.framework.generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.util.GeneratorUtils;

import lombok.AllArgsConstructor;

public class DaoImplGenerator {

	public static StringBuilder generate(Map<String,Object> dataMap,String filePath,String className) {
		StringBuilder stringBuilder=new StringBuilder();
		generateImports(filePath, className, stringBuilder,dataMap);
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");
//		generatePojo(stringBuilder, metaDataEntities);	
		String entityClassName=dataMap.get("entityClassName").toString();

		stringBuilder
		.append("\n\t private JdbcTemplate jdbcTemplate;\n");
		
		stringBuilder.append("}\n");
		return stringBuilder;
		
	}
	
	private static void generateImports(String filePath, String className, StringBuilder stringBuilder,Map<String,Object> dataMap) {
		String entityPackagePath=dataMap.get("entityPackagePath").toString();
		String entityClassName=dataMap.get("entityClassName").toString();
		String dtoPackagePath=dataMap.get("dtoProjectPath").toString();
		String dtoClassName=dataMap.get("dtoClassName").toString();
		
		stringBuilder.append("package "+GeneratorUtils.generateDaoPackageName(filePath)+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+GeneratorUtils.generateEntityPackageName(entityPackagePath)+"."+entityClassName+";\n");
		stringBuilder.append("import "+GeneratorUtils.generateDtoPackageName(dtoPackagePath)+"."+dtoClassName+";\n");
		stringBuilder.append("import "+GeneratorUtils.generateDaoPackageName(filePath)+"."+className+";\n");
		stringBuilder.append("import "+AllArgsConstructor.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+JdbcTemplate.class.getCanonicalName()+";\n");

		stringBuilder.append("\n");
		stringBuilder.append("@AllArgsConstructor\n");
		stringBuilder.append("public class ").append(className).append("Impl implements ")
		.append(className)
		.append(" {\n");
		stringBuilder.append("\n");
	}
}
