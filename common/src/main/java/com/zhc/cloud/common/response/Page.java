package com.zhc.cloud.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhuhongcang
 * @date 2023/4/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    public static final long DEFAULT_PAGE_SIZE = 10L;

    private PageInfo pageInfo;
    private List<T> list;

    @Data
    public static class PageInfo {
        // 总条数
        private long total = 0L;
        // 页码
        private long pageIndex = 1L;
        // 每页大小
        private long pageSize = DEFAULT_PAGE_SIZE;
        // 总页数
        private long totalPage = 0L;

        public PageInfo(long total, long pageIndex, long pageSize) {
            this.total = total;
            this.pageIndex = pageIndex;
            this.pageSize = pageSize;
        }

        public PageInfo(long total, long pageIndex, long pageSize, long totalPage) {
            this.total = total;
            this.pageIndex = pageIndex;
            this.pageSize = pageSize;
            this.totalPage = totalPage;
        }

        public long getPageIndex() {
            if (pageIndex < 1) {
                return 1;
            }
            return pageIndex;
        }

        public void setPageIndex(int pageIndex) {
            if (pageIndex < 1) {
                this.pageIndex = 1;
            } else {
                this.pageIndex = pageIndex;
            }
        }

        public long getTotalPage() {
            return this.total % this.pageSize == 0 ? this.total
                    / this.pageSize : (this.total / this.pageSize) + 1;
        }

    }
}
