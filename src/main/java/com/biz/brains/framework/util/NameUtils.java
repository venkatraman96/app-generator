package com.biz.brains.framework.util;

import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.type.DataType;
import com.google.common.base.CaseFormat;

public class NameUtils {
	
	public static String getDtoClassName(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName)+"Dto";
	}
	
	public static String getEntityClassName(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName)+"Entity";
	}
	
	public static String getMapperClassName(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName)+"Mapper";
	}
	
	public static String getBatchSetterClassName(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName)+"BatchSetter";
	}
	
	public static String getDaoClassName(String tableName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName)+"Dao";
	}
	
	public static String getSourcePath(String filePath) {
		int position=filePath.indexOf("java");
		return filePath.substring(position+5);
	}
	
	public static String getFeildName(String columnName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnName);
	}
	
	public static String getCamelName(String columnName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, columnName);
	}
	
	
	public static String getDtoPath(String entityFilePath) {
		int position=entityFilePath.indexOf("entity");
		return entityFilePath.substring(0, position-1);
	}
	
	
	public static void main(String[] args) {
//		String file="/Users/venkatraman/Develop/Workspace/biz-brains-invoice-project/biz-brains-invoice-project-dto/src/main/java/com/biz/brains/project";
//		String src=getSourcePath(file);
//		src=GeneratorUtils.generatePackageName(file);
//		System.out.println(src);
		
//		MetaDataEntity entity=new MetaDataEntity();
//		entity.setDataType(DataType.INTEGER);
//		DataType e=entity.getDataType();
//		System.out.println(e.dataType.getSimpleName());
		System.out.println(getFeildName("customer_mst_id"));
	}

}
