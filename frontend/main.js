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
  //xhttp.open("POST", "http://sample-env-1.9pp955vp8c.us-west-2.elasticbeanstalk.com/VerifyRequest", true);
  //xhttp.open("POST", "http://antiphishing-env.us-east-1.elasticbeanstalk.com/VerifyRequest", true);
  xhttp.open("POST", "https://antiphishing.yuhengzhan.com/VerifyRequest", true);
  
  xhttp.setRequestHeader("Content-Type", "application/json");
  var data = { "URL" : url };
  xhttp.send(JSON.stringify(data));
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      manipulateResponse(this.responseText);
    }
  };
}

function restore(backgroundDiv, modalDiv) {
  backgroundDiv.parentNode.removeChild(backgroundDiv);
  modalDiv.parentNode.removeChild(modalDiv);
  analyze_page();
}

function analyze_page() {
  var inputs = document.getElementsByTagName('input');

  for(var i = 0; i < inputs.length; i++) {
    if(inputs[i].type.toLowerCase() == 'password') {
      inputs[i].setAttribute("style", "background-color: red;");
      inputs[i].addEventListener("click", alertUser);
    }
  }
}

function alertUser() {
  alert('Your credential information may be sending to others!')
}

function manipulateResponse(responseText) {
  //alert(responseText);
  var response = JSON.parse(responseText);
  var isSecurityProtocol = response.isSecurityProtocol;
  var isInBlackList = response.isInBlackList;
  var isInWhiteList = response.isInWhiteList;
  var result = response.result;

  //alert(result)
  switch (result) {
    case 'Safe':
      chrome.runtime.sendMessage({ "newIconPath" : "/images/safe.png" });
      break;
    case 'Unsafe':
      alert("Unsafe");
      break;
    case 'Suspicious':
      chrome.runtime.sendMessage({ "newIconPath" : "/images/suspicious.png" });
      createDiv("Suspicious", "Yellow");
      break;
    case "Dangerous":
      //alert("Dangerous");
      chrome.runtime.sendMessage({ "newIconPath" : "/images/dangeous.png" });
      createDiv("Dangerous", "Red");
      break;
    case 'Unknown':
      createDiv("Unknown", "Yellow");
      break;
    default:
      alert("default");
  }
   
}

function createDiv(type, color) {
  /*
  HTML structure of the overlay modal
  <div class="modal">
    <div class="modal-header">
      <span class="close">&times;</span>
    </div>
    <div class="modal-body">
      <p>Some text in the Modal Body</p>
    </div>
    <div class="modal-footer">
    </div>
  </div>
  */
  var backgroundDiv = document.createElement("div");
  backgroundDiv.className = "overlay";

  var modalDiv = document.createElement("div");
  modalDiv.className = "myModal";

  var modal_headerDIV = document.createElement("div");
  modal_headerDIV.className = "myModal-header";

  var modal_bodyDIV = document.createElement("div");
  modal_bodyDIV.className = "myModal-body";

  var modal_footerDIV = document.createElement("div");
  modal_footerDIV.className = "myModal-footer";

  //modalDiv.id = "modal";
  var closeSpan = document.createElement("span");
  closeSpan.innerHTML = "&times;";
  closeSpan.id = "closeModal";
  closeSpan.addEventListener("click", function(){restore(backgroundDiv, modalDiv)}, false);

  var textP = document.createElement("p");
  textP.innerHTML = "This is a Dangerous website!";

  modal_headerDIV.appendChild(closeSpan);
  modalDiv.appendChild(modal_headerDIV);
  modal_bodyDIV.appendChild(textP);
  modalDiv.appendChild(modal_bodyDIV);
  modalDiv.appendChild(modal_footerDIV);
  document.body.appendChild(backgroundDiv);
  document.body.appendChild(modalDiv);
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
