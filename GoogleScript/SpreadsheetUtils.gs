/* ****************************

Cell changed --> onCellChanged(e) function:
Better to create a trigger on script.google.com than using onEdit(e), more stable
--- var currentRange = e.range; (position starting at 1)
--- var currentSheet = currentRange.getSheet().getIndex(); (position starting at 1)
--- currentRange.getRow() & currentRange.getColumn() (position starting at 1)

*****************

new row submitted by google form --> onFormSubmitted(e) function:
Setup a trigger on script.google.com to link the script function with a form submission
--- "authMode": indicate if the user is logged in (= "FULL")
--- "namedValues": Fields name and the value in array (e.g. "Timestamp":["4/23/2020 13:18:43"])
--- "range": info about where is added in the spreadsheet (e.g. {"columnEnd":12, "columnStart":1, "rowEnd":134, "rowStart":134 })
--- ""source": {} (???)
---	"triggerUid":"3762234"
--- "values": array of all the values

****************************** */

var spreadSheet = SpreadsheetApp.getActiveSpreadsheet();

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

function getSheet(sheetIndex) {
  return spreadSheet.getSheets()[sheetIndex];
}

// return data array [row][column]
function getDataSheet(sheetIndex) {
  return getSheet(sheetIndex).getDataRange().getValues();
}

function writeInSheet(sheetIndex, rowIndex, columnIndex, value) {
  getSheet(sheetIndex).getRange(rowIndex+1, columnIndex+1).setValue(value);
}

// works with date format (e.g. "d MMM - hh:mm")
function setCellFormat(sheetIndex, rowIndex, columnIndex, format) {
  getSheet(sheetIndex).getRange(rowIndex+1, columnIndex+1).setNumberFormat(format);
}
