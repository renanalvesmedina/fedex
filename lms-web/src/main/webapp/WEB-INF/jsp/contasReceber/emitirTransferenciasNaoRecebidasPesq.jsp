<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirTransferenciasNaoRecebidasAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirTransferenciasNaoRecebidas">

		<adsm:hidden property="siglaNomeFilial" serializable="true" />
		<adsm:hidden property="dsSituacaoTransferencia" serializable="true" />

		<adsm:lookup dataType="text" 
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.contasreceber.emitirTransferenciasNaoRecebidasAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 label="filialDestino" 
					 size="3" 
					 maxLength="3" 
					 onchange="return limpaSgFilialNomeFilial()"
					 width="80%"
					 labelWidth="20%"
					 exactMatch="true"
					 required="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>			
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="31" maxLength="30" disabled="true"/>
		</adsm:lookup>
		<adsm:hidden property="siglaFilial" serializable="true" />
		
        <adsm:combobox label="situacaoTransferencia" 
                       property="tpSituacaoTransferencia" 
                       labelWidth="20%" 
                       onchange="setaDescricao(this)"
                       domain="DM_STATUS_TRANSFERENCIA"/>
        
        <adsm:range label="dataEmissao" labelWidth="20%" width="80%" maxInterval="31" required="true">
			<adsm:textbox property="dataInicial" dataType="JTDate"/>		
			<adsm:textbox property="dataFinal" dataType="JTDate"/>		
		</adsm:range>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio" domain="DM_FORMATO_RELATORIO" required="true" labelWidth="20%" width="80%" defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contasreceber.emitirTransferenciasNaoRecebidasAction"/>
			<adsm:button caption="limpar" id="btnLimpar" onclick="myLimparOnclick(this)" buttonType="resetButton" disabled="false"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>
<script>

	function myLimparOnclick(elem){
		cleanButtonScript(elem.document);
		buscaFilialUsuarioLogado();
	}

	/**
	*	Executa a busca da filial do usuário para setá-la como padrão na lookup de filial
	*
	*/
	function myOnPageLoadCallBack_cb(data, erro){
	
		//setDisabled('btnVisualizar',false);
		
		onPageLoad_cb(data, erro);
		buscaFilialUsuarioLogado();			
		
	}
	
	function buscaFilialUsuarioLogado(){
		var dados = new Array();
         
        var sdo = createServiceDataObject("lms.contasreceber.emitirTransferenciasNaoRecebidasAction.findFilialUsuario",
                                          "retornoBuscaFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});	
	}
	
	/**
	*	Seta a filial do usuário como padrão.
	* 	Se filial usuario for 'MTZ' pesquisa somente filiais ativas
	*/
	function retornoBuscaFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('filial.sgFilial'));
			return false;		
		}
		
		setElementValue('filial.idFilial',data.filial.idFilial);
		setElementValue('filial.sgFilial',data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		setElementValue('siglaNomeFilial',data.siglaNomeFilial);
		
	}
	
	/**
	*	Seta a descrição da situação de transferencia selecionada
	*/
	function setaDescricao(elemento){

		comboboxChange({e:elemento});
		
		var situacao = document.getElementById('tpSituacaoTransferencia');
		
		if( situacao.selectedIndex != 0 ){
			setElementValue('dsSituacaoTransferencia',situacao.options[situacao.selectedIndex].text);
		} else {
			resetValue('dsSituacaoTransferencia');
		}
		
	}
	
	function limpaSgFilialNomeFilial(){
		var retorno = filial_sgFilialOnChangeHandler();
		resetValue('siglaNomeFilial');
		return retorno;
	}
</script>