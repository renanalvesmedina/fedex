<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterVinculoFormulariosImpressoras" type="main">

		<adsm:i18nLabels>
			<adsm:include key="LMS-27025" />
			<adsm:include key="LMS-27026" />
			<adsm:include key="LMS-27027" />
		</adsm:i18nLabels>
		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="listagem" id="pesq" src="/configuracoes/manterVinculoFormulariosImpressoras" cmd="list"/>
			<adsm:tab title="detalhamento" id="cad" src="/configuracoes/manterVinculoFormulariosImpressoras" cmd="cad"/>
		</adsm:tabGroup>
</adsm:window>
