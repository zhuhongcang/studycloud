package com.zhc.cloud.common.requeset;

import lombok.Data;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
@Data
public class PageQuery {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    private int pageSize = DEFAULT_PAGE_SIZE;

    private int pageIndex = 1;

    private String orderByField;

    private String orderByType = DESC;

    private String groupBy;

    private boolean needTotalCount = true;

    public int getPageIndex() {
        if (pageIndex < 1) {
            return 1;
        }
        return pageIndex;
    }

    public int getPageSize() {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }
}
