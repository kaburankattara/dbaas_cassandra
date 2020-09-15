package com.dbaas.cassandra.app.keySpaceRegister.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_REGISTER;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbaas.cassandra.app.keySpaceRegister.form.KeySpaceRegisterForm;
import com.dbaas.cassandra.app.keySpaceRegister.service.KeySpaceRegisterService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Controller
@RequestMapping(URL_KEY_SPACE_REGISTER)
public class  KeySpaceRegisterController {

    private final KeySpaceRegisterService registerService;

    @Autowired
    public KeySpaceRegisterController(KeySpaceRegisterService registerService){
        this.registerService = registerService;
    }
	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private KeySpaceRegisterForm setUpForm() {
		return new KeySpaceRegisterForm();
	}
	
	@GetMapping()
	public String index(@ModelAttribute("form") KeySpaceRegisterForm form) {
		return "keySpaceRegister/keySpaceRegister";
	}

	@PostMapping("regist")
	public String regist(@AuthenticationPrincipal LoginUser user, @ModelAttribute("form") KeySpaceRegisterForm form) {
		
		// サーバ情報を取得する
		// キースペースが作成済みじゃないか判定
		
		try {
			registerService.registKeySpace(user,form.getKeySpaceName());
		} catch(Exception e) {
			
		}
		return createRedirectUri(URL_KEY_SPACE_LIST);
	}
	
}
