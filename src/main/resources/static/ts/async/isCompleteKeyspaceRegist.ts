import { Ajax, AjaxForm } from "../common/ajax.js";

class IsCompleteKeyspaceRegist {
	execRequest(callBack: (msg: any) => void): void {
		Ajax.doAsyncGet("/isCompleteKeyspaceRegist", new AjaxForm(), callBack)
	}
};
class Form extends AjaxForm {

};

export { IsCompleteKeyspaceRegist };