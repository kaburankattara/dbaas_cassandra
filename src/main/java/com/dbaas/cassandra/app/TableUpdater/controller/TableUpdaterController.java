package com.dbaas.cassandra.app.TableUpdater.controller;

import static com.dbaas.cassandra.consts.UrlConsts.URL_KEY_SPACE_LIST;
import static com.dbaas.cassandra.consts.UrlConsts.URL_TABLE_UPDATER;
import static com.dbaas.cassandra.domain.kbn.KbnConsts.COLUMN_TYPE;
import static com.dbaas.cassandra.utils.HttpUtils.getReferer;
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

import com.dbaas.cassandra.app.TableUpdater.form.TableUpdaterForm;
import com.dbaas.cassandra.app.TableUpdater.service.TableUpdaterService;
import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.table.kbn.KbnDao;

@Controller
@RequestMapping(URL_TABLE_UPDATER)
public class  TableUpdaterController {

    private final TableUpdaterService updaterService;
    
    private final KbnDao kbnDao;

    @Autowired
    public TableUpdaterController(TableUpdaterService updaterService, KbnDao kbnDao){
        this.updaterService = updaterService;
        this.kbnDao = kbnDao;
    }
    
	/**
	 * メソッド呼出前処理
	 *
	 * @return form
	 */
	@ModelAttribute("form")
	private TableUpdaterForm setUpForm() {
		return new TableUpdaterForm();
	}

	@GetMapping()
	public String index(HttpServletRequest request, @ModelAttribute("form") TableUpdaterForm form, Model model) {
		form.init();
		model.addAttribute("TABLE_REGISTER_REFERER", getReferer(request));
		initModelForAlways(model);
		return "tableUpdater/tableUpdater";
	}

	@PostMapping("regist")
	public String regist(HttpServletRequest request, @AuthenticationPrincipal LoginUser user, @ModelAttribute("form") TableUpdaterForm form, Model model) {
		try {
			updaterService.registTable(user, form.getKeySpace(), form.toTable());
		} catch(Exception e) {
			
		}
		
		return createRedirectUri(URL_KEY_SPACE_LIST);
	}

	@PostMapping("/addColumn")
	public String addColumn(HttpServletRequest request, @ModelAttribute("form") TableUpdaterForm form, Model model) {
		initModelForAlways(model);
		form.addColumn();
		return "tableUpdater/tableUpdater";
	}

	@PostMapping("/deleteColumn")
	public String deleteColumn(@ModelAttribute("form") TableUpdaterForm form, @RequestParam("targetIndex") String targetIndex, Model model) {
		initModelForAlways(model);
		form.deleteColumn(targetIndex);
		return "tableUpdater/tableUpdater";
	}
	
	private void initModelForAlways(Model model) {
		model.addAttribute("columnTypes", kbnDao.findByTypeCd(COLUMN_TYPE));
	}
	
}
