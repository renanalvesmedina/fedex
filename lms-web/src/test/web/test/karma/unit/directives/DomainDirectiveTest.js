'use strict';

(function() {
    describe('DomainDirective', function() {
        
        var state = UnitTestsUtils.baseInit();
        UnitTestsUtils.enableVerifications(state);
        
        it('deve inicializar a directive corretamente', function() {
        	var $compile = state.$compile;
        	var $rootScope = state.$rootScope;
        	
        	var array = [
				 {"id":7377,"description":"Aprovação Cancelada","value":"C"},{"id":7378,"description":"Reprovada","value":"R"},
				 {"id":7379,"description":"Em Aprovação","value":"E"},{"id":7380,"description":"Aprovada","value":"A"},
				 {"id":7381,"description":"Incluída","value":"I"},{"id":7382,"description":"Em Efetivação","value":"M"},
				 {"id":7383,"description":"Efetivada","value":"F"},{"id":7384,"description":"Efetivação Reprovada","value":"H"}
			];
        	UnitTestsUtils.exceptGet('/rest/config/domainValue/findDomainValues\?name=DM_SITUACAO_SIMULACAO&?', array, state);
        	
        	var element = $compile("<select adsm-domain='DM_SITUACAO_SIMULACAO' id='situacao' ng-model='situacaoProposta'><option value=''></option></select>")($rootScope);
        	
        	state.$httpBackend.flush();
        	
        	$rootScope.$digest();
        	
        	expect(element.attr('data-ng-options')).toEqual('data as data.description for data in domains.DM_SITUACAO_SIMULACAO track by data.value');
        	expect(element.hasClass("chosen-select")).toBe(true);
        	expect(element.attr("data-chosen")).toBe("");
        	expect(element.attr("no-results-text")).toBe("'Nenhum resultado para'");
        	expect(element.attr("allow-single-deselect")).toBe("true");
        	expect(element.attr("search-contains")).toBe("true");
        	expect(element.attr("inherit_select_classes")).toBe("true");
        	
        	expect($rootScope.situacaoProposta).toBeUndefined();
        	expect(element.val()).toBe('');
        	
        	$rootScope.situacaoProposta = array[1];
        	$rootScope.$digest();
        	expect(element.val()).toBe('R');
        });
    });
})();