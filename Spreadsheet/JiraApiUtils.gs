/* ###################################################################### 
##### FIELDS TO UPDATE */

var jiraBaseUrl = "https://<COMPANY>.atlassian.net/";

// * 1 - create an API token: https://id.atlassian.com/manage/api-tokens
// * 2 - generate a Base64 Auth using this command: echo -n <user@example.com>:api_token_string | base64
var apiKey = ""

/* ###################################################################### */

var ticketBaseUrl = jiraBaseUrl + "browse/"

var apiBaseUrl = jiraBaseUrl + "rest/api/2/";
var issueBulkApiUrl = apiBaseUrl + "issue/bulk";
var issueApiUrl = apiBaseUrl + "issue";
var userSearchUrl = apiBaseUrl + "user/search?username=";
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
    priorityName, sprintName,
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
  
  var reporterEmail = ticketData["reporterEmail"];
  
  var failedMessage = "";
  if (isStringEmpty(summary)) { failedMessage = failedMessage + "- summary is empty"; }
  if (isStringEmpty(reporterEmail)) { failedMessage = failedMessage + "- reporter email is empty";  } 
  
  if (!isStringEmpty(failedMessage)) {
    return { "success": "", "fail": failedMessage };
  }
  
  var description = ticketData["description"];
  var labelsText = ticketData["labelsText"];
  var parentName = ticketData["parentName"];
  var assigneeEmail = ticketData["assigneeEmail"];
  var priorityName = ticketData["priorityName"];
  var sprintName = ticketData["sprintName"];
  
  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  // Prepare data
  
  var fields = new Object();
  fields["project"] = { "key": jiraProjectCode };
  fields["issuetype"] = { "name": ticketData["ticketType"] };
  fields["summary"] = summary;
  
  if (!isStringEmpty(description)) {
    fields["description"] = description;
  }
  
  var reporterId = getUserIdByEmail(reporterEmail);
  if (isStringEmpty(reporterId)) {
    return { "success": "", "fail": "repoter email not found" };
  }
  fields["reporter"] = { "id": reporterId };
  
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
    var assigneeId = getUserIdByEmail(assigneeEmail);
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
  
  var customFields = fields["customFields"];
  if (customFields != undefined && Object.keys(customFields).length > 0) {
    for (var key in customFields) {
      fields[key] = customFields[key];
    }
  }

  // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
  // - - SEND API REQUEST
  
  var options = { "content-type": "application/json", "method": "POST",
                 "headers": headers,"payload": JSON.stringify({ "fields": fields }) };  
  var response = new Object();
  try {
    var dataResponse = UrlFetchApp.fetch(issueApiUrl, options);
    var dataAll = JSON.parse(dataResponse.getContentText());
    response["success"] = ticketBaseUrl + dataAll.key;
  } catch (err) {
    response["fail"] = err;
  }
  return response;
}

/* ###################################################################### */

var userIdEmailMap = new Object();
function getUserIdByEmail(email) {
  var url = userSearchUrl.concat(email);
  return getIssueIdByStoryName(email, url, userIdEmailMap, function(dataAll) {
    return dataAll[0].accountId;
  });
}

var issueIdNameMap = new Object();
function getIssueIdByStoryName(storyName) {
  var url = issueSearchUrl.concat(storyName);
  return getIssueIdByStoryName(storyName, url, issueIdNameMap, function(dataAll) {
   return dataAll.issues[0].id;
  });
}

function getIssueIdByStoryName(name, url, map, functionGetId) {
  if (isStringEmpty(name)) { return ""; }
  
  var savedId = map[name];
  if (!isStringEmpty(savedId)) {
    return savedId;
  }
  
  var options = { "content-type": "application/json", "method": "GET", "headers": headers };  
  try {
    var response = UrlFetchApp.fetch(url, options);
    var dataAll = JSON.parse(response.getContentText());
    var issueKey = functionGetId(dataAll);
    if (!isStringEmpty(issueKey)) {
      map[name] = issueKey;
      return issueKey;
    }
  } catch (err) { }
  return "";
}

function testGetIssueJira() {
  var issueId = getIssueIdByStoryName("TEC-107");
}
