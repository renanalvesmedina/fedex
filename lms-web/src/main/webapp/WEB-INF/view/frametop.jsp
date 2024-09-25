<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<%@ page import="com.mercurio.adsm.framework.web.taglibs.*" %>
<%@ include file="/lib/imports.jsp"%>
<adsm:i18nLabels>
	<adsm:include key="empresa"/>
	<adsm:include key="filial"/>
	<adsm:include key="usuario"/>
	<asdm:include key="adsmUserInfoLoading"/>
</adsm:i18nLabels>
<html>
<head>
<%--
	ATEN��O: a variavel 'contextName' est� definida no arquivo: imports.jsp.
--%>
<style type="text/css">
	body {margin-left: 0px; margin-top: 0px; margin-right: 0px; margin-bottom: 0px; background-color: white;}
	table {background-color: white;}
</style>
<script language="javascript" type="text/javascript">
	var contextPath = "/<%=contextName%>/";
</script>
<script language="javascript" type="text/javascript">
document.onkeydown = keyHandler;

var titleStack = []; // armazena a pilha de titulos das paginas no historico

function keyHandler() {
	cancelKey();
	if(event.ctrlKey && (event.keyCode == 80) ) {
		event.keyCode = 0;
		var image = document.getElementById("caminhoes");
		if(image.style.display == "none") {
			image.style.display = "inline";
		} else {
			image.style.display = "none";
		}
		return false;
	}
}

function toPrint(xml) {
	document.getElementById("printer").doPrint(xml);
}

function indexPage() {
	//A funcao close, o x da tela
	if (parent.body) {
		parent.body.clearPages();
	}
	document.getElementById("title").innerHTML = '';
}

function updateTitle(title) {
	// Responsavel por escrever o titulo da tela
	if (parent.body) {
		titleStack.push(title);
		document.getElementById("title").innerHTML = title;
	}
}

function popTitle() {
	document.getElementById("title").innerHTML = "";
	titleStack.length = titleStack.length-1;
	if ( titleStack[titleStack.length-1] != null ) { 
		document.getElementById("title").innerHTML = titleStack[titleStack.length-1];
	}
}

function back() {
	if (parent.body) {
		parent.body.backPage();
	}
}

function hideBack() {
	document.getElementById('back_button').style.display = "none";
}

// funcao com nome reduzido para economizar bytes
function RP(page) {
	parent.body.clearPages();
	parent.body.redirectPage(page);
}

function redirectPage(page) {
	parent.body.redirectPage(page);
}

function waitBody() {
	if (parent.body.frameElement.readyState != 'complete') {
		setTimeout('waitBody()', 100);
	}
}

// reseta o semaforo de bloqueio da tela
getBlockUISemaphore().value = 0;

</script>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" style="border: 1px;">
	<tr>
		<td width="155">
			<%-- workaround para evitar "clique aqui para ativar este controle" - microsoft KB912945 --%>
			<!--script language="javascript" src="./lib/menu.js"></script-->
		</td>
		<td class="TopoUsuarioFilial">
			<div id="dadosUsuario" style="display: none;">
			</div>
			<table width="460" border="0" id="carregandoDadosUsuario">
				<tr>
					<td class="TopoUsuarioFilial" width="150" align="right" id="adsmUserInfoLoading">
					</td>
					<td width="20" class="TopoUsuarioFilial">
						<span id="carregandoDadosUsuarioDots"></span>
					</td>
				</tr>
			</table>
		</td>
		<td width="185" align="right">
			<img alt="FEDEX" src="/<%=contextName%>/images/logo_fedex.gif" height="36"/>
		</td>
	</tr>
	<tr>
		<td class="caminhoTela" colspan="2">
			<div name="title" id="title"></div>
		</td>
		<td class="caminhoTela" align="right">
			<img alt="voltar" height="27" src="/<%=contextName%>/images/top_arquivos/botao_barra_voltar.gif" onclick="back()" align="absmiddle" id="back_button">
			<img alt="sair" height="27" src="/<%=contextName%>/images/top_arquivos/botao_barra_sair.gif" onclick="indexPage();" align="absmiddle"><br>
		</td>
	</tr>
</table>
<div id="adsm_session_systemMessage" style="visibility:hidden; position: absolute; right: 2px; top: 2px; background-color: #FF6600; width: 500px; height: 61px; border: 2px; border-style: inset; padding: 1px; font-weight: bolder; color: white;"></div>
<img alt="sair" id="adsm_session_systemMessage_closeImg" src="/<%=contextName%>/images/top_arquivos/botao_barra_sair.gif" style="visibility:hidden; position: absolute; right: 2px; top: 0px; width: 24px; height: 24px;" onclick="document.getElementById('adsm_session_systemMessage').style.visibility = 'hidden'; this.style.visibility = 'hidden';"/>
<script type="text/javascript">
	var timerId = 0;
	getElement("adsmUserInfoLoading").innerText = i18NLabel.getLabel("adsmUserInfoLoading");
	// Carrega os dados do usu�rio no topo da p�gina principal
	function loadUserInfo() {
		var sdo = createServiceDataObject("adsm.security.usuarioService.findDadosUsuarioLogado", "loadUserInfo", null);
		xmit({serviceDataObjects:[sdo]});
		getElement("carregandoDadosUsuario").style.display = "inline";
		getElement("dadosUsuario").style.display = "none";
		timerId = setInterval("animateDots('carregandoDadosUsuarioDots')", 150);
	}

	function loadUserInfo_cb(data, error) {
		getElement("carregandoDadosUsuario").style.display = "none";
		var userInfo = "";
		
		if (error != null) {
			alert("Erro ao recuperar informa��es do usu�rio logado ("+error+").");
			userInfo = "N�o dispon�vel";
		} else {
			if (data["nomeEmpresa"] != "") {
				userInfo+=i18NLabel.getLabel("empresa") + ": "+data["nomeEmpresa"]+"<BR>";
			}
			if (data["nomeFilial"] != "") {
				userInfo+=i18NLabel.getLabel("filial") + ": "+data["nomeFilial"]+"<BR>";
			}
			if (data["nomeUsuario"] != "") {
				userInfo+=i18NLabel.getLabel("usuario") + ": "+data["nomeUsuario"];
			}
		}
		getElement("dadosUsuario").style.display = "inline";
		getElement("dadosUsuario").innerHTML = userInfo;
		clearInterval(timerId);
	}
	
	loadUserInfo();
</script>
</body>
</html>
