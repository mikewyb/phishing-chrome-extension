/**
 * Get the current URL.
 */
/*
function getCurrentTabUrl(callback) {
  // https://developer.chrome.com/extensions/tabs#method-query
  var queryInfo = {
    active: true,
    currentWindow: true
  };

  //Cannot Use chrome.*API in content_script
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
*/
function getCurrentTabUrl() {
  return window.location.toString();
}

function sendToServer(url) {
  var xhttp = new XMLHttpRequest();
  xhttp.open("POST", "http://sample-env-1.9pp955vp8c.us-west-2.elasticbeanstalk.com/VerifyRequest", true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  var data = { "URL" : url };
  xhttp.send(JSON.stringify(data));
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      manipulateResponse(this.responseText);
    }
  };
}

function manipulateResponse(responseText) {
  alert(responseText);
  var response = JSON.parse(responseText);
  var isSecurityProtocol = response.isSecurityProtocol;
  var isInBlackList = response.isInBlackList;
  var isInWhiteList = response.isInWhiteList;
  var result = response.result;

  //Create div
  var div = document.createElement("div");
  div.className = "modal";
  document.body.appendChild(div);

  switch (result) {
    case 'Safe':
      break;
    case 'Unsafe':
      break;
    case 'Suspicious':
      createDiv("Suspicious", "Yellow");
      break;
    case 'Dangerous':
      createDiv("Dangerous", "Red");
      break;
    case 'Unknown':
      createDiv("Unknown", "Yellow");
      break;
  }
  
}

function createDiv(type, color) {
  //Create div
  var div = document.createElement("div");
  div.className = "modal";
  document.body.appendChild(div);
}

//getCurrentTabUrl(sendToServer);

var currentURL = getCurrentTabUrl();
sendToServer(currentURL);

/*
chrome.runtime.onMessage.addListener(
  function(request, sender, sendResponse) {
    alert(request.requested);
    if (request.requested == "createDiv"){
      //Code to create the div
      var div = document.createElement("div");
      document.body.appendChild(div);
      sendResponse({confirmation: "Successfully created div"});
    }
  }
);
*/
