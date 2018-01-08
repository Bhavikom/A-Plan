var TIME_STAMP = "";
var GLOBAL_FILTER = {"DateRange":"", "PressedCounter": null};
var GLOBAL_RIBBON_FILTER = null;
var GLOBAL_GENERAL_FILTERS = {"Criterion1":"Planned time to be spent on task","Criterion1Type":"Before","Criterion1FromDate":"2016-10-07"};

//Check if filter fired from WEB VIEW just for WEB and testing filters
GLOBAL_RIBBON_FILTER = getQueryVariable("RibbonFilters"); //Remove this line once the filter testing is done

/*
* This method will set filters from IOS or Android App
* */
function SetFiltersDataFromApp(Ribbonfilter, GeneralFilters) {
    var grid = Ext.ComponentQuery.query("#gv")[0];
    if(typeof(Ribbonfilter) !== 'undefined') {
        GLOBAL_RIBBON_FILTER = Ribbonfilter;
    }
    if(typeof(GeneralFilters) !== 'undefined') {
        GLOBAL_GENERAL_FILTERS = GeneralFilters;
    }
    grid.taskStore.load({
        scope: this,
        autoLoad: true,
        url: 'Tasks/Get?isCombine=' + false + '&Outline_Level=1&from=Ribbon'
    });
}


/*This function will handle window or device resize event and accordingly change the width*/
window.onresize = ResizeHandller;
function ResizeHandller() {
    var grid = Ext.ComponentQuery.query("#gv")[0];
    grid.setWidth($(window).width());
    grid.setHeight($(window).height());
}

/*Function to change the resolution of Gantt-Chart control*/
function changeResolution(ResolutionName) {
    var resolutionName = "";
    if (typeof ResolutionName !== 'undefined') {
        resolutionName = ResolutionName;
    } else {
        var mylist = document.getElementById("Resolution");
        resolutionName = mylist.options[mylist.selectedIndex].text;
    }
    var grid = Ext.ComponentQuery.query("#gv")[0];
    //Second View Preset
    if (resolutionName == "Seconds") {
        grid.switchViewPreset('secondAndMinute', Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, 10));
    }
    //Minutes View Preset
    else if (resolutionName == "Minutes") {
        grid.switchViewPreset('minuteAndHour', Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, 10));
    }
    //Hours View Preset
    else if (resolutionName == "Hours") {
        grid.switchViewPreset('hourAndDay', Sch.util.Date.add(new Date(), Sch.util.Date.DAY, -10), Sch.util.Date.add(new Date(), Sch.util.Date.DAY, 10));
    }
    //Days View Preset
    else if (resolutionName == "Days") {
        grid.switchViewPreset('weekAndDay', Sch.util.Date.add(new Date(), Sch.util.Date.WEEK, -10), Sch.util.Date.add(new Date(), Sch.util.Date.WEEK, 10));
    }
    //Week Small View Preset
    else if (resolutionName == "Week S") {
        grid.switchViewPreset('weekAndMonth', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
    }
    //Week Medium View Preset
    else if (resolutionName == "Week M") {
        grid.switchViewPreset('weekAndDayLetter', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
    }
    //Weel Long View Preset
    else if (resolutionName == "Week L") {
        grid.switchViewPreset('weekDateAndMonth', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
    }
    //Months View Preset
    else if (resolutionName == "Months") {
        grid.switchViewPreset('monthAndYear', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
    }
    //Year View Preset
    else if (resolutionName == "Year") {
        grid.switchViewPreset('year', Sch.util.Date.add(new Date(), Sch.util.Date.YEAR, -10), Sch.util.Date.add(new Date(), Sch.util.Date.YEAR, 10));
    }
    //Quarter View Preset
    else if (resolutionName == "Quarter") {
        grid.switchViewPreset('year', Sch.util.Date.add(new Date(), Sch.util.Date.QUARTER, -10), Sch.util.Date.add(new Date(), Sch.util.Date.QUARTER, 10));
    }
    //DayNightShift View Preset
    else if (resolutionName == "DayNightShift") {
        grid.switchViewPreset('dayNightShift', Sch.util.Date.add(new Date(), Sch.util.Date.DAY, -10), Sch.util.Date.add(new Date(), Sch.util.Date.DAY, 10));
    }
    var today = new Date();
    Ext.Date.clearTime(today);
    grid.scrollToDate(Sch.util.Date.add(today, Sch.util.Date.DAY, 0), true);
}

function ScrollToToday() {
    var grid = Ext.ComponentQuery.query("#gv")[0];
    var today = new Date();
    Ext.Date.clearTime(today);
    grid.scrollToDate(Sch.util.Date.add(today, Sch.util.Date.DAY, 0), true);
}