package com.zhc.cloud.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.zhc.cloud.dao.ChannelCodeDao;
import com.zhc.cloud.entity.ChannelCode;
import com.zhc.cloud.service.EasyExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuhongcang
 * @date 2023/11/22
 */
@Slf4j
@Service
public class EasyExcelServiceImpl implements EasyExcelService {
    @Resource
    private ChannelCodeDao channelCodeDao;

    @Override
    public void downlowdFile(HttpServletResponse response) {
        try {
            // todo 有毛病
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            List<ChannelCode> channelCodeList = (List<ChannelCode>) getData();
            EasyExcel.write(response.getOutputStream(), ChannelCode.class).sheet("模板").doWrite(channelCodeList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<ChannelCode> getData() {
        // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
        LambdaQueryWrapper<ChannelCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ChannelCode::getChannelId);
        /*PageHelper.startPage(0, 20000, false);
        List<ChannelCode> channelCodeList = channelCodeDao.selectList(queryWrapper);*/
        List<Long> list = new ArrayList<>();
        list.add(3228L);
        list.add(3229L);
        list.add(3230L);
        list.add(3231L);
        List<ChannelCode> channelCodeList = channelCodeDao.selectBatchIds(list);
        return channelCodeList;
    }
}
