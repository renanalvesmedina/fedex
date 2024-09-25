<%@ page import="java.io.*" %>

<html>
<head>
	<h1><%=System.getProperty("oracle.j2ee.home") + "/log/lms.log"%></h1>

<STYLE TYPE="text/css">
<!--
TD{font-family: Arial; font-size: 10pt;}
--->
</STYLE>

</head>
<body>

<table border="0" cellspacing="0" cellpadding="0" >
>
<%
	try {
		File file = new File(System.getProperty("oracle.j2ee.home") + "/log/lms.log");
		InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file));
		BufferedReader input = new BufferedReader(streamReader);		
				
		String line = new String();
		String bgcolor = "#ffffff";

		while (input.ready()) {
		
			line = input.readLine();

			if (isNewLog(line)) {

				if (bgcolor.equals("#ffffff")){
					bgcolor = "#e6e6e6";
				} else if (bgcolor.equals("#e6e6e6")) {
					bgcolor = "#ffffff";
				}
			}

			out.println("<tr><td bgcolor=\""+ bgcolor +"\">"+ line +"</td></tr>");
		}

		out.flush();
		input.close();

	} catch(Exception e) {

		out.println(e);
	}
%>
</table>
</body>
</html>

<%!
boolean isNewLog (String line){
	try {
		Integer.parseInt(line.substring(0, 3));
	} catch (NumberFormatException nfe) {
		return false;
	}

	return true;
}

%>