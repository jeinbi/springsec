var securityapp = angular.module('securityapp', ['ngResource','ngRoute']);
var contextroot = '/springsec';
var routeCacheName = 'routeCache';
var routeCacheKey = 'savedRoute';

securityapp.config(['$routeProvider', '$provide', function($routeProvider, $provide){
	$routeProvider.when("/login", {"templateUrl":"login.html", "controller":"LoginController"})
	.otherwise({"redirectTo": "/login"});
	$provide.factory('$routeProvider', function(){
		return $routeProvider;
	});
}]);


securityapp.factory('routeCache', ['$cacheFactory', function($cacheFactory) {
	return $cacheFactory(routeCacheName);
}]);

securityapp.factory('transformRequestAsFormPost', 
	function() {
		function transformRequest( data, getHeaders ) {
			var headers = getHeaders();
			return serializeData(data);
		}

		return transformRequest;
		
		function serializeData( data ) {
			if (!angular.isObject(data)) {
				return((data == null) ? "" : data.toString());
			}		
			var buffer = [];
			for ( var name in data ) {
				if (!data.hasOwnProperty(name)) {
					continue;
				}
 
				var value = data[ name ];
				buffer.push(encodeURIComponent( name ) + "=" + encodeURIComponent( ( value == null ) ? "" : value ));
			}	
			var source = buffer.join( "&" ).replace( /%20/g, "+" );
			return source;
		}
	}		
);

securityapp.factory('LoginService', ['$http', '$location', 'routeCache', 'transformRequestAsFormPost', function($http, $location, routePathCache, transformRequestAsFormPost) {
	//var LoginResource = $resource(contextroot+'/login',{},{
		//save: {
			//method: "POST",
			//headers:{'X-Requested-With':'XMLHttpRequest'}
		//}
	//});
	var serviceobj = {
		login: function(username, password) {
			$http({
				method:"POST",
				url:contextroot+'/login',
				data: {'username':username,'password':password},
				headers: {'Content-Type':'application/x-www-form-urlencoded;charset=UTF-8'},
				transformRequest: transformRequestAsFormPost
			}).then(function(resp) {
				if (resp.status == 200) {
					var targetUrl = routePathCache.get(routeCacheKey);
					//var targetUrl = data.data.targeturl;
					if (targetUrl != null) {
						$location.path(targetUrl);
						routePathCache.remove(routeCacheKey);
					}
					else {
						$location.path('/main');
					}	
				}
			}, function(resp) {
				
			});
		}
	};
	return serviceobj;
}]
);

securityapp.factory('MainService', ['$http', '$location', function($http, $location) {
	var serviceobj = {
		loadTestData: function() {
			$http({
				method:"GET",
				url:contextroot+'/test'
			}).then(function(data, status, header, config) {
					
			});
		}
	};
	return serviceobj;
}]
);


securityapp.factory('httpInterceptor', ['$q', '$injector', 
	function($q, $injector) {
		var interceptor = {
			'request': function(config) {
				config.headers['X-Requested-With'] = 'XMLHttpRequest';
				return config;
			},
			'response': function(resp) {
				return resp;		
			},
			'responseError': function(rejection) {
				var rootScope = $injector.get('$rootScope');
				var handled = false;
				switch(rejection.status) {
					case 404:
						break;
					case 500:
						break;
					case 901:
						handled = true;
						rootScope.$broadcast('login.failure', rejection.data);
						break;
					case 902:
						handled = true;
						rootScope.$broadcast('access.denied', rejection.data);
						break;
					case 903:
						handled = true;
						rootScope.$broadcast('session.invalid', rejection.data);
						break;
				}
				if (handled) {
					return $q.resolve(rejection.data);
				}
				else {
					return $q.reject(rejection);
				}	
			}
		};		
		return interceptor;	
	}
]);

securityapp.config(function($httpProvider) {
	$httpProvider.interceptors.push('httpInterceptor');	
});

securityapp.controller('LoginController', ['$scope', 'LoginService', function($scope, loginService) {
	$scope.login = function() {
		loginService.login($scope.username, $scope.password);
	};
}]
);

securityapp.controller('MainController', ['$scope', 'MainService', function($scope, mainService) {
	$scope.loadTestData = function() {
		mainService.loadTestData();
	};
}]
);

securityapp.controller('CartController', ['$scope', function($scope) {
}]
);	

securityapp.run(function($routeProvider, $http, $location, routeCache, $rootScope) {
	 $http.get(contextroot+'/js/dynamic-routes.json').then(function(res) {
		var routeRules = res.data.routeRules;
		for(var i=0; i<routeRules.length; i++) {
			var nextRouteRule = routeRules[i];
			$routeProvider.when(nextRouteRule.pattern, {"controller":nextRouteRule.controller, "templateUrl":nextRouteRule.templateUrl});
		}
		//$route.reload();
		$rootScope.$on('login.failure', function(evt, data)  {
			$rootScope.loginError = true;
			$rootScope.saveRoute = true;
			$rootScope.loginErrorMessage = 'Wrong username/password supplied';
			$location.path('/login');
		});
		
		$rootScope.$on('session.invalid', function(evt, data)  {
			$rootScope.loginError = true;
			$rootScope.saveRoute = true;
			$rootScope.loginErrorMessage = 'Session timeout, please login again';
			$location.path('/login');
		});
		
		$rootScope.$on('access.denied', function(evt, data)  {
			alert("access denied");
		});	
		
		$rootScope.$on('$routeChangeStart', function(evt, next, curr) {
			if ($rootScope.saveRoute) {
				if (curr) {
					var curpath = curr.$$route.originalPath;
					var nextpath = next.$$route.originalPath;
					if (curpath != '/login' && nextpath == '/login') {
						routeCache.put(routeCacheKey, curpath);
					}
				}
				$rootScope.saveRoute = false;
			}
			
			
			console.log('routeChanged');
		});
	 });
});
