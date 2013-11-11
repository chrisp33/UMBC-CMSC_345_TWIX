/*
 * CreateWord.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: CreateWord.java,v 1.2 2004/06/14 20:14:43 arosii_moa Exp $ */

package demos;

import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;

/**
 * Creates a simple Word document 
 *
 * @version     $Revision: 1.2 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class CreateWord {

	public static void main(String[] args) {
		try {
			Ole32.CoInitialize();
			DispatchPtr app = new DispatchPtr("Word.Application");
			app.put("Visible", true);
			Ole32.CoUninitialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
