<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/sim/consultarLocalizacoesMercadorias" height="220">

		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="registronumero" label="numeroRegistro" disabled="true"/>
		<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="registrostatus" label="status" disabled="true"/>

		<adsm:textbox width="82%" labelWidth="18%" size="10" dataType="text" property="registrodata" label="dataEmissao" disabled="true"/>

		<adsm:textbox label="remetente" labelWidth="18%" width="11%" size="10" maxLength="20" dataType="text" property="filialSigla" disabled="true"/>
        <adsm:textbox width="21%" size="20" dataType="text" property="nomeRemetente" disabled="true"/>
		<adsm:textbox label="destinatario" labelWidth="18%" width="11%" size="10" maxLength="20" dataType="text" property="filialSigla" disabled="true" />
        <adsm:textbox width="21%" size="20" dataType="text" property="nomeDestinatario" disabled="true"/>

		<adsm:section caption="controleCargas"/>
			<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="numero" label="numero" disabled="true"/>
			<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="destino" label="destino" disabled="true"/>
	
			<adsm:textbox width="82%" labelWidth="18%" size="10" dataType="text" property="emissao" label="emissao" disabled="true"/>
	
			<adsm:textbox label="meioTransporte" labelWidth="18%" width="11%" size="10" maxLength="20" dataType="text" property="meioTransporte.codigo" disabled="true"/>
	        <adsm:textbox width="21%" size="20" dataType="text" property="meioTransporteNome" disabled="true"/>
			<adsm:textbox label="semiReboque" labelWidth="18%" width="11%" size="10" maxLength="20" dataType="text" property="semiReboque" disabled="true" />
	        <adsm:textbox width="21%" size="20" dataType="text" property="semiReboqueNome" disabled="true"/>

		<adsm:section caption="manifesto"/>
			<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="numero2" label="numero" disabled="true"/>
			<adsm:textbox width="32%" labelWidth="18%" size="25" dataType="text" property="destino2" label="destino" disabled="true"/>
	</adsm:form>
</adsm:window>   