package cn.daenx.lib.excelUtil.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字典翻译注解
 * 导出导入时，与@ExcelProperty一起使用，可以实现翻译功能
 * 接口响应时，可以实现自动添加翻译对象
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {

    /**
     * 字典编码
     * 使用系统字典表
     */
    String dictCode() default "";

    /**
     * 使用自定义字典翻译
     */
    DictDetail[] custom();

}
