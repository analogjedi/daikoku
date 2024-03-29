package com.primateer.daikoku;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.primateer.daikoku.db.DBController;
import com.primateer.daikoku.model.ValueObject;

public class Helper {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.US);
	public static final Pattern DATE_PATTERN = Pattern
			.compile("(\\d\\d\\d\\d)-(\\d\\d)-(\\d\\d)");

	public static String loadAsset(String filename) throws IOException {
		InputStream input = Application.getInstance().getAssets()
				.open(filename);
		byte[] buffer = new byte[input.available()];
		input.read(buffer);
		input.close();
		return new String(buffer);
	}

	public static Date parseDate(String dateString) {
		Matcher m = DATE_PATTERN.matcher(dateString.trim());
		if (!m.matches()) {
			throw new IllegalArgumentException("\"" + dateString
					+ "\" must be in YYYY-MM-DD format.");
		}
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)) - 1,
				Integer.parseInt(m.group(3)));
		return c.getTime();
	}

	public static Date addDays(Date base, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(base);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}
		return toString(date1).equals(toString(date2));
	}

	public static String toString(Date date) {
		return DATE_FORMAT.format(date);
	}

	public static void logError(Object source, String msg) {
		String tag = source.getClass().getSimpleName();
		Log.e(tag, msg);
	}

	public static void logErrorStackTrace(Object source, Exception e, String msg) {
		String tag = source.getClass().getSimpleName();
		Log.e(tag, msg + ": " + e.getClass().getSimpleName() + ":");
		Log.e(tag, Log.getStackTraceString(e));
	}

	public static void toast(String msg) {
		Toast.makeText(Application.getContext(), (CharSequence) msg,
				Toast.LENGTH_SHORT).show();
	}

	public static void displayErrorMessage(Context context, String title,
			String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		// builder.setCancelable(false);
		builder.setNeutralButton(R.string.ok, null);
		builder.create().show();
	}

	public static boolean isEmpty(String string) {
		return string == null || string.length() < 1;
	}

	public static void executeUponConfirmation(Context context, String title,
			String msg, final Runnable action) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		// builder.setCancelable(false);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						action.run();
					}
				});
		builder.create().show();
	}

	public static void deleteOnConfirmation(Context context,
			final ValueObject item) {
		Helper.executeUponConfirmation(context, context.getResources()
				.getString(R.string.dialog_confirm_delete_title), context
				.getResources().getString(R.string.dialog_confirm_delete_msg)
				+ " " + item.toString(), new Runnable() {
			@Override
			public void run() {
				DBController.getInstance().delete(item);
			}
		});
	}
}
