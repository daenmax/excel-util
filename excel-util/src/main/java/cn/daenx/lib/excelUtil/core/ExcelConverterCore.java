package cn.daenx.lib.excelUtil.core;

import cn.daenx.lib.excelUtil.annotation.Dict;
import cn.daenx.lib.excelUtil.annotation.DictDetail;
import cn.daenx.lib.excelUtil.annotation.Masked;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * easyexcel导出导入字典转换和脱敏处理
 */
@Slf4j
public abstract class ExcelConverterCore implements Converter<Object> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Converter.super.supportJavaTypeKey();
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return Converter.super.supportExcelTypeKey();
    }

    /**
     * 导入时
     *
     * @param cellData
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Dict dict = contentProperty.getField().getAnnotation(Dict.class);
        if (dict != null) {
            String label = cellData.getStringValue();
            String value = transDictToValue(dict, label);
            if (ObjectUtil.isNotEmpty(value)) {
                return Convert.convert(contentProperty.getField().getType(), value);
            }
        }
        return Convert.convert(contentProperty.getField().getType(), cellData.getStringValue());
    }

    /**
     * 导出时
     *
     * @param value
     * @param contentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (ObjectUtil.isNull(value)) {
            return new WriteCellData<>("");
        }
        String valueStr = Convert.toStr(value);
        Dict dict = contentProperty.getField().getAnnotation(Dict.class);
        if (dict != null) {
            valueStr = transDictToLabel(dict, valueStr);
        }
        Masked masked = contentProperty.getField().getAnnotation(Masked.class);
        if (masked != null) {
            valueStr = masked(masked.type().getType(), valueStr);
        }
        if (ObjectUtil.isNotEmpty(valueStr)) {
            return new WriteCellData<>(valueStr);
        }
        return Converter.super.convertToExcelData(value, contentProperty, globalConfiguration);
    }

    /**
     * value转换到label
     *
     * @param annotation
     * @param value
     * @return
     */
    private String transDictToLabel(Dict annotation, String value) throws Exception {
        if (ObjectUtil.isNotEmpty(annotation.dictCode())) {
            //根据系统字典翻译
            return dictToLabel(annotation, value);
        } else {
            //根据自定义字典翻译
            DictDetail[] custom = annotation.custom();
            for (DictDetail dictDetail : custom) {
                if (dictDetail.value().equals(value)) {
                    return dictDetail.label();
                }
            }
        }
        return "";
    }

    /**
     * label转换到value
     *
     * @param annotation
     * @param label
     * @return
     */
    private String transDictToValue(Dict annotation, String label) throws Exception {
        if (ObjectUtil.isNotEmpty(annotation.dictCode())) {
            //根据系统字典翻译
            return dictToValue(annotation, label);
        } else {
            //根据自定义字典翻译
            DictDetail[] custom = annotation.custom();
            for (DictDetail dictDetail : custom) {
                if (dictDetail.label().equals(label)) {
                    return dictDetail.value();
                }
            }
        }
        return "";
    }

    /**
     * 自定义字典转换
     *
     * @param annotation
     * @param value
     * @return
     */
    public String dictToLabel(Dict annotation, String value) {
        return "";
    }

    /**
     * 自定义字典转换
     *
     * @param annotation
     * @param label
     * @return
     */
    public String dictToValue(Dict annotation, String label) {
        return "";
    }

    /**
     * 数据脱敏（中国）
     *
     * @param type  数据类型，0=姓名，1=手机号，2=身份证号码，3=银行卡号，4=电子邮箱，5=地址信息，6=IP地址
     * @param value 待脱敏的数据值
     * @return
     */
    public String masked(Integer type, String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        String maskedValue = value;
        switch (type) {
            case 0: // 姓名
                maskedValue = value.replaceAll("([\\u4e00-\\u9fa5])(.*)", "$1" + repeat("*", value.length() - 1));
                break;
            case 1: // 手机号
                maskedValue = value.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                break;
            case 2: // 身份证号码
                maskedValue = value.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1**********$2");
                break;
            case 3: // 银行卡号
                maskedValue = value.replaceAll("(\\d{4})\\d+(\\d{4})", "$1 **** **** $2");
                break;
            case 4: // 电子邮箱
                maskedValue = value.replaceAll("(\\w{1})\\w*@(.*)", "$1****@$2");
                break;
            case 5: // 地址信息
                maskedValue = value.replaceAll("([\\u4e00-\\u9fa5])(.*)", "$1" + repeat("*", value.length() - 1));
                break;
            case 6: // IP地址
                maskedValue = value.replaceAll("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})", "$1.$2.*.*");
                break;
            default:
                break;
        }
        return maskedValue;
    }

    private static String repeat(String str, int repeatCount) {
        String repeatedStr = "";
        for (int i = 0; i < repeatCount; i++) {
            repeatedStr += str;
        }
        return repeatedStr;
    }


}
