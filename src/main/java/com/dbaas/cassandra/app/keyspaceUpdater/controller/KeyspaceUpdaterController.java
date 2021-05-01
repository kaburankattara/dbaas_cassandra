package com.dbaas.cassandra.app.keyspaceUpdater.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEYSPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_KEYSPACE_UPDATER;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbaas.cassandra.app.keyspaceUpdater.form.KeyspaceUpdaterForm;
import com.dbaas.cassandra.app.keyspaceUpdater.service.KeyspaceUpdaterService;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.user.LoginUser;

@Controller
@RequestMapping(URL_KEYSPACE_UPDATER)
public class KeyspaceUpdaterController {

    private final KeyspaceUpdaterService updaterService;

    @Autowired
    public KeyspaceUpdaterController(KeyspaceUpdaterService updaterService){
        this.updaterService = updaterService;
    }
	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private KeyspaceUpdaterForm setUpForm() {
		return new KeyspaceUpdaterForm();
	}
	
	@GetMapping()
	public String index(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeyspaceUpdaterForm form, Model model) {
		//model.addAttribute("form", form);
		Tables tables = updaterService.findTable(user, form.getKeyspace());
		model.addAttribute("tableList", tables.getTableList());
		return "keyspaceUpdater/keyspaceUpdater";
	}

	@PostMapping("update")
	public String update(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeyspaceUpdaterForm form) {
		// TODO 後で実装
		
		// サーバ情報を取得する
		// キースペースが作成済みじゃないか判定
		try {
			// updaterService.registKeyspace(user,form.getKeyspace());
		} catch(Exception e) {
			
		}
		return createRedirectUri(URL_KEYSPACE_LIST);
	}

	@PostMapping("delete")
	public String delete(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeyspaceUpdaterForm form) {
		try {
			updaterService.deleteKeyspace(user, form.toKeyspace());
		} catch(Exception e) {
			
		}
		return createRedirectUri(URL_KEYSPACE_LIST);
	}

}
