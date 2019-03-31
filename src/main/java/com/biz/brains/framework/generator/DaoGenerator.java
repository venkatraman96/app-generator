package com.biz.brains.framework.generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.util.GeneratorUtils;

public class DaoGenerator {

	public static StringBuilder generate(Map<String,Object> dataMap,String filePath,String className) {
		StringBuilder stringBuilder=new StringBuilder();
		generateImports(filePath, className, stringBuilder,dataMap);
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");
//		generatePojo(stringBuilder, metaDataEntities);	
		String entityClassName=dataMap.get("entityClassName").toString();

		stringBuilder
		.append("\n\t public int insert(")
		.append(entityClassName)
		.append(" entity);\n");
		
		stringBuilder
		.append("\n\t public ").append(entityClassName).append(" getSingle(")
		.append(entityClassName)
		.append(" entity);\n");
		
		
		stringBuilder
		.append("\n\t public List<").append(entityClassName).append("> getMultiple(")
		.append(entityClassName)
		.append(" entity);\n");
		
		stringBuilder
		.append("\n\t public int delete(")
		.append(entityClassName)
		.append(" entity);\n");
		
		stringBuilder
		.append("\n\t public int truncate();\n");
		
		stringBuilder
		.append("\n\t public int update(")
		.append(entityClassName)
		.append(" entity);\n");
		
		stringBuilder
		.append("\n\t public int batchUpdate(List<")
		.append(entityClassName)
		.append("> entityList);\n");
		
		stringBuilder
		.append("\n\t public int insertBatch(List<")
		.append(entityClassName)
		.append("> entityList);\n");
		
		stringBuilder.append("}\n");
		return stringBuilder;
		
	}
	
	private static void generateImports(String filePath, String className, StringBuilder stringBuilder,Map<String,Object> dataMap) {
		String entityPackagePath=dataMap.get("entityPackagePath").toString();
		String entityClassName=dataMap.get("entityClassName").toString();
		
		stringBuilder.append("package "+GeneratorUtils.generateDaoPackageName(filePath)+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+GeneratorUtils.generateEntityPackageName(entityPackagePath)+"."+entityClassName+";\n");
		stringBuilder.append("import "+ java.util.List.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("public interface ").append(className).append(" {\n");
		stringBuilder.append("\n");
	}
}
