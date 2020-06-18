package com.dbaas.cassandra.app.TableRegister.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_TABLE_REGISTER;
import static com.dbaas.cassandra.domain.kbn.KbnConsts.COLUMN_TYPE;
import static com.dbaas.cassandra.utils.HttpUtils.getReferer;
import static com.dbaas.cassandra.utils.StringUtils.isContains;
import static com.dbaas.cassandra.utils.UriUtils.createRedirectUri;

import javax.servlet.http.HttpServletRequest;

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

import com.dbaas.cassandra.app.TableRegister.form.TableRegisterForm;
import com.dbaas.cassandra.app.TableRegister.service.TableRegisterService;
import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.table.kbn.KbnDao;

@Controller
@RequestMapping(URL_TABLE_REGISTER)
@SessionAttributes(names = {"TABLE_REGISTER_REFERER"})
public class  TableRegisterController {

    private final TableRegisterService registerService;
    
    private final KbnDao kbnDao;

    @Autowired
    public TableRegisterController(TableRegisterService registerService, KbnDao kbnDao){
        this.registerService = registerService;
        this.kbnDao = kbnDao;
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
	public String regist(HttpServletRequest request, @AuthenticationPrincipal LoginUser user, @ModelAttribute("form") TableRegisterForm form, Model model) {
		try {
			registerService.registTable(user, form.getKeySpace(), form.toTable());
		} catch(Exception e) {
			
		}
		
		// 遷移元の画面によって遷移先を分岐する
		String referer = (String) model.asMap().get("TABLE_REGISTER_REFERER");
		if (isContains(URL_KEY_SPACE_LIST, referer)) {
			// キースペース一覧へ遷移
			return createRedirectUri(URL_KEY_SPACE_LIST);
		}
		// テーブル一覧へ遷移
		return createRedirectUri(URL_KEY_SPACE_LIST);
	}

	@PostMapping("/addColumn")
	public String addColumn(HttpServletRequest request, @ModelAttribute("form") TableRegisterForm form, Model model) {
		initModelForAlways(model);
		form.addColumn();
		return "tableRegister/tableRegister";
	}

	@PostMapping("/deleteColumn")
	public String deleteColumn(@ModelAttribute("form") TableRegisterForm form, @RequestParam("targetIndex") String targetIndex, Model model) {
		initModelForAlways(model);
		form.deleteColumn(targetIndex);
		return "tableRegister/tableRegister";
	}
	
	private void initModelForAlways(Model model) {
		model.addAttribute("columnTypes", kbnDao.findByTypeCd(COLUMN_TYPE));
	}
	
}
