 'use strict';

angular.module('swimmingchallengeApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-swimmingchallengeApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-swimmingchallengeApp-params')});
                }
                return response;
            }
        };
    });
