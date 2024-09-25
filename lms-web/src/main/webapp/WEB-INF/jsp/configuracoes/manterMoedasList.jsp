<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.moedaService">
	<adsm:form action="/configuracoes/manterMoedas" idProperty="idMoeda">
		<adsm:hidden property="moedaPais.pais.unidadeFederativas.municipios.postoPassagems.idPostoPassagem"/>

		<adsm:textbox  dataType="text" property="dsMoeda" label="descricao" size="60" maxLength="60" width="50%"/>
		<adsm:textbox  dataType="text" property="sgMoeda" label="sigla" size="3" maxLength="3" width="20%" />

		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="50%"/>
		<adsm:textbox label="simbolo" property="dsSimbolo" dataType="text" size="5" maxLength="5" width="20%" />
		
		<adsm:hidden property="moedaPais.pais.idPais"/> 		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="moedas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMoeda" property="moedas" defaultOrder="dsMoeda:asc" rows="13" gridHeight="200">
	    <adsm:gridColumn width="20%" title="simbolo" property="dsSimbolo"/>
		<adsm:gridColumn width="20%" title="sigla" property="sgMoeda"/>
		<adsm:gridColumn width="40%" title="descricao" property="dsMoeda"/>
		<adsm:gridColumn width="20%" title="situacao" isDomain="true" property="tpSituacao"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
