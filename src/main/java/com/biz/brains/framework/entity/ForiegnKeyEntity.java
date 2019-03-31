package com.biz.brains.framework.entity;

import com.biz.brains.framework.type.DataType;

import lombok.Data;

@Data
public class ForiegnKeyEntity {
	
	private String columnName;
	
	private DataType dataType;
	
	private boolean primary;
	
	private String tableName;
}
