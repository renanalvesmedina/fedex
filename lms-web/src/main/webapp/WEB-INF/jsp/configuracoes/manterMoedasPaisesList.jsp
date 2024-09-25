<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.moedaPaisService">
	<adsm:form action="/configuracoes/manterMoedasPaises" idProperty="idMoedaPais">

	   	<adsm:lookup labelWidth="20%" width="30%" service="lms.municipios.paisService.findLookup" dataType="text" property="pais" 
			criteriaProperty="nmPais" idProperty="idPais" exactMatch="false" minLengthForAutoPopUpSearch="3"
			label="pais" maxLength="40" action="/municipios/manterPaises"/>

	   	<adsm:lookup labelWidth="20%" width="30%" service="lms.configuracoes.moedaService.findLookup" dataType="text" 
			property="moeda" criteriaProperty="dsMoeda" idProperty="idMoeda" exactMatch="false"
			label="moeda" maxLength="40" action="/configuracoes/manterMoedas" minLengthForAutoPopUpSearch="3"/>

		<adsm:combobox labelWidth="20%" width="30%" label="situacao" property="tpSituacao" domain="DM_STATUS"/>

        <adsm:combobox labelWidth="20%" width="30%" property="blIndicadorPadrao" label="moedaPadrao" domain="DM_SIM_NAO"/>

        <adsm:combobox labelWidth="20%" width="30%" property="blIndicadorMaisUtilizada" label="moedaMaisUtilizada" domain="DM_SIM_NAO"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="moedas"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idMoedaPais" property="moedas" defaultOrder="pais_.nmPais, moeda_.dsMoeda" rows="12" gridHeight="200">
		<adsm:gridColumn width="20%" title="pais" property="pais.nmPais"/>
		<adsm:gridColumn width="20%" title="moeda" property="moeda.dsMoeda"/>
		<adsm:gridColumn width="20%" title="situacao" isDomain="true" property="tpSituacao"/>
		<adsm:gridColumn width="20%" title="moedaPadrao"  property="blIndicadorPadrao" renderMode="image-check"/>
		<adsm:gridColumn width="20%" title="moedaMaisUtilizada"  property="blIndicadorMaisUtilizada" renderMode="image-check"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>		
	</adsm:grid>
</adsm:window>
