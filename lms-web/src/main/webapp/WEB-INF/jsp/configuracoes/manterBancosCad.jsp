<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.bancoService">
	<adsm:form action="/configuracoes/manterBancos" idProperty="idBanco">

		<adsm:textbox dataType="integer" minValue="0" property="nrBanco" label="numero" width="35%" maxLength="3" size="3" required="true"/>
		<adsm:textbox dataType="text" property="nmBanco" label="nome" maxLength="60" size="40" required="true"/>

		<adsm:hidden property="situacaoPais" serializable="false" value="A"/>
		<adsm:lookup service="lms.municipios.paisService.findLookup" 
					 property="pais" 
					 label="pais"
					 idProperty="idPais"
					 criteriaProperty="nmPais"					 
					 dataType="text"
					 maxLength="60"
					 action="/municipios/manterPaises"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"
					 size="30"
					 required="true">
					 
					 <adsm:propertyMapping criteriaProperty="situacaoPais" modelProperty="tpSituacao"/>
		</adsm:lookup>

		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true"/>

		<adsm:buttonBar>
			<adsm:button caption="agencias" action="/configuracoes/manterAgencias" cmd="main">
				<adsm:linkProperty src="idBanco" target="banco.idBanco"/>
				<adsm:linkProperty src="nrBanco" target="banco.nrBanco"/>
				<adsm:linkProperty src="nmBanco" target="banco.nmBanco"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>