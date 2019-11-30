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
globalSheet.getRange(3, 8).setValue();
```

### On edit

```javascript
function onEdit(e) {
  var currentRange = e.range;
  var currentSheet = currentRange.getSheet();
  // Column number = currentRange.getColumn()
}
```
