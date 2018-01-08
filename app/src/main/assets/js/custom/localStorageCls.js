var myLocalStorage = {
    currentUserDetail: null,
    activeProfile: null,
    resolution: null,
    generalFilters: null,//Comma separated string for ribbon filter
    ribbonFilters: {}, //Key and value filter for filter window
    setAllProfilesToLocalStorage: function(userId, profile) {
        if (myLocalStorage.isUserHaveAnyLocalData(userId)) { //User have local data please set Profiles
            if (myLocalStorage.currentUserDetail.profiles) {
                myLocalStorage.currentUserDetail.profiles = profile;
                var LocalJsonData = myLocalStorage.getAllLocalStorageData();
                if (LocalJsonData.users) {
                    $.each(LocalJsonData.users, function() {
                        if (this.id == userId) {
                            this.profiles = myLocalStorage.currentUserDetail.profiles;
                        }
                    });
                    myLocalStorage.setAllLocalStorageData(LocalJsonData);
                    //console.log(LocalJsonData);
                }
            }
        } else {
            if (myLocalStorage.isDeviceSupportLocalStorage()) {
                var AllLocalStorage = myLocalStorage.getAllLocalStorageData();
                if (AllLocalStorage == null) {
                    var AplanData = {
                        users: [
                            {
                                id: userId,
                                profiles: profile,
                                activeProfileId: profile[0].Id
                            }
                        ]
                    };
                    myLocalStorage.setAllLocalStorageData(AplanData);
                } else {
                    AllLocalStorage.users.push({
                        id: userId,
                        profiles: profile,
                        activeProfileId: profile[0].Id
                    });
                    myLocalStorage.setAllLocalStorageData(AllLocalStorage);
                }
            }
        }
    },
    getAllProfilesFromLocalStorage: function(userId) {
        var UserDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if (UserDetail && UserDetail.profiles) {
            return UserDetail.profiles;
        }
        return null;
    },
    isUserHaveAnyLocalData: function(userId) {
        if (myLocalStorage.isDeviceSupportLocalStorage()) {
            var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
            if (userDetail) {
                myLocalStorage.setUserDetail(userDetail);
                return true;
            } else {
                return false;
            }
        }
        return false;
    },
    isDeviceSupportLocalStorage: function() {
        if (typeof (Storage) !== "undefined") {
            return true;
        } else {
            Ext.Msg.alert('Error', "Your Browser does not support local storage please enable it from settings.");
            return false;
        }
    },
    getUserDetailFromLocalStorage: function(userId) {
        if (myLocalStorage.isDeviceSupportLocalStorage()) {
            var localDataString = localStorage.getItem("myAppData");
            if (localDataString) {
                var appData = myLocalStorage.parseLocalData(localDataString);
                if (appData.users) {
                    var allUsers = appData.users;
                    var result = allUsers.filter(function(obj) {
                        return obj.id == userId;
                    });
                    return result[0] || null;
                }
            } else {
                return null;
            }
        }
        return null;
    },
    getAllLocalStorageData: function() {
        var localDataString = localStorage.getItem("myAppData");
        if (localDataString) {
            return myLocalStorage.parseLocalData(localDataString);
        }
        return null;
    },
    setAllLocalStorageData: function(data) {
        if (data) {
            localStorage.setItem("myAppData", myLocalStorage.stringifyLocalData(data));
        }
    },
    stringifyLocalData: function(JSONdata) { //To store local data
        var string = JSON.stringify(JSONdata);
        return string;
    },
    parseLocalData: function(string) { //To parse local data back to json
        var JSONdata = JSON.parse(string);
        return JSONdata;
    },
    setUserDetail: function(userDetail) {
        myLocalStorage.currentUserDetail = userDetail;
    },
    getUserDetail: function(userId) {
        if (myLocalStorage.currentUserDetail) {
            return localStorage.currentUserDetail;
        }
        return null;
    },
    getActiveProfile: function(userId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if (userDetail) {
            if (userDetail.activeProfileId) {
                return userDetail.activeProfileId;
            }
        }
        return null;
    },
    setActiveProfile: function(userId, profileId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if (userDetail) {
            if (userDetail.activeProfileId) {
                userDetail.activeProfileId = profileId;
                myLocalStorage.saveLocalUserData(userId, userDetail);
            }
        }
    },
    saveLocalUserData: function(userId, dataToBeSave) {
        if(myLocalStorage.isUserHaveAnyLocalData(userId)) {
            //Fetch previous version of data
            var LocalJsonData = myLocalStorage.getAllLocalStorageData();
            if (LocalJsonData.users) {
                var recordFound = false;
                $.each(LocalJsonData.users, function(index) {
                    if(recordFound == false) {
                        if (this.id == userId) {
                            LocalJsonData.users[index] = dataToBeSave;
                            recordFound = true;
                        }
                    }
                });
                myLocalStorage.setAllLocalStorageData(LocalJsonData);
                //console.log(LocalJsonData);
            }
        }
    },
    removeGeneralFilter: function (userId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        var operationPerformed = -1;
        if(userDetail) {
            var savedFilters = userDetail.ribbonFilters || {};
            var totalCounts = Object.keys(savedFilters).length;
            if(totalCounts.length > 0) {
                //General filters exist remove it
                userDetail.ribbonFilters = {};
                myLocalStorage.saveLocalUserData(userId, userDetail);
                return operationPerformed = 1;
            }
            return operationPerformed = 0;
        }
        return operationPerformed;
    },
    setOrUnsetGeneralFilters: function(userId, filterToAddRemove) {
        //Set the general filter if not in the comma separated list.
        //If already exist in the string then remove it.
        var performedOperation = -1;
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail){
            var savedFilters = userDetail.generalFilters || "";
            if(myLocalStorage.checkIfGivenFilterExist(savedFilters, filterToAddRemove)){
                //filter exist, remove it
                savedFilters = savedFilters.replace(filterToAddRemove + ",", "");
                performedOperation = 0; //Filter removed
            } else {
                //filter does not exist, add it
                performedOperation = 1; //Filter added
                savedFilters += filterToAddRemove + ",";
            }
            userDetail["generalFilters"] = savedFilters;
            myLocalStorage.saveLocalUserData(userId, userDetail);
            return performedOperation;
        }
    },
    checkIfGivenFilterExist: function(allFilterString, filterToCheck) {
        if(allFilterString && filterToCheck) {
            return (allFilterString.search(filterToCheck) >= 0) ? true : false;
        }
    },
    getGeneralFilters: function(userId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail && userDetail.generalFilters) {
            return userDetail.generalFilters;
        }
        return null;
    },
    setOrUnsetRibbonFilters: function(userId, key, value) {
        //Set the general filter if not in the comma separated list.
        //If already exist in the string then remove it.
        var performedOperation = -1;
        //console.log(key + " = " + value);
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail){
            var savedFilters = userDetail.ribbonFilters || {};
            if(myLocalStorage.checkIfGivenRibbonFilterExist(userId, key)){
                //filter exist, remove it
                delete userDetail.ribbonFilters[key];
                performedOperation = 0; //Filter removed
            } else {
                //filter does not exist, add it
                performedOperation = 1; //Filter added
                savedFilters[key] = value;
            }
            userDetail["ribbonFilters"] = savedFilters;
            myLocalStorage.saveLocalUserData(userId, userDetail);
            return performedOperation;
        }
    },
    setOrUnsetSelectedGroupsIdsObject: function(userId, GroupIdsToAddOrRemove, key) {
        if(userId) {
            var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
            //console.log(userDetail);
            var shouldReloadData = false;
            if(userDetail.hasOwnProperty("ribbonFilters")) {
                if(userDetail.ribbonFilters.hasOwnProperty(key)) {
                    var previousFilters = userDetail.ribbonFilters[key];
                    if(previousFilters)
                        previousFilters = previousFilters.split(",");
                    var dataToSave = null;
                    if(GroupIdsToAddOrRemove.length > 0) {
                        Ext.Array.each(GroupIdsToAddOrRemove, function(rec1) {
                            var found = false;
                            Ext.Array.each(previousFilters, function(rec2) {
                                if(rec1 == rec2) {
                                    found = true;
                                }
                            });
                            if(found == false) {
                                shouldReloadData = true;
                            }
                        });
                        dataToSave = GroupIdsToAddOrRemove.join() + ",";
                    } else {
                        if(previousFilters && previousFilters.length > 1) {
                            shouldReloadData = true;
                            dataToSave =  "";
                        }
                    }
                    userDetail.ribbonFilters[key] = dataToSave;
                } else {
                    //Property GroupId does not set please set it.
                    userDetail.ribbonFilters[key] = GroupIdsToAddOrRemove.join() + ",";
                    shouldReloadData = true;
                    //console.log(shouldReloadData);
                }
            } else {
                //Property ribbonFilters and GroupId does not set please set it.
                userDetail["ribbonFilters"] = {};
                userDetail.ribbonFilters[key] = GroupIdsToAddOrRemove.join() + ",";
                shouldReloadData = true;
            }
            myLocalStorage.saveLocalUserData(userId, userDetail);
        } else {
            console.log("userId missing.");
        }
        return shouldReloadData;
    },
    setUnsetGeneralFilterTabFields: function(userId, key, value) { //key = AnyText value = "abc"
        var shouldReloadData = false;
        if(userId) {
            var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
            //console.log(userDetail);
            if(userDetail.hasOwnProperty("ribbonFilters")) {
                if(userDetail.ribbonFilters.hasOwnProperty(key)) {
                    var previousFilters = userDetail.ribbonFilters[key]; //xyz
                    if(value && value.length > 0) {
                        if(previousFilters != value) {
                            shouldReloadData = true;
                            userDetail.ribbonFilters[key] = value;
                        }
                    } else {
                        if(previousFilters != value) {
                            shouldReloadData = true; //Filter field is empty by user,
                        }
                        delete userDetail.ribbonFilters[key];
                    }
                } else {
                    //Property does not set please set it.
                    if(value && value.length > 0) {
                        userDetail.ribbonFilters[key] = value;
                        shouldReloadData = true;
                    }
                }
            } else {
                //Property ribbonFilters and ResourcesIds does not set please set it.
                userDetail["ribbonFilters"] = {};
                userDetail.ribbonFilters[key] = value;
                shouldReloadData = true;
            }
            myLocalStorage.saveLocalUserData(userId, userDetail);
        } else {
            console.log("userId missing.");
        }
        return shouldReloadData;
    },
    getRibbonFilters: function(userId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail && userDetail.ribbonFilters) {
            return userDetail.ribbonFilters;
        }
        return null;
    },
    getRibbonFilterSingle: function(userId, key) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail && userDetail.ribbonFilters) {
            if(userDetail.ribbonFilters[key]) {
                return userDetail.ribbonFilters[key];
            }
        }
        return null;
    },
    checkIfGivenRibbonFilterExist: function(userId, key) {
        if(!myLocalStorage.ribbonFilters) {
            myLocalStorage.ribbonFilters = myLocalStorage.getRibbonFilters(userId);
        }
        if(myLocalStorage.ribbonFilters && myLocalStorage.ribbonFilters[key]) {
            return true;
        }
        return false;
    },
    checkForActiveGeneralFilter: function(userId, filterToBeCheck) {
        if(!myLocalStorage.generalFilters) {
            myLocalStorage.generalFilters = myLocalStorage.getGeneralFilters(userId);
        }
        return (myLocalStorage.generalFilters && myLocalStorage.generalFilters.search(filterToBeCheck) >= 0)
                ? true : false;
    },
    getResolution: function(userId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail && userDetail.resolution) {
            return userDetail.resolution;
        }
        return "Week M"; //Default
    },
    setResolution: function(userId, resolutionToSet) {
        var performedOperation = -1;
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        var resolution = "";
        if(userDetail){
            userDetail["resolution"] = resolutionToSet;
            myLocalStorage.saveLocalUserData(userId, userDetail);
            return true;
        }
        return false;
    },
    getResolutionForGant: function(userId) {
        var userDetail = myLocalStorage.getUserDetailFromLocalStorage(userId);
        if(userDetail && userDetail.resolution) {
            switch (userDetail.resolution) {
                case "Seconds":
                    return "secondAndMinute";
                    break;

                case "Minutes":
                    return "minuteAndHour";
                    break;

                case "Hours":
                    return "hourAndDay";
                    break;

                case "Days":
                    return "weekAndDay";
                    break;

                case "Week S":
                    return "weekAndMonth";
                    break;

                case "Week M":
                    return "weekAndDayLetter";
                    break;

                case "Week L":
                    return "weekDateAndMonth";
                    break;

                case "Months":
                    return "monthAndYear";
                    break;

                case "Year":
                    return "year";
                    break;

                case "Quarter":
                    return "year";
                    break;

                case "DayNightShift":
                    return "dayNightShift";
                    break;

                default:
                    return "weekAndDayLetter";
                    break;

            }
        }
        return "weekAndDayLetter"; //Default
    }
};