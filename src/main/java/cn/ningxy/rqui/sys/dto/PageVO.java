package cn.ningxy.rqui.sys.dto;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页VO
 * <p>
 * 除了本身返回的数据之外，还包含的分页信息，例如总条数，当前页码等
 * </p>
 *
 * @author ningxy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageVO<T> implements Serializable {
    /**
     * 页码，从1开始
     */
    private Integer pageNum;
    /**
     * 页面大小
     */
    private Integer pageSize;
    /**
     * 起始行
     */
    private Long startRow;
    /**
     * 末行
     */
    private Long endRow;
    /**
     * 总数
     */
    private Long total;
    /**
     * 总页数
     */
    private Integer pages;
    /**
     * 数据
     */
    private List<T> items;

    public static <V, T> PageVO<T> from(PageInfo<V> pageInfo, List<T> pagedData) {
        return PageVO.<T>builder()
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .startRow(pageInfo.getStartRow())
                .endRow(pageInfo.getEndRow())
                .total(pageInfo.getTotal())
                .pages(pageInfo.getPages())
                .items(pagedData)
                .build();
    }
}
