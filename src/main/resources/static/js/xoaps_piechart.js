/**
 * Created by Xue on 03/20/17.
 */
$(function () {

    var yqt = 0;
    var xs = 0;
    var lyhg = 0;
    var trq = 0;
    $.ajax({
        type: "GET",
        async: false,
        url: "/jcpt/xoaps/error_distr",
        success: function (data) {
            if (data.YQT != undefined && data.YQT != null) {
                yqt = data.YQT;
            }
            if (data.XS != undefined && data.XS != null) {
                xs = data.XS;
            }
            if (data.LYHG != undefined && data.LYHG != null) {
                lyhg = data.LYHG;
            }
            if (data.TRQ != undefined && data.TRQ != null) {
                trq = data.TRQ;
            }
        }
    });

    Highcharts.chart('xoaps_piechart', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        colors: ['#77a1e5',
            '#f28f43',
            '#32CD32',
            '#F08080']
        ,
        title: {
            text: '今日上市OSB各板块日志报错占比',
            style: {
                fontFamily: 'Microsoft Yahei UI'
            }
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        series: [{
            name: 'Percent',
            colorByPoint: true,
            data: [{
                name: 'YQT',
                y: yqt
            }, {
                name: 'XS',
                y: xs,
                sliced: true,
                selected: true
            }, {
                name: 'LYHG',
                y: lyhg
            }, {
                name: 'TRQ',
                y: trq
            }]
        }]
    });

    Highcharts.chart('xoaps_barchart', {
        chart: {
            type: 'column'
        },
        title: {
            text: '今日上市OSB各板块日志报错次数',
            style: {
                fontFamily: 'Microsoft Yahei UI'
            }
        },
        subtitle: {
            text: 'Source: http://11.11.44.201:5601'
        },
        xAxis: {
            categories: [
                'YQT',
                'XS',
                'LYHG',
                'TRQ'
            ],
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Errors'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y} 次</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            },
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        colors: ['#77a1e5',
            '#f28f43',
            '#32CD32',
            '#F08080']
        ,
        series: [{
            name: '报错',
            style: {
              fontFamily: 'Microsoft Yahei UI'
            },
            data: [yqt, xs, lyhg, trq]
        }]
    });

});