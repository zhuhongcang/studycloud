package com.zhc.cloud.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhuhongcang
 * @date 2023/11/21
 */
@Data
public class UserResp implements Serializable {
    private Long channelId;

    private Long channelCode;

    private String channelName;

    private Long sourceTypeId;

    private String sourceTypeName;

    private Long auid;

    private String aname;

    private String utmSource;

    private Long utmPlan;

    private String account;

    private String planName;

    private Long vst;

    private Date createTime;

    private Integer isHide;
}
