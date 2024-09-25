<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.diaSemanaService">
	<adsm:form action="/configuracoes/manterDiasSemana" idProperty="idDiaSemana">

		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
		<adsm:lookup action="/municipios/manterPaises" dataType="text" criteriaProperty="nmPais" maxLength="60"
			service="lms.municipios.paisService.findLookup" property="pais" exactMatch="false" minLengthForAutoPopUpSearch="3"
			idProperty="idPais" label="pais" required="true" width="85%">
			
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
			
		</adsm:lookup>

		<adsm:multicheckbox 
			texts="dom|seg|ter|qua|qui|sex|sab|"
			property="blUtilDom|blUtilSeg|blUtilTer|blUtilQua|blUtilQui|blUtilSex|blUtilSab|" 
			align="top" label="diasSemana" />

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>