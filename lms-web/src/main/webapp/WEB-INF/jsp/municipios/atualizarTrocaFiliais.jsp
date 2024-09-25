<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="atualizarTrocaFiliais" type="main">

		<adsm:i18nLabels>
                <adsm:include key="LMS-29135"/>
    	</adsm:i18nLabels>

		<adsm:tabGroup selectedTab="0">
			<adsm:tab title="processo" id="proc" src="/municipios/atualizarTrocaFiliais" cmd="proc"/>
		</adsm:tabGroup>
</adsm:window>
