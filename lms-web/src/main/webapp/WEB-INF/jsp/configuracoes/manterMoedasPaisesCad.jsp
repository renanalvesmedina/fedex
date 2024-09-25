<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.moedaPaisService">
	<adsm:form action="/configuracoes/manterMoedasPaises"
		idProperty="idMoedaPais">
		
		<adsm:hidden property="dsAtivo" serializable="false" value="A"/>

		<adsm:lookup labelWidth="20%" 
		             width="30%"
		             service="lms.municipios.paisService.findLookup" 
		             dataType="text"
		             property="pais" 
		             required="true" 
		             criteriaProperty="nmPais"
		             idProperty="idPais" 
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3" 
		             label="pais" 
		             maxLength="40"
		             action="/municipios/manterPaises">
			<adsm:propertyMapping criteriaProperty="dsAtivo" modelProperty="tpSituacao"/>
		</adsm:lookup>		             

		<adsm:lookup labelWidth="20%" 
		             width="30%"
		             service="lms.configuracoes.moedaService.findLookup" 
		             dataType="text"
		             required="true" 
		             property="moeda" 
		             criteriaProperty="dsMoeda"
		             idProperty="idMoeda" 
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="3" 
		             label="moeda" 
		             maxLength="40"
		             action="/configuracoes/manterMoedas">
			<adsm:propertyMapping criteriaProperty="dsAtivo" modelProperty="tpSituacao"/>
		</adsm:lookup>		             

		<adsm:combobox labelWidth="20%" width="30%" label="situacao"
			property="tpSituacao" domain="DM_STATUS" required="true" />

		<adsm:checkbox labelWidth="20%" width="30%" label="moedaPadrao"
			property="blIndicadorPadrao" />

		<adsm:checkbox labelWidth="20%" width="30%" label="moedaMaisUtilizada"
			property="blIndicadorMaisUtilizada" />

		<adsm:buttonBar>
			<adsm:button caption="cotacoes"
				action="/configuracoes/manterCotacoesMoedas" cmd="main">
				<adsm:linkProperty src="moeda.idMoeda"
					target="moedaPais.moeda.idMoeda" />
				<adsm:linkProperty src="moeda.dsMoeda"
					target="moedaPais.moeda.dsMoeda" />
				<adsm:linkProperty src="pais.idPais" target="moedaPais.pais.idPais" />
				<adsm:linkProperty src="pais.nmPais" target="moedaPais.pais.nmPais" />
			</adsm:button>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
