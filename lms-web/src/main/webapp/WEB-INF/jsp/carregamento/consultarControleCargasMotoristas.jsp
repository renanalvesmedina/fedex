<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="motoristasControleCargas" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >

	<adsm:form action="/carregamento/consultarControleCargasMotoristas">
		<adsm:section caption="motoristasControleCargas" />
		
		<adsm:hidden property="controleCarga.idControleCarga"/>
		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" width="85%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
	</adsm:form>


	<adsm:grid idProperty="idMotoristaControleCarga" property="motoristasControleCarga" 
			   selectionMode="none" title="historicoMotoristas" autoSearch="false"
			   gridHeight="265" rows="13" unique="true" scrollBars="horizontal" 
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedMotorista"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountMotorista"
			   onRowClick="motoristasControleCarga_onClick"
			   >
		<adsm:gridColumn title="identificacao"			property="tpIdentificacao" width="60" align="left" isDomain="true" />
		<adsm:gridColumn title="" 						property="nrIdentificacaoFormatado" width="110" align="right" />
		<adsm:gridColumn title="motorista" 				property="nmPessoaMotorista" width="220" />
		<adsm:gridColumn title="dataHoraTroca" 			property="dhTroca" dataType="JTDateTimeZone" align="center" width="130" />
		<adsm:gridColumn title="trecho" 				property="trecho" width="100" />
		<adsm:gridColumn title="rodovia" 				property="sgRodovia" width="80" />
		<adsm:gridColumn title="km" 					property="nrKmRodoviaTroca" width="50" align="right" />
		<adsm:gridColumn title="municipio" 				property="nmMunicipio" width="200" />
		<adsm:gridColumn title="uf" 					property="sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="descricaoLocal" 		property="descricaoLocal" image="/images/popup.gif" link="javascript:exibirDescricaoLocal" openPopup="true" width="120" align="center" />
		<adsm:gridColumn title="usuario"			 	property="nmUsuarioAlteraStatus" width="200" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>
function motoristasControleCarga_onClick(id) {
	return false;
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
		setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
		povoaGrid(getElementValue("controleCarga.idControleCarga"));
	}
}

function povoaGrid(idControleCarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
    motoristasControleCargaGridDef.executeSearch(filtro, true);
    return false;
}

function exibirDescricaoLocal(id) {
	var idLocalTroca = motoristasControleCargaGridDef.findById(id).idLocalTroca;
	if (idLocalTroca != undefined && idLocalTroca != "")
		showModalDialog('carregamento/consultarControleCargasLocalTroca.do?cmd=descricao&idLocalTroca=' + idLocalTroca, window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:450px;dialogHeight:200px;');
}
</script>