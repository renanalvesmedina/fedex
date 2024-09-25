<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/sim/emitirServicosLocalizacaoCliente" >
		
		<adsm:combobox property="tpRelatorio" label="tipoRelatorio" domain="DM_TIPO_RELATORIO_DOCUMENTO_SERVICO_CLI" 
					   labelWidth="20%" width="80%" required="true" />	
				
		<adsm:lookup action="/vendas/manterDadosIdentificacao" criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" criteriaSerializable="true"
					 dataType="text" exactMatch="true" idProperty="idCliente" label="remetente" maxLength="20" property="remetente" 
					 service="lms.sim.emitirServicosLocalizacaoClienteAction.findLookupCliente" size="20" 	
					 labelWidth="20%" 	width="80%"
					 onPopupSetValue="remetentePopup" 
					 onchange="return remetenteOnChange(this)">
			
			<adsm:propertyMapping relatedProperty="remetente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>			
			<adsm:textbox dataType="text" disabled="true" property="remetente.pessoa.nmPessoa" serializable="true" size="30"/>
		</adsm:lookup>

		<adsm:lookup action="/vendas/manterDadosIdentificacao" criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" criteriaSerializable="true"
					 dataType="text" exactMatch="true" idProperty="idCliente" label="destinatario" maxLength="20" property="destinatario" 
					 service="lms.sim.emitirServicosLocalizacaoClienteAction.findLookupCliente" size="20" 	
					 labelWidth="20%" 	width="80%"
					 onPopupSetValue="destinatarioPopup" 
					 onchange="return destinatarioOnChange(this)">
			
			<adsm:propertyMapping relatedProperty="destinatario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>			
			<adsm:textbox dataType="text" disabled="true" property="destinatario.pessoa.nmPessoa" serializable="true" size="30"/>
		</adsm:lookup>
		
		<adsm:combobox label="contato" 
						property="contato.idContato" 
						optionLabelProperty="nmContato" 
						optionProperty="idContato" 
						service="lms.sim.emitirServicosLocalizacaoClienteAction.findContatos"
						labelWidth="20%"
						width="80%"
						boxWidth="313">
			<adsm:propertyMapping criteriaProperty="remetente.idCliente" modelProperty="idPessoa"/>
			<adsm:propertyMapping criteriaProperty="destinatario.idCliente" modelProperty="idPessoa"/>
			<adsm:propertyMapping relatedProperty="contato.nmContato" modelProperty="nmContato"/>
		</adsm:combobox>
		
		<adsm:hidden property="contato.nmContato"/>		

		<adsm:lookup property="localizacaoMercadoria" idProperty="idLocalizacaoMercadoria" cellStyle="vertical-align:bottom;" criteriaProperty="cdLocalizacaoMercadoria" 
					service="lms.sim.emitirServicosLocalizacaoClienteAction.findLookupLocalizacaoMercadoria" dataType="integer" label="localizacaoMercadoria" size="3" 
					action="/sim/manterLocalizacoesMercadoria" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="1"
					exactMatch="true" maxLength="3" disabled="false" criteriaSerializable="true">
			<adsm:propertyMapping relatedProperty="localizacaoMercadoria.dsLocalizacaoMercadoria" modelProperty="dsLocalizacaoMercadoria" />
			<adsm:textbox property="localizacaoMercadoria.dsLocalizacaoMercadoria" dataType="text" size="60" disabled="true" />
		</adsm:lookup>
		
		<adsm:range label="periodoEmissao" required="true" labelWidth="20%" width="80%" maxInterval="31">
             <adsm:textbox dataType="JTDate" property="dhEmissaoInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dhEmissaoFinal" picker="true"/>
        </adsm:range>
        
        <adsm:combobox property="moedaPais.idMoedaPais" autoLoad="true"
				optionProperty="idMoedaPais" optionLabelProperty="moeda.siglaSimbolo"
				service="lms.sim.emitirServicosLocalizacaoClienteAction.findComboMoedaPais"
				label="converterParaMoeda" labelWidth="20%" width="80%" required="true" >
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.idMoeda" modelProperty="moeda.idMoeda" />
			<adsm:propertyMapping relatedProperty="moedaPais.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.siglaSimbolo" modelProperty="moeda.siglaSimbolo" />
			<adsm:propertyMapping relatedProperty="moedaPais.moeda.dsSimbolo" modelProperty="moeda.dsSimbolo" />
		</adsm:combobox>
        <adsm:hidden property="moedaPais.moeda.idMoeda" />
        <adsm:hidden property="moedaPais.pais.idPais" />
        <adsm:hidden property="moedaPais.moeda.siglaSimbolo" />
        <adsm:hidden property="moedaPais.moeda.dsSimbolo" />
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" 
					   labelWidth="20%" width="80%" required="true" defaultValue="pdf"/>	
		
		
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.sim.emitirServicosLocalizacaoClienteAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>	
</adsm:window>
<script>

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		findInfoUsuarioLogado();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateInfoUsuarioLogado();
		}
	}

	var infoUsuario = undefined;
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.entrega.emitirEficienciaEntregasAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		infoUsuario = data;
		populateInfoUsuarioLogado();
	}

	function populateInfoUsuarioLogado() {
	
		fillFormWithFormBeanData(document.forms[0].tabIndex, infoUsuario);
		comboboxChange({e:document.getElementById("moedaPais.idMoedaPais")});

	}

	function remetentePopup(data){
        if (data != undefined){
      		clearDestinatario();
      	}
      	return true;
    }
	
	function remetenteDataLoadExact_cb(data){
		//lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:"municipioDataLoadLikeEnd"});
      	if (data != undefined){
      		clearDestinatario();
      	}
    }

	function remetenteOnChange(obj){
		var retorno = remetente_pessoa_nrIdentificacaoOnChangeHandler();
		if (obj.value != ''){
			clearDestinatario();
		}
		
		return retorno;
	}
	
	function destinatarioPopup(data){
        if (data != undefined){
      		clearRemetente();
      	}
      	return true;
    }
	
	function destinatarioDataLoadExact_cb(data){
		//lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:"municipioDataLoadLikeEnd"});
      	if (data != undefined){
      		clearRemetente();
      	}
    }

	function destinatarioOnChange(obj){
		var retorno = destinatario_pessoa_nrIdentificacaoOnChangeHandler();
		if (obj.value != ''){
			clearRemetente();
		}
		
		return retorno;
	}

	function clearRemetente() {
		setElementValue("remetente.idCliente","");
		setElementValue("remetente.pessoa.nrIdentificacao","");
		setElementValue("remetente.pessoa.nmPessoa","");
	}

	function clearDestinatario() {
		setElementValue("destinatario.idCliente","");
		setElementValue("destinatario.pessoa.nrIdentificacao","");
		setElementValue("destinatario.pessoa.nmPessoa","");
	}
	
	/*function remetenteOnChange(obj){
		var retorno = remetente_pessoa_nrIdentificacaoOnChangeHandler();
		if (obj.value != ''){
			setElementValue("destinatario.idCliente","");
			setElementValue("destinatario.pessoa.nrIdentificacao","");
			setElementValue("destinatario.pessoa.nmPessoa","");
		}
		return retorno;
	}

	function destinatarioOnChange(obj){
		var retorno = destinatario_pessoa_nrIdentificacaoOnChangeHandler();
		if (obj.value != ''){
			setElementValue("remetente.idCliente","");
			setElementValue("remetente.pessoa.nrIdentificacao","");
			setElementValue("remetente.pessoa.nmPessoa","");
		}
		return retorno;
	}*/
</script>
