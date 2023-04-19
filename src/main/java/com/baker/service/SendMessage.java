package com.baker.service;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.Common;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @ClassName: SendMessage
 * @Author: luodeng
 * @Date: 2023/4/18 22:43
 * @Version: 1.0
 */
@Component
public class SendMessage {

    @Value("${ali.accessKey_id}")
    private String accessKey_id;
    @Value("${ali.accessKey_secret}")
    private String accessKey_secret;
    @Value("${ali.signname}")
    private String signname;

    @Value("${ali.templatecode}")
    private String templatecode;


    public void DoSend(String phoneNumber,String data) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKey_id)
                .setAccessKeySecret(accessKey_secret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";

        Client client = new Client(config);
        SendSmsRequest smsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(signname)
                .setTemplateCode(templatecode)
                .setTemplateParam(data);
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(smsRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            System.out.println(Common.assertAsString(error.message));

        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error

            System.out.println(com.aliyun.teautil.Common.assertAsString(error.message));
        }
    }



}
