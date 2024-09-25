<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function empresaCobrancaOnLoad(){
		onPageLoad();
		//setDisabled("pessoa.nrIdentificacao", true );
	     initPessoaWidget({ tpTipoElement:document.getElementById("pessoa.tpPessoa")
	     , tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
	       , numberElement:document.getElementById("pessoa.idPessoa") });		
	}
	
	
</script>
<adsm:window service="lms.configuracoes.empresaCobrancaService" onPageLoad="empresaCobrancaOnLoad" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/configuracoes/manterPessoas" idProperty="idEmpresaCobranca">

		<adsm:hidden property="pessoa.tpPessoa" value="J"/>
	
		<adsm:complement label="identificacao" labelWidth="18%" width="82%">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.list" service="lms.configuracoes.manterEmpresasCobrancaAction.findTpIdentificacaoPessoa"/>
			<adsm:textbox definition="IDENTIFICACAO_PESSOA" />
		</adsm:complement>	
		
		<adsm:textbox dataType="text" size="95%" labelWidth="18%" width="82%"
			property="pessoa.nmPessoa" label="nome" maxLength="50" depends="pessoa.nrIdentificacao"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS_PESSOA" 
			label="situacao" labelWidth="18%" width="32%" depends="pessoa.nrIdentificacao"/>

		<adsm:buttonBar freeLayout="true">
			<%--adsm:findButton callbackProperty="empresas"/--%>
			<adsm:button caption="consultar" id="btnConsultar" onclick="verificaNrIdentificacao(this)" buttonType="findButton" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid property="empresas" idProperty="idEmpresaCobranca" defaultOrder="pessoa_.nmPessoa"
		gridHeight="200" rows="12" unique="true">
		<adsm:gridColumn title="identificacao" property="pessoa.tpIdentificacao" isDomain="true" width="70" />
		<adsm:gridColumn title="" property="pessoa.nrIdentificacaoFormatado" width="100" align="right"/>
		
		<adsm:gridColumn title="nome" property="pessoa.nmPessoa" width="450"/>
		<adsm:gridColumn title="situacao" property="tpSituacao"  isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton service="lms.configuracoes.empresaCobrancaService.removeEmpresaCobrancaByIds"/>
		</adsm:buttonBar>

	</adsm:grid>
</adsm:window>
<script>

	function verificaNrIdentificacao(elem){
	
		var nrIdentificacao = document.getElementById("pessoa.nrIdentificacao");
		nrIdentificacao.unmaskTo = "0-9";
        im_init(nrIdentificacao);
        
		findButtonScript('empresas', elem.form);
		
	}

	/**
	 * Se o Tipo de Identificação for Júridico, então o dataType do Número de Identificação é CNPJ.
	 * Ao atribuir o tipo CNPJ é feita a formatação do campo, setando o focus
	 */
	function setTypeNrIdentificacao(){
		var tpIdentificacao = getElementValue("pessoa.tpIdentificacao");
		resetValue(document.getElementById("pessoa.nrIdentificacao"));

		var nrIdentificacao = document.getElementById("pessoa.nrIdentificacao");
		if (tpIdentificacao == "C" ){
			nrIdentificacao.dataType = "CNPJ";
		}else if(tpIdentificacao == "I" ){
			nrIdentificacao.dataType = "CUIT";
		}else if(tpIdentificacao == "R" ){
			nrIdentificacao.dataType = "RUT";
		}else if(tpIdentificacao == "U" ){
			nrIdentificacao.dataType = "RUC";
		}else{
			nrIdentificacao.dataType = "text";
		}
		setFocus(nrIdentificacao);
	}
	
	/**
	 * onChange de tpIdentificação
	 */
	function pessoa_tpIdentificacao_onChange(eThis){
		comboboxChange({e:eThis});
		resetValue(document.getElementById("pessoa.nrIdentificacao"));
		//setTypeNrIdentificacao();
	}
	
	function myPageLoadCallBack_cb(data, errors){
		onPageLoad_cb(data);
		//changeTypePessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa"), tpIdentificacaoElement:document.getElementById('pessoa.tpIdentificacao'), numberElement:document.getElementById('pessoa.idPessoa'), tabCmd:'list'});
	}

	
</script>