package com.crowd.funding.manager.controller;

import com.crowd.funding.bean.Advert;
import com.crowd.funding.bean.Member;
import com.crowd.funding.manager.service.AdvertService;
import com.crowd.funding.manager.service.AuthAdvService;
import com.crowd.funding.utils.AjaxResultUtils;
import com.crowd.funding.utils.PageUtils;
import com.crowd.funding.utils.StringUtils;
import org.springframework.aop.Advisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authadv")
public class AuthAdvController {

    @Autowired
    private AuthAdvService authAdvService;

    // 跳转到广告审核页面的方法
    @RequestMapping("/index")
    public String index() {
        return "authadv/index";
    }

    // 跳转到显示广告信息的页面的方法
    @RequestMapping("/show")
    public String show(Integer id, Model model) {
        // 根据广告id查询广告
        Advert advert = authAdvService.queryById(id);
        model.addAttribute("advert", advert);

        return "authadv/show";
    }

    // 广告审批通过的方法
    @ResponseBody
    @RequestMapping("/pass")
    public Object pass(Integer id, String status) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {

            // 调用修改状态的方法
            int num = authAdvService.updateStatus(id,status);

            // 如果成功则返回true值
            resultUtils.setSuccess(num==1);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("修改数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }

    // 广告审批不通过的方法
    @ResponseBody
    @RequestMapping("/refuse")
    public Object refuse(Integer id, String status) {
        // 获取Ajax中传递的数据
        AjaxResultUtils resultUtils = new AjaxResultUtils();

        try {

            // 调用修改状态的方法
            int num = authAdvService.updateStatus(id,status);

            // 如果成功则返回true值
            resultUtils.setSuccess(num==1);

        } catch (Exception e) {
            // 如果失败则返回false
            resultUtils.setSuccess(false);
            // 将异常打印出来
            e.printStackTrace();
            // 提示信息
            resultUtils.setMessage("修改数据失败!");
        }
        // 将对象序列化为JSON字符串,以流的形式返回
        return resultUtils;
    }

    /**
     * 分页查询广告审核数据
     * @advert pageno
     * @advert pagesize
     * @return
     */
    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(String pagetext, Integer pageno, Integer pagesize) {
        AjaxResultUtils result = new AjaxResultUtils();

        try {
            // 查询广告数据
            Map<String, Object> advertMap = new HashMap<String, Object>();
            advertMap.put("pageno", pageno);
            advertMap.put("pagesize", pagesize);
            if ( StringUtils.isNotEmpty(pagetext) ) {
                pagetext = pagetext.replaceAll("%", "\\\\%");
            }
            advertMap.put("pagetext", pagetext);

            // 分页查询
            PageUtils<Advert> page = authAdvService.pageQuery(advertMap);
            result.setPages(page);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }
}
