'use strict';

angular.module('swimmingchallengeApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


