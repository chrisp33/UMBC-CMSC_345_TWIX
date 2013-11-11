<?xml version="1.0"?> 
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:xalan="http://xml.apache.org/xslt"
  >

<xsl:output method="html" indent="yes"
  xalan:omit-meta-tag="yes"
  />

<xsl:template match="/">
<html>
  <head>
    <style type="text/css">
      td { font-family: sans-serif; font-size: 12; }
      td.heading { font-size: 14; }
    </style>
  </head>
<body>

<xsl:apply-templates select="/interface"/>

</body>
</html>
</xsl:template>

<xsl:template match="interface">
<table width="100%" cellpadding="2" cellspacing="0" border="0">
<tr bgcolor="#C5DF76">
	<td class="heading" colspan="2">
		Interface: <xsl:value-of select="@name"/>
	</td>
</tr>
<tr bgcolor="#F1F1F1">
	<td>
		Description
	</td>
	<td>
		<xsl:value-of select="@description"/>
	</td>
</tr>
<tr bgcolor="#F1F1F1">
	<td>
		GUID
	</td>
	<td>
		<xsl:value-of select="@guid"/>
	</td>
</tr>
<tr bgcolor="#F1F1F1">
	<td>
		Help file
	</td>
	<td>
		<xsl:value-of select="@helpFile"/>
	</td>
</tr>
<xsl:call-template name="emptyRow"/>
<tr bgcolor="#C5DF76">
	<td class="heading" colspan="2">
		Functions
	</td>
</tr>
<xsl:apply-templates select="function"/>

</table>
</xsl:template>

<xsl:template match="function">
<tr bgcolor="#F1F1F1">
	<td colspan="2">
		&#160;<xsl:value-of select="return/@javaString"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>(<xsl:apply-templates select="parameter"/>)
	</td>
</tr>
</xsl:template>

<xsl:template match="parameter">
<xsl:choose>
	<xsl:when test="position() = 1"><xsl:value-of select="@name"/></xsl:when>
	<xsl:otherwise>,<xsl:text> </xsl:text><xsl:value-of select="@name"/></xsl:otherwise>
</xsl:choose>
</xsl:template>

<xsl:template name="emptyRow">
<tr>
	<td colspan="2">&#160;</td>
</tr>
</xsl:template>

</xsl:stylesheet>
