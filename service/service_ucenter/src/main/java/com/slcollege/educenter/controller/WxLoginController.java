package com.slcollege.educenter.controller;

import com.google.gson.Gson;

import com.slcollege.commonutils.JwtUtils;
import com.slcollege.educenter.entity.UcenterMember;
import com.slcollege.educenter.service.UcenterMemberService;
import com.slcollege.educenter.utils.ConstantPropertiesUtils;
import com.slcollege.educenter.utils.HttpClientUtils;

import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.net.URLEncoder;
import java.util.HashMap;

@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
public class WxLoginController {
    @Autowired
    private UcenterMemberService memberService;

    // 2.扫描微信登录信息,添加数据
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            // 1.获取code值,临时票据,类似于验证码
            // 2.拿着code请求微信固定的地址,得到两个值access_token 和 open_id
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            // 3.拼接三个参数
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantPropertiesUtils.WX_OPEN_APP_ID,
                    ConstantPropertiesUtils.WX_OPEN_APP_SECRET,
                    code
            );
            // 4.请求这个拼接好的地址,得到这两个返回值access_token 和 open_id
            // 使用httpClient(不用浏览器也可以模仿浏览器的请求)发送请求,得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            // 从accessTokenInfo字符串里面获取两个值 access_token 和 open_id
            // 把accessTokenInfo字符串转换map集合,根据map里面获取key对应值
            // 使用json转换工具-Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String open_id = (String)mapAccessToken.get("openid");


            // 把微信扫描的用户信息添加的数据库里面
            // 判断数据表里面是否存在相同微信信息,根据open_id判断
            UcenterMember member = memberService.getOpenIdMember(open_id);
            if(member == null) { // 如果member为空,则没有相同的数据,进行添加
                // 使用access_token 和 openid, 再去请求微信提供的固定的地址,获取到扫描人的信息
                // 访问微信的资源服务器,获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                // 拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        open_id
                );
                // 发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                // System.out.println("userInfo" +userInfo);
                // 获取返回userInfo字符串信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                // 获取微信用户昵称
                String nickname = (String) userInfoMap.get("nickname");
                // 获取头像
                String headImgUrl = (String) userInfoMap.get("headimgurl");

                member = new UcenterMember();
                member.setOpenid(open_id);
                member.setNickname(nickname);
                member.setAvatar(headImgUrl);
                memberService.save(member);
            }

            // 使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            // 返回首页面
            return "redirect:http://localhost:3000?token=" + jwtToken;
        } catch (Exception e) {
            throw new SlCollegeException(20001,"登录失败");
        }


    }

    // 1.生成微信扫描的二维码
    @GetMapping("wxLogin")
    public String getWxCode() {
        // 固定地址,后面拼接参数——不建议使用
        // String url = "https://open.weixin.qq.com/connect/qrconnect?appid="+ ConstantPropertiesUtils.WX_OPEN_APP_ID+"&response_type=code";

        // 微信开放平台授权baseUrl,%s相当于?,占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        // 对redirect_url进行URLEncoder编码
        String redirect_url = ConstantPropertiesUtils.WX_OPEN_REDIRECT_URL;

        try {
            redirect_url = URLEncoder.encode(redirect_url, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置%s里面这些值
        String url = String.format(
                baseUrl,
                ConstantPropertiesUtils.WX_OPEN_APP_ID,
                ConstantPropertiesUtils.WX_OPEN_REDIRECT_URL,
                redirect_url,
                "atguigu"
        );

        // 重定向至请求微信地址
        return "redirect:"+url;
    }



}
