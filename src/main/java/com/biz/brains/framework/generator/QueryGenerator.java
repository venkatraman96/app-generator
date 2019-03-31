package com.biz.brains.framework.generator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;

import com.biz.brains.framework.entity.ForiegnKeyEntity;
import com.biz.brains.framework.entity.MetaDataEntity;

public class QueryGenerator {
	
	public static StringBuilder generate(Map<String,Object> dataMap) {
		StringBuilder queryStringBuilder=new StringBuilder();
		queryStringBuilder.append("CREATE TABLE ");
		queryStringBuilder.append(dataMap.get("tableName").toString()+" ");
		queryStringBuilder.append("\n(");
		
		List<MetaDataEntity> metaDataEntities=new LinkedList<>();
		metaDataEntities=(List<MetaDataEntity>) dataMap.get("metatData");
		metaDataEntities.stream().forEach(entity->{
			queryStringBuilder.append("\n\t").append(entity.getColumnName())
			.append(" ").append(formDataType(entity))
			.append(" ");
		});
		
		//PRIMARY KEY
		List<String> primaryColumnsList=metaDataEntities.stream().filter(predicate->predicate.isPrimary()).map(entity->{
			return entity.getColumnName();
		}).collect(Collectors.toList());
		queryStringBuilder.append("\n\t").append("PRIMARY KEY (");
		IntStream.range(0, primaryColumnsList.size()).forEach(idx->{
			queryStringBuilder.append(primaryColumnsList.get(idx));
			if(idx!=primaryColumnsList.size()-1) {
				queryStringBuilder.append(",");
			}
		});
		queryStringBuilder.append(")");
		
		List<ForiegnKeyEntity> foriegnKeyEntities=(List<ForiegnKeyEntity>) dataMap.get("fkeyList");
		if(CollectionUtils.isNotEmpty(foriegnKeyEntities)) {
			queryStringBuilder.append(",");
			IntStream.range(0, foriegnKeyEntities.size()).forEach(idx->{
				queryStringBuilder.append("\n\t")
				.append("CONSTRAINT ")
				.append(
						dataMap
						.get("tableName")
						.toString()
						+"_"+
						foriegnKeyEntities.get(idx).getColumnName()
						+"_fkey"
						)
				.append(" FOREIGN KEY ("+foriegnKeyEntities.get(idx).getReferenceCol()+")");
				
				queryStringBuilder.append("\n\t\t")
				.append("REFERENCES ").append(foriegnKeyEntities.get(idx).getTableName())
				.append(" (").append(foriegnKeyEntities.get(idx).getColumnName()).append(")")
				.append(" MATCH")
				.append(" SIMPLE");//FIXME HARD CODE
				
				queryStringBuilder.append("\n\t\t")
				.append("ON UPDATE NO ACTION ON DELETE NO ACTION");
				
				if(idx!=foriegnKeyEntities.size()-1) {
					queryStringBuilder.append(",");
				}
			});
		}
		
		
		queryStringBuilder.append("\n);");
		
		
		
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println(queryStringBuilder.toString());
		return queryStringBuilder;
	}

	private static String formDataType(MetaDataEntity entity) {
		String sqlDataType = entity.getDataType().sqlDataType.toUpperCase();
		
		switch (sqlDataType) {
		case "VARCHAR":
			return buildVarchar(entity, sqlDataType);
		default:
			return (sqlDataType+",").trim();
		}
		
	}

	private static String buildVarchar(MetaDataEntity entity, String sqlDataType) {
		if(entity.getMaxLength()!=0) {
			sqlDataType=sqlDataType+"("+entity.getMaxLength()+") ";
		}else {
			sqlDataType=(sqlDataType+" ").trim();
		}
		
		if(entity.isRequired()) {
			sqlDataType=sqlDataType.trim()+" NOT NULL ";
		}else {
//			if(entity.isPrimary()) {
//				sqlDataType=sqlDataType+" NOT NULL UNIQUE ";
//			}
			sqlDataType=(sqlDataType+" ").trim();
		}
		
		if(entity.isUnique()) {
			sqlDataType=sqlDataType.trim()+" UNIQUE ";
		}else {
//			if(entity.isPrimary()) {
//				sqlDataType=sqlDataType+" NOT NULL UNIQUE ";
//			}
			sqlDataType=(sqlDataType+" ").trim();
		}
		return (sqlDataType+",").trim();
		
	}
	
	

}
