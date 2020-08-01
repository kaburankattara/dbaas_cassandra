var BlockUI = /** @class */ (function () {
    function BlockUI() {
        this.blockUI = function () {
            if ($.blockUI == undefined) {
                return;
            }
            $.blockUI({ message: '<p><img src="/img/loader.gif"  style="vertical-align:middle; width:15px; height:15px;" /> Please wait...</p>' });
        };
        this.unblockUI = function () {
            if ($.unblockUI == undefined) {
                return;
            }
            $.unblockUI();
        };
    }
    return BlockUI;
}());
;
$(function () {
    $(".clickLink").on("click", function () {
        var blockUI = new BlockUI();
        blockUI.blockUI;
    });
});
export { BlockUI };
