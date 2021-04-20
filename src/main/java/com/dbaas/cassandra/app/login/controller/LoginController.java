package com.dbaas.cassandra.app.login.controller;

import com.dbaas.cassandra.app.login.form.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.dbaas.cassandra.domain.message.Message.MESSAGE_KEY_ERROR;

@Controller
public class LoginController {

	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private LoginForm setUpForm() {
		return new LoginForm();
	}
	
	@GetMapping("/login")
	public String index(@ModelAttribute("form") LoginForm form) {
		return "login/login";
	}

	/**
	 * ログインエラー時処理
	 * @param form Form
	 * @param model Model
	 * @return 遷移先画面
	 */
	@RequestMapping(path = "/loginError", method = RequestMethod.GET)
	public String loginFail(@ModelAttribute("form") LoginForm form, Model model) {
		model.addAttribute(MESSAGE_KEY_ERROR, "認証に失敗しました。ユーザIDまたはパスワードが間違っています。");
		return "login/login";
	}
}
