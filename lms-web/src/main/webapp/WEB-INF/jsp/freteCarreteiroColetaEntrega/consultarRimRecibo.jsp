<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaPagina() {
	onPageLoad();
	setMasterLink(document, true);
	povoaForm();
}
</script>
<adsm:window title="documentos" service="lms.fretecarreteirocoletaentrega.consultarRimReciboAction" onPageLoad="carregaPagina" >
	<adsm:form action="/freteCarreteiroColetaEntrega/consultarRimRecibo" idProperty="idReciboFreteCarreteiro" 
			   service="lms.fretecarreteirocoletaentrega.consultarRimReciboAction.findRim" onDataLoadCallBack="carregaDados_retorno" >

		<adsm:section caption="documentos" />

		<adsm:textbox dataType="text" property="filial.sgFilial" label="recibo"   labelWidth="10%" width="90%" size="3" disabled="true" serializable="false" >
				<adsm:textbox dataType="text" property="nrReciboFreteCarreteiro2" size="15" disabled="true" serializable="false" style="text-align:right" />
		</adsm:textbox>

		<adsm:hidden property="nrReciboFreteCarreteiro" />
		
		<adsm:textbox dataType="text" property="tpSituacaoRecibo.description" 	label="situacao" labelWidth="10%" width="40%" size="25" disabled="true" />
		
		<adsm:hidden property="meioTransporte.idMeioTransporte" serializable="false" />
		<adsm:textbox dataType="text" property="meioTransporte.nrFrota"	label="meioTransporte" size="8" labelWidth="10%" width="40%" cellStyle="vertical-align=bottom;"	disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="meioTransporte.nrIdentificador"	size="20" cellStyle="vertical-align=bottom;" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:hidden property="proprietario.idProprietario" serializable="false" />
		<adsm:textbox dataType="text" property="proprietario.pessoa.nrIdentificacaoFormatado" label="proprietario" size="18" maxLength="20" labelWidth="10%" width="90%" disabled="true" serializable="false" >
			<adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="30"	disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:buttonBar freeLayout="true">
		</adsm:buttonBar>
		
		
	</adsm:form>
	
	
		<adsm:grid property="reciboFreteCarreteiro" idProperty="urlRecibo"
			service="lms.fretecarreteirocoletaentrega.consultarRimReciboAction.findReciboIndenizacao"
			rowCountService="lms.fretecarreteirocoletaentrega.consultarRimReciboAction.getRowCountFindReciboIndenizacao"
			selectionMode="none" unique="true" scrollBars="horizontal" gridHeight="250" rows="12" onRowClick="disableRowClick"  >
								
        	<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
        		<adsm:gridColumn title="documentoServico" property="doctoServico.tpDocumentoServico" width="35"/>
        		<adsm:gridColumn title="" property="doctoServico.filialByIdFilialOrigem.sgFilial" width="30" />
           		<adsm:gridColumn title="" property="doctoServico.nrDoctoServico" width="70" align="right" dataType="integer" mask="00000000" />
			</adsm:gridColumnGroup>	
	
			
			<adsm:gridColumn title="dataEmissao" property="doctoServico.dhEmissao" width="140"  />		
			
			<adsm:gridColumn title="valor" property="doctoServico.moeda" width="30"/>
			<adsm:gridColumn title="" property="doctoServico.vlMercadoria" dataType="decimal" mask="#,###,###,###,###,##0.00" width="70" align="right"/>
			
			<adsm:gridColumnGroup separatorType="RNC">
				<adsm:gridColumn property="reciboIndenizacao.filial"     title="numeroRIM" dataType="text"    width="30"/>	
				<adsm:gridColumn property="reciboIndenizacao.nrReciboIndenizacao"           title=""          dataType="integer" width="80" mask="00000000"/>
			</adsm:gridColumnGroup>
			
			<adsm:gridColumn title="dataEmissao" property="reciboIndenizacao.dhEmissao" width="140" />
			
			<adsm:gridColumn title="valorIndenizado" property="reciboIndenizacao.moeda" width="30"/>			
			<adsm:gridColumn title="" property="reciboIndenizacao.nrValorIndenizacaoReal" dataType="decimal" mask="#,###,###,###,###,##0.00" width="70" align="right"/>
			<adsm:gridColumn title="visualizar" property="reciboIndenizacao" image="/images/popup.gif" openPopup="true" link="javascript:carregarRim" width="100" align="center" />
		<adsm:buttonBar />
	</adsm:grid>
	
	<adsm:buttonBar> 			
			<adsm:button caption="fechar" id="botaoFechar" onclick="javascript:window.close();" disabled="false" />
		</adsm:buttonBar>
	
</adsm:window>

<script>

function povoaForm() {
	onDataLoad( getElementValue('idReciboFreteCarreteiro') );
}

function carregaDados_retorno_cb(data, error) {
	onDataLoad_cb(data, error);
	findButtonScript('reciboFreteCarreteiro', document.forms[0]);
}

function carregarRim(parametros){
	window.showModalDialog('/indenizacoes/consultarReciboIndenizacao.do?cmd=main'+parametros,window,'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:770px;dialogHeight:500px;');
}

function disableRowClick() {
    return false;
}



</script>