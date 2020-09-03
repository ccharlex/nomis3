<%-- 
    Document   : ReportOrganizationUnitParameter
    Created on : Oct 8, 2018, 6:24:04 AM
    Author     : smomoh
--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<table >
<tr><td colspan="4" align="center"> </td></tr>
<%--<logic:present name="siteSetupOu">
<tr><td colspan="4" align="center"><label style="color: blue">${level2Ouh.name}: ${siteSetupOu.name} </label></td></tr>
</logic:present>--%>
<tr>
<td align="right">${level2Ouh.name}</td>
<td colspan="3">&nbsp; 
    <html:select property="level2OuId" styleId="level2OuId" style="width:570px;" onchange="setActionName('cboList');forms[0].submit()" >
        <html:option value="select">select....</html:option>
        <logic:present name="allLevel2OuList">
            <logic:iterate name="allLevel2OuList" id="ou">
                <html:option value="${ou.uid}">${ou.name}</html:option>
            </logic:iterate>
        </logic:present>
    </html:select>
    </td>

</tr>
<tr>
<td align="right"><jsp:include page="LocalOrganizationName.jsp" /> </td>
<td colspan="3">&nbsp;
    <html:select property="cboId" styleId="cboId" style="width:570px;" onchange="setActionName('level3OuList');forms[0].submit()" >
        <html:option value="select">select....</html:option>
        <logic:present name="cboList">
            <logic:iterate name="cboList" id="cbo">
                <html:option value="${cbo.uniqueId}">${cbo.cboName}</html:option>
            </logic:iterate>
        </logic:present>
    </html:select>
    </td>

</tr>
<tr>
<td align="right"><logic:present name="level3Ouh">${level3Ouh.name}</logic:present> </td>
<td > &nbsp;
<html:select property="level3OuId" styleId="level3OuId" onchange="setActionName('level4OuList');forms[0].submit()" >
        <html:option value="select">select....</html:option>
        <logic:present name="level3OuListForReports">
            <logic:iterate name="level3OuListForReports" id="ou">
                <html:option value="${ou.uid}">${ou.name}</html:option>
            </logic:iterate>
        </logic:present>
    </html:select>
    </td>
    <td align="right"><label style="margin-left: 20px;"><logic:present name="level4Ouh">${level4Ouh.name}</logic:present> </label></td>
<td>
<html:select property="organizationUnitId" styleId="organizationUnitId" onchange="generateUniqueId()">
        <html:option value="select">select....</html:option>
        <logic:present name="level4OuListForReports">
            <logic:iterate name="level4OuListForReports" id="ou">
                <html:option value="${ou.uid}">${ou.name}</html:option>
            </logic:iterate>
        </logic:present>
    </html:select>
    </td>
</tr>
<tr>
<td align="right">Period </td>
<td colspan="4">&nbsp; <%--value="${reportStartDate}" value="${reportEndDate}" --%>
<html:text property="startDate" styleId="startDate" readonly="true"/>
    
    <label style="margin-left: 20px; margin-right: 20px;">To </label>
    <html:text property="endDate" styleId="endDate" readonly="true"/>
    </td>
</tr>
</table>
