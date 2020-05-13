# Google Script

|  | Spreadsheet | Calendar | Jira |
| ----- | ----- | ----- | ----- |
| Utils file | [SpreadsheetUtils.gs](https://github.com/arnaud33200/dev-tool-box/blob/master/GoogleScript/SpreadsheetUtils.gs) | [CalendarUtils.gs](https://github.com/arnaud33200/dev-tool-box/blob/master/GoogleScript/CalendarUtils.gs) | [JiraUtils.gs](https://github.com/arnaud33200/dev-tool-box/blob/master/GoogleScript/JiraApiUtils.gs) |
| Doc Link | [SpreadsheetApp Doc](https://developers.google.com/apps-script/reference/spreadsheet/spreadsheet-app) | [CalendarApp Doc](https://developers.google.com/apps-script/reference/calendar/calendar-app) | |


### Other Utils

compare string regex:
```javascript
var result = RegExp(textToSearch).test(fullText);

// OR Index of
if (fullText.indexOf(textToSearch) > -1) {
```

send Mail
```
MailApp.sendEmail(destEmail, subject, body, {
  name: "<name>" // optional
})
```
