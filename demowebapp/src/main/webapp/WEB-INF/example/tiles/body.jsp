<!-- src/main/webapp/example/tiles/body.jsp -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<h2>Hello ${who}</h2>


<table>
	<thead>
	<tr>
	<th>Character</th>
	<th>Corp</th>
	</tr>
	</thead>
	<tbody>
		<c:forEach items="${chars}" var="eachChar">
			<tr>
				<td><span title="Character-ID: ${eachChar.characterID}">${eachChar.name}</span></td>
				<td><span title="Corporation-ID: ${eachChar.corporationID}">${eachChar.corporationName}</span></td>

			</tr>
		</c:forEach>
	</tbody>
</table>

<table>
	<thead>
	<tr>
	<th>Id</th>
	<th>Name</th>
	</tr>
	</thead>
	<tbody>
		<c:forEach items="${users}" var="eachUser">
			<tr>
				<td><span title="User-ID: ${eachUser.id}">${eachUser.id}</span></td>
				<td><span title="User: ${eachUser.name}">${eachUser.name}</span></td>

			</tr>
		</c:forEach>
	</tbody>
</table>
