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

<xsl:apply-templates select="//library"/>

</body>
</html>
</xsl:template>

<xsl:template match="library">

<table width="100%" cellpadding="2" cellspacing="0" border="0">
<tr bgcolor="#C5DF76">
	<td class="heading" colspan="2" nowrap="nowrap">
		Library: <xsl:value-of select="@name"/>
	</td>
</tr>

<tr bgcolor="#F1F1F1">
	<td nowrap="nowrap">
		Library location
	</td>
	<td>
		<xsl:value-of select="@path"/>
	</td>
</tr>
<tr bgcolor="#F1F1F1">
	<td nowrap="nowrap">
		Java package
	</td>
	<td>
		<xsl:value-of select="@package"/>
	</td>
</tr>

<tr bgcolor="#F1F1F1">
	<td nowrap="nowrap">
		Export directory
	</td>
	<td>
		<xsl:value-of select="@export"/>
	</td>
</tr>

<tr bgcolor="#F1F1F1">
	<td nowrap="nowrap">
		Java encoding
	</td>
	<td>
		<xsl:value-of select="@encoding"/>
	</td>
</tr>

<xsl:call-template name="emptyRow"/>

</table>

</xsl:template>

<xsl:template name="emptyRow">
<tr>
	<td colspan="2">&#160;</td>
</tr>
</xsl:template>

</xsl:stylesheet>
