/* ###################################################################### 
##### FIELDS TO UPDATE */

// documentation: https://developer.atlassian.com/cloud/jira/platform/rest/v3/

var jiraBaseUrl = "https://<COMPANY>.atlassian.net/";

// * 1 - create an API token: https://id.atlassian.com/manage/api-tokens
// * 2 - generate a Base64 Auth using this command: echo -n <user@example.com>:api_token_string | base64

var apiKey = ""

/* ###################################################################### */

var ticketBaseUrl = jiraBaseUrl + "browse/"

var apiBaseUrl = jiraBaseUrl + "rest/api/2/";
var apiBaseUrlV3 = jiraBaseUrl + "rest/api/3/";

var issueBulkApiUrl = apiBaseUrl + "issue/bulk";
var issueApiUrl = apiBaseUrl + "issue";

// var userSearchUrl = apiBaseUrl + "user/search?username=";
var userSearchUrl = apiBaseUrlV3 + "user/assignable/multiProjectSearch?projectKeys=<PROJECT_KEY>&query=<EMAIL_ADDRESS>";

var issueSearchUrl = apiBaseUrl + "search?jql=id=";

var headers = { 
    "content-type": "application/json",
    "Accept": "application/json",
    "Authorization": "Basic " + apiKey
  };

/* ###################################################################### */

/* ticketData = {

    // ~~~~  mandatory fiels
    
	jiraProjectCode, ticketType, summary, 
	reporterEmail, description
    
    // ~~~~  optional fields
    
    labelsText, parentName, assigneeEmail, 
    priorityName,
    customFields = [ {key: value}, {key: value}, ... ]
} */

// return response object (["success"] and ["fail"])
function createJiraTicket(ticketData) {
  
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  // Check Required Fields
  
  var summary = ticketData["summary"];
  
  var jiraProjectCode = ticketData["jiraProjectCode"];
  if (isStringEmpty(jiraProjectCode)) {
    return { "success": "", "fail": !isStringEmpty(summary) ? "Project Code is required": "" };
  }
  
  var failedMessage = "";
  if (isStringEmpty(summary)) { failedMessage = failedMessage + "- summary is empty"; }
  
  var reporterEmail = ticketData["reporterEmail"];
//  if (isStringEmpty(reporterEmail)) { failedMessage = failedMessage + "- reporter email is empty";  } 
  
  if (!isStringEmpty(failedMessage)) {
    return { "success": "", "fail": failedMessage };
  }
  
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  // - - Generate fields 
  
  var fields = generateFieldsFromTicketdata(ticketData);
  
  if (!isStringEmpty(fields["fail"])) {
    return fields;
  }
  
  fields["project"] = { "key": jiraProjectCode };
  
  var options = { "content-type": "application/json", "method": "POST",
                 "headers": headers,"payload": JSON.stringify({ "fields": fields }) };  
  var response = new Object();
  try {
    var dataResponse = UrlFetchApp.fetch(issueApiUrl, options);
    var dataAll = JSON.parse(dataResponse.getContentText());
    response["ticketId"] = dataAll.key;
    response["ticketUrl"] = ticketBaseUrl + dataAll.key;
  } catch (err) {
    response["fail"] = err;
  }
  return response;
}

/* ticketData = {
    // ~~~~  mandatory fiels
 
	ticketId
    
    // ~~~~  optional fields
    
    ticketType, summary, reporterEmail, description, 
    labelsText, parentName, assigneeEmail, priorityName,
    customFields = [ {key: value}, {key: value}, ... ]
} */
function updateJiraTicket(ticketData) {
  
  var ticketId = ticketData["ticketId"];
  
  var fields = generateFieldsFromTicketdata(ticketData);
  
  if (!isStringEmpty(fields["fail"])) {
    return fields;
  }
  
  var jsonPayload = JSON.stringify({ "fields": fields });
  var options = { "content-type": "application/json", "method": "PUT",
                 "headers": headers,"payload": jsonPayload };  
  var response = new Object();
  try {
    var dataResponse = UrlFetchApp.fetch(issueApiUrl + "/" + ticketId, options);
    var responseCode = dataResponse.getResponseCode();
    var dataText = dataResponse.getContentText();
    if (responseCode == 204) {
      response["ticketId"] = ticketId;
      response["ticketUrl"] = ticketBaseUrl + ticketId;
    } else {
      response["fail"] = "failed, response code " + responseCode;
    }
  } catch (err) {
    response["fail"] = err;
  }
  return response;
}

function generateFieldsFromTicketdata(ticketData) {

  var issueType = ticketData["ticketType"];
  var summary = ticketData["summary"];
  var reporterEmail = ticketData["reporterEmail"];
  var jiraProjectCode = ticketData["jiraProjectCode"];
  var description = ticketData["description"];
  var labelsText = ticketData["labelsText"];
  var parentName = ticketData["parentName"];
  var assigneeEmail = ticketData["assigneeEmail"];
  var priorityName = ticketData["priorityName"];
  
  var fields = new Object();
  
  if (!isStringEmpty(issueType)) {
    fields["issuetype"] = { "name": issueType };
  }
  
  
  if (!isStringEmpty(summary)) {
  fields["summary"] = summary.replace(/(\r\n|\n|\r)/gm, "");
  }
  
  if (!isStringEmpty(description)) {
    fields["description"] = description;
  }
  
  if (!isStringEmpty(reporterEmail)) {
    var reporterId = getUserIdByEmail(reporterEmail, jiraProjectCode);
    if (isStringEmpty(reporterId)) {
      return { "success": "", "fail": "repoter email not found" };
    }
    fields["reporter"] = { "id": reporterId };
  }
  
  if (!isStringEmpty(labelsText)) {
    var labels = labelsText.replace(" ", "").split(",");
    fields["labels"] = labels;
  }
  
  if (!isStringEmpty(parentName)) {
    var parentId = getIssueIdByStoryName(parentName);
    if (isStringEmpty(parentId)) {
      return { "success": "", "fail": "parent issue not found" };
    }
    fields["parent"] = { "id": parentId };
  }
  
  if (!isStringEmpty(assigneeEmail)) {
    var assigneeId = getUserIdByEmail(assigneeEmail, jiraProjectCode);
    if (isStringEmpty(assigneeId)) {
      return { "success": "", "fail": "assignee email not found" };
    }
    fields["assignee"] = { "id": assigneeId };
  }
  
  if (!isStringEmpty(priorityName)) {
    fields["priority"] = { "name": priorityName };
  }
  
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  // -- CUSTOM FIELDS
  
  var customFields = ticketData["customFields"];
  if (customFields != undefined && Object.keys(customFields).length > 0) {
    for (var key in customFields) {
      fields[key] = customFields[key];
    }
  }
  
  return fields;
}

/* ###################################################################### */

var userIdEmailMap = new Object();
function getUserIdByEmail(email, projectKey) {
  var url = userSearchUrl.replace("<PROJECT_KEY>", projectKey).replace("<EMAIL_ADDRESS>", email);
  return fetchIdFromName(email, url, userIdEmailMap, function(dataAll) {
    return dataAll[0].accountId;
  });
}

var issueIdNameMap = new Object();
function getIssueIdByStoryName(storyName) {
  var url = issueSearchUrl.concat(storyName);
  return fetchIdFromName(storyName, url, issueIdNameMap, function(dataAll) {
   return dataAll.issues[0].id;
  });
}

function fetchIdFromName(name, url, keyNameMap, functionGetId) {
  if (isStringEmpty(name)) { return ""; }
  
  var savedId = keyNameMap[name];
  if (!isStringEmpty(savedId)) {
    return savedId;
  }
  
  var options = { "content-type": "application/json", "method": "GET", "headers": headers };  
  try {
    var response = UrlFetchApp.fetch(url, options);
    var dataAll = JSON.parse(response.getContentText());
    var issueKey = functionGetId(dataAll);
    if (!isStringEmpty(issueKey)) {
      keyNameMap[name] = issueKey;
      return issueKey;
    }
  } catch (err) { }
  return "";
}

function testGetIssueJira() {
  var issueId = getIssueIdByStoryName("TEC-107");
}

function isStringEmpty(value) {
  if (value != undefined && value.length > 0) { return false; } 
  else { return true; }
}
