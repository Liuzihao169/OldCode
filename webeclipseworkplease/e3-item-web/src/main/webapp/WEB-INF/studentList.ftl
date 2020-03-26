<body>
<html>
	<table border='1'>
		<tr>
		
			<td>序号</td>
			<td>姓名</td>
			<td>年龄</td>
			<td>地址</td>
		</tr>
		<#list studentList as student>
			<#if student_index % 2 == 0>
				<tr bgcolor='red'>
			<#else>
					<tr bgcolor='green'>
			</#if>
				<td>${student_index}</td>
				<td>${student.name}</td>
				<td>${student.age}</td>
				<td>${student.address}</td>
			</tr>
		</#list>
	<table>
</html>
</body>