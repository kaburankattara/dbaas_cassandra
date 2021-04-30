package com.dbaas.cassandra.app.keyspaceRegister.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEYSPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_KEYSPACE_REGISTER;
import static com.dbaas.cassandra.domain.message.Message.MESSAGE_KEY_ERROR;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbaas.cassandra.app.keyspaceRegister.form.KeyspaceRegisterForm;
import com.dbaas.cassandra.app.keyspaceRegister.service.KeyspaceRegisterService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Controller
@RequestMapping(URL_KEYSPACE_REGISTER)
public class  KeyspaceRegisterController {

    private final KeyspaceRegisterService registerService;

    @Autowired
    public KeyspaceRegisterController(KeyspaceRegisterService registerService){
        this.registerService = registerService;
    }
	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private KeyspaceRegisterForm setUpForm() {
		return new KeyspaceRegisterForm();
	}
	
	@GetMapping()
	public String index(@ModelAttribute("form") KeyspaceRegisterForm form) {
		return "keyspaceRegister/keyspaceRegister";
	}

	@PostMapping("regist")
	public String regist(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeyspaceRegisterForm form, Model model) {
		
		// サーバ情報を取得する
		// キースペースが作成済みじゃないか判定
		try {
			boolean isRegist = registerService.registKeyspace(user,form.getKeyspaceName());

			if (!isRegist) {
				model.addAttribute(MESSAGE_KEY_ERROR, "入力されたキースペースは登録済です。");
			}
		} catch(Exception e) {
			// TODO 後で追記
		}

		return createRedirectUri(URL_KEYSPACE_LIST);
	}
	
}
