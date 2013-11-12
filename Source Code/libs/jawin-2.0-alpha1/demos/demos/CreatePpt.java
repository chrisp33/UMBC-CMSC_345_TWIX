/*
 * CreatePpt.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: CreatePpt.java,v 1.4 2004/07/18 15:00:04 arosii_moa Exp $ */

package demos;

import org.jawin.DispatchPtr;
import org.jawin.win32.Ole32;

/**
 * Creates a simple PowerPoint presentation
 *
 * @version     $Revision: 1.4 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class CreatePpt {

	public static void main(String[] args) {
		try {
			Ole32.CoInitialize();
			DispatchPtr app = new DispatchPtr("PowerPoint.Application");
			app.put("Visible", true);
			DispatchPtr preses = (DispatchPtr)app.get("Presentations");
			DispatchPtr pres = (DispatchPtr) preses.invoke("add", new Integer(-1));
			DispatchPtr slides = (DispatchPtr)pres.get("Slides");
			DispatchPtr slide = (DispatchPtr) slides.invoke("Add", new Integer(1), new Integer(2));
			DispatchPtr shapes = (DispatchPtr)slide.get("Shapes");
			DispatchPtr shape = (DispatchPtr) shapes.invoke("Item", new Integer(1));
			DispatchPtr frame = (DispatchPtr)shape.get("TextFrame");
			DispatchPtr range = (DispatchPtr)frame.get("TextRange");
			range.put("Text", "Use Jawin to call COM objects");
			Ole32.CoUninitialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
