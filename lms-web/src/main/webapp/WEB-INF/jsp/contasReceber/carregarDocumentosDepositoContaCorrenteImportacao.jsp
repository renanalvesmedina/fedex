<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.carregarDocumentosDepositoContaCorrenteAction" onPageLoadCallBack="myOnPageLoad">
	
	<adsm:form action="/contasReceber/carregarDocumentosDepositoContaCorrente">
	
		<adsm:lookup label="cliente"
					 service="lms.contasreceber.carregarDocumentosDepositoContaCorrenteAction.findLookupCliente" 
					 dataType="text"
					 property="cliente" 
					 idProperty="idCliente"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 exactMatch="true" 
					 size="20"
					 maxLength="20" 
					 width="85%" 
					 action="/vendas/manterDadosIdentificacao"
					 required="true">
				<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="nomeCliente" />
				<adsm:propertyMapping modelProperty="pessoa.nrIdentificacao" formProperty="nrIdentificacao" />
				<adsm:textbox dataType="text" property="nomeCliente" disabled="true" size="30" />	
		</adsm:lookup>
		<adsm:hidden property="nrIdentificacao" serializable="true"/>
	
		<adsm:textbox label="arquivo" property="arquivo" dataType="file" width="85%" size="72" required="true"/>
	
		<adsm:buttonBar>
			<adsm:storeButton caption="importarArquivo" 
					id="btnImportar"
					service="lms.contasreceber.carregarDocumentosDepositoContaCorrenteAction.executeImportDocumentosDepositadosContaCorrente" 
					callbackProperty="retornoImport" />
			<adsm:resetButton id="btnLimpar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>
<script>
	function retornoImport_cb(data,erro){
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('cliente.pessoa.nrIdentificacao'));
			return false;
		} else {
			showSuccessMessage();
			setFocus(document.getElementById('btnLimpar'),true,true);
		}
	}
	
	function myOnPageLoad_cb(data, erro){
		onPageLoad_cb(data,erro);
		setDisabled('btnImportar',false);
		resetFileElementValue(document.getElementById('arquivo'));
	}
	
	function validateTab() {
		if (!validateTabScript(document.forms)) {
			if (getElementValue("cliente.idCliente") != "") {
				setFocus("arquivo_browse", false);
			}
			return false;
		}
		return true;
	}
</script>