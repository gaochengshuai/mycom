package com.gaocs.utils.EasyExecl;

import com.alibaba.druid.util.lang.Consumer;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils.EasyExecl
 * @Description:
 * @time:2021/11/24 23:21
 */
public class BaseDataListener<T> extends AnalysisEventListener<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseDataListener.class);
    /** 默认行数 */
    private static int BATCH_COUNT = 3000;
    private List<T> list;
    private Consumer <List<T>> consumer;
    private Predicate<T> predicate;
    /** 保存错误行数及数据  */
    private List<RowData> errorList;

    /**
     * 读取每一条数据进行predicate操作，
     * 读取到count行数进行consumer操作
     *
     * @param count     读取的行数
     * @param predicate 读取一条数据执行的方法。例：校验数据规则
     * @param consumer  读取规定行数后执行的方法
     */
    public BaseDataListener(int count, Predicate<T> predicate, Consumer<List<T>> consumer) {
        BATCH_COUNT = count;
        this.consumer = consumer == null ? ts -> {} : consumer;
        this.predicate = predicate == null ? t -> true : predicate;
        list = new ArrayList<>(BATCH_COUNT);
    }


    /**
     * 每一条数据解析时操作
     *
     * @param data    已经转换成实体的excel数据
     * @param context EasyExcel上下文
     */
    @Override
    public void invoke(T data, AnalysisContext context) {

        logger.info("解析到一条数据:{}", JSON.toJSONString(data));
        //符合校验规则则添加
        if (!predicate.test(data)) {
            errorList.add(new RowData(
                    context.readRowHolder().getRowIndex()
                    , data));
            return;
        }
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            consumer.accept(list);
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (list.size() > 0) {
            consumer.accept(list);
        }
        logger.info("所有数据解析完成！");
    }

    /**
     * 获取转换异常
     *
     * @param exception ExcelDataConvertException
     * @param context   excel上下文
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合doAfterAllAnalysedHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            logger.error("第{}行，第{}列解析异常", excelDataConvertException.getRowIndex() + 1,
                    excelDataConvertException.getColumnIndex() + 1);
            errorList.add(new RowData(
                    context.readRowHolder().getRowIndex()
                    , context.readRowHolder().getCurrentRowAnalysisResult()));
        }
    }

}
