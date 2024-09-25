<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.reguladoraSeguradoraService">
	<adsm:form action="/seguros/manterSeguradorasReguladora" height="390" idProperty="idReguladoraSeguradora">

		<adsm:hidden property="tipoSituacaoSeguradora" value="A" />
		<adsm:hidden property="reguladoraSeguro.idReguladora"/>
		<adsm:textbox label="corretoraSeguro" size="50" maxLength="50" width="85%" disabled="true" 
					  dataType="text" 
					  property="reguladoraSeguro.pessoa.nmPessoa"/>

		<adsm:lookup label="seguradora" size="20" maxLength="20" width="85%" dataType="text" required="true"
					 idProperty="idSeguradora"
					 property="seguradora" 
					 criteriaProperty="pessoa.nrIdentificacao"
					 action="/seguros/manterSeguradoras" 
					 service="lms.seguros.manterSeguradorasAction.findLookupSeguradora"
			 		 afterPopupSetValue="afterPopupSeguradora"
			 		 onDataLoadCallBack="callBackSeguradora">
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tipoSituacaoSeguradora"/>
			<adsm:propertyMapping relatedProperty="seguradora.pessoa.nmPessoa"   modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="seguradora.pessoa.nmPessoa" size="40" maxLength="50" disabled="true"/>
		</adsm:lookup>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
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