<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function myOnPageLoad(){  
		onPageLoad();
		//setDisabled("pessoa.nrIdentificacao", true );
	     initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.nrIdentificacao") });		
	}
	
	
</script>
<adsm:window service="lms.configuracoes.manterPessoasAction" onPageLoad="myOnPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-27053" />
		<adsm:include key="LMS-00049" />
		<adsm:include key="pessoa" />
	</adsm:i18nLabels>
	<adsm:form action="/configuracoes/manterPessoas">
		<%--adsm:combobox property="tpPessoa" onchange="trocaTpIdentificacao(this);" labelWidth="15%" width="35%" domain="DM_TIPO_PESSOA" label="tipoPessoa"/>		
		<adsm:complement labelWidth="15%" width="35%" label="identificacao">
			<adsm:combobox property="tpIdentificacao" width="15%" domain="DM_TIPO_IDENTIFICACAO"
				onchange="trocaTpIdentificacao(this);"/>
			<adsm:textbox dataType="text" property="nrIdentificacao" width="20%" size="20" maxLength="20"/>
		</adsm:complement--%>	
        <adsm:combobox property="pessoa.tpPessoa" onlyActiveValues="true" labelWidth="15%" width="35%" label="tipoPessoa" domain="DM_TIPO_PESSOA" definition="TIPO_PESSOA.list" />	
		<adsm:complement label="identificacao" >
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" />
			<adsm:textbox definition="IDENTIFICACAO_PESSOA" serializable="true"/>
		</adsm:complement>	
				
		<adsm:textbox dataType="text" size="95%" labelWidth="15%" width="82%" property="nmPessoa" label="nome" maxLength="50"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" onclick="pesquisarPessoas(this)" disabled="false" buttonType="findButton"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idPessoa" property="pessoa" gridHeight="200" rows="13" defaultOrder="nmPessoa" unique="true" onRowClick="myRowClick">
		<adsm:gridColumn title="identificacao"	property="tpIdentificacao" isDomain="true" width="55"/>
		<adsm:gridColumn title="" 			   	property="nrIdentificacaoFormatado" width="120" align="right"/>
		<adsm:gridColumn title="nome" 			property="nmPessoa" width="62%" />
		<adsm:gridColumn title="tipoPessoa" 	property="tpPessoa" width="" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">

    document.getElementById("pessoa.nrIdentificacao").serializable="true"  ;

	function validatePesquisar(){
		var nrIdentificacao = document.getElementById("pessoa.nrIdentificacao");
		var nmPessoa = document.getElementById("nmPessoa");

		// Caso o nome da pessoa tenha menos de 2 caracteres ou o número de identificação seja nulo, lança a mensagem LMS-27053
		if (trim(nmPessoa.value).length < 2 && trim(nrIdentificacao.value) == "" ){
			return 1;
		}
		
		// Caso o número de identificação não contenha 5 caracteres validos, lança a mensagem LMS-00049
		if( trim(nmPessoa.value).length < 2 && nrIdentificacao.value.replace(/[^a-zA-Z0-9%]/gi, '').length < 5 ){
			return 2;
		}
		
		return 0;
	}

	/**
	 * Valida o nome da pessoa e o nrIdentificação
	 */
	function pesquisarPessoas(obj){
		
		var retorno = validatePesquisar();
		
		if ( retorno != 0 ){		
			
			if ( retorno == 1){
				alert(''+i18NLabel.getLabel("LMS-27053"));
			}else if ( retorno == 2 ){
				alert(''+i18NLabel.getLabel("LMS-00049"));
			}
			
			setFocusOnFirstFocusableField();
			return false;
		}
		
		return findButtonScript('pessoa', obj.form);
	}
	
	/**
	 * Desabilita a aba de detalhe no selecionar da grig
	 */	
	function onShowAba(abaSource, abaTarget){
		if (abaSource != undefined){
			//ref para tabgroup 	
			var tabGroup = getTabGroup(this.document);			
			//ref para tab cad
			var tabCad = tabGroup.getTab("cad");
			
			tabCad.setDisabled(true);
			
			//executar o código padrão do onShow da tab
			tab_onShow();
		}
	}     
	
	/**
	 * Habilita a aba de detalhe no selecionar da grig
	 */
	function myRowClick(pkValue){
		//ref para tabgroup    	
		var tabGroup = getTabGroup(this.document);
		if (tabGroup) {
			//ref para tab cad		
			var tabCad = tabGroup.getTab("cad");
			tabCad.setDisabled(false);
		}	
	}
</script>
