/**
 * Get the current URL.
 */
function getCurrentTabUrl(callback) {
  // https://developer.chrome.com/extensions/tabs#method-query
  var queryInfo = {
    active: true,
    currentWindow: true
  };

  chrome.tabs.query(queryInfo, function(tabs) {
    // A window can only have one active tab at a time, so the array consists of
    // exactly one tab.
    var tab = tabs[0];
    var url = tab.url;

    // tab.url is only available if the "activeTab" permission in manifest.json is declared.
    console.assert(typeof url == 'string', 'tab.url should be a string');
    callback(url);
  });
}
