var exec = require("cordova/exec");

exports.isInstalled = function (success, error) {
    exec(success, error, "Dingtalk", "isDingTalkInstalled", []);
};
exports.shareTextObject = function (success, error, parameter) {
    exec(success, error, "Dingtalk", "shareTextObject", [parameter]);
};
exports.shareImageObject = function (success, error, parameter) {
    exec(success, error, "Dingtalk", "shareImageObject", [parameter]);
};
exports.shareWebObject = function (success, error, parameter) {
    exec(success, error, "Dingtalk", "shareWebObject", [parameter]);
};
exports.isDingTalkSupportApi = function (success, error) {
    exec(success, error, "Dingtalk", "isDingTalkSupportApi", []);
};
exports.appStoreUrlOfDingTalk = function (success, error) {
    exec(success, error, "Dingtalk", "appStoreUrlOfDingTalk", []);
};
