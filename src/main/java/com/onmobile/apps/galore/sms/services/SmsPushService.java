package com.onmobile.apps.galore.sms.services;

import com.onmobile.apps.galore.sms.controller.dto.SmsPushRequest;

public interface SmsPushService {


    String sendSms(SmsPushRequest smsPushRequest) throws Exception;
    

}
