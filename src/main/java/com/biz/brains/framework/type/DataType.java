package com.biz.brains.framework.type;

import java.time.LocalDateTime;
import java.util.Objects;

public enum DataType {

	INTEGER("int",Integer.class,"integer","Int"),
	AUTO_NUMBER("autonumber",String.class,"varchar","String"),
	TEXT("text",String.class,"varchar","String"),
	BOOLEAN("boolean",Boolean.class,"varchar","Boolean"),
	TIMESTAMP_WITH_TIME_ZONE("timestamp with timezone",LocalDateTime.class,"timestamp with time zone","Timestamp");
	
	public String key;
	public Class dataType;
	public String sqlDataType;
	public String mapper;
	DataType(String key,Class dataType,String sqlDataType,String mapper){
		this.key=key;
		this.dataType=dataType;
		this.sqlDataType=sqlDataType;
		this.mapper=mapper;
	}
	
	public static DataType getDataType(String key) {
		Objects.requireNonNull(key,"Arguments must not be empty.");
		for (DataType dataType:DataType.values()){
			if(dataType.key.equals(key)){
				return dataType;
			}
		}
		throw new IllegalArgumentException("Cannot detect the key.");
	}
	
}
