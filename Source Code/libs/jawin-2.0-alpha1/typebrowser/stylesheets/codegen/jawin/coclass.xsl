<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="text" indent="yes"/>

<xsl:template match="/">
	<xsl:apply-templates select="coclass"/>
</xsl:template>

<xsl:template match="coclass">
package <xsl:value-of select="@packageName"/>;

import org.jawin.*;
import org.jawin.constants.*;
import org.jawin.marshal.*;
import org.jawin.io.*;
import java.io.*;

/**
 * Jawin generated code please do not edit
 *
 * CoClass Interface: <xsl:value-of select="@name"/>
 * Description: <xsl:value-of select="@description"/>
 * Help file:   <xsl:value-of select="@helpFile"/>
 *
 * @author jawin_gen
 */

public class <xsl:value-of select="@name"/>
{
	public static final GUID CLSID = new GUID("<xsl:value-of select="@guid"/>");
	
	<xsl:apply-templates select="function"/>
}
</xsl:template>

<xsl:template match="function">
	public void <xsl:value-of select="@name"/>()
	{

	}
</xsl:template>

</xsl:stylesheet>