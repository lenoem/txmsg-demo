package com.linewell.msgdemo.controller;

import com.linewell.msgdemo.common.Result;
import com.linewell.msgdemo.entity.ApasInfo;
import com.linewell.msgdemo.service.ApasInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class ApasInfoController {
    @Autowired
    private ApasInfoService apasInfoService;

    @GetMapping(value = "transaction/submitInfo")
    public String submitInfoTransaction(@RequestParam("id")Long id,@RequestParam("projid")String projid) {
        ApasInfo apasInfo = new ApasInfo();
        apasInfo.setId(id);
        apasInfo.setProjid(projid);
        apasInfo.setServiceName("测试事项");
        apasInfo.setHandstates("收件");
        // 可统一控制异常
        try {
            apasInfoService.sendTransactionSubmitInfo(apasInfo);
            return "提交成功";
        } catch (Exception e) {
            return "提交失败";
        }
    }

    @GetMapping(value = "sync/submitInfo")
    public Result submitInfo(@RequestParam("id")Long id,@RequestParam("projid")String projid) {
        ApasInfo apasInfo = new ApasInfo();
        apasInfo.setId(id);
        apasInfo.setProjid(projid);
        apasInfo.setServiceName("测试事项");
        apasInfo.setHandstates("收件");

        Result result = new Result();
        apasInfo = apasInfoService.syncMsgSubmitInfo(apasInfo);
        result.setMessage("保存成功");
        result.put("apasInfo",apasInfo);
        return result;
    }

    @GetMapping(value = "list")
    public String submitInfo() {
        List<ApasInfo> apasInfoList = apasInfoService.list();
        if(apasInfoList==null){
            return "无数据";
        }
        StringBuffer sb = new StringBuffer();
        for (ApasInfo apasInfo : apasInfoList) {
            sb.append("projid:"+ apasInfo.getProjid() + "<br><br>");
        }
        return sb.toString();
    }
}
