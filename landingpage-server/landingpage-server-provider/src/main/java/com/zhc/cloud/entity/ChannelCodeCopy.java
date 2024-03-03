package com.zhc.cloud.entity;

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
@TableName("channel_code_copy")
public class ChannelCodeCopy {
    @TableId(value = "channel_id", type = IdType.AUTO)
    private Long channelId;

    @TableField("channel_code")
    private Long channelCode;

    @TableField("channel_name")
    private String channelName;

    @TableField("source_type_id")
    private Long sourceTypeId;

    @TableField("source_type_name")
    private String sourceTypeName;

    @TableField("auid")
    private Long auid;

    @TableField("aname")
    private String aname;

    @TableField("utm_source")
    private String utmSource;

    @TableField("utm_plan")
    private Long utmPlan;

    @TableField("account")
    private String account;

    @TableField("plan_name")
    private String planName;

    @TableField("vst")
    private Long vst;

    @TableField("create_time")
    private Date createTime;

    @TableField("is_hide")
    private Integer isHide;
}
