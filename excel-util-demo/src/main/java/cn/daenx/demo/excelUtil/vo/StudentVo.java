package cn.daenx.demo.excelUtil.vo;

import cn.daenx.demo.excelUtil.converter.ExcelConverter;
import cn.daenx.lib.excelUtil.annotation.Dict;
import cn.daenx.lib.excelUtil.annotation.DictDetail;
import cn.daenx.lib.excelUtil.annotation.Masked;
import cn.daenx.lib.excelUtil.enums.MaskedType;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
//导出时忽略没有@ExcelProperty的字段
@ExcelIgnoreUnannotated
public class StudentVo {
    private Integer line;

    @ExcelProperty(value = "学生姓名", converter = ExcelConverter.class)
    @Masked(type = MaskedType.NAME)
    private String studentName;

    @ExcelProperty(value = "学生年龄")
    private String studentAge;

    @ExcelProperty(value = "学生性别", converter = ExcelConverter.class)
    //使用自定义字典进行翻译，意思是直接写死在代码里的
    @Dict(custom = {@DictDetail(value = "0", label = "女"), @DictDetail(value = "1", label = "男")})
    //使用重写方法进行翻译，例如通过redis
//    @Dict(dictCode = "student_sex", custom = {})
    private String studentSex;

    @ExcelProperty(value = "学生职位", converter = ExcelConverter.class)
    //使用自定义字典进行翻译，意思是直接写死在代码里的
//    @Dict(custom = {@DictDetail(value = "0", label = "班长"), @DictDetail(value = "1", label = "副班长"), @DictDetail(value = "2", label = "纪律委员"), @DictDetail(value = "3", label = "其他")})
    //使用重写方法进行翻译，例如通过redis
    @Dict(dictCode = "student_post", custom = {})
    private String studentPost;

    @ExcelProperty(value = "备注")
    private String remark;
}
