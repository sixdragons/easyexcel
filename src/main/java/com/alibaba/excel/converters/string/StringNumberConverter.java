package com.alibaba.excel.converters.string;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.excel.util.NumberUtils;

/**
 * String and number converter
 *
 * @author Jiaju Zhuang
 */
public class StringNumberConverter implements Converter<String> {

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        // If there are "DateTimeFormat", read as date
        if (contentProperty != null && contentProperty.getDateTimeFormatProperty() != null) {
            return DateUtils.format(
                HSSFDateUtil.getJavaDate(cellData.getDoubleValue(),
                    contentProperty.getDateTimeFormatProperty().getUse1904windowing(), null),
                contentProperty.getDateTimeFormatProperty().getFormat());
        }
        // If there are "NumberFormat", read as number
        if (contentProperty != null && contentProperty.getNumberFormatProperty() != null) {
            return NumberUtils.format(cellData.getDoubleValue(), contentProperty);
        }
        // Excel defines formatting
        if (cellData.getDataFormat() != null) {
            if (DateUtil.isADateFormat(cellData.getDataFormat(), cellData.getDataFormatString())) {
                return DateUtils.format(HSSFDateUtil.getJavaDate(cellData.getDoubleValue(),
                    globalConfiguration.getUse1904windowing(), null));
            } else {
                return NumberUtils.format(cellData.getDoubleValue(), contentProperty);
            }
        }
        // Default conversion number
        return NumberUtils.format(cellData.getDoubleValue(), contentProperty);
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        return new CellData(Double.valueOf(value));
    }
}
