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

public class EntityGenerator {

	
	public static StringBuilder generate(Map<String,Object> dataMap,String filePath,String className) {
		String dtoClassName=dataMap.get("dtoClassName").toString();
		
		
		StringBuilder stringBuilder=new StringBuilder();
		generateImports(filePath, className, dtoClassName, stringBuilder);
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");
		generatePojo(stringBuilder, metaDataEntities);		
		
		generateFormDtoMethod(className, dtoClassName, stringBuilder, metaDataEntities);
		
		generateFormEntityMethod(className, dtoClassName, stringBuilder, metaDataEntities);
		
		stringBuilder.append("}\n");
		
		
		return stringBuilder;
		
	}

	private static void generateImports(String filePath, String className, String dtoClassName,
			StringBuilder stringBuilder) {
		stringBuilder.append("package "+GeneratorUtils.generateEntityPackageName(filePath)+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+LocalDateTime.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("import "+LocalDate.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		
		//Dto import
		stringBuilder.append("import "+GeneratorUtils.generateDtoPackageName(NameUtils.getDtoPath(filePath)+"/dto")).append("."+dtoClassName+";\n");
		stringBuilder.append("\n");
		
		stringBuilder.append("import "+Data.class.getCanonicalName()+";\n");
		stringBuilder.append("\n");
		stringBuilder.append("@Data\n");
		stringBuilder.append("public class ").append(className).append(" {\n");
		stringBuilder.append("\n");
	}

	private static void generateFormDtoMethod(String className, String dtoClassName, StringBuilder stringBuilder,
			List<MetaDataEntity> metaDataEntities) {
		stringBuilder.append("\tpublic static ").append(dtoClassName).append(" formDto("+className+" entity) {\n");
		StringBuilder sB=new StringBuilder();
		sB.append(dtoClassName).append(" dto = new "+dtoClassName+"();\n");
		stringBuilder.append(GeneratorUtils.indent(sB.toString(),1));
		metaDataEntities.stream().forEach(entity->{
			String content="dto."+"set"+NameUtils.getCamelName(entity.getColumnName());
			content=content+"(entity.get"+NameUtils.getCamelName(entity.getColumnName())+"());\n";
			stringBuilder.append(GeneratorUtils.indent(content,1));
		});
		stringBuilder.append(GeneratorUtils.indent("return dto;\n",1));
		stringBuilder.append("\t}\n\n");
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
	
	private static void generateFormEntityMethod(String className, String dtoClassName, StringBuilder stringBuilder,
			List<MetaDataEntity> metaDataEntities) {
		stringBuilder.append("\tpublic static ").append(className).append(" formEntity("+dtoClassName+" dto) {\n");
		StringBuilder sB=new StringBuilder();
		sB.append(className).append(" entity = new "+className+"();\n");
		stringBuilder.append(GeneratorUtils.indent(sB.toString(),1));
		metaDataEntities.stream().forEach(dto->{
			String content="entity."+"set"+NameUtils.getCamelName(dto.getColumnName());
			content=content+"(dto.get"+NameUtils.getCamelName(dto.getColumnName())+"());\n";
			stringBuilder.append(GeneratorUtils.indent(content,1));
		});
		stringBuilder.append(GeneratorUtils.indent("return entity;\n",1));
		stringBuilder.append("\t}\n\n");
	}


}
