package com.dbaas.cassandra.app.async.getEndPoint.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_GET_END_POINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbaas.cassandra.app.async.getEndPoint.service.GetEndPointService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Controller
@RequestMapping(URL_GET_END_POINT)
public class GetEndPointController {

    private final GetEndPointService getEndPointService;
    
    @Autowired
    public GetEndPointController(GetEndPointService getEndPointService){
        this.getEndPointService = getEndPointService;
    }

	@GetMapping()
	@ResponseBody
	public String index(@AuthenticationPrincipal LoginUser user) {
		return getEndPointService.getEndPoint(user);
	}
}
