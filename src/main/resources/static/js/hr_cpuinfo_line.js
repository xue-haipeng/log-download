/**
 * Created by Xue on 12/01/16.
 */
$(function () {
    $.getJSON('/hr_cpuInfo', function (data) {

        var ezhap5001 = [];
        var ezhap5002 = [];
        var ezhap5003 = [];
        var ezhap5004 = [];
        var ezhap5005 = [];
        var ezhap5006 = [];
        var ezhap5007 = [];
        var ezhap5008 = [];
        var ezhap5009 = [];
        var ezhap5010 = [];
        var j1 = 0;
        var j2 = 0;
        var j3 = 0;
        var j4 = 0;
        var j5 = 0;
        var j6 = 0;
        var j7 = 0;
        var j8 = 0;
        var j9 = 0;
        var j10 = 0;

        for (i=0; i<data.length; i++) {
            if(data[i].hostname == 'EZHAP5001') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5001[j1++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5002') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5002[j2++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5003') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5003[j3++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5004') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5004[j4++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5005') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5005[j5++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5006') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5006[j6++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5007') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5007[j7++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5008') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5008[j8++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5009') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5009[j9++] = [timepoint, cpuusage]
            }
            if(data[i].hostname == 'EZHAP5010') {
                var timepoint = data[i].datetime;
                var cpuusage = data[i].cpuusage;
                ezhap5010[j10++] = [timepoint, cpuusage]
            }
        }

        Highcharts.setOptions({
            global: {
                useUTC: false
            }
        });

        $('#zhap5').highcharts({
            chart: {
                type: 'spline'
            },
            title: {
                text: '',
                style: {
                    'font-family': 'Microsoft YaHei'
                }
            },
            subtitle: {
                text: '最近6小时CPU使用率',
                style: {
                    'font-family': 'Microsoft YaHei'
                }
            },
            xAxis: {
                type: 'datetime',
                tickPixelInterval: 50,
                dateTimeLabelFormats: { // don't display the dummy year
                    second: '%H:%M:%S',
                    minute: '%H:%M',
                    hour: '%H:%M'
                },
                title: {
                    text: 'Time'
                }
            },
            yAxis: {
                title: {
                    text: 'CPU使用率(%)',
                    style: {
                        'font-family': 'Microsoft YaHei'
                    }
                }
            },
            tooltip: {
                headerFormat: '<b>{series.name}</b><br>',
                pointFormat: '{point.x:%b.%e, %H:%M}  <b>{point.y:.2f} %</b>'
//            	pointFormat: '{point.x:%X}  <b>{point.y:.2f} %</b>'
            },

            plotOptions: {
                spline: {
                    marker: {
                        enabled: true
                    }
                }
            },

            series: [{name: 'ezhap5001', data: ezhap5001},
                {name: 'ezhap5002', data: ezhap5002},
                {name: 'ezhap5003', data: ezhap5003},
                {name: 'ezhap5004', data: ezhap5004},
                {name: 'ezhap5005', data: ezhap5005},
                {name: 'ezhap5006', data: ezhap5006},
                {name: 'ezhap5007', data: ezhap5007},
                {name: 'ezhap5008', data: ezhap5008},
                {name: 'ezhap5009', data: ezhap5009},
                {name: 'ezhap5010', data: ezhap5010}]
        });

        $("#switch").on('switchChange.bootstrapSwitch', function(event, state) {
            if (state == false) {
                var ezhap5001 = [];
                var ezhap5002 = [];
                var ezhap5003 = [];
                var ezhap5004 = [];
                var ezhap5005 = [];
                var ezhap5006 = [];
                var ezhap5007 = [];
                var ezhap5008 = [];
                var ezhap5009 = [];
                var ezhap5010 = [];
                var j1 = 0;
                var j2 = 0;
                var j3 = 0;
                var j4 = 0;
                var j5 = 0;
                var j6 = 0;
                var j7 = 0;
                var j8 = 0;
                var j9 = 0;
                var j10 = 0;

                for (i=0; i<data.length; i++) {
                    if(data[i].hostname == 'EZHAP5001') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5001[j1++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5002') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5002[j2++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5003') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5003[j3++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5004') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5004[j4++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5005') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5005[j5++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5006') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5006[j6++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5007') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5007[j7++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5008') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5008[j8++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5009') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5009[j9++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5010') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5010[j10++] = [timepoint, cpuusage]
                    }
                }

                Highcharts.setOptions({
                    global: {
                        useUTC: false
                    }
                });

                $('#zhap5').highcharts({
                    chart: {
                        type: 'spline'
                    },
                    title: {
                        text: '',
                        style: {
                            'font-family': 'Microsoft YaHei'
                        }
                    },
                    subtitle: {
                        text: '最近6小时CPU使用率',
                        style: {
                            'font-family': 'Microsoft YaHei'
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        tickPixelInterval: 50,
                        dateTimeLabelFormats: { // don't display the dummy year
                            second: '%H:%M:%S',
                            minute: '%H:%M',
                            hour: '%H:%M'
                        },
                        title: {
                            text: 'Time'
                        }
                    },
                    yAxis: {
                        title: {
                            text: 'CPU使用率(%)',
                            style: {
                                'font-family': 'Microsoft YaHei'
                            }
                        },
                        min: 0,
                        max: 100
                    },
                    tooltip: {
                        headerFormat: '<b>{series.name}</b><br>',
                        pointFormat: '{point.x:%b.%e, %H:%M}  <b>{point.y:.2f} %</b>'
//            	pointFormat: '{point.x:%X}  <b>{point.y:.2f} %</b>'
                    },

                    plotOptions: {
                        spline: {
                            marker: {
                                enabled: true
                            }
                        }
                    },

                    series: [{name: 'ezhap5001', data: ezhap5001},
                        {name: 'ezhap5002', data: ezhap5002},
                        {name: 'ezhap5003', data: ezhap5003},
                        {name: 'ezhap5004', data: ezhap5004},
                        {name: 'ezhap5005', data: ezhap5005},
                        {name: 'ezhap5006', data: ezhap5006},
                        {name: 'ezhap5007', data: ezhap5007},
                        {name: 'ezhap5008', data: ezhap5008},
                        {name: 'ezhap5009', data: ezhap5009},
                        {name: 'ezhap5010', data: ezhap5010}]
                });
            } else {
                var ezhap5001 = [];
                var ezhap5002 = [];
                var ezhap5003 = [];
                var ezhap5004 = [];
                var ezhap5005 = [];
                var ezhap5006 = [];
                var ezhap5007 = [];
                var ezhap5008 = [];
                var ezhap5009 = [];
                var ezhap5010 = [];
                var j1 = 0;
                var j2 = 0;
                var j3 = 0;
                var j4 = 0;
                var j5 = 0;
                var j6 = 0;
                var j7 = 0;
                var j8 = 0;
                var j9 = 0;
                var j10 = 0;

                for (i=0; i<data.length; i++) {
                    if(data[i].hostname == 'EZHAP5001') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5001[j1++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5002') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5002[j2++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5003') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5003[j3++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5004') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5004[j4++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5005') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5005[j5++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5006') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5006[j6++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5007') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5007[j7++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5008') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5008[j8++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5009') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5009[j9++] = [timepoint, cpuusage]
                    }
                    if(data[i].hostname == 'EZHAP5010') {
                        var timepoint = data[i].datetime;
                        var cpuusage = data[i].cpuusage;
                        ezhap5010[j10++] = [timepoint, cpuusage]
                    }
                }

                Highcharts.setOptions({
                    global: {
                        useUTC: false
                    }
                });

                $('#zhap5').highcharts({
                    chart: {
                        type: 'spline'
                    },
                    title: {
                        text: '',
                        style: {
                            'font-family': 'Microsoft YaHei'
                        }
                    },
                    subtitle: {
                        text: '最近6小时CPU使用率',
                        style: {
                            'font-family': 'Microsoft YaHei'
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        tickPixelInterval: 50,
                        dateTimeLabelFormats: { // don't display the dummy year
                            second: '%H:%M:%S',
                            minute: '%H:%M',
                            hour: '%H:%M'
                        },
                        title: {
                            text: 'Time'
                        }
                    },
                    yAxis: {
                        title: {
                            text: 'CPU使用率(%)',
                            style: {
                                'font-family': 'Microsoft YaHei'
                            }
                        }
                    },
                    tooltip: {
                        headerFormat: '<b>{series.name}</b><br>',
                        pointFormat: '{point.x:%b.%e, %H:%M}  <b>{point.y:.2f} %</b>'
//            	pointFormat: '{point.x:%X}  <b>{point.y:.2f} %</b>'
                    },

                    plotOptions: {
                        spline: {
                            marker: {
                                enabled: true
                            }
                        }
                    },

                    series: [{name: 'ezhap5001', data: ezhap5001},
                        {name: 'ezhap5002', data: ezhap5002},
                        {name: 'ezhap5003', data: ezhap5003},
                        {name: 'ezhap5004', data: ezhap5004},
                        {name: 'ezhap5005', data: ezhap5005},
                        {name: 'ezhap5006', data: ezhap5006},
                        {name: 'ezhap5007', data: ezhap5007},
                        {name: 'ezhap5008', data: ezhap5008},
                        {name: 'ezhap5009', data: ezhap5009},
                        {name: 'ezhap5010', data: ezhap5010}]
                });
            }
        });
    });
});

function compare(a, b) {
    return a[0] - b[0]
}