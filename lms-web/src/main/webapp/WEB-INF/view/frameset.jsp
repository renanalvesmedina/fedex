<%
String systemName = getServletConfig().getServletContext().getInitParameter("systemName").toUpperCase();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<html>
<head>
<title>Expresso Mercúrio LMS</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="X-Frame-Options" content="sameorigin">
<meta http-equiv="X-UA-Compatible" content="IE=5" />
<script>
	function updateTitle(title) {
		if (document.frames[0].updateTitle)	{
			document.frames[0].updateTitle(title);
		}
	}
	function tabTitle(xTi,xTf,erase) {
		if (document.frames[0].tabTitle) {
			document.frames[0].tabTitle(xTi,xTf,erase);
		}
	}
	function popTitle() {
		if (document.frames[0].popTitle)	{
			document.frames[0].popTitle();
		}
	}

	function getFormataDataHora() {
		date = new Date();
		ano = date.getFullYear();
		mes = date.getMonth();
		dia = date.getDay();
		hora = date.getHours();
		minuto = date.getMinutes();
		dataFormatada = '('+ dia + '/' + mes + '/' + ano + ' ' + hora + ':'+ minuto + ')';

		return dataFormatada;
	}

	document.title = 'TNT Mercúrio - <%=systemName%> - ( <%=request.getContextPath()+':'+request.getLocalPort()%> )  - ' + getFormataDataHora();	

	function atualizaDataHora(){
		document.title = 'TNT Mercúrio - <%=systemName%> - ( <%=request.getContextPath()+':'+request.getLocalPort()%> )  - ' + getFormataDataHora();
	}
	 
	window.setInterval(atualizaDataHora, 60000);

	var waitLmsTimes = 100;
	
	function waitBody(page) {
		if (waitLmsTimes < 1) {
			return;
		}
		waitLmsTimes--;
		var p = page
		if (document.frames && document.frames[1] && document.frames[0] && document.frames[0].RP) {
			setTimeout(function () {
    			//document.frames[0].RP(page);
    			body.clearPages();
    			body.redirectPage(page);
			}, 1000);
		} else {
			setTimeout(function () {waitBody(p)}, 100);
		}
	}

	function init() {
		waitLmsTimes = 100;
		var url = "<%=request.getParameter("action")%>";
		if (url) {
			<%
			for (String name : new java.util.ArrayList<String>(request.getParameterMap().keySet())) {
				if (!"action".equals(name)) {
				    String value = request.getParameter(name);
				    %>url+="&<%=name%>=<%=value%>";<%
				}
			}
			%>
			url = url.replace("&","?");
			waitBody(url);
		}
	}
	
	init();

</script>
</head>

<frameset id="frmSet" rows="66,*" frameborder="NO" border="0" framespacing="0">
  <frame src="${pageContext.request.contextPath}/view/frametop" name="top" scrolling="NO" noresize >
  <frame src="${pageContext.request.contextPath}/framebottom.do" name="body" scrolling="no">
</frameset>

</html>
