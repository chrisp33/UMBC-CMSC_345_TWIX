/*
 * DemoEventLog.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: DemoEventLog.java,v 1.4 2004/07/02 18:45:00 arosii_moa Exp $ */

package demos;

import java.io.IOException;

import org.jawin.COMException;
import org.jawin.donated.win32.EVENTLOGRECORD;
import org.jawin.donated.win32.EventLog;
import org.jawin.donated.win32.EventLogConstants;
import org.jawin.donated.win32.io.Console;
import org.jawin.util.HexFormatter;

/**
 * NOTE: This demo modifies the eventlog.
 * <br><br>
 * Demo that read a number of entries from the Application Eventlog. Finally it
 * adds a new entry.
 * 
 * @version     $Revision: 1.4 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class DemoEventLog {

	public static void main(String[] args) throws IOException, COMException {
		//read from the event log
		int hRead = EventLog.OpenEventLog(null, "Application");
		int oldest = EventLog.GetOldestEventLogRecord(hRead);
		int count = EventLog.GetNumberOfEventLogRecords(hRead);
		System.out.println(
			"The application event log (handle "
				+ Integer.toHexString(hRead)
				+ ") has "
				+ count
				+ " records, "
				+ oldest
				+ " is the oldest.");
		byte[] event =
			EventLog.RawReadEventLog(
				hRead,
				EventLogConstants.EVENTLOG_FORWARDS_READ + EventLogConstants.EVENTLOG_SEQUENTIAL_READ,
				0);
		System.out.println("First Event in application log (raw)");
		System.out.println(HexFormatter.convertBytesToString(event));

		int max =
			Integer.parseInt(
				Console.prompt(
					"The demo will now display a number of entries from the application log."
						+ "\nNumber of entries to read? "));
		for (int n = 0; n < max; n++) {
			EVENTLOGRECORD rec =
				EventLog.ReadEventLog(
					hRead,
					EventLogConstants.EVENTLOG_FORWARDS_READ + EventLogConstants.EVENTLOG_SEQUENTIAL_READ,
					0);
			System.out.println("Event " + rec.recordNumber + "\n" + rec);
		}
		EventLog.CloseEventLog(hRead);

		//write an entry to the event log
		int handle = EventLog.RegisterEventSource(null, "Jawin");
		EventLog.ReportEvent(
			handle,
			EventLogConstants.EVENTLOG_SUCCESS,
			0,
			0,
			"Jawin demo ran successfully.");
		EventLog.DeregisterEventSource(handle);
	}

}
