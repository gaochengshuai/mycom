package com.gaocs.utils.EasyExecl;

/**
 * @Author gaocs
 * @PackageName:com.gaocs.utils.EasyExecl
 * @Description:
 * @time:2021/11/24 23:20
 */
public class RowData {

    //行数
    private Integer rowIndex;

    //excel导入转换后实体类
    private Object data;

    public RowData(){}

    public RowData(Integer rowIndex, Object data){
        this.rowIndex = rowIndex;
        this.data = data;
    }

}
