package com.biz.brains.framework.entity;

import com.biz.brains.framework.type.DataType;

import lombok.Data;

@Data
public class MetaDataEntity {
	
	private String columnName;
	
	private DataType dataType;
	
	private boolean primary;
	
	private boolean foriegnKey;
	
	private boolean searchable;
	
	private boolean required;
	
	private boolean unique;
	
	private int maxLength;
	
	private int minLength;
}
