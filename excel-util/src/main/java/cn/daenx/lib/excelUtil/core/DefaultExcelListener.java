package cn.daenx.lib.excelUtil.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Excel 导入监听
 */
@Slf4j
public class DefaultExcelListener<T> extends AnalysisEventListener<T> implements ExcelListener<T> {

    /**
     * excelUtil 表头数据
     */
    private Map<Integer, String> headMap;

    /**
     * 导入回执
     */
    private ExcelResult<T> excelResult;

    public DefaultExcelListener() {
        this.excelResult = new DefautExcelResult<>();
    }

    /**
     * 处理异常
     *
     * @param exception ExcelDataConvertException
     * @param context   Excel 上下文
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        String errMsg = null;
        if (exception instanceof ExcelDataConvertException) {
            // 如果是某一个单元格的转换异常 能获取到具体行号
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            errMsg = StrUtil.format("第{}行-第{}列-表头{}: 解析异常<br/>",
                rowIndex + 1, columnIndex + 1, headMap.get(columnIndex));
            if (log.isDebugEnabled()) {
                log.error(errMsg);
            }
        }
        excelResult.getErrorList().add(errMsg);
        throw new ExcelAnalysisException(errMsg);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
        log.debug("解析到一条表头数据: {}", headMap.toString());
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        excelResult.getList().add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.debug("所有数据解析完成！");
    }

    @Override
    public ExcelResult<T> getExcelResult() {
        return excelResult;
    }

    /**
     * 拼接List<对象>中指定字段
     *
     * @param collection
     * @param function
     * @param flag       拼接符号
     * @param <E>
     * @return
     */
    public static <E> String join(Collection<E> collection, Function<E, String> function, String flag) {
        if (CollUtil.isEmpty(collection)) {
            return "";
        }
        return collection.stream().map(function).filter(Objects::nonNull).collect(Collectors.joining(flag));
    }
}
