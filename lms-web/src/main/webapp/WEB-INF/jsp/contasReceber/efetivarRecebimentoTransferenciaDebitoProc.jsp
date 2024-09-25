<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction">
	<adsm:form action="/contasReceber/efetivarRecebimentoTransferenciaDebito" onDataLoadCallBack="myDataLoad">

		<adsm:hidden property="idTransferencia" />
		<adsm:hidden property="filialDestino.idFilial" />
		<adsm:hidden property="filialOrigem.idFilial" />
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-02067"/>
		</adsm:i18nLabels>

        <adsm:masterLink idProperty="idTransferencia" showSaveAll="false">
			<adsm:masterLinkItem label="transferencia" property="transferencia"/>
			<adsm:masterLinkItem label="dataEmissao" property="dtEmissao"/>
		</adsm:masterLink>
		 
		<adsm:hidden property="responsavelAntigo.idResponsavelAntigo" />
		<adsm:textbox property="responsavelAntigo.nrIdentificacao" label="responsavelAnterior" labelWidth="20%" width="80%"
				dataType="text" serializable="false" disabled="true" size="18">
				<adsm:textbox property="responsavelAntigo.nmPessoa" dataType="text" serializable="false"
						disabled="true" size="35"/>
		</adsm:textbox>
		
		<adsm:textbox property="dsDivisaoClienteAnterior" dataType="text" serializable="false" label="divisaoResponsavelAnterior"
				disabled="true" size="60" labelWidth="20%" width="80%"/>
				
		<adsm:hidden property="responsavelNovo.idResponsavelAntigo" />
		<adsm:textbox property="responsavelNovo.nrIdentificacao" label="novoResponsavel" labelWidth="20%" width="80%"
				dataType="text" serializable="false" disabled="true" size="18">
				<adsm:textbox property="responsavelNovo.nmPessoa" dataType="text" serializable="false" 
					disabled="true" size="35"/>		
		</adsm:textbox>	
		
		<adsm:textbox property="dsDivisaoClienteNovo" dataType="text" serializable="false" label="divisaoNovoResponsavel"
				disabled="true" size="60" labelWidth="20%" width="80%"/>
				
		<adsm:textbox label="motivoTransferencia" property="motivoTransferencia" 
			dataType="text" 
			serializable="false" disabled="true" size="100" 
			labelWidth="20%" width="80%"/>

		<adsm:textbox label="documentoServico" property="doctoServico.tpDocumento" dataType="text" serializable="false" 
			disabled="true" size="10" labelWidth="20%" width="80%">

			<adsm:textbox property="doctoServico.sgFilial" dataType="text" serializable="false" 
				disabled="true" size="5"/>

			<adsm:textbox property="doctoServico.nrDoctoServico" dataType="integer" serializable="false" 
				disabled="true" size="15"/>
			
		</adsm:textbox>

        <adsm:textarea width="80%" columns="100" rows="3" maxLength="255" property="obItemTransferencia" 
        	label="observacao" labelWidth="20%" disabled="true"/>		
	</adsm:form>

	<adsm:grid idProperty="idItemTransferencia" property="itens" rows="6" 
		selectionMode="none"	    
		disableMarkAll="true"
		service="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findItensTransferencia"
		rowCountService="lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.getRowCountItensTransferencia" 
		autoSearch="false" onRowClick="findItemTransferenciaById" >
		
		<adsm:gridColumn width="35" title="documentoServico" property="tpDocumentoServico" isDomain="true"/>
		<adsm:gridColumnGroup separatorType="DOCTO_SERVICO">
			<adsm:gridColumn width="0" title="" property="sgFilialOrigem"/>	
			<adsm:gridColumn width="130" title="" property="nrDocumento" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="valorFrete" property="vlrFrete" dataType="currency"/>
		
		
		<%--adsm:gridColumn width="15%" title="tipoDocumento" property="tpDocumentoServico" isDomain="true"/>
		<adsm:gridColumn width="12%" title="documento" property="nrDocumento" dataType="integer"/--%>
		
		
		<adsm:gridColumn title="novoResponsavel" property="novoResponsavel.nmPessoa"/>
		<adsm:gridColumn title="motivoTransferencia" property="motivoTransferencia.dsMotivoTransferencia" />
		<adsm:buttonBar>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

		
	function findItemTransferenciaById(idItem){
		_serviceDataObjects = new Array();
		var dados = new Array();
        setNestedBeanPropertyValue(dados, "idItem", idItem );

        var sdo = createServiceDataObject("lms.contasreceber.efetivarRecebimentoTransferenciaDebitoAction.findItemTransferenciaById",
                                          "findItemTransferenciaById",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		return false;
	}
	
	function findItemTransferenciaById_cb(data,erro){

		if (data == undefined) return false;

		setElementValue( "responsavelAntigo.idResponsavelAntigo", data.responsavelAntigo.idResponsavelAntigo);
		setElementValue( "responsavelAntigo.nrIdentificacao", data.responsavelAntigo.nrIdentificacao);
		setElementValue( "responsavelAntigo.nmPessoa", data.responsavelAntigo.nmPessoa);

		setElementValue( "responsavelNovo.idResponsavelAntigo", data.responsavelNovo.idResponsavelAntigo);
		setElementValue( "responsavelNovo.nrIdentificacao", data.responsavelNovo.nrIdentificacao);
		setElementValue( "responsavelNovo.nmPessoa", data.responsavelNovo.nmPessoa);

		setElementValue( "motivoTransferencia", data.motivoTransferencia);
		setElementValue( "doctoServico.tpDocumento", data.doctoServico.tpDocumento);
		setElementValue( "doctoServico.sgFilial", data.doctoServico.sgFilial);
		setElementValue( "doctoServico.nrDoctoServico", data.doctoServico.nrDoctoServico);
		setElementValue( "obItemTransferencia", data.obItemTransferencia);
		
		setElementValue( "dsDivisaoClienteNovo", data.dsDivisaoClienteNovo);
		setElementValue( "dsDivisaoClienteAnterior", data.dsDivisaoClienteAntigo);
	}
	

	var tabGroup = getTabGroup(this.document);
	var abaCad = tabGroup.getTab("cad");

	function myDataLoad_cb(data,erros){
		onDataLoad_cb(data,erros);
		setProperties();

	}
	


	function setProperties(){
		setElementValue("idTransferencia", abaCad.getFormProperty("idTransferencia"));
		document.getElementById("idTransferencia").masterLink = "true";
		setElementValue("filialDestino.idFilial", abaCad.getFormProperty("filialByIdFilialDestino.idFilial"));
		setElementValue("filialOrigem.idFilial", abaCad.getFormProperty("filialByIdFilialOrigem.idFilial"));

		//masterLink
		setElementValue("_dtEmissao", abaCad.getFormProperty("dtEmissao"));
		setElementValue("_transferencia", 
			abaCad.getFormProperty("filialByIdFilialOrigem.sgFilial") + " - " + abaCad.getFormProperty("nrTransferencia") );
	}

	function onProcShow(evento){
		setProperties();
		findItensGrid();
	}
	
	function findItensGrid(){
		//var data = buildFormBeanFromForm(document.forms[0]);
		//alert(getElementValue("idTransferencia"));
		itensGridDef.executeSearch( { idTransferencia:getElementValue("idTransferencia") } );
		
	}
	
	function returnFalse(){ return false; }
	
	function initWindow(eventObj){
		if(eventObj.name == "tab_click"){
			cleanButtonScript(document);
		}
	}
</script>