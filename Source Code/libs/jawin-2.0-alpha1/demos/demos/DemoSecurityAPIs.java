/*
 * DemoSecurityAPIs.java -
 *
 * This file is part of the Jawin Project: http://jawinproject.sourceforge.net/
 * 
 * Please consult the LICENSE file in the project root directory,
 * or at the project site before using this software.
 */

/* $Id: DemoSecurityAPIs.java,v 1.4 2004/07/02 18:45:00 arosii_moa Exp $ */

package demos;

import org.jawin.donated.win32.Security;
import org.jawin.donated.win32.SecurityConstants;
import org.jawin.donated.win32.W32Process;
import org.jawin.util.HexFormatter;

/**
 * Reads win32 process and user information using the Win32 API. 
 *
 * @version     $Revision: 1.4 $
 * @author      Stuart Halloway, http://www.relevancellc.com/halloway/weblog/
 */
public class DemoSecurityAPIs {

	public static void main(String[] args) throws Exception {
		int handle = W32Process.GetCurrentProcess();
		System.out.println("Process handle is " + handle);
		int token = Security.OpenProcessToken(handle, SecurityConstants.TOKEN_READ);
		System.out.println("Process token is " + token);
		byte[] tokinfo = Security.GetTokenInformation(token, SecurityConstants.TokenUser);
		System.out.println("TokenUser raw data is...");
		System.out.println(HexFormatter.convertBytesToString(tokinfo, 16, true));
		byte[] sid = Security.GetTokenUserSid(token);
		System.out.println("User SID is...");
		System.out.println(HexFormatter.convertBytesToString(sid, 16, true));
		byte[] sididauth = Security.GetSidIdentifierAuthority(sid);
		System.out.println("SID_IDENTIFIER_AUTHORITY raw data is...");
		System.out.println(HexFormatter.convertBytesToString(sididauth, 16, true));
		System.out.println("Your human-readable sid is " + Security.SidToString(sid));
	}
}
