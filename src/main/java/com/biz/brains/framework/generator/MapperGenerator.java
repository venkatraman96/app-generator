package com.biz.brains.framework.generator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.util.GeneratorUtils;
import com.biz.brains.framework.util.NameUtils;

public class MapperGenerator {

	public static StringBuilder generate(Map<String, Object> dataMap, String filePath, String className) {
		String dtoClassName=dataMap.get("dtoClassName").toString();
//		String entityClassName=dataMap.get("entityClassName").toString();
		
		
		StringBuilder stringBuilder=new StringBuilder();
		generateImports(filePath, className, dtoClassName, stringBuilder,dataMap);
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");

		generateMapRowMethod(className, dtoClassName, stringBuilder, metaDataEntities);
//		
//		generateFormEntityMethod(className, dtoClassName, stringBuilder, metaDataEntities);
		
		stringBuilder.append("}\n");
		
		
		return stringBuilder;
	}
	
	private static void generateImports(String filePath, String className, String dtoClassName,
			StringBuilder stringBuilder,Map<String, Object> dataMap) {
//		String entityClassName=dataMap.get("entityClassName").toString();
		
		stringBuilder.append("package "+GeneratorUtils.generateMapperPackageName(filePath)+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+org.springframework.jdbc.core.RowMapper.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+GeneratorUtils.generateDtoPackageName(dataMap.get("dtoPackage").toString())+"."+dtoClassName+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+ResultSet.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+SQLException.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("public class ").append(className).append(" implements RowMapper<"+dtoClassName+"> {\n");
		stringBuilder.append("\n");
	}
	
	private static void generateMapRowMethod(String className, String dtoClassName, StringBuilder stringBuilder,
			List<MetaDataEntity> metaDataEntities) {
		stringBuilder.append("\t@Override\n");
		stringBuilder.append("\tpublic ").append(dtoClassName).append(" mapRow(ResultSet resultSet, int currentRow) throws SQLException {\n");
		StringBuilder sB=new StringBuilder();
		sB.append(dtoClassName).append(" entity = new "+dtoClassName+"();\n");
		stringBuilder.append(GeneratorUtils.indent(sB.toString(),1));
		metaDataEntities.stream().forEach(entity->{
			String content="entity."+"set"+NameUtils.getCamelName(entity.getColumnName());
			content=content+"(resultSet.get"+getResultSet(entity)+"(\""+entity.getColumnName()+"\")";
			if(entity.getDataType().mapper=="Timestamp") {
				content=content+".toLocalDateTime()";
			}
			if(entity.getDataType().mapper=="Date") {
				content=content+".toLocalDate()";
			}
			content=content+");\n";
			stringBuilder.append(GeneratorUtils.indent(content,1));
		});
		stringBuilder.append(GeneratorUtils.indent("return entity;\n",1));
		stringBuilder.append("\t}\n\n");
	}

	private static String getResultSet(MetaDataEntity entity) {
		return entity.getDataType().mapper;
	}

}
