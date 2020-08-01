var StringUtils = /** @class */ (function () {
    function StringUtils() {
    }
    StringUtils.toString = function (value) {
        return JSON.stringify(value.toString);
    };
    StringUtils.isEmpty = function (value) {
        return value == null || value == undefined || (this.instanceOf(value) && !value.length);
    };
    StringUtils.isNotEmpty = function (value) {
        return !this.isEmpty(value);
    };
    StringUtils.instanceOf = function (obj) {
        return obj != null && obj != undefined && obj.constructor == String;
    };
    StringUtils.false = "";
    return StringUtils;
}());
;
export { StringUtils };
