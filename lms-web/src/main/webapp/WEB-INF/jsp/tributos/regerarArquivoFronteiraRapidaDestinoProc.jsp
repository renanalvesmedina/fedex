<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.tributos.regerarArquivoFronteiraRapidaDestinoAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/tributos/regerarArquivoFronteiraRapidaDestino" id="regerar.form">		

		<adsm:hidden property="tpStatusManifesto" serializable="true" />
		
		<adsm:hidden property="manifesto.tpManifesto" value="VN"/>		
		<adsm:hidden property="manifesto.idManifesto" />
		<adsm:hidden property="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia"/>
		<adsm:lookup label="manifestoViagem"
					 dataType="text"
					 property="manifesto.filialByIdFilialOrigem"
				 	 idProperty="idFilial" criteriaProperty="sgFilial" 
					 service="" 
					 action="" 
					 size="3" maxLength="3" picker="false" 
					 disabled="true" 
					 serializable="false" 
					 required="true" 
					 onDataLoadCallBack="enableManifestoViagemNacional"
					 onchange="return myFilialOnChange();">
					 
			<adsm:propertyMapping relatedProperty="manifesto.filialByIdFilialOrigem.pessoa.nmFantasia" 
			                      modelProperty="pessoa.nmFantasia"/>		 
			
			<adsm:lookup dataType="integer" 
						 property="manifesto.manifestoViagemNacional" 
						 idProperty="idManifestoViagemNacional" 
						 criteriaProperty="nrManifestoOrigem" 
						 service=""
						 action="" 
						 popupLabel="pesquisarManifestoViagem"
						 criteriaSerializable="true"
						 size="10" 
						 maxLength="8" 
						 mask="00000000" 
						 disabled="false" 
						 onPopupSetValue="myOnPopupSetValue"
						 onDataLoadCallBack="myOnDataLoadCallBackManifestoViagemNacional"
						 serializable="true"/>	
						 			
		</adsm:lookup> 

		<adsm:buttonBar>
			<adsm:button caption="regerar" id="btnRegerar" onclick="regerar()" disabled="false"/>
			<adsm:button caption="limpar" id="btnLimpar" onclick="clean()" disabled="false"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>
<script>

	//-------------------------------- Códigos para a lookup de manifesto -----------------------------------
	
	/**
	*	Realiza o onchange da filial
	*   Chamada por : lookup de manifestos de viagem 
	*/
	function myFilialOnChange(){
		
		var retorno = changeDocumentWidgetFilial({
						 	filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						 	documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional')
						 	}); 
		
		return retorno;
		
		
	}	
	
	/**
	*	No OnPageLoad_cb realiza o onchange do tipo de manifesto para poder setar
	*   o manifesto de viagem nacional como padrão
	*/
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		
		setDisabled("btnLimpar",false);
		setDisabled("btnRegerar", false);
		
		getElement("manifesto.manifestoViagemNacional.idManifestoViagemNacional").required = "true";
		
		onChangeTpManifesto();		
		findFilialUsuarioLogado();
	}
	
	function onChangeTpManifesto(){
		
		changeDocumentWidgetType({
						   documentTypeElement:getElement('manifesto.tpManifesto'), 
						   filialElement:document.getElementById('manifesto.filialByIdFilialOrigem.idFilial'), 
						   documentNumberElement:document.getElementById('manifesto.manifestoViagemNacional.idManifestoViagemNacional'), 
						   documentGroup:'MANIFESTO',
						   actionService:'lms.tributos.regerarArquivoFronteiraRapidaDestinoAction'
						   });
						   
		setDisabled('manifesto.filialByIdFilialOrigem.idFilial', true);
		setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
		
	}
	
	/**
	*	Retorno da busca da lookup de filial do manifesto viagem via digitação
	*
	*/
	function enableManifestoViagemNacional_cb(data) {
	   var r = manifesto_filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
	   if (r == true) {
	      setDisabled("manifesto.manifestoViagemNacional.idManifestoViagemNacional", false);
	      setFocus(document.getElementById("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
	   }
	}
	
	/**
	*	Retorno da busca da lookup de manifesto viagem via tela da lookup
	*
	*/
	function myOnPopupSetValue(data,error){
		if( data != undefined ){
		
			if( data.nrManifestoOrigem != undefined ){
			
				var nrManifestoOrigem;
				
				if( !isNaN(parseInt(data.nrManifestoOrigem,10)) ){
					nrManifestoOrigem = data.nrManifestoOrigem;	
				}
				else {	
					nrManifestoOrigem = data.nrManifestoOrigem.substr(4);
				}			
				
			}
			
			data.nrManifestoOrigem = nrManifestoOrigem;
		
			setElementValue('manifesto.filialByIdFilialOrigem.pessoa.nmFantasia',data.pessoa.nmFantasia);
			setElementValue('manifesto.filialByIdFilialOrigem.idFilial',data.idFilialOrigem);
			setElementValue('manifesto.filialByIdFilialOrigem.sgFilial',data.sgFilialOrigem);
			setElementValue('manifesto.manifestoViagemNacional.nrManifestoOrigem',data.nrManifestoOrigem);
			setElementValue('manifesto.manifestoViagemNacional.idManifestoViagemNacional',data.idManifestoViagemNacional);			
			setElementValue('tpStatusManifesto', data.tpStatusManifesto.value );					
			
			setFocus(document.getElementById("ocorrenciaDoctoServico.doctoServico.nrDoctoServico"));
			
		} else {
			setFocus('manifesto.manifestoViagemNacional.nrManifestoOrigem');
		}
	}
	
	/**
	*	Retorno da busca da lookup de manifesto viagem nacional via digitação
	*
	*/
	function myOnDataLoadCallBackManifestoViagemNacional_cb(data,error){
		var retorno =manifesto_manifestoViagemNacional_nrManifestoOrigem_exactMatch_cb(data);
		
		if( retorno == true && data[0] != undefined ){
	
			setElementValue('tpStatusManifesto', data[0].tpStatusManifesto.value );		
			
		}
		
		return retorno;
	}
	
	function regerar() {
	
		storeButtonScript("lms.tributos.regerarArquivoFronteiraRapidaDestinoAction.generateFronteiraRapidaDestino", 
		                  "myRetorno", 
		                  document.getElementById("regerar.form"));
		                  
	    setFocusOnFirstFocusableField();	
	}
	
	function myRetorno_cb(data,erro){
		setFocusOnFirstFocusableField();
		if( erro != undefined ){
			alert(erro);
			return false;
		} 
		
		if( data._value != undefined ) {
			alert(data._value);
			statusInicial();
		}		
		
	}

	function statusInicial() {
		onChangeTpManifesto();		
	}
	
	function clean(){
		resetValue("manifesto.manifestoViagemNacional.idManifestoViagemNacional");
		setFocusOnFirstFocusableField();	
		
	}
	
	function findFilialUsuarioLogado(){
		_serviceDataObjects = new Array();
		var dados = new Array();	
        addServiceDataObject(createServiceDataObject("lms.tributos.manterArquivoFronteiraRapidaOrigemAction.findFilialUsuarioLogado",
			"retornoFindFilialUsuarioLogado", 
			dados));

        xmit(false); 
	}
	
	function retornoFindFilialUsuarioLogado_cb(data,error){
		
		setFocusOnFirstFocusableField(document);
		if (error != undefined){
			alert(error);
			return false;
		}
		
		setElementValue("manifesto.filialByIdFilialOrigem.idFilial", data.filial.idFilial);
		setElementValue("manifesto.filialByIdFilialOrigem.sgFilial", data.filial.sgFilial);
		setElementValue("manifesto.filialByIdFilialOrigem.pessoa.nmFantasia", data.filial.pessoa.nmFantasia)
		
	}	
	
</script>

