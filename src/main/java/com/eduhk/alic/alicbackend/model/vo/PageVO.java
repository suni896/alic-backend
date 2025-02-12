package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/9 19:27
 */
@Data
public class PageVO<T> {
    /**
     * 每页条数
     */
    private int pageSize;
    /**
     * 页码
     */
    private int pageNum;

    /**
     * 总页数
     */
    private int pages;
    /**
     * 总条数
     */
    private int total;

    /**
     * 当前页的数据
     */
    private List<T> data;

    public PageVO(int pageSize, int pageNum, int total, List<T> data) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.total = total;
        this.data = data;
        this.pages = total / pageSize + (total % pageSize == 0 ? 0 : 1);
    }

    public PageVO() {
    }
}
