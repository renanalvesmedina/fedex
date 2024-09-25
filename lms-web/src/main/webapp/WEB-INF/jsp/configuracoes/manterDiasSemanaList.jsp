<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.diaSemanaService">
	<adsm:form action="/configuracoes/manterDiasSemana" idProperty="idDiaSemana">
	
		<adsm:lookup action="/municipios/manterPaises" dataType="text" criteriaProperty="nmPais" maxLength="60"
			service="lms.municipios.paisService.findLookup" property="pais" exactMatch="false" minLengthForAutoPopUpSearch="3"
			idProperty="idPais" label="pais" width="85%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="diasSemana"/>
			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>
	
	<adsm:grid idProperty="idDiaSemana" property="diasSemana" defaultOrder="pais_.nmPais" gridHeight="200" rows="14" unique="true">
		
		<adsm:gridColumn title="pais" property="pais.nmPais" width="30%" />

        <adsm:gridColumn title="dom" property="blUtilDom" width="10%" renderMode="image-check"/>
        <adsm:gridColumn title="seg" property="blUtilSeg" width="10%" renderMode="image-check"/>
        <adsm:gridColumn title="ter" property="blUtilTer" width="10%" renderMode="image-check"/>
        <adsm:gridColumn title="qua" property="blUtilQua" width="10%" renderMode="image-check"/>
        <adsm:gridColumn title="qui" property="blUtilQui" width="10%" renderMode="image-check"/>
        <adsm:gridColumn title="sex" property="blUtilSex" width="10%" renderMode="image-check"/>
        <adsm:gridColumn title="sab" property="blUtilSab" width="10%" renderMode="image-check"/>
        
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
