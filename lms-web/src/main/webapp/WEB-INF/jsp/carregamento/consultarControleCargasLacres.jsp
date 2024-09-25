<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="lacres" service="lms.carregamento.consultarControleCargasJanelasAction" 
			 onPageLoadCallBack="retornoCarregaPagina" >
			 
	<adsm:form action="/carregamento/consultarControleCargasLacres" idProperty="idLacreControleCarga" onDataLoadCallBack="retorno_carregaDados"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findByIdLacre" >

		<adsm:hidden property="controleCarga.idControleCarga" serializable="true" />
		<adsm:textbox dataType="text" label="controleCargas" property="controleCarga.filialByIdFilialOrigem.sgFilial"
					  size="3" width="85%" disabled="true" serializable="false" >
	 		<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" mask="00000000" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:textbox label="numeroLacre" property="nrLacres" dataType="text" size="15" maxLength="8" width="42%" disabled="true" />

		<adsm:textbox label="status" property="tpStatusLacre" dataType="text" labelWidth="14%" width="29%" disabled="true"/>


		<adsm:textbox label="filialInclusao" property="filialByIdFilialInclusao.sgFilial" dataType="text" size="3" 
					  width="42%" disabled="true" cellStyle="vertical-align: middle">
			<adsm:textbox dataType="text" property="filialByIdFilialInclusao.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:textbox>

		<adsm:textarea label="observacaoInclusao" property="obInclusaoLacre" 
					   maxLength="200" rows="2" columns="40" labelWidth="14%" width="29%" />

		<adsm:textbox label="localInclusao" property="dsLocalInclusao" dataType="text" size="60" maxLength="80" width="85%" disabled="true" />


		<adsm:textbox label="filialConferencia" property="filialByIdFilialAlteraStatus.sgFilial" dataType="text" size="3" 
					  width="42%" disabled="true" cellStyle="vertical-align: middle">
			<adsm:textbox dataType="text" property="filialByIdFilialAlteraStatus.pessoa.nmFantasia" size="30" disabled="true" />
		</adsm:textbox>

		<adsm:textarea label="observacaoConferencia" property="obConferenciaLacre" 
					   maxLength="200" rows="2" columns="40" labelWidth="14%" width="29%" />

		<adsm:textbox label="localConferencia" property="dsLocalConferencia" dataType="text" 
					  size="60" maxLength="80" width="85%" disabled="true" />

	</adsm:form>

	<adsm:grid idProperty="idLacreControleCarga" property="lacresControleCarga" 
			   selectionMode="none" scrollBars="horizontal" gridHeight="190" title="lacres" unique="true" rows="9"
			   onRowClick="populaForm"
			   service="lms.carregamento.consultarControleCargasJanelasAction.findPaginatedLacre"
			   rowCountService="lms.carregamento.consultarControleCargasJanelasAction.getRowCountLacre"
			   >
		<adsm:gridColumn title="lacre" 					property="nrLacres" width="100"/>
		<adsm:gridColumn title="dataInclusao"			property="dhInclusao" width="140" dataType="JTDateTimeZone" align="center" />
		<adsm:gridColumn title="status" 				property="tpStatusLacre" isDomain="true" width="125" />
		<adsm:gridColumn title="filialInclusao" 		property="filialByIdFilialInclusao.sgFilial" width="110" />
		<adsm:gridColumn title="observacaoInclusao" 	property="obInclusaoLacre" width="200" />
		<adsm:gridColumn title="dataAlteracao"			property="dhAlteracao" width="140" dataType="JTDateTimeZone" align="center" />
		<adsm:gridColumn title="filialConferencia" 		property="filialByIdFilialAlteraStatus.sgFilial" width="120" />
		<adsm:gridColumn title="observacaoConferencia" 	property="obConferenciaLacre" width="200" />
		<adsm:gridColumn title="usuarioInclusao"	 	property="usuarioByIdFuncInclusao.nmUsuario" width="200" />
		<adsm:gridColumn title="usuarioAlteracao"	 	property="usuarioByIdFuncAlteraStatus.nmUsuario" width="200" />
		<adsm:buttonBar>
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>


<script>
function populaForm(id) {
	onDataLoad(id);
	return false;
}

function retorno_carregaDados_cb(data, error) {
	onDataLoad_cb(data, error);
	povoaDadosMaster();
	document.getElementById("obInclusaoLacre").readOnly=true;
	document.getElementById("obConferenciaLacre").readOnly=true;
	setDisabled("botaoFechar", false);
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	document.getElementById("obInclusaoLacre").readOnly=true;
	document.getElementById("obConferenciaLacre").readOnly=true;
	if (error == undefined) {
		inicializaTela();
		povoaGrid(getElementValue("controleCarga.idControleCarga"));
	}
	setFocus(document.getElementById("botaoFechar"), true, true);
}

function povoaDadosMaster() {
	setElementValue("controleCarga.idControleCarga", dialogArguments.window.document.getElementById('idControleCarga').value);
	setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", dialogArguments.window.document.getElementById('filialByIdFilialOrigem.sgFilial').value);
	setElementValue("controleCarga.nrControleCarga", dialogArguments.window.document.getElementById('nrControleCarga').value);
}

function povoaGrid(idControleCarga) {
	var filtro = new Array();
    setNestedBeanPropertyValue(filtro, "controleCarga.idControleCarga", idControleCarga);
    lacresControleCargaGridDef.executeSearch(filtro, true);
    return false;
}

function inicializaTela() {
	resetValue(this.document);
	povoaDadosMaster();
}

</script>