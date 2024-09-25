<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.contasreceber.consultarReciboDemonstrativoDescontoAction">

	<adsm:form action="/contasReceber/consultarReciboDemonstrativoDesconto">

		<adsm:masterLink showSaveAll="false" idProperty="idReciboDemonstrativo">
			<adsm:masterLinkItem property="desconto" label="desconto"  itemWidth="35"/>
			<adsm:hidden property="tpTipoDesconto"/>
			<adsm:hidden property="idProcessoWorkflow"/>
		</adsm:masterLink>
		
		<adsm:hidden property="tpDocumento"/>
		<adsm:textbox label="tipoDocumento" property="tpDocumentoServico" dataType="text" width="35%" size="5" disabled="true"/>

		<adsm:hidden property="idFilialOrigem"/>
		<adsm:textbox property="sgFilialOrigem" dataType="text" size="3" width="35%" label="documentoServico" disabled="true">
			<adsm:textbox property="nrDocumento" dataType="text" disabled="true" mask="00000000"/>
		</adsm:textbox>
		
		<adsm:textbox property="nrIdentificacao" width="85%" dataType="text" size="20" label="clienteResponsavel" disabled="true">
			<adsm:textbox property="nmCliente" dataType="text" disabled="true" size="30"/>
		</adsm:textbox>

		<adsm:textbox label="valorDocumento" dataType="text" size="10" disabled="true" property="siglaSimboloDocumento" width="35%">
			<adsm:textbox property="vlFrete" dataType="currency" size="15" disabled="true" />
		</adsm:textbox>	
		
		<adsm:textbox label="valorDesconto" dataType="text" size="10" disabled="true" property="siglaSimboloDesconto" width="35%">
			<adsm:textbox property="vlDesconto" dataType="currency" size="15" disabled="true" />
		</adsm:textbox>		

		<adsm:hidden property="tpSituacaoAprovacao"/>
		<adsm:textbox property="dsSituacaoAprovacao" label="situacaoAprovacao" width="35%" disabled="true" dataType="text"/>

        <adsm:hidden property="tpMotivoDesconto"/>
        <adsm:textbox property="dsMotivoDesconto" label="motivoDesconto" width="35%" disabled="true" dataType="text" size="40" maxLength="60"/>

       <adsm:textarea label="observacao" property="observacao" maxLength="500" columns="90" rows="5" width="85%" disabled="true"/>
		
	</adsm:form>

	<adsm:grid idProperty="idGeral" property="desconto" onRowClick="myFindById" 
			   service="lms.contasreceber.consultarReciboDemonstrativoDescontoAction.findDadosListagemAbaDocumentosServico" 
			   rowCountService="lms.contasreceber.consultarReciboDemonstrativoDescontoAction.getRowCountDadosListagemAbaDocumentosServico"
			   rows="14" selectionMode="none" disableMarkAll="true" autoSearch="false">
			   
		<adsm:gridColumn width="30" title="documentoServico" property="tpDocumentoServico"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="" property="sgFilialOrigem"/>	
			<adsm:gridColumn width="100" title="" property="nrDocumentoServico" />
		</adsm:gridColumnGroup>		   
			   
    	<adsm:gridColumn title="clienteResponsavel" property="clienteResponsavel"   width="155"/>
		<adsm:gridColumn title="situacaoAprovacao" 	property="tpSituacaoAprovacao" 	width="155" isDomain="true"/>
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="valorDesconto" property="sgMoeda" dataType="text"/>	
			<adsm:gridColumn width="50" title="" property="dsSimbolo" dataType="text"/>
		</adsm:gridColumnGroup>							
		<adsm:gridColumn title="" property="vlDesconto" width="60" dataType="currency" align="right"/>	
		
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="valorFrete" property="sgMoedaFrete" dataType="text"/>	
			<adsm:gridColumn width="50" title="" property="dsSimboloFrete" dataType="text"/>
		</adsm:gridColumnGroup>							
		<adsm:gridColumn title="" property="vlFrete" width="60" dataType="currency" align="right"/>	
		<adsm:buttonBar />				
	</adsm:grid>
	
</adsm:window>
<script>

	function myOnShow() {
	  resetValue(document);	
	  var tpTipoDesconto = getTabGroup(this.document).getTab("cad").tabOwnerFrame.getElementValue("tpTipoDesconto")      
      setElementValue('tpTipoDesconto',tpTipoDesconto);
	}		
	
	function myFindById(id){
		
		var dataGrid = null;
		
		for( var i = 0; i < descontoGridDef.gridState.data.length; i++ ){
			if( descontoGridDef.gridState.data[i].idGeral == id ){
				dataGrid = descontoGridDef.gridState.data[i];
			}
		}
		
		if( dataGrid == null ){
			return false;
		}	
		
		setElementValue('tpDocumentoServico',dataGrid.tpDocumentoServico);
		setElementValue('sgFilialOrigem',dataGrid.sgFilialOrigem);
		setElementValue('nrDocumento',dataGrid.nrDocumentoServico);
		setElementValue('nrIdentificacao',dataGrid.nrIdentificacao);
		setElementValue('nmCliente',dataGrid.nmCliente);
		setElementValue('siglaSimboloDesconto',dataGrid.siglaSimboloDesconto);
		setElementValue('vlDesconto',setFormat(document.getElementById('vlDesconto'), dataGrid.vlDesconto));
		setElementValue('siglaSimboloDocumento',dataGrid.siglaSimboloDocumento);		
		setElementValue('vlFrete', setFormat(document.getElementById('vlFrete'), dataGrid.vlFrete));
		
		if( dataGrid.tpSituacaoAprovacao != undefined ){		
			setElementValue('dsSituacaoAprovacao',dataGrid.tpSituacaoAprovacao.description);
		}
		
		setElementValue('dsMotivoDesconto',dataGrid.dsMotivoDesconto);
		setElementValue('observacao',dataGrid.observacao);	
		
		setFocusOnFirstFocusableField(document);
        
        return false;
	
	}

</script>