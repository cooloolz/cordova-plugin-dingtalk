# cordova-plugin-dingTalk

钉钉分享 Cordova 插件
a plugin for cordova in ios platform to share something to DingTalk

# Feature

分享文本、图片、网页链接到钉钉
Share text, image, web

# Install 安装命令

1. `cordova plugin add https://github.com/cooloolz/cordova-plugin-dingtalk.git --variable DINGTALK_APPID=YOUR_DingTalk_APPID`

# Usage

## Check if Dingtalk is installed 检验钉钉是否安装

```Javascript
Dingtalk.isInstalled(function (installed) {
    alert("DingTalk installed: " + (installed ? "Yes" : "No"));
}, function (reason) {
    alert("Failed: " + reason);
});
```

## Share Text 分享文本

```Javascript
var msgObj = {
    text: "测试分享纯文本"
}
Dingtalk.shareTextObject(successcallback, errorcallback, msgObj);
```

## Share Image 分享图片

```Javascript
var msgObj = {
    imageURL: "http://pic2.cxtuku.com/00/02/31/b945758fd74d.jpg"
}
Dingtalk.shareImageObject(successcallback, errorcallback, msgObj);
```

## Share Web 分享网页链接

```Javascript
var msgObj = {
    title: "测试分享纯Web",
    pageURL: "https://www.baidu.com/",
    thumbURL: "http://pic2.cxtuku.com/00/02/31/b945758fd74d.jpg",
    messageDescription: "这是这个网页的描述文字"
}
Dingtalk.shareWebObject(successcallback, errorcallback, msgObj);
```
