<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguranca.manterMetodoTelaAction">
	<adsm:form action="/seguranca/manterMetodoTela">
	
 	    <adsm:lookup dataType="text" label="modulo" 
		     	 	 property="modulo" idProperty="idModuloSistema" criteriaProperty="nmModuloSistema"
	 				 exactMatch="false"
	 				 minLengthForAutoPopUpSearch="3"
	  			 	 maxLength="60"
	 				 action="/seguranca/manterModulo"
	 				 service="lms.seguranca.manterMetodoTelaAction.findLookupModulo"
			 	     width="100%" required="true" />				


		<adsm:combobox property="tpMetodoTela" label="tipo" domain="ADSM_MANTER_METODO_TELA" width="80%" required="true"/>
		<adsm:textbox dataType="text" property="nmRecurso" label="nome" width="80%" size="60" maxLength="200"/>
		<adsm:textbox dataType="text" property="dsRecurso" label="descricao" width="80%" size="40" maxLength="60"/>


		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridMetodoTela" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idRecurso" property="gridMetodoTela" 
			   service="lms.seguranca.manterMetodoTelaAction.findPaginatedTipo"
			   rowCountService="lms.seguranca.manterMetodoTelaAction.getRowCountTipo">
		<adsm:gridColumn property="nmRecurso" title="nome" />
		<adsm:gridColumn property="nmModuloSistema" title="modulo" />
		<adsm:gridColumn property="nmSistema" title="nmSistema" />
		<adsm:buttonBar>
			<adsm:removeButton service="lms.seguranca.manterMetodoTelaAction.removeByIdsTipo" />
		</adsm:buttonBar>
	</adsm:grid>
	
</adsm:window>

