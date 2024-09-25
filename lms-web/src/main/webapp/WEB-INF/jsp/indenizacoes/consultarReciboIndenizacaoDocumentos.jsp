<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.indenizacoes.consultarReciboIndenizacaoAction">
	<adsm:form action="/indenizacoes/consultarReciboIndenizacao" height="190" service="lms.indenizacoes.consultarReciboIndenizacaoAction.findItemById" idProperty="idDoctoServicoIndenizacao">
	    <adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="false">
		    <adsm:masterLinkItem property="nrReciboComposto"   label="numeroRIM" itemWidth="50" />
	    </adsm:masterLink>
		<adsm:hidden property="tpIndenizacaoValue"/>
		<adsm:hidden property="doctoServico.idDoctoServico" serializable="true" />
		<adsm:textbox property="doctoServico.tpDocumentoServico" 
					   size="5"
					   label="documentoServico" 
					   dataType="text"
					   labelWidth="20%" 
					   width="30%" 
					   serializable="false" 
					   disabled="true">

			<adsm:textbox dataType="text"
						 property="doctoServico.filialByIdFilialOrigem.sgFilial"
						 size="3"disabled="true" serializable="false"/>

			<adsm:textbox dataType="integer"
						 mask="00000000"
						 property="doctoServico.nrDoctoServico" 
						 picker="false"	size="10" serializable="false" disabled="true" />
		</adsm:textbox>
		
		
		<adsm:textbox label="numeroProcesso" property="nrProcessoSinistro" dataType="text" labelWidth="25%" width="75%" disabled="true" />
		
		<adsm:textbox label="tipoProduto" property="produto.dsProduto" dataType="text" disabled="true" labelWidth="20%" width="30%" size="14"/>

		<adsm:complement labelWidth="20%" width="30%" separator="branco" label="rnc">
			<adsm:textbox property="naoConformidade.filial.sgFilial" dataType="text" disabled="true" size="3" />
			<adsm:textbox property="naoConformidade.nrRnc" dataType="text" disabled="true" size="8"/>
		</adsm:complement>		
		<adsm:hidden  property="naoConformidade.idNaoConformidade"/>

		<adsm:complement label="valorIndenizacao" width="80%" labelWidth="20%" separator="branco" required="false" >
			<adsm:combobox property="moeda.idMoeda" disabled="true"
						   boxWidth="85"
						   service="lms.indenizacoes.consultarReciboIndenizacaoAction.findComboMoeda" 
						   optionProperty="idMoeda" 
						   optionLabelProperty="siglaSimbolo" 
						   onlyActiveValues="true">
			</adsm:combobox>
			<adsm:textbox property="vlIndenizacao" dataType="currency" disabled="true"/>
		</adsm:complement>				
							
		<adsm:listbox label="notasFiscais"
						property="nfs"
						optionProperty="idNotaFiscalConhecimento"
						optionLabelProperty="nrNotaFiscal"
						size="6" 
						boxWidth="91"
						width="80%"
						labelWidth="20%"
		/>

		<adsm:buttonBar freeLayout="true">
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid  
		property="doctoServicoIndenizacao" 
		idProperty="idDoctoServicoIndenizacao" 
		service="lms.indenizacoes.consultarReciboIndenizacaoAction.findPaginatedDocumentos"
		rowCountService="lms.indenizacoes.consultarReciboIndenizacaoAction.getRowCountDocumentos"
		selectionMode="none" gridHeight="99" 
		detailFrameName="documentos"		
		scrollBars="both" 		
		unique="true" 
		rows="30">
		
		<adsm:gridColumn property="tpDoctoServico" isDomain="true" title="documentoServico" dataType="text"  width="40"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn property="sgFilialOrigemDocto" dataType="text" title="" width="40" />
			<adsm:gridColumn property="nrDoctoServico" dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="sgFilialDestinoDocto"      title="destino" width="60"/>
		<adsm:gridColumn property="nmClienteRemetente"        title="remetente" width="200"/>
		<adsm:gridColumn property="nmClienteDestinatario"     title="destinatario" width="200"/>
		<adsm:gridColumn property="sgSimboloTotalDocto"       title="valorDocumentoServico" width="50" align="left"                      />
		<adsm:gridColumn property="vlMercadoria"              title=""                      width="120" align="right" dataType="currency"/>		
		<adsm:gridColumn property="sgSimboloVlIndenizado"     title="valorIndenizado" width="50"  align="left"                     />
		<adsm:gridColumn property="vlIndenizado"              title=""                width="120" align="right" dataType="currency"/>
		<adsm:gridColumn property="qtVolumes"                 title="quantidadeVolumes" width="90" align="right" dataType="integer"/>
		<adsm:gridColumnGroup separatorType="CONTROLE_CARGA">
			<adsm:gridColumn property="sgFilialControleCarga" title="controleCarga" width="50" />	
			<adsm:gridColumn property="nrControleCarga"       title=""  width="60" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumnGroup separatorType="MANIFESTO">
		<adsm:gridColumn property="sgFilialManifesto"         title="manifesto" width="50" />
		<adsm:gridColumn property="nrManifesto"               title=""  width="50" align="right" dataType="integer" mask="00000000" />
		</adsm:gridColumnGroup>
		<adsm:gridColumn property="dsProduto"                 title="tipoProduto" width="150"/>
		<adsm:gridColumnGroup separatorType="RNC">
			<adsm:gridColumn property="sgFilialNaoConformidade" dataType="text" title="RNC" width="40" />
			<adsm:gridColumn property="nrNaoConformidade"       dataType="integer" mask="00000000" title="" width="60" align="right"/>
		</adsm:gridColumnGroup>
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	document.getElementById('nrProcessoSinistro').masterLink = 'true';

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");
	
	function setMasterLinkProperties() {
		document.getElementById('nrProcessoSinistro').value = abaDetalhamento.getFormProperty("nrProcessoSinistro");
	}

	function onTabShow(fromTab) {
		resetValue(document);
		setMasterLinkProperties();		
	}
		
	
</script>
