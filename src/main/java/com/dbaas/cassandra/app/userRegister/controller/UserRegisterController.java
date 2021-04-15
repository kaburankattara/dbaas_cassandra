package com.dbaas.cassandra.app.userRegister.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_REDIRECT_LOGIN;
import static com.dbaas.cassandra.consts.UrlConsts.URL_USER_REGISTER;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbaas.cassandra.app.userRegister.dto.RegistUserResultDto;
import com.dbaas.cassandra.app.userRegister.form.UserRegisterForm;
import com.dbaas.cassandra.app.userRegister.service.UserRegisterService;
import com.dbaas.cassandra.domain.message.Message;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.table.kbn.KbnDao;

@Controller
@RequestMapping(URL_USER_REGISTER)
@SessionAttributes(names = { "TABLE_REGISTER_REFERER" })
public class UserRegisterController {

	private final UserRegisterService registerService;

	private final MessageSourceService messageSource;

	@Autowired
	public UserRegisterController(UserRegisterService registerService, KbnDao kbnDao,
								  MessageSourceService messageSource) {
		this.registerService = registerService;
		this.messageSource = messageSource;
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
	public String index(HttpServletRequest request, @ModelAttribute("form") UserRegisterForm form, Model model) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
		System.out.println("aaa(8):" + passwordEncoder.encode("aaa"));
		passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("aaa(def):" + passwordEncoder.encode("aaa"));
		passwordEncoder = new BCryptPasswordEncoder();
		System.out.println("aaa(10):" + passwordEncoder.encode("aaa"));

		return "userRegister/userRegister";
	}

	@PostMapping("regist")
	public String regist(@ModelAttribute("form") UserRegisterForm form, RedirectAttributes attributes, Model model) {
		RegistUserResultDto result = registerService.registUser(form.createUser());
		if (result.hasError()) {
			model.addAttribute(Message.MESSAGE_KEY_ERROR, result.getErrorMessage(messageSource));
			return "userRegister/userRegister";
		}

		// 登録が完了した場合、リダイレクトログインする
		attributes.addFlashAttribute("form", form.createRedirectLoginForm());
		return createRedirectUri(URL_KEY_REDIRECT_LOGIN);
	}
}
