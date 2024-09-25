<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function myPageLoad() {
		onPageLoad();
 		initPessoaWidget({tpTipoElement:document.getElementById("pessoa.tpPessoa")
				,tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao")
				,numberElement:document.getElementById("pessoa.idPessoa")});
	}
</script>
<adsm:window service="lms.configuracoes.consultarInscricaoEstadualLogAction" onPageLoad="myPageLoad">
	<adsm:form action="/configuracoes/consultarInscricaoEstadualLog" >
		
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
			idProperty="idInscricaoEstadualLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="inscricaoEstadual.idInscricaoEstadual" title="inscricaoEstadual.idInscricaoEstadual" dataType="integer"/>
		<adsm:gridColumn property="pessoa.idPessoa" title="pessoa.idPessoa" dataType="integer"/>
		<adsm:gridColumn property="unidadeFederativa.idUnidadeFederativa" title="unidadeFederativa.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="nrInscricaoEstadual" title="nrInscricaoEstadual"/>
		<adsm:gridColumn property="blIndicadorPadrao" title="blIndicadorPadrao" renderMode="image-check"/>
		<adsm:gridColumn property="tpSituacao" title="tpSituacao" isDomain="true"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>