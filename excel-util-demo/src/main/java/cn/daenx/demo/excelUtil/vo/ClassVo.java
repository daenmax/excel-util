package cn.daenx.demo.excelUtil.vo;

import cn.daenx.demo.excelUtil.converter.ExcelConverter;
import cn.daenx.lib.excelUtil.annotation.Dict;
import cn.daenx.lib.excelUtil.annotation.DictDetail;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
//导出时忽略没有@ExcelProperty的字段
@ExcelIgnoreUnannotated
public class ClassVo {
    private Integer line;

    @ExcelProperty(value = "班级名称")
    private String className;

    @ExcelProperty(value = "班级人数")
    private String classNum;

    @ExcelProperty(value = "类型", converter = ExcelConverter.class)
    //使用自定义字典进行翻译，意思是直接写死在代码里的
    @Dict(custom = {@DictDetail(value = "0", label = "理科班"), @DictDetail(value = "1", label = "文科班"), @DictDetail(value = "2", label = "艺术班"), @DictDetail(value = "3", label = "体育班")})
    //使用重写方法进行翻译，例如通过redis
//    @Dict(dictCode = "class_type", custom = {})
    private String classType;

    @ExcelProperty(value = "备注")
    private String remark;
}
