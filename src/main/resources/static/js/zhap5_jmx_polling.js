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
                { "data": "serverState" , "defaultContent": "" },
                { "data": "serverHealth" , "defaultContent": "" },
                { "data": "heapSizeCurrent" , "defaultContent": "" },
                { "data": "heapFreeCurrent" , "defaultContent": "" },
                { "data": "heapFreePercent" , "defaultContent": "" },
                { "data": "appName" , "defaultContent": "" },
                { "data": "appActiveState" , "defaultContent": "" },
                { "data": "appHealthState" , "defaultContent": "" },
                { "data": "openSessions" , "defaultContent": "" },
                { "data": "dsState" , "defaultContent": "" },
                { "data": "dsCapacity" , "defaultContent": "" },
                { "data": "dsActiveConn" , "defaultContent": "" },
                { "data": "dsLeakedConn" , "defaultContent": "" }
            ]
        });
    });
});

