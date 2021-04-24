import { Ajax, AjaxForm } from "../common/ajax.js";

class GetEndPoint {
	execRequest(callBack: (msg: any) => void): void {
		Ajax.doAsyncGet("/getEndPoint", new AjaxForm(), callBack)
	}
};
class Form extends AjaxForm {

};

export { GetEndPoint };