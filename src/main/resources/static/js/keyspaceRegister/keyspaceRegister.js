import { Request } from "../common/request.js";
$(function () {
    /** 遷移元であるキースペース一覧画面に遷移する **/
    $("#referrer").on("click", function () {
        var request = new Request();
        request.getSubmit("/keyspaceList");
    });
});
