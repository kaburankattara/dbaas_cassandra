package com.dbaas.cassandra.app.keySpaceUpdater.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_UPDATER;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbaas.cassandra.app.keySpaceUpdater.form.KeySpaceUpdaterForm;
import com.dbaas.cassandra.app.keySpaceUpdater.service.KeySpaceUpdaterService;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.user.LoginUser;

@Controller
@RequestMapping(URL_KEY_SPACE_UPDATER)
public class  KeySpaceUpdaterController {

    private final KeySpaceUpdaterService updaterService;

    @Autowired
    public KeySpaceUpdaterController(KeySpaceUpdaterService updaterService){
        this.updaterService = updaterService;
    }
	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private KeySpaceUpdaterForm setUpForm() {
		return new KeySpaceUpdaterForm();
	}
	
	@GetMapping()
	public String index(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeySpaceUpdaterForm form, Model model) {
		//model.addAttribute("form", form);
		Tables tables = updaterService.findTable(user, form.getKeySpace());
		model.addAttribute("tableList", tables.getTableList());
		return "keySpaceUpdater/keySpaceUpdater";
	}

	@PostMapping("update")
	public String regist(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeySpaceUpdaterForm form) {
		// TODO 後で実装
		
		// サーバ情報を取得する
		// キースペースが作成済みじゃないか判定
		
		try {
			// updaterService.registKeySpace(user,form.getKeySpace());
		} catch(Exception e) {
			
		}
		return createRedirectUri(URL_KEY_SPACE_LIST);
	}

	@PostMapping("delete")
	public String delete(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeySpaceUpdaterForm form) {
		try {
			updaterService.deleteKeySpace(user, form.getKeySpace());
		} catch(Exception e) {
			
		}
		return createRedirectUri(URL_KEY_SPACE_LIST);
	}
	
	
	
}
