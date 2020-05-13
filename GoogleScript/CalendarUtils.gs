var calendarId = "<CALENDAR_ID>@group.calendar.google.com";

/** data = { startDate, endDate, name, color, description } **/
function createCalendarEvent(data) {
  var calendar = CalendarApp.getCalendarById(calendarId);
  var event = calendar.createEvent(data.name, data.startDate, data.endDate);
  if (data.color != null) {
    event.setColor(data.color);
  }
  if (data.description != null) {
    event.setDescription(data.description);
  }
  return event.getId();
}

function deleteCalendarEvent(eventId) {
  var calendar = CalendarApp.getCalendarById(calendarId);
  try {
    var event = calendar.getEventById(eventId);
    event.deleteEvent();
    return true;
  } catch(err) { 
    return false;
  }
}

/** eventInfo = { title, description } **/
function updateCalendarEventInfo(eventId, eventInfo) {
  var calendar = CalendarApp.getCalendarById(calendarId);
  try {
    var event = calendar.getEventById(eventId);
    if (eventInfo.title != null) {
      event.setTitle(eventInfo.title);
    }
    if (eventInfo.description != null) {
      event.setDescription(eventInfo.description);
    }
    return true;
  } catch(err) { 
    return false;
  }
}

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

function isValidDate(date) {
  return (date != null && date != undefined 
          && typeof date.getMonth === 'function');
}

function isTimeSameOfBefore(date, toThisDate) {
  return (isValidDate(date) && isValidDate(toThisDate)
  && date.getTime() <= toThisDate.getTime());
}

function isTimeBefore(date, toThisDate) {
  return (isValidDate(date) && isValidDate(toThisDate)
  && date.getTime() < toThisDate.getTime());
}

function addHoursCalendar(cal, h) {
  cal.setTime(cal.getTime() + (h*60*60*1000));
  return cal;
}

function copyCalendar(cal) {
  var newCal = new Date();
  newCal.setTime(cal.getTime());
  return newCal;
}
