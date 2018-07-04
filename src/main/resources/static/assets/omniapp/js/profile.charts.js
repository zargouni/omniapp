//== Class definition
var userClosedTasksChart = function() {

	var demo7 = function() {
		var chartData = generateChartData();
		var chart = AmCharts.makeChart("user_closed_tasks_chart", {
			"type" : "serial",
			"theme" : "light",
			"marginRight" : 80,
			"autoMarginOffset" : 20,
			"marginTop" : 7,
			"dataProvider" : chartData,
			"valueAxes" : [ {
				"axisAlpha" : 0.2,
				"dashLength" : 1,
				"position" : "left"
			} ],
			"mouseWheelZoomEnabled" : true,
			"graphs" : [ {
				"id" : "g1",
				"balloonText" : "[[value]]",
				"bullet" : "round",
				"bulletBorderAlpha" : 1,
				"bulletColor" : "#FFFFFF",
				"hideBulletsCount" : 50,
				"title" : "red line",
				"valueField" : "tasks",
				"useLineColorForBulletBorder" : true,
				"balloon" : {
					"drop" : true
				}
			} ],
			"chartScrollbar" : {
				"autoGridCount" : true,
				"graph" : "g1",
				"scrollbarHeight" : 40
			},
			"chartCursor" : {
				"limitToGraph" : "g1"
			},
			"categoryField" : "date",
			"categoryAxis" : {
				"parseDates" : true,
				"axisColor" : "#DADADA",
				"dashLength" : 1,
				"minorGridEnabled" : true
			},
			"export" : {
				"enabled" : false
			}
		});

		chart.addListener("rendered", zoomChart);
		zoomChart();

		// this method is called when chart is first inited as we listen for
		// "rendered" event
		function zoomChart() {
			// different zoom methods can be used - zoomToIndexes, zoomToDates,
			// zoomToCategoryValues
			chart.zoomToIndexes(chartData.length - 40, chartData.length - 1);
		}

		// generate some random data, quite different range
		function generateChartData() {
			var profileId = $('#profile_user_id').val();

			var chartData = [];
			
		
			$.ajax({
				type : "GET",
				url : '/json-user-closed-tasks-feed',
				data : 'id=' + profileId,
				async : false,
				success : function(response) {

					$.each(response, function(date, val) {

							var newDate = new Date(date);

							var tasksCount = val;
						
						chartData.push({
							date : newDate,
							tasks : tasksCount
						});
					});

				},
				error : function(e) {
					alert('Error: Profile task stats widget ' + e);
				}
			});

			return chartData;
		}
	}

	return {
		// public functions
		init : function() {

			demo7();

		}
	};
}();


//== Class definition
var userClosedIssuesChart = function() {

	var demo7 = function() {
		var chartData = generateChartData();
		var chart = AmCharts.makeChart("user_closed_issues_chart", {
			"type" : "serial",
			"theme" : "dark",
			"marginRight" : 80,
			"autoMarginOffset" : 20,
			"marginTop" : 7,
			"dataProvider" : chartData,
			"valueAxes" : [ {
				"axisAlpha" : 0.2,
				"dashLength" : 1,
				"position" : "left"
			} ],
			"mouseWheelZoomEnabled" : true,
			"graphs" : [ {
				"id" : "g1",
				"balloonText" : "[[value]]",
				"bullet" : "round",
				"bulletBorderAlpha" : 1,
				"bulletColor" : "#FFFFFF",
				"hideBulletsCount" : 50,
				"title" : "red line",
				"valueField" : "tasks",
				"useLineColorForBulletBorder" : true,
				"balloon" : {
					"drop" : true
				}
			} ],
			"chartScrollbar" : {
				"autoGridCount" : true,
				"graph" : "g1",
				"scrollbarHeight" : 40
			},
			"chartCursor" : {
				"limitToGraph" : "g1"
			},
			"categoryField" : "date",
			"categoryAxis" : {
				"parseDates" : true,
				"axisColor" : "#DADADA",
				"dashLength" : 1,
				"minorGridEnabled" : true
			},
			"export" : {
				"enabled" : false
			}
		});

		chart.addListener("rendered", zoomChart);
		zoomChart();

		// this method is called when chart is first inited as we listen for
		// "rendered" event
		function zoomChart() {
			// different zoom methods can be used - zoomToIndexes, zoomToDates,
			// zoomToCategoryValues
			chart.zoomToIndexes(chartData.length - 40, chartData.length - 1);
		}

		// generate some random data, quite different range
		function generateChartData() {
			var profileId = $('#profile_user_id').val();

			var chartData = [];
			
		
			$.ajax({
				type : "GET",
				url : '/json-user-closed-issues-feed',
				data : 'id=' + profileId,
				async : false,
				success : function(response) {

					$.each(response, function(date, val) {

							var newDate = new Date(date);

							var tasksCount = val;
						
						chartData.push({
							date : newDate,
							tasks : tasksCount
						});
					});

				},
				error : function(e) {
					alert('Error: Profile task stats widget ' + e);
				}
			});

			return chartData;
		}
	}

	return {
		// public functions
		init : function() {

			demo7();

		}
	};
}();


