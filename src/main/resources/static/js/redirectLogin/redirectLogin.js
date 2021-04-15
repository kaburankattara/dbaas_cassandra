import { Request } from "../common/request.js";
$(function () {
    /** 認証リクエストする **/
    new Request().postSubmit("/authenticate");
});
