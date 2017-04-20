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
    //alert(url);

    // tab.url is only available if the "activeTab" permission in manifest.json is declared.
    console.assert(typeof url == 'string', 'tab.url should be a string');
    callback(url);
  });
}

function sendToServer(url) {
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "http://sample-env-1.9pp955vp8c.us-west-2.elasticbeanstalk.com/VerifyRequest", true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  var data = { "URL" : url };
  xhttp.send(JSON.stringify(data));
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      alert(this.responseText);
    }
  };
}

getCurrentTabUrl(sendToServer);  

//var Btn = document.querySelector('.trigger');
//Btn.addEventListener('click', getCurrentTabUrl);
