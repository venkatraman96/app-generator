package com.biz.brains.framework.generator;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.util.GeneratorUtils;
import com.biz.brains.framework.util.NameUtils;

import lombok.AllArgsConstructor;

public class BatchSetterGenerator {

	public static StringBuilder generate(Map<String,Object> dataMap,String filePath,String className) {
		StringBuilder stringBuilder=new StringBuilder();
		generateImports(filePath, className, stringBuilder,dataMap);
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");
		String dtoClassName=dataMap.get("dtoClassName").toString();

		
		stringBuilder.append("private List<").append(dtoClassName).append("> dataList = new LinkedList<>();\n");
		stringBuilder.append("\n");
		
		generateSetValuesMethod(className, dtoClassName, stringBuilder, metaDataEntities);

		
		generateGetBatchSizeMethod(className, dtoClassName, stringBuilder, metaDataEntities);

		stringBuilder.append("}\n");
		return stringBuilder;
		
	}
	
	private static void generateSetValuesMethod(String className, String dtoClassName, StringBuilder stringBuilder,
			List<MetaDataEntity> metaDataEntities) {
		stringBuilder.append("\t@Override\n");
		stringBuilder.append("\tpublic void setValues(PreparedStatement ps, int i) throws SQLException {\n");
		stringBuilder.append(GeneratorUtils.indent(dtoClassName+" dto = dataList.get(i);\n",1));
		
		IntStream.range(0, metaDataEntities.size()).forEach(idx->{
			StringBuilder psB=new StringBuilder();
			boolean flag=getResultSet(metaDataEntities.get(idx))=="Timestamp";
			boolean flag1=getResultSet(metaDataEntities.get(idx))=="Date";
			psB.append("ps.set").append(getResultSet(metaDataEntities.get(idx)))
			.append("(")
			.append(idx+1)
			.append(", ");
			if(flag) {
				psB.append("Timestamp.valueOf(");
			}
			if(flag1) {
				psB.append("Date.valueOf(");
			}
			psB.append("dto.get")
			.append(NameUtils.getCamelName(metaDataEntities.get(idx).getColumnName()))
			.append("()");
			if(flag) {
				psB.append(")");
			}
			if(flag1) {
				psB.append(")");
			}
			psB.append(");\n");
			stringBuilder.append(GeneratorUtils.indent(psB.toString(),1));
			
		});
		
		
		stringBuilder.append("\t}\n\n");
		
	}
	
	private static String getResultSet(MetaDataEntity entity) {
		return entity.getDataType().mapper;
	}
	
	private static void generateGetBatchSizeMethod(String className, String dtoClassName, StringBuilder stringBuilder,
			List<MetaDataEntity> metaDataEntities) {
		stringBuilder.append("\t@Override\n");
		stringBuilder.append("\tpublic int getBatchSize() {\n");
	
		stringBuilder.append(GeneratorUtils.indent("return dataList.size();\n",1));
		stringBuilder.append("\t}\n\n");
		
	}

	private static void generateImports(String filePath, String className, StringBuilder stringBuilder,Map<String,Object> dataMap) {
		String dtoClassName=dataMap.get("dtoClassName").toString();
		
		stringBuilder.append("package "+GeneratorUtils.generateBSPackageName(filePath)+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+java.sql.PreparedStatement.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+SQLException.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+Timestamp.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+Date.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+LinkedList.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+List.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+BatchPreparedStatementSetter.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+GeneratorUtils.generateDtoPackageName(dataMap.get("dtoPackage").toString())+"."+dtoClassName+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+AllArgsConstructor.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("@AllArgsConstructor\n");
		stringBuilder.append("public class ").append(className).append(" implements BatchPreparedStatementSetter {\n");
		stringBuilder.append("\n");
		stringBuilder.append("\n");
	}
}
