<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function myPageLoad() {
		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
				,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
				,numberElement:document.getElementById("pessoa.idPessoa")});
	}
</script>
<adsm:window service="lms.configuracoes.consultarPessoaLogAction" onPageLoad="myPageLoad">
	<adsm:form action="/configuracoes/consultarPessoaLog" >
		
		<adsm:hidden property="pessoa.tpPessoa" value="F" serializable="false"/>
		<adsm:hidden property="pessoa.dsEmail" serializable="false"/>
		<adsm:complement label="identificacao" labelWidth="17%" width="33%">		
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad" serializable="false" required="true"/>		
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" service="lms.contratacaoveiculos.manterMotoristasAction.validateIdentificacao" required="true"/>		
		</adsm:complement>
		
		<adsm:textbox dataType="text" property="pessoa.nmPessoa" label="nome" size="30" maxLength="50" labelWidth="17%" width="33%" required="false" depends="pessoa.nrIdentificacao"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog"
			idProperty="idPessoaLog"
			selectionMode="none"
			width="1800"
			scrollBars="horizontal"
			onRowClick="rowClick"
			rows="12" 
  	>
		
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
  		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
  		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>

		<adsm:gridColumn property="nmPessoa" title="nome" width="200"/>
		<adsm:gridColumn property="nrIdentificacao" title="identificacao" width="150"/>
		<adsm:gridColumn property="tpIdentificacao" title="tipoIdentificacao" isDomain="true"/>
		<adsm:gridColumn property="dsEmail" title="email" width="150"/>
		<adsm:gridColumn property="nrRg" title="numeroRG"/>
		<adsm:gridColumn property="dtEmissaoRg" title="dataEmissaoRG" dataType="JTDate"/>	
		<adsm:gridColumn property="dsOrgaoEmissorRg" title="orgaoEmissorRG"/>
		<adsm:gridColumn property="nmFantasia" title="nomeFantasia" width="200" />
		<adsm:gridColumn property="nrInscricaoMunicipal" title="inscricaoMunicipal" width="150"/>
		<adsm:gridColumn property="blAtualizacaoCountasse" title="atualizacaoCountasse" renderMode="image-check"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>
<script>
function rowClick(){
	return false;
}
</script>