<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction">
	<adsm:form action="/configuracoes/manterDistribuicaoFreteInternacional" idProperty="idTramoFreteInternacional"
		service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findTramoById"
		onDataLoadCallBack="myDataLoadCallBack">

		<adsm:masterLink showSaveAll="true" idProperty="idDistrFreteInternacional">
			<adsm:masterLinkItem property="filialOrigem.siglaNomeFilial" label="filialOrigem"/>
			<adsm:masterLinkItem property="filialDestino.siglaNomeFilial" label="filialDestino"/>
			<adsm:masterLinkItem property="cdPermisso" label="permisso"/>
		</adsm:masterLink>

		<adsm:textbox label="numero" property="nrTramoFreteInternacional" dataType="integer" size="2" maxLength="2" required="true" minValue="1"/>

		<adsm:textbox label="percentualFrete" property="pcFrete" dataType="percent" size="6" maxLength="6" labelWidth="25%" width="25%" required="true" minLength="0" minValue="1" maxValue="100" mask="##0.00"/>

		<adsm:textbox label="descricao" property="dsTramoFreteInternacional" dataType="text" size="98" maxLength="60" width="85%" required="true"/>
		
		<adsm:hidden property="statusAtivo" serializable="false" value="A"/>

		<adsm:lookup label="cliente"
					 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupClientes" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="85%" 
					 action="/vendas/manterDadosIdentificacao">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="cliente.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" formProperty="nrIdentificacao" />
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" disabled="true" size="30" />
			<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao" />
		</adsm:lookup>
		
		<adsm:hidden property="nrIdentificacao" serializable="true"/>

		<adsm:checkbox property="blCruze" 
					   onclick="return validateBlCruze(false);"
					   label="cruze" 
					   labelWidth="15%" 
					   width="35%"/>
		
		<adsm:checkbox property="blTramoOrigem" 
					   label="tramoOrigem" 
					   labelWidth="15%" 
					   width="35%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button buttonType="storeButton" caption="incluirTramo" disabled="false" onclick="incluirTramo(this)"/>
			<adsm:newButton/>
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid idProperty="idTramoFreteInternacional" property="tramoFreteInternacionais" gridHeight="200" 
		autoSearch="false" unique="true" rows="7"
		showGotoBox="true" showPagging="true" detailFrameName="tramos"
		rowCountService="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.getRowCountTramos"
		service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findPaginatedTramos">
		<adsm:gridColumn title="numero" property="nrTramoFreteInternacional" width="100" dataType="integer"/>
		<adsm:gridColumn title="descricao" property="dsTramoFreteInternacional" width="200"/>
		
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn width="0" title="cliente" property="cliente.pessoa.nrIdentificacaoFormatado" dataType="text"/>	
			<adsm:gridColumn width="360" title="" property="cliente.pessoa.nmPessoa" dataType="text"/>
		</adsm:gridColumnGroup>			

		<adsm:gridColumn title="percentualFrete" property="pcFrete" width="" dataType="currency"/>
		<adsm:buttonBar>
			<adsm:removeButton caption="excluirTramo" 
				service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.removeTramoByIds"/>
		</adsm:buttonBar>
	</adsm:grid>
	
	<adsm:i18nLabels>
		<adsm:include key="LMS-27018"/>
	</adsm:i18nLabels>
	
</adsm:window>
<script>

	/**
	  * Função chamada no ínicio da página
	  */
	function initWindow(event){
		validateBlCruze(true);
	}
	
	/**
	  * Função chamada no storeButton 
	  */
	function incluirTramo(eThis){
		setElementValue("nrIdentificacao", getElementValue("cliente.pessoa.nrIdentificacao"));

		storeButtonScript('lms.configuracoes.manterDistribuicaoFreteInternacionalAction.saveTramo', 'storeItem', eThis.form);	
	}
	
	/**
	  * DataLoad_cb override 
	  */
	function myDataLoadCallBack_cb(data, error){
		onDataLoad_cb(data, error);
		validateBlCruze(false);
	}
	
	/**
	  * REGRA 3.2 DA ESPECIFICAÇÃO
	  */
	function validateBlCruze(clicButton){
		
		if(getElement("blCruze").checked){
			
			setElementValue("pcFrete", "0,00");
			setDisabled("pcFrete", true);
			
		}else if(!getElement("blCruze").checked || clicButton){
			
			setDisabled("pcFrete", false);
			
		}
		
	}
	
	/**
	  * Função chamada no show da página
	  */
	function myOnShow(){
		validateBlCruze(true);
	}
	
</script>