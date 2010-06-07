<%-- src/main/webapp/example/example.jsp --%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
  <head><title>Example Page ${who} ${4/3}</title></head>
  <body>
    <table cellspacing="0" cellpadding="0" border="0">
      <tr>
        <td colspan="2">
          <tiles:insertDefinition name="head-tile" />
        </td>
      </tr>
      <tr>
        <td width="25%">
          <tiles:insertDefinition name="left-nav-tile" />
        </td>
        <td width="75%">
          <tiles:insertDefinition name="body-tile" />
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <tiles:insertDefinition name="foot-tile"/>
        </td>
      </tr>
    </table>
  </body>
</html>
