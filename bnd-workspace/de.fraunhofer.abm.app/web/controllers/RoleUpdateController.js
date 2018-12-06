angular.module('de.fraunhofer.abm').controller('roleupdateController', 
[ '$rootScope', '$scope', '$http', 'Notification', '$location',
function($rootScope, $scope, $http, Notification, $location){
	var self = this;
	self.available = true;
	self.invalidEmail = false;
	
$scope.request = {};
self.userroleupdate = function(){
		$rootScope.loading = true;
                $scope.request.username = "anitha";
		$http.post('/rest/roleupdate', $scope.request, null).then(
			function(d){
				if(d.data){
					$location.path('/registered');
				} else {
					Notification.error("This username has been taken. Please select a different one");
					self.available = false;
				}
			}, function(d){
				Notification.error('Internal error: registrations cannot be done at the moment. Please try agin later. If the error persists, please report it here: https://github.com/ABenchM/abm/issues');
			})['finally'](function() {
				$rootScope.loading = false
			});	
	};
}]);