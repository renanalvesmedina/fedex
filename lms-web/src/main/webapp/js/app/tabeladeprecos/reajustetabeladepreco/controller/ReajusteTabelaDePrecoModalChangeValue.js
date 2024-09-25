
var openModalValue = function(modalService, list, index, columnBaseValue, columnShowValue, columnShowPercent) {

	var changeValueController = ["$scope", "$modalInstance", "$window", function ($scope, $modalInstance, $window) { 
		
		function calculateValue($window, value, orginalValue) {
			return ( ((value * 100)/ orginalValue) - 100 );
		}
		
		function calculatePercent($window, percentValue, orginalValue) {
   			return $window.Math.abs((orginalValue + (orginalValue * percentValue) / 100));
   	    }
		
		function updateByPercent($window, index, columnBaseValue, columnShowValue, columnShowPercent, list, percentValue){
   			angular.forEach(list, function(value, key) {
   				var json = angular.fromJson(value);
   				if(json[columnBaseValue] && (parseInt(index) === -1 || parseInt(index) === key)){
	   				json[columnShowValue] = calculatePercent($window, percentValue, json[columnBaseValue]);
	   				json[columnShowPercent] = percentValue;
   				}
   			});
   			
   			return list;
   		}
	   
   	    function updateByValue($window, index, columnBaseValue, columnShowValue, columnShowPercent, list, newValue){
			angular.forEach(list, function(value, key) {
				var json = angular.fromJson(value);
				if(json[columnBaseValue] && (parseInt(index) === -1 || parseInt(index) === key)){
					json[columnShowValue] = newValue;
					json[columnShowPercent] = calculateValue($window, newValue, json[columnBaseValue]);
				}
			});
			
			return list;
		}
		
		$scope.title = $scope.$eval("'modal' | translate");
		$scope.innerTemplate = contextPath+"js/app/tabeladeprecos/reajustetabeladepreco/view/modalChangeValue.html";
		
		$scope.valueClick = function() {
			$("#percent").val('');
			$("#percent").attr('disabled','disabled');
			$("#value").removeAttr('disabled');
		};
		
		$scope.percentClick = function() {
			$("#value").val('');
			$("#value").attr('disabled','disabled');
			$("#percent").removeAttr('disabled');
		};
		
		$scope.changeValue = function(percent, value){
			
			if($("#percent").val() !== undefined && $("#percent").val() !== '' ) {
				list = updateByPercent($window, index, columnBaseValue, columnShowValue, columnShowPercent, list, percent);
    		
			} else if($("#value").val() !== undefined && $("#value").val() !== '') {
				list = updateByValue($window, index, columnBaseValue, columnShowValue, columnShowPercent, list, value);
			}
			
			$modalInstance.close();
    	};
		
		$scope.close = function () {
			$modalInstance.close();
		};

	}];
	
	modalService.open({windowClass: "modal-reajuste", controller: changeValueController});

};

