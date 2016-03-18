'use strict';

angular.module('swimmingchallengeApp')
    .factory('Timeslot', function ($resource, DateUtils) {
        return $resource('api/timeslots/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'hasPay': { method: 'GET', params: {hasPay: true} },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
