/*
 * ExcelProp.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: ExcelProp.java,v 1.1 2004/06/14 20:22:23 arosii_moa Exp $ */

package demos;

import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;

/**
 * Creates a simple Excel workbook. TODO - Should be revised as it uses
 * arg[0] for opening a local file?
 *
 * @version     $Revision: 1.1 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class ExcelProp {
	public static void main(String[] args) throws Exception {
		Ole32.CoInitialize();

		DispatchPtr app = new DispatchPtr("Excel.Application");
		app.put("Visible", -1);
		DispatchPtr books = (DispatchPtr)app.get("Workbooks");
		DispatchPtr book =
			(DispatchPtr) books.invoke(
				"Open",
				args[0] + "\\demos\\data\\propput.xls");
		DispatchPtr sheets = (DispatchPtr)book.get("Worksheets");
		DispatchPtr sheet = (DispatchPtr)sheets.get("Item", new Integer(1));

		DispatchPtr x =
			(DispatchPtr) sheet.getN("Range", new Object[] { "A2", "C3" });
		x.invoke("Select");

		DispatchPtr range = (DispatchPtr)sheet.get("Range", "A1:A3");

		System.out.println("Count=" + range.get("Count"));

		DispatchPtr range2 = (DispatchPtr)range.get("Item", new Integer(2));
		System.out.println("value=" + range2.get("Formula"));
		range.putN("Item", new Object[] { new Integer(1)}, 2, range2);

		Ole32.CoUninitialize();
	}

}
