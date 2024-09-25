<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window>
	<adsm:form action="/tributos/emitirValoresPISCOFINSCSLLIRINSSISSCompetencia">

        <adsm:lookup label="filial" property="filial" 
        	service="lms.tributos.emitirValoresPISCOFINSCSLLIRINSSISSCompetenciaAction.findLookupFilial" 
        	action="/municipios/manterFiliais" idProperty="idFilial" 
        	criteriaProperty="sgFilial" dataType="text" size="3" 
        	maxLength="3">
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmPessoa"
	            modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" property="filial.pessoa.nmPessoa" 
				size="30" disabled="true"/>
        </adsm:lookup>
		<adsm:textbox label="competencia" dataType="monthYear" property="competencia" size="21" required="true" />
		<adsm:checkbox property="soTotais" label="soTotais" />
		<adsm:combobox property="tpFormatoRelatorio" 
  				   label="formatoRelatorio" 
  				   required="true"
  				   defaultValue="pdf"
			   domain="DM_FORMATO_RELATORIO"/>
		<adsm:buttonBar>
			<!-- adsm:reportViewerButton caption="soTotais" service="lms.tributos.emitirValoresPISCOFINSCSLLIRINSSISSCompetenciaAction"/-->
			<adsm:reportViewerButton service="lms.tributos.emitirValoresPISCOFINSCSLLIRINSSISSCompetenciaAction" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>