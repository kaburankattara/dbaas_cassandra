package com.dbaas.cassandra.app.keySpaceList.controller;

import com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keySpaceList.service.KeySpaceListService;
import com.dbaas.cassandra.app.login.form.LoginForm;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/keySpaceList")
public class KeySpaceListController {

    private KeySpaceListService keySpaceListService;

    @Autowired
    public KeySpaceListController(KeySpaceListService keySpaceListService) {
        this.keySpaceListService = keySpaceListService;
    }

    /**
     * メソッド呼出前処理
     *
     * @return form
     */
    @ModelAttribute("form")
    private LoginForm setUpForm() {
        return new LoginForm();
    }

    @RequestMapping
    public String index(@AuthenticationPrincipal LoginUser user, Model model) {
        try {
            KeySpaceListInitServiceResultDto initResultDto = keySpaceListService.init(user);
            model.addAttribute("initResultDto", initResultDto);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
        }
        return "keySpaceList/keySpaceList";
    }
}
