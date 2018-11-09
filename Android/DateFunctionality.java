import android.annotation.SuppressLint;
import com.google.common.base.Strings;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@SuppressLint("SimpleDateFormat")
public class DateFunctionality {

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region METHOD TO IMPLEMENTS

    private static String getResourceString(int stringRes) {

        // TODO

        return "";
    }

    /**
     * TO COPY IN STRING RES

     <string name="am">AM</string>
     <string name="pm">PM</string>

     <string name="jan">Jan</string>
     <string name="feb">Feb</string>
     <string name="mar">Mar</string>
     <string name="apr">Apr</string>
     <string name="may">May</string>
     <string name="june">June</string>
     <string name="july">July</string>
     <string name="aug">Aug</string>
     <string name="sep">Sep</string>
     <string name="oct">Oct</string>
     <string name="nov">Nov</string>
     <string name="dec">Dec</string>

     <string name="sunday">Sunday</string>
     <string name="monday">Monday</string>
     <string name="tuesday">Tuesday</string>
     <string name="wednesday">Wednesday</string>
     <string name="thursday">Thursday</string>
     <string name="friday">Friday</string>
     <string name="saturday">Saturday</string>

     <string name="date_format_now">Now</string>
     <string name="date_format_today">Today</string>
     <string name="date_format_yesterday">Yesterday</string>

     */


// endregion

    // Format And Calendar

    private static Calendar getCalendar(Date date) {
        Calendar calendar = new GregorianCalendar(Calendar.getInstance().getTimeZone());
        calendar.setTime(date);
        return calendar;
    }

    private static SimpleDateFormat getDateFormatServer() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    public static long getTimeInUTC() {
        return System.currentTimeMillis();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // region Utils

    /**
     * Get date in string format (MM/DD/YYYY)
     * @param date Date
     * @return MM/DD/YYYY will be return format
     */

    private static String getDateStringInMonthDayYearFormate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(date);
    }

    /**
     * @param calendar Calendar
     * @return Dec 16th 2017 will be return format
     */

    private static String getDayAndMonthDateLetterFormat(Calendar calendar) {
        String monthName = getMonthShortName(calendar.get(Calendar.MONTH));
        String dayNumber = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return monthName + " " + dayNumber + ", " + year;
    }

    /**
     * @param month Month index in Int
     * @return Short name will return depend on month index
     */

    private static String getMonthShortName(int month) {
        if (month == 0) { return getResourceString(R.string.jan); }
        if (month == 1) { return getResourceString(R.string.feb); }
        if (month == 2) { return getResourceString(R.string.mar); }
        if (month == 3) { return getResourceString(R.string.apr); }
        if (month == 4) { return getResourceString(R.string.may); }
        if (month == 5) { return getResourceString(R.string.june); }
        if (month == 6) { return getResourceString(R.string.july); }
        if (month == 7) { return getResourceString(R.string.aug); }
        if (month == 8) { return getResourceString(R.string.sep); }
        if (month == 9) { return getResourceString(R.string.oct); }
        if (month == 10) { return getResourceString(R.string.nov); }
        if (month == 11) { return getResourceString(R.string.dec); }
        return getResourceString(R.string.jan);
    }

    /**
     * @param day Day index in Int
     * @return Get week day short name depend on day index
     */

    private static String getWeekShortName(int day) {
        if (day == 1) { return getResourceString(R.string.sunday); }
        if (day == 2) { return getResourceString(R.string.monday); }
        if (day == 3) { return getResourceString(R.string.tuesday); }
        if (day == 4) { return getResourceString(R.string.wednesday); }
        if (day == 5) { return getResourceString(R.string.thursday); }
        if (day == 6) { return getResourceString(R.string.friday); }
        if (day == 7) { return getResourceString(R.string.saturday); }
        return getResourceString(R.string.sunday);
    }

    // endregion

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // region TIME FORMAT

    private static String getFormattedNumberTimeFromInt(int time) {
        String formattedTime;
        if (time == 0) { formattedTime = "00"; }
        else if (time < 10) { formattedTime = "0" + time; }
        else { formattedTime = "" + time; }
        return formattedTime;
    }

    public static String getDisplayHourWithDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        return df.format(date.getTime());
    }

    private static String decodeTime(int hour, int minute) {
        String AMPM = getResourceString(R.string.am);
        String minuteString = "" + minute;
        int trueHour = hour;
        if (hour >= 12) {
            trueHour = hour - 12;
            AMPM = getResourceString(R.string.pm);
        }
        if (trueHour == 0) {
            trueHour = 12;
        }

        if (minute < 10) {
            minuteString = "0" + minute;
        }
        return trueHour + ":" + minuteString + " " + AMPM;
    }

    // endregion

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region DATE CONVERSION

    public static String getStringServerFormatFromDate(Date date) {
        if (date == null) {
            return "";
        }
        // "2017-01-31T15:15:15.000Z"
        return getDateFormatServer().format(date);
    }

    public static String getStringHeaderFormatFromDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(date);
    }

    public static Date getDateHeaderFromStringFormat(String dateString) {
        if (Strings.isNullOrEmpty(dateString)) {
            return null;
        }
        dateString += "T12:00:00Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateFromStringFormat(String dateString) {
        if (Strings.isNullOrEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString()));
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    // endregion

// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region DATE CHECK

    private static boolean checkDatesAreSameDate(Calendar fCalendar, Calendar currCalendar) {
        if (fCalendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {
            if (fCalendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)) {
                if (fCalendar.get(Calendar.DAY_OF_MONTH) == currCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkDatesAreYesterdayDate(Calendar fCalendar, Calendar currCalendar) {
        if (fCalendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {
            if (fCalendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)) {
                int dayfirstCalendar = fCalendar.get(Calendar.DAY_OF_MONTH);
                int daysecondCalendar = currCalendar.get(Calendar.DAY_OF_MONTH);
                if (dayfirstCalendar + 1 == daysecondCalendar) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkDatesAreFromSameWeek(Calendar newCalendar, Calendar currCalendar) {

        //TODO:
        // These comparision may create conflict at 31 Dec and 1 Jan
        // Need to Check more.
        int newWeek = newCalendar.get(Calendar.WEEK_OF_YEAR);
        int currentWeek = currCalendar.get(Calendar.WEEK_OF_YEAR);
        if (newWeek == currentWeek) {
            return true;
        }
        return false;
    }

    public static boolean checkNowIsNightTime() {
//        return true;
        Calendar calendar = getCalendar(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour <= 7) {
            return true;
        }
        return false;
    }

    public static boolean DatesAreSameMinute(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar calendar1 = getCalendar(date1);
        Calendar calendar2 = getCalendar(date2);

        boolean sameday = checkDatesAreSameDate(calendar1, calendar2);
        boolean sameMinute = (calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE));
        return sameday && sameMinute;
    }

    // endregion

    public static boolean checkDatesAreSameDate(String firstDate, String secondDate) {
        if (firstDate == null || firstDate.length() == 0 || secondDate == null || secondDate.length() == 0) {
            return false;
        }
        Date fDate = new Date(Long.parseLong(firstDate) * 1000);
        Calendar fCalendar = getCalendar(fDate);
        Date sDate = new Date(Long.parseLong(secondDate) * 1000);
        Calendar sCalendar = getCalendar(sDate);
        if (fCalendar.get(Calendar.YEAR) == sCalendar.get(Calendar.YEAR)) {
            if (fCalendar.get(Calendar.MONTH) == sCalendar.get(Calendar.MONTH)) {
                if (fCalendar.get(Calendar.DAY_OF_MONTH) == sCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        return isSameDay(cal1, cal2);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean checkDatesWithinFifteenMinutes(String firstDate, String secondDate) {
        long fDate = Long.parseLong(firstDate);
        long sDate = Long.parseLong(secondDate);
        if (Math.abs(fDate - sDate) < (15 * 60)) {
            return true;
        }
        return false;
    }

    public static String getDateFromEpoch(String epochTime) {
        Date date = new Date(Long.parseLong(epochTime) * 1000);
        Calendar calendar = getCalendar(date);
        return getDateFromCalendar(calendar);
    }

    public static String getTimeFromEpoch(String epochTime) {
        Date date = new Date(Long.parseLong(epochTime) * 1000);
        Calendar calendar = getCalendar(date);
        return getTimeFromCalendar(calendar);
    }

    private static String getTimeFromCalendar(Calendar calendar) {
        return decodeTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private static String getDateFromCalendar(Calendar calendar) {
        Date currDate = new Date(System.currentTimeMillis());
        Calendar currCalendar = getCalendar(currDate);
        if (calendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {
            if (calendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)) {
                if (calendar.get(Calendar.DAY_OF_MONTH) == currCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return getResourceString(R.string.date_format_today);
                }
            }
        }
        return getMonthShortName(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    public static String getSecondsFormatFromSeconds(int duration) {
        if (duration <= 0) {
            return "00:00";
        }

        int second = duration / 1000;
        int miliseconds = duration - (second * 1000);
        String secondToDisplay = getFormattedNumberTimeFromInt(second);
        return secondToDisplay + ":" + miliseconds;
    }

    public static String getMinutesFormatFromSeconds(int duration) {
        if (duration <= 0) {
            return "00:00";
        }

        int second = duration % 60;
        int minute = (duration / 60) % 60;
        int hour = duration / 3600;
        String secondToDisplay = getFormattedNumberTimeFromInt(second);
        String minuteToDisplay = getFormattedNumberTimeFromInt(minute);
        String hourToDisplay = getFormattedNumberTimeFromInt(hour);

        String formatedMinutedString = "";
        if (hour == 0) {
            formatedMinutedString = minuteToDisplay + ":" + secondToDisplay;
        } else {
            formatedMinutedString = hourToDisplay + ":" + minuteToDisplay + ":" + secondToDisplay;
        }
        return formatedMinutedString;
    }

    private long secondsElapsed(Date date) {
        return secondsElapsed(date.getTime());
    }

    public static long secondsElapsed(long timestamp) {
        return (System.currentTimeMillis() - timestamp) / 1000;
    }
}
