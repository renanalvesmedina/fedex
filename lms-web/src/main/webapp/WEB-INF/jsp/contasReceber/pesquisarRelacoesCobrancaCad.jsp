<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.pesquisarRelacoesCobrancaAction">
	<adsm:form action="/contasReceber/pesquisarRelacoesCobranca" idProperty="idRelacaoCobranca"  onDataLoadCallBack="myOnDataLoad">
	
		
		<adsm:hidden  property="idFilial" />
		<adsm:hidden  property="sgFilialNmFantasiaFilial" />
		<adsm:textbox property="sgFilial" label="filialCobranca" dataType="text" size="3" labelWidth="22%" width="5%" disabled="true" />
		<adsm:textbox property="nmFantasiaFilial"  dataType="text" size="30" width="70%" disabled="true" />
			
		<adsm:textbox property="nrRelacaoCobrancaFilial" label="relacaoCobranca" dataType="integer" size="10" labelWidth="22%" width="28%" disabled="true"/>

		<adsm:textbox dataType="JTDate" picker="false" property="dtLiquidacao" label="dataLiquidacao" labelWidth="22%" width="28%" disabled="true"/>


		<adsm:textbox property="tpSituacaoRelacaoCobranca" label="situacao" dataType="text" size="10" labelWidth="22%" width="28%" disabled="true"/>

		
		<adsm:textbox dataType="text" property="dsOrigem" label="cobrador" size="30" labelWidth="22%" width="28%" disabled="true"/>

		<adsm:textbox dataType="integer" property="quantidade_documentos" label="quantidadeDocumentos" disabled="true" labelWidth="22%" width="28%"/>
		
		<adsm:textbox dataType="text" property="siglaMoeda" label="moeda" disabled="true" labelWidth="22%" width="28%"/>
		
		<adsm:textbox dataType="currency" property="valorTotalDevido" label="valorTotalDocumentos" disabled="true" labelWidth="22%" width="28%"/>
		
		<adsm:textbox dataType="decimal" property="vlJuros" label="valorTotalJuros" disabled="true" labelWidth="22%" width="28%"/>
		<adsm:textbox dataType="currency" property="vlDesconto" label="valorTotalDesconto" disabled="true" labelWidth="22%" width="28%"/>
		<adsm:textbox dataType="currency" property="vlTarifa" label="valorTotalTarifas" disabled="true" labelWidth="22%" width="28%"/>

		<adsm:textbox dataType="currency" property="valorTotalPago" label="valorTotalRecebido" disabled="true" labelWidth="22%" width="28%"/>

        <adsm:textbox label="redeco" property="filialRedeco" dataType="text"  size="3"  maxLength="3" labelWidth="22%" width="5%"  disabled="true"/>
		<adsm:textbox property="nrRedeco" dataType="integer" size="10" width="22%" disabled="true" mask="0000000000"/>

		<adsm:hidden property="idFilialRedeco" />
		<adsm:hidden property="idRedeco" />
		<adsm:hidden property="redecoNmFantasia" />
		
		
		<adsm:buttonBar>
			<adsm:button boxWidth="55" caption="redeco" id="redecoButton" action="/contasReceber/manterRedeco" cmd="main" disabled="false">
				<adsm:linkProperty src="idRedeco" target="idRedeco"/>
				<adsm:linkProperty src="nrRedeco" target="nrRedeco"/>	
				<adsm:linkProperty src="idFilialRedeco" target="filial.idFilial"/>							
				<adsm:linkProperty src="filialRedeco" target="filial.sgFilial"/>
				<adsm:linkProperty src="redecoNmFantasia" target="filial.pessoa.nmFantasia"/>
			</adsm:button>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>
	function myOnDataLoad_cb(data,erro,codigoErro,evento){
		onDataLoad_cb(data,erro,codigoErro,evento);
		disableTabDocumentosServico(false);
		
		if (data != undefined) {
			setElementValue("sgFilialNmFantasiaFilial", getElementValue("sgFilial") + " - " + getElementValue("nmFantasiaFilial"))
		}

	}
	
	function disableTabDocumentosServico(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("documentosServico", disabled);
	}
	
	
	
</script>
