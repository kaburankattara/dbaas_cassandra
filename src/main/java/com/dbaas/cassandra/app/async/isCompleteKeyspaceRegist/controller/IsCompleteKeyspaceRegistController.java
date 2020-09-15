package com.dbaas.cassandra.app.async.isCompleteKeyspaceRegist.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_IS_COMPLETE_KEYSPACE_REGIST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbaas.cassandra.app.async.isCompleteKeyspaceRegist.service.IsCompleteKeyspaceRegistService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Controller
@RequestMapping(URL_IS_COMPLETE_KEYSPACE_REGIST)
public class  IsCompleteKeyspaceRegistController {

    private final IsCompleteKeyspaceRegistService isCompleteKeyspaceRegistService;
    
    @Autowired
    public IsCompleteKeyspaceRegistController(IsCompleteKeyspaceRegistService isCompleteKeyspaceRegistService){
        this.isCompleteKeyspaceRegistService = isCompleteKeyspaceRegistService;
    }

	@GetMapping()
	@ResponseBody
	public List<String> index(@AuthenticationPrincipal LoginUser user, Model model) {
		return isCompleteKeyspaceRegistService.isCompleteKeyspaceRegist(user);
	}
}
