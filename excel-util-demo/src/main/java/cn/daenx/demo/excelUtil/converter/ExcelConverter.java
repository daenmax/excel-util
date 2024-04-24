package cn.daenx.demo.excelUtil.converter;

import cn.daenx.lib.excelUtil.annotation.Dict;
import cn.daenx.lib.excelUtil.core.ExcelConverterCore;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class ExcelConverter extends ExcelConverterCore {
    /**
     * 字典转换：字典值转换到标签
     * 你可在该方法中查询你的redis、数据库或者其他方式进行翻译
     * <p>
     * 如果使用自定义字典进行翻译，那么可以不重写该方法
     *
     * @param dict
     * @param value
     * @return
     */
    @Override
    public String dictToLabel(Dict dict, String value) {
        if (dict.dictCode().equals("class_type")) {
            switch (value) {
                case "0":
                    return "理科班";
                case "1":
                    return "文科班";
                case "2":
                    return "艺术班";
                case "3":
                    return "体育班";
            }
        } else if (dict.dictCode().equals("student_sex")) {
            switch (value) {
                case "0":
                    return "女";
                case "1":
                    return "男";
            }
        } else if (dict.dictCode().equals("student_post")) {
            switch (value) {
                case "0":
                    return "班长";
                case "1":
                    return "副班长";
                case "2":
                    return "纪律委员";
                case "3":
                    return "其他";
            }
        }
        return "";
    }

    /**
     * 字典转换：字典标签转换到值
     * 你可在该方法中查询你的redis、数据库或者其他方式进行翻译
     * <p>
     * 如果使用自定义字典进行翻译，那么可以不重写该方法
     *
     * @param dict
     * @param label
     * @return
     */
    @Override
    public String dictToValue(Dict dict, String label) {
        if (dict.dictCode().equals("class_type")) {
            switch (label) {
                case "理科班":
                    return "0";
                case "文科班":
                    return "1";
                case "艺术班":
                    return "2";
                case "体育班":
                    return "3";
            }
        } else if (dict.dictCode().equals("student_sex")) {
            switch (label) {
                case "女":
                    return "0";
                case "男":
                    return "1";
            }
        } else if (dict.dictCode().equals("student_post")) {
            switch (label) {
                case "班长":
                    return "0";
                case "副班长":
                    return "1";
                case "纪律委员":
                    return "2";
                case "其他":
                    return "3";
            }
        }
        return "";
    }

    /**
     * 数据脱敏
     * 如果你不想使用内置的脱敏算法，那么可以通过重写该方法来实现使用自己的脱敏算法
     *
     * @param type  数据类型，@Masked的type参数
     * @param value 待脱敏的数据值
     * @return
     */
    @Override
    public String masked(Integer type, String value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        return "...";
    }


}
