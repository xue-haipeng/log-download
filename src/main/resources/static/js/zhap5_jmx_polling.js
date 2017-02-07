/**
 * Created by Xue on 12/04/16.
 */
$(function() {
    $("#mserver").click(function() {
        $('#mserver_table').DataTable({
            retrieve: true,
            "ajax": {
                "url": "/zzkf/zhap5_data",
                "dataSrc": ""
            },
            "columns": [
                { "data": "serverName" },
                { "data": "serverState" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "serverHealth" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "heapSizeCurrent" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "heapFreeCurrent" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "heapFreePercent" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "appName" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "appActiveState" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "appHealthState" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                },
                { "data": "openSessions" ,
                    render: function (data, type, row) {
                        if (data.length == 0) {
                            return 'N/A';
                        }
                    }
                }
            ]
        });
    });
});

