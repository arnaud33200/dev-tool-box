# Google Drive Spreadsheet Script

### Reading Spreadsheet

```javascript
var spreadSheet = SpreadsheetApp.getActiveSpreadsheet();  
var sheet = spreadSheet.getSheets()[1];
var data = dataSheet.getDataRange().getValues();
for (var i = 1; i < data.length; i++) {
  var status = data[i][0];
}
```

### Writting

```javascript
var spreadSheet = SpreadsheetApp.getActiveSpreadsheet();  
var sheet = spreadSheet.getSheets()[4];
// Row, Column - starting at 1
sheet.getRange(3, 8).setValue();
```

### On edit

```javascript
function onEdit(e) {
  var currentRange = e.range;
  var currentSheet = currentRange.getSheet();
  // Column number = currentRange.getColumn()
}
```


### Utils

compare string regex:
```javascript
var result = RegExp(textToSearch).test(fullText);

// OR Index of
if (fullText.indexOf(textToSearch) > -1) {
```

### Jira Integration

```javascript
// * 1 - create an API token: https://id.atlassian.com/manage/api-tokens
// * 2 - generate a Base64 Auth using this command: echo -n user@example.com:api_token_string | base64
var base64Auth = "<BASE_64_TOKEN>";
var jiraProjectCode = "<PROJECT_CODE"; // e.g. "ABC"

var headers = { 
  "content-type": "application/json",
  "Accept": "application/json",
  "Authorization": "Basic " + base64Auth
};

var issueSearchUrl = "https://<company>.atlassian.net/rest/api/2/issue";

function createJiraIssue(e) {
  var data = {
    "fields": {
      "project":{ "key": jiraProjectCode },
      "summary": summary,
      "labels": [label2, label1],
      "description": fullDescription,
      "issuetype": { "name": taskType },
      "assignee": { "id": assignee },
      "reporter": { "id": requesterId },
      "priority": { "name": priorityOption },
    }
  };
  
  // Json + API Request
  
  var payload = JSON.stringify(data);
  var options = { "content-type": "application/json", "method": "POST",
    "headers": headers,"payload": payload };  
  
  var response = UrlFetchApp.fetch(issueApiUrl, options);
  var dataAll = JSON.parse(response.getContentText());
  var issueKey = dataAll.key;
}
```
