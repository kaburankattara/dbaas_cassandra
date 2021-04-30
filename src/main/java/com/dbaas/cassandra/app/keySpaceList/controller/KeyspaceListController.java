package com.dbaas.cassandra.app.keyspaceList.controller;

import com.dbaas.cassandra.app.keyspaceList.dto.KeyspaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keyspaceList.form.keyspaceListForm;
import com.dbaas.cassandra.app.keyspaceList.service.KeyspaceListService;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/keyspaceList")
public class KeyspaceListController {

    private KeyspaceListService keyspaceListService;

    @Autowired
    public KeyspaceListController(KeyspaceListService keyspaceListService) {
        this.keyspaceListService = keyspaceListService;
    }

    /**
     * メソッド呼出前処理
     *
     * @return form
     */
    @ModelAttribute("form")
    private keyspaceListForm setUpForm() {
        return new keyspaceListForm();
    }


    @RequestMapping
    public String index(@AuthenticationPrincipal LoginUser user, Model model) {

        KeyspaceListInitServiceResultDto initResultDto = new KeyspaceListInitServiceResultDto();
        try {
            initResultDto = keyspaceListService.init(user);
            model.addAttribute("initResultDto", initResultDto);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
        }
        model.addAttribute("initResultDto", initResultDto);
        return "keyspaceList/keyspaceList";
    }
}
