/*
var queryInfo = {
    active: true,
    currentWindow: true
};*/

chrome.runtime.onMessage.addListener(
    function(request, sender, sendResponse) {
        // read `newIconPath` from request and read `tab.id` from sender
        //alert(request.newIconPath);
        chrome.browserAction.setIcon({
            path: request.newIconPath,
            tabId: sender.tab.id
        });
    }
);
