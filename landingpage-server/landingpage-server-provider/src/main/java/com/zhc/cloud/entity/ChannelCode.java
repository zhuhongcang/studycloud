package com.zhc.cloud.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Data
@TableName("channel_code")
public class ChannelCode {

    @ExcelProperty("渠道id")
    @TableId(value = "channel_id", type = IdType.AUTO)
    private Long channelId;

    @ExcelProperty("渠道Code")
    @TableField("channel_code")
    private Long channelCode;

    @ExcelProperty("渠道名称")
    @TableField("channel_name")
    private String channelName;

    @ExcelIgnore
    @TableField("source_type_id")
    private Long sourceTypeId;

    @ExcelIgnore
    @TableField("source_type_name")
    private String sourceTypeName;

    @ExcelIgnore
    @TableField("auid")
    private Long auid;

    @ExcelIgnore
    @TableField("aname")
    private String aname;

    @ExcelIgnore
    @TableField("utm_source")
    private String utm_source;

    @ExcelIgnore
    @TableField("utm_plan")
    private Long utm_plan;

    @ExcelIgnore
    @TableField("account")
    private String account;

    @ExcelIgnore
    @TableField("plan_name")
    private String planName;

    @ExcelIgnore
    @TableField("vst")
    private Long vst;

    @ExcelIgnore
    @TableField("create_time")
    private Date createTime;

    @ExcelIgnore
    @TableField("is_hide")
    private Integer isHide;
}
