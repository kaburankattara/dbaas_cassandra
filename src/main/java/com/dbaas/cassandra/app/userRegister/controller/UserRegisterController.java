package com.dbaas.cassandra.app.userRegister.controller;

import com.dbaas.cassandra.domain.user.dto.RegistUserResultDto;
import com.dbaas.cassandra.app.userRegister.form.UserRegisterForm;
import com.dbaas.cassandra.app.userRegister.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_REDIRECT_LOGIN;
import static com.dbaas.cassandra.consts.UrlConsts.URL_USER_REGISTER;
import static com.dbaas.cassandra.domain.message.Message.MESSAGE_KEY_ERROR;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

@Controller
@RequestMapping(URL_USER_REGISTER)
public class UserRegisterController {

	private final UserRegisterService registerService;

	@Autowired
	public UserRegisterController(UserRegisterService registerService) {
		this.registerService = registerService;
	}

	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private UserRegisterForm setUpForm() {
		return new UserRegisterForm();
	}

	@GetMapping()
	public String index() {
		return "userRegister/userRegister";
	}

	@PostMapping("regist")
	public String regist(@ModelAttribute("form") UserRegisterForm form, RedirectAttributes attributes, Model model) {

		// 引数で指定したユーザーを登録する
		RegistUserResultDto result = registerService.regist(form.createUser());

		// 登録の結果、エラーの場合はメッセージを設定して処理を終了する
		if (result.hasError()) {
			model.addAttribute(MESSAGE_KEY_ERROR, result.getValidateResult().getFirstErrorMessage());
			return "userRegister/userRegister";
		}

		// 登録が完了した場合、リダイレクトログインする
		attributes.addFlashAttribute("form", form.createRedirectLoginForm());
		return createRedirectUri(URL_KEY_REDIRECT_LOGIN);
	}
}
