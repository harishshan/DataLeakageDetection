var mainApp =angular.module('mainApp',[]);
mainApp.controller('agentListController', function($scope,$http) {
	$scope.activate = function(agentIndex){
		var agent=$scope.agentList[agentIndex];
		var url = "activate/"+agent.id;
		$http.get(url).success( function(response) {
			$scope.message = response; 
		});
		$http.get(url).success( function(response) {
			$scope.agentList = response; 
		});
	};
	$scope.deactivate = function(agentIndex){
		var agent=$scope.agentList[agentIndex];
		var url = "deactivate/"+agent.id;
		$http.get(url).success( function(response) {
			$scope.message = response; 
		});
		$http.get(url).success( function(response) {
			$scope.agentList = response; 
		});
	};
	var url = "getAgentList";
	$http.get(url).success( function(response) {
		$scope.agentList = response; 
	});
});

mainApp.controller('filesListController', function($scope,$http) {
	var url = "getFilesList";
	$http.get(url).success( function(response) {
		$scope.filesList = response; 
	});
	$scope.lock = function(filesIndex){
		var files=$scope.filesList[filesIndex];
		var url = "lock/"+files.id;
		$http.get(url).success( function(response) {
			$scope.message = response; 
		});
		$http.get(url).success( function(response) {
			$scope.filesList = response; 
		});
	};
	$scope.unlock = function(filesIndex){
		var files=$scope.filesList[filesIndex];
		var url = "unlock/"+files.id;
		$http.get(url).success( function(response) {
			$scope.message = response; 
		});
		$http.get(url).success( function(response) {
			$scope.filesList = response; 
		});
	};
});

mainApp.controller('detectorListController', function($scope,$http) {
	var url = "getDetectorList";
	$http.get(url).success( function(response) {
		$scope.detectorList = response; 
	});
});