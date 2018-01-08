var TIME_STAMP = "";
var GLOBAL_FILTER = { "DateRange": "", "PressedCounter": null };
var GLOBAL_RIBBON_FILTER = null;
var GLOBAL_GENERAL_FILTERS = null;
var includeFolderFilter = true;
var includeGeneralFilter = true;
//Check if filter fired from WEB VIEW just for WEB and testing filters
GLOBAL_RIBBON_FILTER = getQueryVariable("RibbonFilters"); //Remove this line once the filter testing is done

function SetFiltersDataFromAppMobile(Ribbonfilter, GeneralFilters) {

    if (typeof (Ribbonfilter) !== 'undefined') {
        GLOBAL_RIBBON_FILTER = Ribbonfilter;
    }
    if (typeof (GeneralFilters) !== 'undefined') {
        GLOBAL_GENERAL_FILTERS = GeneralFilters;
    }
    var grid = Ext.ComponentQuery.query("#gv")[0];
    if (grid) {
        grid.taskStore.load({
            scope: this,
            autoLoad: true,
            url: 'Tasks/Get?filter=&isCombine=' + false + '&Outline_Level=1&from=Ribbon'
        });
    }
    var grid2 = Ext.ComponentQuery.query("#resourceGV")[0];
    if (grid2) {
        grid2.taskStore.load({
            scope: this,
            autoLoad: true,
            url: 'Resource/Get?filter=&isCombine=' + false + '&Outline_Level=1&from=Ribbon'
        });
    }
}

/*
* This method will set filters from IOS or Android App
* */
function SetFiltersDataFromApp(Ribbonfilter, GeneralFilters, isExpandAll, collapseAll, nfoFilter, isReload) {
    var ExtraParam = "";
    /*if(isReload && isReload == true) { //For get latest data
        window.location.reload(true);
    }*/
    if (typeof (Ribbonfilter) !== 'undefined' && Ribbonfilter) {
        //GLOBAL_RIBBON_FILTER = Ribbonfilter;
        //Check of ribbon filter already exist
        if (!GLOBAL_RIBBON_FILTER) {
            GLOBAL_RIBBON_FILTER = "";
        }
        addOrRemoveFilter(Ribbonfilter);
    }
    if (typeof (GeneralFilters) !== 'undefined' && GeneralFilters) {
        GeneralFilters = removeBlankFields(GeneralFilters);
        GLOBAL_GENERAL_FILTERS = GeneralFilters;
        if (includeFolderFilter == false) {
            delete GLOBAL_GENERAL_FILTERS["GroupId"];
        }
        if (includeGeneralFilter == false) {
            delete GLOBAL_GENERAL_FILTERS["AnyText"];
            delete GLOBAL_GENERAL_FILTERS["PrioFrom"];
            delete GLOBAL_GENERAL_FILTERS["PrioTo"];
            delete GLOBAL_GENERAL_FILTERS["Level"];
            delete GLOBAL_GENERAL_FILTERS["IndentNo"];
            delete GLOBAL_GENERAL_FILTERS["TaskName"];
            delete GLOBAL_GENERAL_FILTERS["ClientId"];
            delete GLOBAL_GENERAL_FILTERS["ResponsiblePersonId"];
            delete GLOBAL_GENERAL_FILTERS["ResourceGroups"];
            delete GLOBAL_GENERAL_FILTERS["ResourceId"];
            delete GLOBAL_GENERAL_FILTERS["ChangeBy"];
            delete GLOBAL_GENERAL_FILTERS["ChangeBy"];
        }
        //addOrRemoveKeyValuePairFilter(GeneralFilters);
    }
    if (typeof (isExpandAll) !== 'undefined' && isExpandAll == true) {
        ExtraParam = "&explodeAll=explodeAll";
    }
    if (typeof (isExpandAll) !== 'undefined' && collapseAll == true) {
        ExtraParam = "&collapseAll=collapseAll";
    }
    var grid = Ext.ComponentQuery.query("#gv")[0];
    grid.taskStore.load({
        scope: this,
        autoLoad: true,
        url: 'Tasks/Get?filter=&isCombine=' + false + '&Outline_Level=1&from=Ribbon' + ExtraParam
    });

    grid.dependencyStore.load({ scope: this, autoLoad: true, url: "Dependencies/Get" });
    grid.resourceStore.load({ scope: this, autoLoad: true, url: "Resource/GetResourceFolder" });
    grid.assignmentStore.load({ scope: this, autoLoad: true, url: "Resource/Assignments" });

    var grid2 = Ext.ComponentQuery.query("#resourceGV")[0];
    if (grid2) {
        grid2.taskStore.load({
            scope: this,
            autoLoad: true,
            url: 'Resource/Get?filter=&isCombine=' + false + '&Outline_Level=1&from=Ribbon' + ExtraParam
        });
        grid2.resourceStore.load({ scope: this, autoLoad: true, url: 'Resource/GetResourceFolder' });
        grid2.assignmentStore.load({ scope: this, autoLoad: true, url: 'Resource/Assignments' });
    }


}

function removeBlankFields(data) {
    var loopAllKey = data;
    var ribbonFilter = data;
    for (var k in loopAllKey) {
        if (ribbonFilter.hasOwnProperty(k)) {
            //console.log(k);
            if (ribbonFilter[k] == "") {
                delete ribbonFilter[k];
            }
            if (k == "Criterion1" && ribbonFilter["Criterion1"] == "(empty)") {
                //remove every thing from crite area
                delete ribbonFilter["Criterion1"];
                if (ribbonFilter.hasOwnProperty("Criterion1FromDate")) {
                    delete ribbonFilter["Criterion1FromDate"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion1ToDate")) {
                    delete ribbonFilter["Criterion1ToDate"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion1TimeFrom")) {
                    delete ribbonFilter["Criterion1TimeFrom"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion1TimeTo")) {
                    delete ribbonFilter["Criterion1TimeTo"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion1TimeUnit")) {
                    delete ribbonFilter["Criterion1TimeUnit"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion1Type")) {
                    delete ribbonFilter["Criterion1Type"];
                }

            }

            if (k == "Criterion2" && ribbonFilter["Criterion2"] == "(empty)") {
                //remove every thing from crite area
                delete ribbonFilter["Criterion2"];
                if (ribbonFilter.hasOwnProperty("Criterion2FromDate")) {
                    delete ribbonFilter["Criterion2FromDate"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion2ToDate")) {
                    delete ribbonFilter["Criterion2ToDate"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion2TimeFrom")) {
                    delete ribbonFilter["Criterion2TimeFrom"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion2TimeTo")) {
                    delete ribbonFilter["Criterion2TimeTo"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion2TimeUnit")) {
                    delete ribbonFilter["Criterion2TimeUnit"];
                }
                if (ribbonFilter.hasOwnProperty("Criterion2Type")) {
                    delete ribbonFilter["Criterion2Type"];
                }
            }
        }
    }
    return ribbonFilter;
}

function addOrRemoveKeyValuePairFilter(keyAndValueJson) {
    var singleKey = Object.keys(keyAndValueJson)[0]; //Key to add or remove
    var allKeys = Object.keys(GLOBAL_RIBBON_FILTER);
    if (GLOBAL_RIBBON_FILTER && GLOBAL_RIBBON_FILTER[singleKey]) {
        delete GLOBAL_RIBBON_FILTER[singleKey];
    } else {
        if (GLOBAL_RIBBON_FILTER) {
            GLOBAL_RIBBON_FILTER[singleKey] = keyAndValueJson[singleKey];
        } else {
            GLOBAL_RIBBON_FILTER = keyAndValueJson;
        }
    }
}

function addOrRemoveFilter(param) {
    console.log(GLOBAL_RIBBON_FILTER.search(param + ","));
    var foundIndex = GLOBAL_RIBBON_FILTER.search(param);
    if (foundIndex < 0) {
        GLOBAL_RIBBON_FILTER += param + ",";
    } else {
        GLOBAL_RIBBON_FILTER = GLOBAL_RIBBON_FILTER.replace(param + ",", "");
    }
    console.log(GLOBAL_RIBBON_FILTER);
}

/*Function to change the resolution of Gantt-Chart control*/
function changeResolution(ResolutionName) {
    var resolutionName = "";
    if (typeof ResolutionName !== 'undefined' && ResolutionName) {
        resolutionName = ResolutionName;
    } else {
        var mylist = document.getElementById("Resolution");
        resolutionName = mylist.options[mylist.selectedIndex].text;
    }
    var grid = Ext.ComponentQuery.query("#gv")[0];
    var resourceControl = Ext.ComponentQuery.query("#resourceGV")[0];

    //Second View Preset
    if (resolutionName == "Seconds") {
        if (grid) {
            grid.switchViewPreset('secondAndMinute', Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('secondAndMinute', Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, 10));
        }
    }
    //Minutes View Preset
    else if (resolutionName == "Minutes") {
        if (grid) {
            grid.switchViewPreset('minuteAndHour', Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('minuteAndHour', Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MINUTE, 10));
        }
    }
    //Hours View Preset
    else if (resolutionName == "Hours") {
        if (grid) {
            grid.switchViewPreset('hourAndDay', Sch.util.Date.add(new Date(), Sch.util.Date.DAY, -10), Sch.util.Date.add(new Date(), Sch.util.Date.DAY, 10));
        }

        if (resourceControl) {
            resourceControl.switchViewPreset('hourAndDay', Sch.util.Date.add(new Date(), Sch.util.Date.DAY, -10), Sch.util.Date.add(new Date(), Sch.util.Date.DAY, 10));
        }
    }
    //Days View Preset
    else if (resolutionName == "Days") {
        if (grid) {
            grid.switchViewPreset('weekAndDay', Sch.util.Date.add(new Date(), Sch.util.Date.WEEK, -10), Sch.util.Date.add(new Date(), Sch.util.Date.WEEK, 10));
        }

        if (resourceControl) {
            resourceControl.switchViewPreset('weekAndDay', Sch.util.Date.add(new Date(), Sch.util.Date.WEEK, -10), Sch.util.Date.add(new Date(), Sch.util.Date.WEEK, 10));
        }
    }
    //Week Small View Preset
    else if (resolutionName == "Week S") {
        if (grid) {
            grid.switchViewPreset('weekAndMonth', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('weekAndMonth', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
    }
    //Week Medium View Preset
    else if (resolutionName == "Week M") {
        if (grid) {
            grid.switchViewPreset('weekAndDayLetter', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }

        if (resourceControl) {
            resourceControl.switchViewPreset('weekAndDayLetter', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
    }
    //Weel Long View Preset
    else if (resolutionName == "Week L") {
        if (grid) {
            grid.switchViewPreset('weekDateAndMonth', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('weekDateAndMonth', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
    }
    //Months View Preset
    else if (resolutionName == "Months") {
        if (grid) {
            grid.switchViewPreset('monthAndYear', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('monthAndYear', Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, -10), Sch.util.Date.add(new Date(), Sch.util.Date.MONTH, 10));
        }
    }
    //Year View Preset
    else if (resolutionName == "Year") {
        if (grid) {
            grid.switchViewPreset('year', Sch.util.Date.add(new Date(), Sch.util.Date.YEAR, -10), Sch.util.Date.add(new Date(), Sch.util.Date.YEAR, 10));
        }

        if (resourceControl) {
            resourceControl.switchViewPreset('year', Sch.util.Date.add(new Date(), Sch.util.Date.YEAR, -10), Sch.util.Date.add(new Date(), Sch.util.Date.YEAR, 10));
        }
    }
    //Quarter View Preset
    else if (resolutionName == "Quarter") {
        if (grid) {
            grid.switchViewPreset('year', Sch.util.Date.add(new Date(), Sch.util.Date.QUARTER, -10), Sch.util.Date.add(new Date(), Sch.util.Date.QUARTER, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('year', Sch.util.Date.add(new Date(), Sch.util.Date.QUARTER, -10), Sch.util.Date.add(new Date(), Sch.util.Date.QUARTER, 10));
        }
    }
    //DayNightShift View Preset
    else if (resolutionName == "DayNightShift") {
        if (grid) {
            grid.switchViewPreset('dayNightShift', Sch.util.Date.add(new Date(), Sch.util.Date.DAY, -10), Sch.util.Date.add(new Date(), Sch.util.Date.DAY, 10));
        }
        if (resourceControl) {
            resourceControl.switchViewPreset('dayNightShift', Sch.util.Date.add(new Date(), Sch.util.Date.DAY, -10), Sch.util.Date.add(new Date(), Sch.util.Date.DAY, 10));
        }
    }
    var today = new Date();
    Ext.Date.clearTime(today);
    if (grid) {
        grid.scrollToDate(Sch.util.Date.add(today, Sch.util.Date.DAY, 0), true);
    }
    if (resourceControl) {
        resourceControl.scrollToDate(Sch.util.Date.add(today, Sch.util.Date.DAY, 0), true);
    }
}

function ScrollToToday() {
    var grid = Ext.ComponentQuery.query("#gv")[0];
    var today = new Date();
    Ext.Date.clearTime(today);
    if (grid) {
        grid.scrollToDate(Sch.util.Date.add(today, Sch.util.Date.DAY, 0), true);
    }
    var grid2 = Ext.ComponentQuery.query("#resourceGV")[0];
    if (grid2) {
        grid2.scrollToDate(Sch.util.Date.add(today, Sch.util.Date.DAY, 0), true);
    }
}

function GetWorkUnit(Unit) {
    var UnitAbbreviation = "";
    switch (Unit) {
        case 1:
            return " Amin";
            break;
        case 3:
            return " AT";
            break;
        case 4:
            return " AW";
            break;
        case 5:
            return " AM";
            break;
        case 6:
            return " AJ";
            break;
        default:
            return " Ah";
            break;
    }
}

function GetDurationUnit(Unit) {
    var UnitAbbreviation = "";
    switch (Unit) {
        case 1:
            return " min";
            break;
        case 3:
            return " T";
            break;
        case 4:
            return " W";
            break;
        case 5:
            return " M";
            break;
        case 6:
            return " J";
            break;
        default:
            return " h";
            break;
    }
}
function GetDurationUnitColumn(Unit) {
    var UnitAbbreviation = "";
    switch (Unit) {
        case 1:
            return "Minutes(min)";
            break;
        case 3:
            return "Days(T)";
            break;
        case 4:
            return "Weeks(W)";
            break;
        case 5:
            return "Months(M)";
            break;
        case 6:
            return "Years(J)";
            break;
        default:
            return "^Hours(h)";
            break;
    }
}
function InsertLog(Log) {
    var AllError = [];
    window.location.hash = 'logs';
    if (localStorage.getItem("AplanErrorLog")) {
        var stringData = localStorage.getItem('AplanErrorLog');
        AllError = JSON.parse(stringData);
    }
    AllError.push(Log);
    localStorage.setItem('AplanErrorLog', JSON.stringify(AllError));
}
function GetErrorLogs() {
    //var SingleError = {"Id" : GUID(), "ErrorCode": "Service Error: 101" , "Message": "Testing Error", "URL": "http://google.com", "Date": new Date()};
    //InsertLog(SingleError);
    var AllError = [];
    if (localStorage.getItem("AplanErrorLog")) {
        AllError = JSON.parse(localStorage.getItem('AplanErrorLog'));
    }
    //return JSON.stringify(AllError));
    //return JSON.Strin AllError;
     window.cpjs.sendToAndroid(JSON.stringify(AllError));
}

function removeSubmittedLogs() {
    localStorage.setItem("AplanErrorLog", null);
    history.pushState("", document.title, window.location.pathname + window.location.search);
}

function GUID() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }
    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

function GetFormatedDate(date) {
    var FinaleDate = "";
    if (date && typeof (date) !== 'undefined') {
        var hours = date.getHours();
        var minutes = date.getMinutes();
        var ampm = hours >= 12 ? 'pm' : 'am';
        hours = hours % 12;
        hours = hours ? hours : 12; // the hour '0' should be '12'
        minutes = minutes < 10 ? '0' + minutes : minutes;
        var strTime = hours + ' :' + minutes + ' ' + ampm;
        FinaleDate = Config.Data.DE_DATE_FORMAT + strTime;
    }
    return FinaleDate;
}
function FormatedNumber(number) {
    return number > 0 ? number + " €" : "";
}
function setBtnClass(elem, doNotUseToggle) {
    var setOrUnsetParam = false;
    if (!Ext.getCmp(elem)) {
        return false;
    }
    if (Ext.getCmp(elem).pressed) {
        Ext.getCmp(elem).removeCls('active-btn');
        Ext.getCmp(elem).addCls('in-active-btn');
        setOrUnsetParam = false;
    } else {
        Ext.getCmp(elem).addCls('active-btn');
        Ext.getCmp(elem).removeCls('in-active-btn');
        setOrUnsetParam = true;
    }
    if (!doNotUseToggle) {
        Ext.getCmp(elem).toggle();
    }
    return setOrUnsetParam;
}
function GET_ERROR_DETAIL(CODE, LANG) {
    if(LANG && LANG.length > 0) {
        if (Config.Data.ERROR_CODE[LANG][CODE]) {
            return Config.Data.ERROR_CODE[LANG][CODE];
        }
    }
    if (Config.Data.ERROR_CODE[getQueryVariable("Lang")][CODE]) {
        return Config.Data.ERROR_CODE[getQueryVariable("Lang")][CODE];
    }
    return "UNKNOWN ERROR!";
}

function checkIfGivenRibbonFilterExist(filterToCheck) {
    return (GLOBAL_RIBBON_FILTER && filterToCheck && GLOBAL_RIBBON_FILTER.search(filterToCheck) >= 0) ? true : false;
}

function setActiveRibbonFilters() {
    var allRibbonFilters = ['ShowOverdueFinished', 'ShowStarted', 'ShowFinished', 'ShowFinishToday', 'ShowPending', 'ShowPassiv', 'ShowFinishWeek',
        'ShowNoTime', 'ShowCanceled', 'TL_Red', 'TL_Yellow', 'TL_Green', 'ShowArrow', 'ShowReminder', 'ShowCritical', 'ShowOverdueLimit',
        'ShowQuestion', 'closeAllFolder', 'expandAllFolder', 'showresource'
    ];
    try {
        for (var i = 0; i < allRibbonFilters.length; i++) {
            if (checkIfGivenRibbonFilterExist(allRibbonFilters[i])) {
                Ext.getCmp(allRibbonFilters[i]).toggle(true);  // to force pressed state
                setBtnClass(allRibbonFilters[i], true);
            } else {
                Ext.getCmp(allRibbonFilters[i]).toggle(false);
            }
        }
    } catch (err) {
        console.log(err);
    }

}