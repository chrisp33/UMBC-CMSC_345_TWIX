<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="text" indent="yes"/>

<xsl:template match="/">
	<xsl:apply-templates select="record"/>
</xsl:template>

<xsl:template match="record">
package <xsl:value-of select="@packageName"/>;

/**
 * Jawin generated code please do not edit
 *
 * Record: <xsl:value-of select="@name"/>
 * Description: <xsl:value-of select="@description"/>
 * Help file:   <xsl:value-of select="@helpFile"/>
 *
 * @author jawin_gen
 */

public class <xsl:value-of select="@name"/>
{
	<xsl:apply-templates select="property"/>
}
</xsl:template>

<xsl:template match="property">
	public static final <xsl:value-of select="@javaString"/><xsl:text> </xsl:text><xsl:value-of select="@name"/><xsl:apply-templates select="variant"/>;</xsl:template>

<xsl:template match="variant">
<xsl:if test="string-length(@value) &gt; 0"> = <xsl:value-of select="@value"/></xsl:if>
</xsl:template>

</xsl:stylesheet>