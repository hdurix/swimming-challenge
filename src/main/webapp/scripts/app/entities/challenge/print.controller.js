(function () {
    'use strict';

    angular.module('swimmingchallengeApp')
        .controller('PrintController', PrintController);

    function PrintController(Timeslot, $filter, $timeout, $window, $state) {

        var vm = this;

        activate();

        function activate() {
            loadTimeslots();
        }

        function loadTimeslots() {
            Timeslot.userTimeslots(function(timeslots) {
                vm.timeslots = $filter('orderBy')(timeslots, 'startTime');
                print();
            });
        }

        function print() {
            $timeout(function() {
                $window.print();
                $state.go('challenge');
            });
        }
    }
})();
