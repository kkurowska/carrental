(function() {
    'use strict';
    angular
        .module('carrentalApp')
        .factory('Rent', Rent);

    Rent.$inject = ['$resource'];

    function Rent ($resource) {
        var resourceUrl =  'api/rents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
