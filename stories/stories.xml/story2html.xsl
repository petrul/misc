<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
        <link href="../../style/main.css" rel="stylesheet" media="screen" />
        <link href="../../style/datatable.css" rel="stylesheet" media="screen" />
    </head>
    
    <body>
        <xsl:apply-templates select="story" />
    </body>
</html>
</xsl:template>

<xsl:template match="story">
    <div>
    <h2>Story #<xsl:value-of select="id"/> <!--(vdm:<xsl:value-of select="vdmid"/>)--></h2>
    
    <!-- 
    <i>Categories : <xsl:apply-templates select="categories/category" /></i>
        <div class="main-comment">
            <xsl:apply-templates select="comment" />
        </div>
    </div>

    <hr/>
    <b>Original :</b>
    <div class="original">
    <xsl:value-of select="original"/>
    </div>
    <div id="alternatives">
    <hr/>
    <b>Versions :</b>
        <ol><xsl:apply-templates select="alternatives/alt" /></ol>
     </div>
      -->
      <xsl:apply-templates select="insert-test" />
     </div>
</xsl:template>

<xsl:template match="insert-test">
    <q><xsl:apply-templates select="main" /></q>
    <hr/>
    <table>
        <thead>
            <th>option id</th>
            <th>option text</th>
        </thead>
        <tbody>
            <xsl:apply-templates select="alternatives/alt" />
        </tbody>
    </table>
</xsl:template>

<xsl:template match="main/insert">
    <span class="highlight">[...]</span>
</xsl:template>

<xsl:template match="alternatives/alt">
    <tr>
        <td><xsl:value-of select="@altid"/>.</td>
        <td><q><xsl:apply-templates /></q></td>
    </tr>
</xsl:template>

</xsl:stylesheet>
