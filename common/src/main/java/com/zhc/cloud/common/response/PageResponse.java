package com.zhc.cloud.common.response;

import java.util.List;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
public class PageResponse<T> extends Response<Page<T>> {
    public static <T> PageResponse<T> of(List<T> data, int totalCount, int pageIndex, int pageSize) {
        PageResponse<T> response = new PageResponse<>();
        Page.PageInfo pageInfo = new Page.PageInfo(totalCount, pageIndex, pageSize);
        response.setData(new Page<T>(pageInfo, data));
        return response;
    }
}
