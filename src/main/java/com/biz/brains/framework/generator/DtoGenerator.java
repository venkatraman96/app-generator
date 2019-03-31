package com.biz.brains.framework.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.util.GeneratorUtils;
import com.biz.brains.framework.util.NameUtils;

import lombok.Data;
import lombok.Lombok;

public class DtoGenerator {
	
	public static StringBuilder generate(Map<String,Object> dataMap,String filePath,String className) {
		StringBuilder stringBuilder=new StringBuilder();
		generateImports(filePath, className, stringBuilder);
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");
		generatePojo(stringBuilder, metaDataEntities);		
		stringBuilder.append("}\n");
		return stringBuilder;
		
	}

	private static void generatePojo(StringBuilder stringBuilder, List<MetaDataEntity> metaDataEntities) {
		metaDataEntities.stream().forEach(entity->{
			stringBuilder.append("\tprivate ");
			stringBuilder.append(entity.getDataType().dataType.getSimpleName());
			stringBuilder.append(" ");
			stringBuilder.append(NameUtils.getFeildName(entity.getColumnName()));
			stringBuilder.append(";\n");
			stringBuilder.append("\n");
		});
	}

	private static void generateImports(String filePath, String className, StringBuilder stringBuilder) {
		stringBuilder.append("package "+GeneratorUtils.generateDtoPackageName(filePath)+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+LocalDateTime.class.getCanonicalName()+";\n");
		stringBuilder.append("import "+LocalDate.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+Data.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("@Data\n");
		stringBuilder.append("public class ").append(className).append(" {\n");
		stringBuilder.append("\n");
	}

}
