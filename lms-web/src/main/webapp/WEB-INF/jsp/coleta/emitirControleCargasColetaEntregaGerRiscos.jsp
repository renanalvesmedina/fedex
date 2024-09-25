<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.coleta.emitirControleCargasColetaEntregaAction" >

	<adsm:form action="/coleta/emitirControleCargasColetaEntrega" >
		<adsm:masterLink idProperty="idControleCarga" showSaveAll="false">
			<adsm:masterLinkItem property="controleCargaConcatenado" label="controleCargas" itemWidth="100" />
		</adsm:masterLink>
	</adsm:form>


	<adsm:grid property="exigencias" idProperty="idExigenciaGerRisco" 
			   selectionMode="none" unique="true" title="exigenciasConteudoAtual"
			   scrollBars="vertical" showPagging="false" autoSearch="false" rows="10" gridHeight="300"
			   service="lms.coleta.emitirControleCargasColetaEntregaAction.findPaginatedExigenciasGerRisco"
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

function exigenciasGerRisco_OnClick(id) {
	return false;
}

function exibeEnquadramento() {
	var controleCarga = document.forms[0]._controleCargaConcatenado.value;
	var idControleCarga = getTabGroup(this.document).getTab("emissao").getFormProperty("idControleCarga");
	showModalDialog('/coleta/emitirControleCargasColetaEntrega.do?cmd=enquadramento&idControleCarga='+idControleCarga+'&controleCarga='+controleCarga,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:765px;dialogHeight:425px;');
}
</script>