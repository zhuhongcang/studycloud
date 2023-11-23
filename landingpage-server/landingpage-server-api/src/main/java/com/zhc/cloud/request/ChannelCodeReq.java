package com.zhc.cloud.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author zhuhongcang
 * @date 2023/11/23
 */
@Data
public class ChannelCodeReq {
    @NotNull(message = "渠道id不能为空")
    private Long channelId;

    @NotNull(message = "渠道Code不能为空")
    private Long channelCode;

    @NotEmpty(message = "渠道名称不能为空")
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

    /*private Date createTime;*/

    /*private Integer isHide;*/
}
