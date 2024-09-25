<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.reguladoraSeguradoraService">
	<adsm:form action="/seguros/manterSeguradorasReguladora" height="70">		

		<adsm:hidden property="reguladoraSeguro.idReguladora"/>
		<adsm:textbox label="corretoraSeguro" size="50" maxLength="50" width="85%" disabled="true" 
					  dataType="text" 
					  property="reguladoraSeguro.pessoa.nmPessoa"/>
		
		<adsm:lookup label="seguradora" size="20" maxLength="20" width="85%" 
					 idProperty="idSeguradora"
					 property="seguradora" 
					 criteriaProperty="pessoa.nrIdentificacao" 
					 action="/seguros/manterSeguradoras" 
					 service="lms.seguros.manterSeguradorasAction.findLookupSeguradora" 
					 dataType="text" 
			 		 afterPopupSetValue="afterPopupSeguradora"
			 		 onDataLoadCallBack="callBackSeguradora">
			<adsm:propertyMapping relatedProperty="seguradora.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="seguradora.pessoa.nmPessoa" size="40" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="reguladoraSeguradora" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="reguladoraSeguradora" idProperty="idReguladoraSeguradora" selectionMode="check" gridHeight="200" unique="true"
			   defaultOrder="reguladoraSeguro_pessoa_.nmPessoa, seguradora_pessoa_.nmPessoa:asc">
		<adsm:gridColumn property="reguladoraSeguro.pessoa.nrIdentificacaoFormatado" title="corretoraSeguro" width="117" align="right" />
		<adsm:gridColumn property="reguladoraSeguro.pessoa.nmPessoa" title="" width="273" />
		<adsm:gridColumn property="seguradora.pessoa.nrIdentificacaoFormatado" title="seguradora" width="117" align="right" />
		<adsm:gridColumn property="seguradora.pessoa.nmPessoa" title="" width="273" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script language="javascript">
	function afterPopupSeguradora(data){
		setElementValue("seguradora.pessoa.nrIdentificacao",data.pessoa.nrIdentificacaoFormatado);	
	}
	
	function callBackSeguradora_cb(data){
		seguradora_pessoa_nrIdentificacao_exactMatch_cb(data);
		if(data[0] != null){
			setElementValue("seguradora.pessoa.nrIdentificacao", data[0].pessoa.nrIdentificacaoFormatado);
		}
		return true;
	}
</script>