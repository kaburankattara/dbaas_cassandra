var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
import { Ajax, AjaxForm } from "../common/ajax.js";
var GetEndPoint = /** @class */ (function () {
    function GetEndPoint() {
    }
    GetEndPoint.prototype.execRequest = function (callBack) {
        Ajax.doAsyncGet("/getEndPoint", new AjaxForm(), callBack);
    };
    return GetEndPoint;
}());
;
var Form = /** @class */ (function (_super) {
    __extends(Form, _super);
    function Form() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    return Form;
}(AjaxForm));
;
export { GetEndPoint };
