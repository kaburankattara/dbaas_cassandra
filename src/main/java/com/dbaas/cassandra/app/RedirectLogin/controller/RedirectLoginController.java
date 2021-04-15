package com.dbaas.cassandra.app.RedirectLogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.dbaas.cassandra.app.RedirectLogin.form.RedirectLoginForm;
import com.dbaas.cassandra.consts.UrlConsts;

@Controller
public class RedirectLoginController {

	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private RedirectLoginForm setUpForm() {
		return new RedirectLoginForm();
	}
	
	@GetMapping(UrlConsts.URL_KEY_REDIRECT_LOGIN)
	public String index(@ModelAttribute("form") RedirectLoginForm form) {
		return "redirectLogin/redirectLogin";
	}
}
