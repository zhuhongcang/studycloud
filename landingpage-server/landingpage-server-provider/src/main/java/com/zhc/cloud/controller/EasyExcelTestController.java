package com.zhc.cloud.controller;

import com.zhc.cloud.service.EasyExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhuhongcang
 * @date 2024/3/28
 */
@Controller
@RequestMapping("/easyExcel")
@Slf4j
public class EasyExcelTestController {
    @Autowired
    private EasyExcelService easyExcelService;

    @GetMapping("/downloadFile")
    public void testGetById(HttpServletResponse response) {
        easyExcelService.downlowdFile(response);
    }

}
