<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.consultarInformacaoDocServicoLogAction">
	<adsm:form action="/expedicao/consultarInformacaoDocServicoLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idInformacaoDocServicoLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="informacaoDocServico.idInformacaoDocServico" title="informacaoDocServico.idInformacaoDocServico" dataType="integer"/>
		<adsm:gridColumn property="tipoRegistroComplemento.idTipoRegistroComplemento" title="tipoRegistroComplemento.idTipoRegistroComplemento" dataType="integer"/>
		<adsm:gridColumn property="nrTamanho" title="nrTamanho" dataType="integer"/>
		<adsm:gridColumn property="dsCampo" title="dsCampo"/>
		<adsm:gridColumn property="blImprimeConhecimento" title="blImprimeConhecimento" renderMode="image-check"/>
		<adsm:gridColumn property="tpCampo" title="tpCampo" isDomain="true"/>
		<adsm:gridColumn property="tpSituacao" title="tpSituacao" isDomain="true"/>
		<adsm:gridColumn property="nrDecimais" title="nrDecimais" dataType="integer"/>
		<adsm:gridColumn property="blOpcional" title="blOpcional" renderMode="image-check"/>
		<adsm:gridColumn property="dsFormatacao" title="dsFormatacao"/>
		<adsm:gridColumn property="blIndicadorNotaFiscal" title="blIndicadorNotaFiscal" renderMode="image-check"/>
		<adsm:gridColumn property="nrVersao" title="nrVersao" dataType="integer"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>