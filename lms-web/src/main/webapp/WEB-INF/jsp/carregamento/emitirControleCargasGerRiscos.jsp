<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="carregarVeiculo" service="lms.carregamento.emitirControleCargasAction" >
	<adsm:form action="/carregamento/emitirControleCargas">
		<adsm:hidden property="idControleCarga"/>
		<adsm:hidden property="controleCargaConcatenado"/>
	</adsm:form>


	<adsm:grid property="exigencias" idProperty="idExigenciaGerRisco" 
			   selectionMode="none" unique="true" title="exigenciasConteudoAtual"
			   scrollBars="vertical" showPagging="false" autoSearch="false" rows="15" gridHeight="300"
			   service="lms.carregamento.emitirControleCargasAction.findPaginatedExigenciasGerRisco"
			   onRowClick="exigenciasGerRisco_OnClick"
			   >
		<adsm:gridColumn title="tipo" property="dsTipoExigencia" width="12%" align="left" />
		<adsm:gridColumn title="exigencia" property="dsResumida" width="46%" align="left" />
		<adsm:gridColumn title="quantidade" property="qtExigida" width="10%" align="left" />
		<adsm:gridColumn title="filialDeInicio" property="filialInicio" width="12%" align="left" />
		<adsm:gridColumn title="kmExigida" property="vlKmFranquia" width="10%" align="left" />
		<adsm:gridColumn width="8%" title="detalhe" property="detalhe" link="sgr/manterSMP.do?cmd=providenciasDet" popupDimension="550,220" linkIdProperty="exigenciaGerRisco.idExigenciaGerRisco" image="/images/popup.gif" align="center"/>
		<adsm:buttonBar/>
	</adsm:grid>
	
	<adsm:buttonBar>
		<adsm:button id="enquadramento" caption="enquadramento" onclick="exibeEnquadramento();" disabled="false" />
	</adsm:buttonBar>
</adsm:window>

<script>


function initWindow(eventObj) {
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("pesq");
		
		var idControleCarga = tab.getFormProperty("controleCarga.idControleCarga");
		var controleCargaConcatenado = tab.getFormProperty("controleCargaConcatenado");
		
		setElementValue("idControleCarga",idControleCarga);
		setElementValue("controleCargaConcatenado",controleCargaConcatenado);
		
		findButtonScript('exigencias', document.forms[0]);  
}

function exigenciasGerRisco_OnClick(id) {
	return false;
}

function exibeEnquadramento() {
	var controleCarga = getElementValue("controleCargaConcatenado");
	var idControleCarga = getElementValue("idControleCarga");
	showModalDialog('/carregamento/emitirControleCargas.do?cmd=enquadramento&idControleCarga='+idControleCarga+'&controleCarga='+controleCarga,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:765px;dialogHeight:425px;');
}
</script>