<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction" >
	<adsm:form action="/sim/consultarLocalizacaoDetalhadaWeb" service="lms.sim.consultarLocalizacoesMercadoriasAction.findByIdDetalhamento"  idProperty="idDoctoServico" onDataLoadCallBack="onDataLoadCustom">
		<adsm:hidden property="tpDocumentoServicoLinkProperty"/>
		<adsm:hidden property="notaFiscalConhecimento" value=""/>
		
		<adsm:hidden property="nrIdentificacaoReme"/>
		<adsm:hidden property="nmPessoaReme"/>
		<adsm:hidden property="idPessoaReme"/>
		<adsm:hidden property="nrIdentificacaoDest"/>
		<adsm:hidden property="nmPessoaDest"/>
		<adsm:hidden property="idPessoaDest"/>
		<adsm:hidden property="nrIdentificacaoConsi"/>
		<adsm:hidden property="nmPessoaConsi"/>
		<adsm:hidden property="idPessoaConsi"/>
	    <adsm:hidden property="idFilial"/>
				
		<adsm:textbox disabled="true" property="tpDocumentoServico" label="documentoServico" labelWidth="18%" width="32%" size="8" dataType="text">
		
        <adsm:textbox disabled="true"  size="3" dataType="text" property="dsSgFilial"/>
        
		<adsm:textbox disabled="true" property="nrDoctoServico" dataType="integer" size="12"  mask="00000000" />
		
		<adsm:textbox disabled="true" property="dvConhecimento" dataType="integer" size="1" />
		
		</adsm:textbox>

		<adsm:textbox dataType="text" size="30" property="tpConhecimento" label="finalidade" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:textbox dataType="text" size="3" property="filialDestino" label="filialDestino" labelWidth="18%" width="32%" disabled="true">
		<adsm:textbox dataType="text" property="nmFantasiaDestino" disabled="true" size="30"/>
		</adsm:textbox>
		
		
		<adsm:textbox dataType="text" size="45" property="dsServico" label="servico" labelWidth="18%" width="32%" disabled="true"/>

		<adsm:textbox dataType="text" size="25" property="dsDhEmissao" label="emitidoEm"  labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:textbox dataType="text" size="30" property="tpFrete" label="tipoFrete" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:textbox dataType="text" size="15" property="vlTotalParcelas" style="text-align:right" label="valorFrete" labelWidth="18%" width="32%" disabled="true"/>
		
		<adsm:textbox dataType="text" size="45" property="dsLocalMercadoria" label="localizacao" labelWidth="18%" width="32%" disabled="true"/>
	</adsm:form>
	<adsm:buttonBar freeLayout="false">
		<adsm:button caption="consultarMCDButton" action="/municipios/consultarMCD" cmd="main" disabled="false" boxWidth="265"/>
		<adsm:button caption="solicitacaoPriorizacaoEmbarque" action="/sim/registrarSolicitacoesEmbarque" cmd="main" disabled="false" boxWidth="230">
			<adsm:linkProperty src="tpDocumentoServicoLinkProperty" target="doctoServico.tpDocumentoServico"/>
			<adsm:linkProperty src="dsSgFilial" target="doctoServico.filialByIdFilialOrigem.sgFilial"/>
			<adsm:linkProperty src="nrDoctoServico" target="doctoServico.nrDoctoServico"/>
			<adsm:linkProperty src="idDoctoServico" target="doctoServico.idDoctoServico"/>
			<adsm:linkProperty src="idDoctoServico" target="idDoctoServico"/>
			<adsm:linkProperty src="notaFiscalConhecimento" target="notaFiscalConhecimento.idDoctoServico" disabled="true"/>
			
			<adsm:linkProperty src="nrIdentificacaoReme" target="remetentePessoaNrIdentificacao"/>
			<adsm:linkProperty src="nmPessoaReme" target="remetentePessoaNmPessoa"/>
			<adsm:linkProperty src="idPessoaReme" target="remetenteIdCliente"/>
			<adsm:linkProperty src="nrIdentificacaoDest" target="destinatarioPessoaNrIdentificacao"/>
			<adsm:linkProperty src="nmPessoaDest" target="destinatarioPessoaNmPessoa"/>
			<adsm:linkProperty src="idPessoaDest" target="destinatarioIdCliente"/>
			
			<adsm:linkProperty src="tpDocumentoServicoLinkProperty" target="tpDocumentoServicoLocMerc"/>
			<adsm:linkProperty src="dsSgFilial" target="sgFilialDoctoServLocMerc"/>
			<adsm:linkProperty src="idFilial" target="idFilialDoctoServLocMerc"/>
			<adsm:linkProperty src="nrDoctoServico" target="nrDoctoServicoLocMerc"/>
			<adsm:linkProperty src="idDoctoServico" target="idDoctoServicoLocMerc"/>
			
			
		</adsm:button>
		
		<adsm:button caption="solicitacaoRetirada" action="/sim/registrarSolicitacoesRetirada" cmd="main" disabled="false" boxWidth="160">
			<adsm:linkProperty src="tpDocumentoServicoLinkProperty" target="doctoServico.tpDocumentoServico"/>
			<adsm:linkProperty src="dsSgFilial" target="doctoServico.filialByIdFilialOrigem.sgFilial"/>
			<adsm:linkProperty src="nrDoctoServico" target="doctoServico.nrDoctoServico"/>
			<adsm:linkProperty src="idDoctoServico" target="doctoServico.idDoctoServico"/>
			<adsm:linkProperty src="idDoctoServico" target="idDoctoServico"/>
						
			<adsm:linkProperty src="nrIdentificacaoReme" target="remetentePessoaNrIdentificacao"/>
			<adsm:linkProperty src="nmPessoaReme" target="remetentePessoaNmPessoa"/>
			<adsm:linkProperty src="idPessoaReme" target="remetenteIdCliente"/>
			<adsm:linkProperty src="nrIdentificacaoDest" target="destinatarioPessoaNrIdentificacao"/>
			<adsm:linkProperty src="nmPessoaDest" target="destinatarioPessoaNmPessoa"/>
			<adsm:linkProperty src="idPessoaDest" target="destinatarioIdCliente"/>
			
			<adsm:linkProperty src="nrIdentificacaoConsi" target="consignatario.pessoa.nrIdentificacao"/>
			<adsm:linkProperty src="nmPessoaConsi" target="consignatario.pessoa.nmPessoa"/>
			<adsm:linkProperty src="idPessoaConsi" target="consignatario.idCliente" />
						
			<adsm:linkProperty src="notaFiscalConhecimento" target="notaFiscalCliente.idNotaFiscalConhecimento" disabled="true"/>
		</adsm:button>
		
		<adsm:button id="reportButton" caption="visualizar" disabled="false" onclick="reportButton()"/>
		
	</adsm:buttonBar>
	<adsm:tabGroup selectedTab="0">
		<adsm:tab title="principal" id="principal" src="/sim/consultarLocalizacoesMercadorias" cmd="principal" height="256" onShow="buscaIdDoctoServicoAbaPrincipal" autoLoad="true"/>
		
		<adsm:tab title="integrantes" id="integrantes" src="/sim/consultarLocalizacoesMercadorias" cmd="integrantes" height="256" autoLoad="false" onShow="findPaginatedIntegrantes"/>
		
		<adsm:tab title="parcerias" disabled="false" id="parcerias" src="/sim/consultarLocalizacoesMercadorias" cmd="parcerias" height="256" autoLoad="false" onShow="buscaIdDoctoServicoAbaParcerias"/>
		
		<adsm:tab title="complementos" id="complementos" src="/sim/consultarLocalizacoesMercadorias" cmd="complementos" height="256" autoLoad="false" onShow="buscaIdDoctoServico"/>
		
		<adsm:tab title="frete" id="frete" src="/sim/consultarLocalizacoesMercadorias" cmd="frete" height="256" autoLoad="false" onShow="buscaIdDoctoServicoAbaFrete"/>
		
		<adsm:tab title="cobranca" id="cobranca" src="/sim/consultarLocalizacoesMercadorias" cmd="cobranca" height="256" autoLoad="false" onShow="findPaginatedDevedor"/>
		
		<adsm:tab title="CC" id="CC" src="/sim/consultarLocalizacoesMercadorias" cmd="CC" height="256" autoLoad="false" onShow="buscaIdControleCargaByDoctoServico"/>
		
		<adsm:tab title="manifestos" id="manifestos" src="/sim/consultarLocalizacoesMercadorias" cmd="manifestos" height="256" autoLoad="false" onShow="buscaIdDoctoServicoAbaManifesto"/>
		
		<adsm:tab title="eventos" id="eventos" src="/sim/consultarLocalizacoesMercadorias" cmd="eventos" height="256" autoLoad="false" onShow="findEventosByIdDocto"/>
		
		<adsm:tab title="bloqLiberacoes" disabled="true" id="bloqueios" src="/sim/consultarLocalizacoesMercadorias" cmd="bloqueios" height="256" autoLoad="false" onShow="findBloqueiosLiberacoesByIdDocto"/>
		
		<adsm:tab title="RNC" id="RNCROI" disabled="true" src="/sim/consultarLocalizacoesMercadorias" cmd="RNCROI" height="256" autoLoad="false" onShow="findByIdDetalhamentoRNC"/>
	</adsm:tabGroup>
</adsm:window>
<script>
	
	function buscaIdDoctoServico(){
		cleanButtonScript(this.document);
		var tabGroup = getTabGroup(this.document);
		
		var tabCons = tabGroup.getTab("doc");
	    var idDoctoServicoConsulta = tabCons.getFormProperty("idDoctoServicoConsulta");
	    
	    if(idDoctoServicoConsulta != ""){
	    		setElementValue("idDoctoServico",idDoctoServicoConsulta);
				var form = document.forms[0];
				var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{idDoctoServico:getElementValue("idDoctoServico")});
				xmit({serviceDataObjects:[sdo]});
	    }else{
		
		    var tabList = tabGroup.getTab("list");
		    var idDoctoServico = tabList.getFormProperty("idDoctoServicoSelecionado");
		    setElementValue("idDoctoServico",idDoctoServico);
		       
		    _serviceDataObjects = new Array();
		   	addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findByIdDetalhamento", "onDataLoadCustom", {idDoctoServico:idDoctoServico}));
		  	xmit();
		  	
		  	
	  	}
	  	
	}

	function onDataLoadCustom_cb(data,exception){
		onDataLoad_cb(data,exception);
		var idDoctoServico = getNestedBeanPropertyValue(data,"idDoctoServico");
		_serviceDataObjects = new Array();
		addServiceDataObject(createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.findRespostaAbasDetalhamento", "retorno", {idDoctoServico:idDoctoServico}));
		xmit();
	  	
	}

	function retorno_cb(data, exception){
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab('cad');
		if(data != null){
			if(getNestedBeanPropertyValue(data,"abaRNC")!= undefined)
				tabDet.childTabGroup.setDisabledTab('RNCROI', false);
			else	
				tabDet.childTabGroup.setDisabledTab('RNCROI', true);
				
			if(getNestedBeanPropertyValue(data,"abaBloqueio")!= undefined)	
				tabDet.childTabGroup.setDisabledTab('bloqueios', false);
			else 	
			   	tabDet.childTabGroup.setDisabledTab('bloqueios', true);
				
			if(getNestedBeanPropertyValue(data,"abaParceiras")!= undefined)	
				tabDet.childTabGroup.setDisabledTab('parcerias', false);
			else
				tabDet.childTabGroup.setDisabledTab('parcerias', true);	
				
			if(getNestedBeanPropertyValue(data,"abaCC")!= undefined)	
				tabDet.childTabGroup.setDisabledTab('CC', false);
			else
				tabDet.childTabGroup.setDisabledTab('CC', true);		
		}
		tabDet.childTabGroup.selectTab("principal",{name:'tab_click'},true);
		
	}

	function reportButton() {	
		var data = {idDoctoServico:getElementValue('idDoctoServico')};
		var sdo = createServiceDataObject("lms.sim.consultarLocalizacoesMercadoriasAction.execute", "openPdf", data);
    	executeReportWindowed(sdo, "pdf");
	}

</script>