package com.dbaas.cassandra.app.TableRegister.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_UPDATER;
import static com.dbaas.cassandra.consts.UrlConsts.URL_TABLE_REGISTER;
import static com.dbaas.cassandra.domain.kbn.KbnConsts.COLUMN_TYPE;
import static com.dbaas.cassandra.domain.message.Message.MESSAGE_KEY_WARNING;
import static com.dbaas.cassandra.domain.message.Message.MSG001W;
import static com.dbaas.cassandra.utils.HttpUtils.getReferer;
import static com.dbaas.cassandra.utils.StringUtils.isContains;
import static com.dbaas.cassandra.utils.StringUtils.isEquals;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import javax.servlet.http.HttpServletRequest;

import com.dbaas.cassandra.app.TableRegister.form.TableRegisterForm;
import com.dbaas.cassandra.app.TableRegister.service.TableRegisterService;
import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.table.kbn.KbnDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(URL_TABLE_REGISTER)
@SessionAttributes(names = { "TABLE_REGISTER_REFERER" })
public class TableRegisterController {

	private final TableRegisterService registerService;

	private final KbnDao kbnDao;

	private final MessageSourceService messageSource;

	@Autowired
	public TableRegisterController(TableRegisterService registerService, KbnDao kbnDao,
			MessageSourceService messageSource) {
		this.registerService = registerService;
		this.kbnDao = kbnDao;
		this.messageSource = messageSource;
	}

	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private TableRegisterForm setUpForm() {
		return new TableRegisterForm();
	}

	@GetMapping()
	public String index(HttpServletRequest request, @ModelAttribute("form") TableRegisterForm form, Model model) {
		form.init();
		model.addAttribute("TABLE_REGISTER_REFERER", getReferer(request));
		initModelForAlways(model);
		return "tableRegister/tableRegister";
	}

	@PostMapping("regist")
	public String regist(HttpServletRequest request, @AuthenticationPrincipal LoginUser user,
			@ModelAttribute("form") TableRegisterForm form, RedirectAttributes attributes, Model model) {

		// テーブルを登録
		try {
			registerService.registTable(user, form.getKeySpace(), form.toTable());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}

		// 登録したテーブルが取得可能かチェック
		// 取得できなければ、登録失敗かもしれないため、ワーニングメッセージを表示する
		Table findedTable = registerService.findTableByRetry(user, form.getKeySpace(), form.toTable());
		if (findedTable.isEmpty()) {
			attributes.addFlashAttribute(MESSAGE_KEY_WARNING, messageSource.getMessage(MSG001W));
		}

		// 遷移元の画面によって遷移先を分岐する
		String referer = (String) model.asMap().get("TABLE_REGISTER_REFERER");
		String url = isContains(URL_KEY_SPACE_LIST, referer) ? URL_KEY_SPACE_LIST : URL_KEY_SPACE_UPDATER;
		if (isEquals(url, URL_KEY_SPACE_UPDATER)) {
			attributes.addAttribute("keySpace", form.getKeySpace());
		}
		return createRedirectUri(url);
	}

	@PostMapping("/addColumn")
	public String addColumn(HttpServletRequest request, @ModelAttribute("form") TableRegisterForm form, Model model) {
		initModelForAlways(model);
		form.addColumn();
		return "tableRegister/tableRegister";
	}

	@PostMapping("/deleteColumn")
	public String deleteColumn(@ModelAttribute("form") TableRegisterForm form,
			@RequestParam("targetIndex") String targetIndex, Model model) {
		initModelForAlways(model);
		form.deleteColumn(targetIndex);
		return "tableRegister/tableRegister";
	}

	private void initModelForAlways(Model model) {
		model.addAttribute("columnTypes", kbnDao.findByTypeCd(COLUMN_TYPE));
	}

}
