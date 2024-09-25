<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.moedaCotacaoService">
<adsm:form action="/configuracoes/manterCotacoesMoedas" idProperty="idMoedaCotacao">

   		<adsm:lookup service="lms.municipios.paisService.findPaisesByMoeda" dataType="text" property="moedaPais.pais" 
			criteriaProperty="nmPais" idProperty="idPais" exactMatch="false" minLengthForAutoPopUpSearch="3"
			label="pais" maxLength="60" action="/municipios/manterPaises">
				<adsm:propertyMapping criteriaProperty="moedaPais.moeda.idMoeda" modelProperty="moedaPais.moeda.idMoeda"
					addChangeListener="false"/>
		</adsm:lookup>

	   	<adsm:lookup service="lms.configuracoes.moedaService.findMoedasByPais" dataType="text" exactMatch="false"
			property="moedaPais.moeda" criteriaProperty="dsMoeda" idProperty="idMoeda" minLengthForAutoPopUpSearch="3"
			label="moeda" maxLength="60" action="/configuracoes/manterMoedas">
				<adsm:propertyMapping criteriaProperty="moedaPais.pais.idPais" modelProperty="moedaPais.pais.idPais"
					addChangeListener="false"/>
		</adsm:lookup>
		<adsm:range label="data">
			<adsm:textbox dataType="JTDate" property="dtCotacaoMoedaInicial"/>
			<adsm:textbox dataType="JTDate" property="dtCotacaoMoedaFinal"/>		
		</adsm:range>
		
		<adsm:range label="valor">		
			<adsm:textbox dataType="currency" mask="##,###,###,###,##0.0000" property="vlCotacaoMoedaInicial" size="15" minValue="0.0001"/>
			<adsm:textbox dataType="currency" mask="##,###,###,###,##0.0000" property="vlCotacaoMoedaFinal" size="15" minValue="0.0001"/>		
		</adsm:range>
		
		<adsm:hidden property="vlCotacaoMoeda" serializable="false"/>
		
	<adsm:buttonBar freeLayout="true">
		<adsm:findButton callbackProperty="moedas"/>
		<adsm:resetButton/>
	</adsm:buttonBar>
</adsm:form>
<adsm:grid idProperty="idCotacaoMoeda" property="moedas" gridHeight="200" defaultOrder="moedaPais_pais_.nmPais, moedaPais_moeda_.dsMoeda, dtCotacaoMoeda" rows="13">
	<adsm:gridColumn width="25%" title="pais"  property="moedaPais.pais.nmPais"/>	
	<adsm:gridColumn width="25%" title="moeda" property="moedaPais.moeda.dsMoeda"/>
	<adsm:gridColumn width="25%" title="data"  property="dtCotacaoMoeda" dataType="JTDate"/>
	<adsm:gridColumn width="25%" title="valor" property="vlCotacaoMoeda" dataType="currency" mask="##,###,###,###,##0.0000"/>
	<adsm:buttonBar>
		<adsm:removeButton/>
	</adsm:buttonBar>	
</adsm:grid>
</adsm:window>