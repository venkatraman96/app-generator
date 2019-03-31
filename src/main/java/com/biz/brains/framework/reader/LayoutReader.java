package com.biz.brains.framework.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.biz.brains.framework.entity.ForiegnKeyEntity;
import com.biz.brains.framework.entity.MetaDataEntity;
import com.biz.brains.framework.type.DataType;

public class LayoutReader {

	private static final int SERIAL_NO_COLUMN = 1;
	private static final int INITIAL_VALUE = 0;
	private static final int TABLE_NAME_COLUMN = 2;
	private static final int TABLE_NAME_ROW = 6;
	private static final int TABLE_DATA_ROW = 11;

	public static Map<String,Object> read(String fileName) {
		Map<String,Object> dataMap=new HashMap<>();
		try (

			FileInputStream excelFile = new FileInputStream(new File(fileName));
			Workbook workbook = new XSSFWorkbook(excelFile);)
		{
			Sheet datatypeSheet = workbook.getSheetAt(INITIAL_VALUE);

			loadTableSheet(dataMap, datatypeSheet);
			
			
			loadFKList(dataMap, workbook);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataMap;
	}

	private static void loadFKList(Map<String, Object> dataMap, Workbook workbook) {
		Sheet foreignSheet = workbook.getSheetAt(SERIAL_NO_COLUMN);
		Row tableNameRow=foreignSheet.getRow(TABLE_NAME_ROW);
		Cell totalColumnsCell=tableNameRow.getCell(4);
		int totalColumns=getCellData(totalColumnsCell)==null?0:((Double)getCellData(totalColumnsCell)).intValue();
//		System.out.println(totalColumns);
		List<ForiegnKeyEntity> metaDataEntities=new LinkedList<>();
		if(totalColumns!=0) {

		for (int row = TABLE_DATA_ROW; row < totalColumns+TABLE_DATA_ROW; row++) {
			ForiegnKeyEntity foriegnKeyEntity=new ForiegnKeyEntity();
			Row currentRow = foreignSheet.getRow(row);


			Cell columnNameCell=currentRow.getCell(SERIAL_NO_COLUMN+1);
			if(Objects.nonNull(columnNameCell)) {
			String columnName=getCellData(columnNameCell).toString();
			foriegnKeyEntity.setColumnName(columnName);
			}
//			System.out.print(columnName+" | ");
			
			Cell dataTypeCell=currentRow.getCell(SERIAL_NO_COLUMN+2);
			if(Objects.nonNull(dataTypeCell)) {
			String dataType=getCellData(dataTypeCell).toString();
			foriegnKeyEntity.setDataType(DataType.getDataType(dataType));
			}
//			System.out.print(DataType.getDataType(dataType)+" | ");
			
			Cell keyTypeCell=currentRow.getCell(SERIAL_NO_COLUMN+3);
			if(Objects.nonNull(keyTypeCell)) {
			boolean isprimary=false;
			if(Objects.nonNull(getCellData(keyTypeCell))){
				isprimary=Boolean.valueOf(getCellData(keyTypeCell).toString());
			}
			foriegnKeyEntity.setPrimary(isprimary);
			}
//			System.out.print(isprimary+" | ");
			
			Cell tableNameCell=currentRow.getCell(SERIAL_NO_COLUMN+4);
			if(Objects.nonNull(tableNameCell)) {
			String tableName=getCellData(tableNameCell).toString();
			foriegnKeyEntity.setTableName(tableName);
			}
			
			Cell colNameCell=currentRow.getCell(SERIAL_NO_COLUMN+5);
			if(Objects.nonNull(colNameCell)) {
			String refCol=getCellData(colNameCell).toString();
			foriegnKeyEntity.setReferenceCol(refCol);
			}
//			System.out.println(tableName+" | ");
			
			metaDataEntities.add(foriegnKeyEntity);
		}
		}
		dataMap.put("fkeyList", metaDataEntities);
	}

	private static void loadTableSheet(Map<String, Object> dataMap, Sheet datatypeSheet) {
		Row tableNameRow=datatypeSheet.getRow(TABLE_NAME_ROW);
		Cell tableNameCell=tableNameRow.getCell(TABLE_NAME_COLUMN);

		dataMap.put("tableName", getCellData(tableNameCell));
		Cell totalColumnsCell=tableNameRow.getCell(TABLE_NAME_ROW);
		int totalColumns=((Double)getCellData(totalColumnsCell)).intValue();
//		System.out.println(totalColumns);

		List<MetaDataEntity> metaDataEntities=new LinkedList<>();

		for (int row = TABLE_DATA_ROW; row < totalColumns+TABLE_DATA_ROW; row++) {

			MetaDataEntity metaDataEntity=new MetaDataEntity();
			Row currentRow = datatypeSheet.getRow(row);


			Cell columnNameCell=currentRow.getCell(SERIAL_NO_COLUMN+1);
			if(Objects.nonNull(columnNameCell)) {
			String columnName=getCellData(columnNameCell).toString();
			metaDataEntity.setColumnName(columnName);
			}
//			System.out.print(columnName+" | ");

			Cell dataTypeCell=currentRow.getCell(SERIAL_NO_COLUMN+2);
			if(Objects.nonNull(dataTypeCell)) {
			String dataType=getCellData(dataTypeCell).toString();
			metaDataEntity.setDataType(DataType.getDataType(dataType));
			}
//			System.out.print(DataType.getDataType(dataType)+" | ");

			Cell keyTypeCell=currentRow.getCell(SERIAL_NO_COLUMN+3);
			boolean isprimary=false;
			if(Objects.nonNull(keyTypeCell)) {
			if(Objects.nonNull(getCellData(keyTypeCell))){
				isprimary=Boolean.valueOf(getCellData(keyTypeCell).toString());
			}
			metaDataEntity.setPrimary(isprimary);
			}
//			System.out.print(isprimary+" | ");

			Cell fkTypeCell=currentRow.getCell(SERIAL_NO_COLUMN+4);
			boolean isfk=false;
			if(Objects.nonNull(fkTypeCell)) {
			if(Objects.nonNull(getCellData(fkTypeCell))){
			isfk=Boolean.valueOf(getCellData(fkTypeCell).toString());
			}
			metaDataEntity.setForiegnKey(isfk);
			}
//			System.out.print(isfk+" | ");


			Cell elasticCell=currentRow.getCell(SERIAL_NO_COLUMN+5);
			boolean isElastic=false;
			if(Objects.nonNull(elasticCell)) {
			if(Objects.nonNull(getCellData(elasticCell))){
				isElastic =Boolean.valueOf(getCellData(elasticCell).toString());
			}
			metaDataEntity.setSearchable(isElastic);
			}
//			System.out.print(isElastic+" | ");

			Cell requiredCell=currentRow.getCell(SERIAL_NO_COLUMN+6);
			boolean isRequired=false;
			if(Objects.nonNull(requiredCell)) {
			if(Objects.nonNull(getCellData(requiredCell))){
				isRequired= Boolean.valueOf(getCellData(requiredCell).toString());
			}metaDataEntity.setRequired(isRequired);
			}
//			System.out.print(isRequired+" | ");

			Cell uniqueCell=currentRow.getCell(SERIAL_NO_COLUMN+7);
			boolean isUnique=false;
			if(Objects.nonNull(uniqueCell)) {
			if(Objects.nonNull(getCellData(uniqueCell))){
				isUnique=Boolean.valueOf(getCellData(uniqueCell).toString());
			}
			metaDataEntity.setUnique(isUnique);
			}
//			System.out.print(isUnique+" | ");
			
			Cell maxLengthCell=currentRow.getCell(SERIAL_NO_COLUMN+8);
			int maxLength=0;
			if(Objects.nonNull(maxLengthCell)) {
			if(Objects.nonNull(getCellData(maxLengthCell))){
				maxLength=((Double)getCellData(maxLengthCell)).intValue();
				metaDataEntity.setMaxLength(maxLength);
			}}
//			System.out.print(maxLength+" | ");
			
			Cell minLengthCell=currentRow.getCell(SERIAL_NO_COLUMN+9);
			int minLength=0;
			if(Objects.nonNull(minLengthCell)) {
			if(Objects.nonNull(getCellData(minLengthCell))){
				minLength=((Double)getCellData(minLengthCell)).intValue();
				metaDataEntity.setMinLength(minLength);
			}
			}
//			System.out.println(minLength+" | ");
			
			metaDataEntities.add(metaDataEntity);

		}
		dataMap.put("metatData", metaDataEntities);
	}

	private static int getSerialNumber(Row currentRow) {
		Cell serialNoColumnCell=currentRow.getCell(SERIAL_NO_COLUMN);
		int serialNo=((Double)getCellData(serialNoColumnCell)).intValue();
//		if(serialNo/10==0) {
//			System.out.print("|   "+serialNo+"  | ");
//		}else {
//			System.out.print("|  "+serialNo+"  | ");
//		}
		return serialNo;
	}

	private static Object getCellData(Cell currentCell) {
		try {
		if (currentCell.getCellTypeEnum() == CellType.STRING) {
			return currentCell.getStringCellValue();
		} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
			return currentCell.getNumericCellValue();
		}else if (currentCell.getCellTypeEnum() == CellType.BOOLEAN) {
			return currentCell.getBooleanCellValue();
		}else {
			return null;
		}
		}catch (Exception e) {
			System.out.println(currentCell.getRowIndex());
			System.out.println(currentCell.getColumnIndex());

		}
		return null;
	}



}
