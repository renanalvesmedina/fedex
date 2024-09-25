<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.consultarManifestosEntregaAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/entrega/consultarManifestosEntrega" idProperty="idManifestoEntrega" height="420"
			service="lms.entrega.consultarManifestosEntregaAction.findByIdConsultaManifesto" onDataLoadCallBack="dataLoadCustom" >
		
		<adsm:section caption="manifestoEntrega" />
		<adsm:hidden property="idProcessoWorkflow" serializable="false" />
		<adsm:hidden property="filial_idFilial" />
		<adsm:hidden property="nmFantasiaFilial" />
		<adsm:textbox dataType="text" property="filial_sgFilial"
				label="numero" size="3" labelWidth="19%" width="31%" disabled="true" >
			<adsm:textbox dataType="integer" property="nrManifestoEntrega"
				 size="8" mask="00000000" disabled="true"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="tpStatusManifesto"
				label="situacao" size="30" labelWidth="19%" width="31%" disabled="true"/>
				
		<adsm:textbox dataType="text" property="tpManifestoEntrega"
				label="tipo" size="20" labelWidth="19%" width="81%" disabled="true"/>
				
		<adsm:textbox dataType="text"
				property="cliente_pessoa_nrIdentificacaoFormatado"
				label="consignatario" size="18" labelWidth="19%" width="81%"
				disabled="true" serializable="false">
			<adsm:textbox dataType="text" 
					property="cliente_pessoa_nmPessoa" 
					size="30" disabled="true" />
		</adsm:textbox>
				
		<adsm:textbox dataType="JTDateTimeZone" property="dhEmissao" 
				label="dataHoraDeEmissao" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dhFechamento"
				label="dataHoraFechamento" labelWidth="19%" width="31%" disabled="true"/>

		<adsm:textbox dataType="integer" property="qtDocumentos"
				label="quantidadeDocumentos" size="4" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="integer" property="qtEntregas"
				label="quantidadeEntregas" size="4" labelWidth="19%" width="31%" disabled="true"/>
				
		<adsm:section caption="controleCarga" />
		<adsm:hidden property="controleCarga_idControleCarga" />
		<adsm:hidden property="controleCarga_idFilial" />
		<adsm:hidden property="controleCarga_nmFantasia" />
		<adsm:textbox dataType="text" property="controleCarga_sgFilial"
				label="numero" size="3" labelWidth="19%" width="81%" disabled="true" >
			<adsm:textbox dataType="integer" property="controleCarga_nrControleCarga"
					size="8" mask="00000000" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="dsEquipe" 
				label="equipe" size="30" labelWidth="19%" width="31%" disabled="true" >
			<adsm:listbox property="integrantesEquipe" optionProperty="id" optionLabelProperty="nmPessoa"
					 size="4" boxWidth="170" />
		</adsm:textbox>
		<adsm:listbox property="lacres" optionProperty="id" optionLabelProperty="descricao"
				label="lacres" labelWidth="19%" width="31%" size="5" boxWidth="190"
				cellStyle="vertical-Align:bottom;" />

		<adsm:textbox dataType="integer" property="rotaColetaEntrega_nrRota"
				label="rotaColetaEntrega" size="3" mask="000" labelWidth="19%" width="31%" disabled="true" >
			<adsm:textbox dataType="text" property="rotaColetaEntrega_dsRota"
					size="22" disabled="true"/>
		</adsm:textbox>
		<adsm:textbox dataType="text" property="dsRegiao"
				label="regiaoColetaEntrega" size="30" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="text" property="dsTipoTabelaColetaEntrega"
				label="tipoTabela" size="20" labelWidth="19%" width="31%" disabled="true"/>
		<adsm:textbox dataType="text" property="notaCredito_sgFilial"
				label="notaCredito" size="3" labelWidth="19%" width="31%" disabled="true" >
			<adsm:textbox dataType="integer" property="notaCredito_nrNotaCredito"
					size="10" mask="0000000000" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="meioTransporte_nrFrota"
				label="meioTransporte" size="8" labelWidth="19%" width="31%" disabled="true" >
			<adsm:textbox dataType="text" property="meioTransporte_nrIdentificador"
					size="20" disabled="true" />
		</adsm:textbox>
		<adsm:textbox dataType="text" property="semiReboque_nrFrota"
				label="semiReboque" size="8" labelWidth="19%" width="31%" disabled="true" >
			<adsm:textbox dataType="text" property="semiReboque_nrIdentificador"
					size="20" disabled="true" />
		</adsm:textbox>

		<adsm:textbox dataType="JTDateTimeZone" property="dhSaidaColetaEntrega"
				label="dataHoraSaidaPortaria" size="20" labelWidth="19%" width="31%"
				disabled="true" cellStyle="vertical-Align: bottom;" />
		<adsm:textbox dataType="JTDateTimeZone" property="dhChegadaColetaEntrega"
				label="dataHoraChegadaPortaria" size="20" labelWidth="19%" width="31%"
				disabled="true" cellStyle="vertical-Align: bottom;" />
		
		<adsm:buttonBar>
		
			<adsm:button id="btnCancelarReemitir"
					caption="reemitirCancelarManifestosButton" action="/entrega/reemitirCancelarManifestos" cmd="main" >
				<adsm:linkProperty src="filial_idFilial" target="filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial_sgFilial" target="filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="nmFantasiaFilial" target="filial.pessoa.nmFantasia" disabled="true" />
				
				<adsm:linkProperty src="idManifestoEntrega" target="manifestoEntrega.idManifestoEntrega" disabled="true" />
				<adsm:linkProperty src="filial_idFilial" target="manifestoEntrega.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial_sgFilial" target="manifestoEntrega.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="nrManifestoEntrega" target="manifestoEntrega.nrManifestoEntrega" disabled="true" />
				
				<adsm:linkProperty src="filial_idFilial"
						target="controleCarga.filialByIdFilialOrigem.idFilial" disabled="true" />
				<adsm:linkProperty src="filial_sgFilial"
						target="controleCarga.filialByIdFilialOrigem.sgFilial" disabled="true" />
			</adsm:button>
			
			<adsm:button id="btnControleCarga"
					caption="controleCargas" action="carregamento/consultarControleCargas" cmd="main" >
				<adsm:linkProperty src="controleCarga_idControleCarga" target="idControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga_nrControleCarga" target="nrControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga_idFilial"
						target="filialByIdFilialOrigem.idFilial" disabled="true" />
				<adsm:linkProperty src="controleCarga_sgFilial"
						target="filialByIdFilialOrigem.sgFilial" disabled="true" />
				<adsm:linkProperty src="controleCarga_nmFantasia"
						target="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true" />
			</adsm:button>
			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--
	
	function initWindow(eventObj) {
		setDisabled("btnCancelarReemitir",getElementValue("idProcessoWorkflow") != "");
		setDisabled("btnControleCarga",getElementValue("idProcessoWorkflow") != "");
	}
	
	function dataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
		var qtDocumentos = getElementValue("qtDocumentos");
		if (qtDocumentos != undefined && qtDocumentos > 0) {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab('doc', false);
		}
		
		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled("btnCancelarReemitir",true);
			setDisabled("btnControleCarga",true);
		}
	}
		
		
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			
			getTabGroup(this.document).setDisabledTab("cad",false);
			getTabGroup(this.document).selectTab(1,null,true);
			getTabGroup(this.document).setDisabledTab("list",true);
		}
	}
//-->
</script>