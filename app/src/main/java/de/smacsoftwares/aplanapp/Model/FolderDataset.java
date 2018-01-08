
package de.smacsoftwares.aplanapp.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// this is model class for folder listview
public class FolderDataset {
    boolean isSelected;
    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("Page")
    @Expose
    private Integer page;
    @SerializedName("PageSize")
    @Expose
    private Integer pageSize;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("GroupId")
    @Expose
    private String groupId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("Priority")
    @Expose
    private Integer priority;
    @SerializedName("Stage")
    @Expose
    private Integer stage;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("StatusObj")
    @Expose
    private String statusObj;
    @SerializedName("CreatorId")
    @Expose
    private Integer creatorId;
    @SerializedName("TrafficLightObj")
    @Expose
    private String trafficLightObj;
    @SerializedName("ProjectId")
    @Expose
    private Object projectId;
    @SerializedName("TotalSubTask")
    @Expose
    private Integer totalSubTask;
    @SerializedName("Object")
    @Expose
    private Integer object;
    @SerializedName("parentId")
    @Expose
    private Object parentId;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("LateFinish")
    @Expose
    private Object lateFinish;
    @SerializedName("LateStart")
    @Expose
    private Object lateStart;
    @SerializedName("EarlyStart")
    @Expose
    private Object earlyStart;
    @SerializedName("Actual_Start")
    @Expose
    private String actualStart;
    @SerializedName("Metas")
    @Expose
    private Object metas;
    @SerializedName("Actual_Finish")
    @Expose
    private String actualFinish;
    @SerializedName("Rem_Time")
    @Expose
    private Object remTime;
    @SerializedName("Reshow_Date")
    @Expose
    private Object reshowDate;
    @SerializedName("Completed")
    @Expose
    private Object completed;
    @SerializedName("LatestEndDate")
    @Expose
    private Object latestEndDate;
    @SerializedName("isCriticalTask")
    @Expose
    private Object isCriticalTask;
    @SerializedName("isClarificationRequired")
    @Expose
    private Object isClarificationRequired;
    @SerializedName("isNotice")
    @Expose
    private Object isNotice;
    @SerializedName("isReshow")
    @Expose
    private Object isReshow;
    @SerializedName("isCompleted")
    @Expose
    private Object isCompleted;
    @SerializedName("isPassive")
    @Expose
    private Object isPassive;
    @SerializedName("isCancelled")
    @Expose
    private Object isCancelled;
    @SerializedName("UnitForWork")
    @Expose
    private String unitForWork;
    @SerializedName("DurationUnit")
    @Expose
    private String durationUnit;
    @SerializedName("Duration")
    @Expose
    private Integer duration;
    @SerializedName("Duration_Act_div_Pld")
    @Expose
    private Integer durationActDivPld;
    @SerializedName("Actual_Duration")
    @Expose
    private Integer actualDuration;
    @SerializedName("ForcastedDuration")
    @Expose
    private Integer forcastedDuration;
    @SerializedName("DurationUnitDefault")
    @Expose
    private Integer durationUnitDefault;
    @SerializedName("Req_Ress")
    @Expose
    private Integer reqRess;
    @SerializedName("Req_Effort")
    @Expose
    private Double reqEffort;
    @SerializedName("Effort")
    @Expose
    private Double effort;
    @SerializedName("Ovt_Effort")
    @Expose
    private Double ovtEffort;
    @SerializedName("Actual_Effort")
    @Expose
    private Double actualEffort;
    @SerializedName("Work_progn")
    @Expose
    private Double workProgn;
    @SerializedName("Actual_Ovt_Effort")
    @Expose
    private Double actualOvtEffort;
    @SerializedName("PercentDone")
    @Expose
    private Double percentDone;
    @SerializedName("Time_Unit_of_Cost")
    @Expose
    private Integer timeUnitOfCost;
    @SerializedName("Cost_Calc_Meth")
    @Expose
    private Integer costCalcMeth;
    @SerializedName("Ovt_Cost")
    @Expose
    private Double ovtCost;
    @SerializedName("Act_Ovt_Cost")
    @Expose
    private Integer actOvtCost;
    @SerializedName("Standard_Rate")
    @Expose
    private Double standardRate;
    @SerializedName("Act_Standard_Rate")
    @Expose
    private Integer actStandardRate;
    @SerializedName("Fixed_Cost")
    @Expose
    private Integer fixedCost;
    @SerializedName("Act_Fixed_Cost")
    @Expose
    private Integer actFixedCost;
    @SerializedName("Total_Cost")
    @Expose
    private Double totalCost;
    @SerializedName("Act_Total_Cost")
    @Expose
    private Integer actTotalCost;
    @SerializedName("Total_Cost_Progn")
    @Expose
    private Double totalCostProgn;
    @SerializedName("leaf")
    @Expose
    private Boolean leaf;
    @SerializedName("children")
    @Expose
    private Object children;
    @SerializedName("NoticeStatus")
    @Expose
    private String noticeStatus;
    @SerializedName("Ord_ID")
    @Expose
    private String ordID;
    @SerializedName("Pro_ID")
    @Expose
    private Object proID;
    @SerializedName("Vor_ID")
    @Expose
    private String vorID;
    @SerializedName("expanded")
    @Expose
    private Boolean expanded;
    @SerializedName("Number1")
    @Expose
    private Integer number1;
    @SerializedName("Number2")
    @Expose
    private Integer number2;
    @SerializedName("Number3")
    @Expose
    private Integer number3;
    @SerializedName("Number4")
    @Expose
    private Integer number4;
    @SerializedName("Number5")
    @Expose
    private Integer number5;
    @SerializedName("Number6")
    @Expose
    private Integer number6;
    @SerializedName("Number7")
    @Expose
    private Integer number7;
    @SerializedName("Number8")
    @Expose
    private Integer number8;
    @SerializedName("Number9")
    @Expose
    private Integer number9;
    @SerializedName("Number10")
    @Expose
    private Integer number10;
    @SerializedName("Text1")
    @Expose
    private String text1;
    @SerializedName("Text2")
    @Expose
    private Object text2;
    @SerializedName("Text3")
    @Expose
    private Object text3;
    @SerializedName("Text4")
    @Expose
    private Object text4;
    @SerializedName("Text5")
    @Expose
    private Object text5;
    @SerializedName("Text6")
    @Expose
    private Object text6;
    @SerializedName("Text7")
    @Expose
    private Object text7;
    @SerializedName("Text8")
    @Expose
    private Object text8;
    @SerializedName("Text9")
    @Expose
    private Object text9;
    @SerializedName("Text10")
    @Expose
    private Object text10;

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The __type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * 
     * @param page
     *     The Page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 
     * @return
     *     The pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 
     * @param pageSize
     *     The PageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 
     * @param groupId
     *     The GroupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * 
     * @param notes
     *     The Notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 
     * @return
     *     The priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The Priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The stage
     */
    public Integer getStage() {
        return stage;
    }

    /**
     * 
     * @param stage
     *     The Stage
     */
    public void setStage(Integer stage) {
        this.stage = stage;
    }

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The Status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The statusObj
     */
    public String getStatusObj() {
        return statusObj;
    }

    /**
     * 
     * @param statusObj
     *     The StatusObj
     */
    public void setStatusObj(String statusObj) {
        this.statusObj = statusObj;
    }

    /**
     * 
     * @return
     *     The creatorId
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * 
     * @param creatorId
     *     The CreatorId
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 
     * @return
     *     The trafficLightObj
     */
    public String getTrafficLightObj() {
        return trafficLightObj;
    }

    /**
     * 
     * @param trafficLightObj
     *     The TrafficLightObj
     */
    public void setTrafficLightObj(String trafficLightObj) {
        this.trafficLightObj = trafficLightObj;
    }

    /**
     * 
     * @return
     *     The projectId
     */
    public Object getProjectId() {
        return projectId;
    }

    /**
     * 
     * @param projectId
     *     The ProjectId
     */
    public void setProjectId(Object projectId) {
        this.projectId = projectId;
    }

    /**
     * 
     * @return
     *     The totalSubTask
     */
    public Integer getTotalSubTask() {
        return totalSubTask;
    }

    /**
     * 
     * @param totalSubTask
     *     The TotalSubTask
     */
    public void setTotalSubTask(Integer totalSubTask) {
        this.totalSubTask = totalSubTask;
    }

    /**
     * 
     * @return
     *     The object
     */
    public Integer getObject() {
        return object;
    }

    /**
     * 
     * @param object
     *     The Object
     */
    public void setObject(Integer object) {
        this.object = object;
    }

    /**
     * 
     * @return
     *     The parentId
     */
    public Object getParentId() {
        return parentId;
    }

    /**
     * 
     * @param parentId
     *     The parentId
     */
    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The StartDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The EndDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     *     The lateFinish
     */
    public Object getLateFinish() {
        return lateFinish;
    }

    /**
     * 
     * @param lateFinish
     *     The LateFinish
     */
    public void setLateFinish(Object lateFinish) {
        this.lateFinish = lateFinish;
    }

    /**
     * 
     * @return
     *     The lateStart
     */
    public Object getLateStart() {
        return lateStart;
    }

    /**
     * 
     * @param lateStart
     *     The LateStart
     */
    public void setLateStart(Object lateStart) {
        this.lateStart = lateStart;
    }

    /**
     * 
     * @return
     *     The earlyStart
     */
    public Object getEarlyStart() {
        return earlyStart;
    }

    /**
     * 
     * @param earlyStart
     *     The EarlyStart
     */
    public void setEarlyStart(Object earlyStart) {
        this.earlyStart = earlyStart;
    }

    /**
     * 
     * @return
     *     The actualStart
     */
    public String getActualStart() {
        return actualStart;
    }

    /**
     * 
     * @param actualStart
     *     The Actual_Start
     */
    public void setActualStart(String actualStart) {
        this.actualStart = actualStart;
    }

    /**
     * 
     * @return
     *     The metas
     */
    public Object getMetas() {
        return metas;
    }

    /**
     * 
     * @param metas
     *     The Metas
     */
    public void setMetas(Object metas) {
        this.metas = metas;
    }

    /**
     * 
     * @return
     *     The actualFinish
     */
    public String getActualFinish() {
        return actualFinish;
    }

    /**
     * 
     * @param actualFinish
     *     The Actual_Finish
     */
    public void setActualFinish(String actualFinish) {
        this.actualFinish = actualFinish;
    }

    /**
     * 
     * @return
     *     The remTime
     */
    public Object getRemTime() {
        return remTime;
    }

    /**
     * 
     * @param remTime
     *     The Rem_Time
     */
    public void setRemTime(Object remTime) {
        this.remTime = remTime;
    }

    /**
     * 
     * @return
     *     The reshowDate
     */
    public Object getReshowDate() {
        return reshowDate;
    }

    /**
     * 
     * @param reshowDate
     *     The Reshow_Date
     */
    public void setReshowDate(Object reshowDate) {
        this.reshowDate = reshowDate;
    }

    /**
     * 
     * @return
     *     The completed
     */
    public Object getCompleted() {
        return completed;
    }

    /**
     * 
     * @param completed
     *     The Completed
     */
    public void setCompleted(Object completed) {
        this.completed = completed;
    }

    /**
     * 
     * @return
     *     The latestEndDate
     */
    public Object getLatestEndDate() {
        return latestEndDate;
    }

    /**
     * 
     * @param latestEndDate
     *     The LatestEndDate
     */
    public void setLatestEndDate(Object latestEndDate) {
        this.latestEndDate = latestEndDate;
    }

    /**
     * 
     * @return
     *     The isCriticalTask
     */
    public Object getIsCriticalTask() {
        return isCriticalTask;
    }

    /**
     * 
     * @param isCriticalTask
     *     The isCriticalTask
     */
    public void setIsCriticalTask(Object isCriticalTask) {
        this.isCriticalTask = isCriticalTask;
    }

    /**
     * 
     * @return
     *     The isClarificationRequired
     */
    public Object getIsClarificationRequired() {
        return isClarificationRequired;
    }

    /**
     * 
     * @param isClarificationRequired
     *     The isClarificationRequired
     */
    public void setIsClarificationRequired(Object isClarificationRequired) {
        this.isClarificationRequired = isClarificationRequired;
    }

    /**
     * 
     * @return
     *     The isNotice
     */
    public Object getIsNotice() {
        return isNotice;
    }

    /**
     * 
     * @param isNotice
     *     The isNotice
     */
    public void setIsNotice(Object isNotice) {
        this.isNotice = isNotice;
    }

    /**
     * 
     * @return
     *     The isReshow
     */
    public Object getIsReshow() {
        return isReshow;
    }

    /**
     * 
     * @param isReshow
     *     The isReshow
     */
    public void setIsReshow(Object isReshow) {
        this.isReshow = isReshow;
    }

    /**
     * 
     * @return
     *     The isCompleted
     */
    public Object getIsCompleted() {
        return isCompleted;
    }

    /**
     * 
     * @param isCompleted
     *     The isCompleted
     */
    public void setIsCompleted(Object isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * 
     * @return
     *     The isPassive
     */
    public Object getIsPassive() {
        return isPassive;
    }

    /**
     * 
     * @param isPassive
     *     The isPassive
     */
    public void setIsPassive(Object isPassive) {
        this.isPassive = isPassive;
    }

    /**
     * 
     * @return
     *     The isCancelled
     */
    public Object getIsCancelled() {
        return isCancelled;
    }

    /**
     * 
     * @param isCancelled
     *     The isCancelled
     */
    public void setIsCancelled(Object isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * 
     * @return
     *     The unitForWork
     */
    public String getUnitForWork() {
        return unitForWork;
    }

    /**
     * 
     * @param unitForWork
     *     The UnitForWork
     */
    public void setUnitForWork(String unitForWork) {
        this.unitForWork = unitForWork;
    }

    /**
     * 
     * @return
     *     The durationUnit
     */
    public String getDurationUnit() {
        return durationUnit;
    }

    /**
     * 
     * @param durationUnit
     *     The DurationUnit
     */
    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    /**
     * 
     * @return
     *     The duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * 
     * @param duration
     *     The Duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * 
     * @return
     *     The durationActDivPld
     */
    public Integer getDurationActDivPld() {
        return durationActDivPld;
    }

    /**
     * 
     * @param durationActDivPld
     *     The Duration_Act_div_Pld
     */
    public void setDurationActDivPld(Integer durationActDivPld) {
        this.durationActDivPld = durationActDivPld;
    }

    /**
     * 
     * @return
     *     The actualDuration
     */
    public Integer getActualDuration() {
        return actualDuration;
    }

    /**
     * 
     * @param actualDuration
     *     The Actual_Duration
     */
    public void setActualDuration(Integer actualDuration) {
        this.actualDuration = actualDuration;
    }

    /**
     * 
     * @return
     *     The forcastedDuration
     */
    public Integer getForcastedDuration() {
        return forcastedDuration;
    }

    /**
     * 
     * @param forcastedDuration
     *     The ForcastedDuration
     */
    public void setForcastedDuration(Integer forcastedDuration) {
        this.forcastedDuration = forcastedDuration;
    }

    /**
     * 
     * @return
     *     The durationUnitDefault
     */
    public Integer getDurationUnitDefault() {
        return durationUnitDefault;
    }

    /**
     * 
     * @param durationUnitDefault
     *     The DurationUnitDefault
     */
    public void setDurationUnitDefault(Integer durationUnitDefault) {
        this.durationUnitDefault = durationUnitDefault;
    }

    /**
     * 
     * @return
     *     The reqRess
     */
    public Integer getReqRess() {
        return reqRess;
    }

    /**
     * 
     * @param reqRess
     *     The Req_Ress
     */
    public void setReqRess(Integer reqRess) {
        this.reqRess = reqRess;
    }

    /**
     * 
     * @return
     *     The reqEffort
     */
    public Double getReqEffort() {
        return reqEffort;
    }

    /**
     * 
     * @param reqEffort
     *     The Req_Effort
     */
    public void setReqEffort(Double reqEffort) {
        this.reqEffort = reqEffort;
    }

    /**
     * 
     * @return
     *     The effort
     */
    public Double getEffort() {
        return effort;
    }

    /**
     * 
     * @param effort
     *     The Effort
     */
    public void setEffort(Double effort) {
        this.effort = effort;
    }

    /**
     * 
     * @return
     *     The ovtEffort
     */
    public Double getOvtEffort() {
        return ovtEffort;
    }

    /**
     * 
     * @param ovtEffort
     *     The Ovt_Effort
     */
    public void setOvtEffort(Double ovtEffort) {
        this.ovtEffort = ovtEffort;
    }

    /**
     * 
     * @return
     *     The actualEffort
     */
    public Double getActualEffort() {
        return actualEffort;
    }

    /**
     * 
     * @param actualEffort
     *     The Actual_Effort
     */
    public void setActualEffort(Double actualEffort) {
        this.actualEffort = actualEffort;
    }

    /**
     * 
     * @return
     *     The workProgn
     */
    public Double getWorkProgn() {
        return workProgn;
    }

    /**
     * 
     * @param workProgn
     *     The Work_progn
     */
    public void setWorkProgn(Double workProgn) {
        this.workProgn = workProgn;
    }

    /**
     * 
     * @return
     *     The actualOvtEffort
     */
    public Double getActualOvtEffort() {
        return actualOvtEffort;
    }

    /**
     * 
     * @param actualOvtEffort
     *     The Actual_Ovt_Effort
     */
    public void setActualOvtEffort(Double actualOvtEffort) {
        this.actualOvtEffort = actualOvtEffort;
    }

    /**
     * 
     * @return
     *     The percentDone
     */
    public Double getPercentDone() {
        return percentDone;
    }

    /**
     * 
     * @param percentDone
     *     The PercentDone
     */
    public void setPercentDone(Double percentDone) {
        this.percentDone = percentDone;
    }

    /**
     * 
     * @return
     *     The timeUnitOfCost
     */
    public Integer getTimeUnitOfCost() {
        return timeUnitOfCost;
    }

    /**
     * 
     * @param timeUnitOfCost
     *     The Time_Unit_of_Cost
     */
    public void setTimeUnitOfCost(Integer timeUnitOfCost) {
        this.timeUnitOfCost = timeUnitOfCost;
    }

    /**
     * 
     * @return
     *     The costCalcMeth
     */
    public Integer getCostCalcMeth() {
        return costCalcMeth;
    }

    /**
     * 
     * @param costCalcMeth
     *     The Cost_Calc_Meth
     */
    public void setCostCalcMeth(Integer costCalcMeth) {
        this.costCalcMeth = costCalcMeth;
    }

    /**
     * 
     * @return
     *     The ovtCost
     */
    public Double getOvtCost() {
        return ovtCost;
    }

    /**
     * 
     * @param ovtCost
     *     The Ovt_Cost
     */
    public void setOvtCost(Double ovtCost) {
        this.ovtCost = ovtCost;
    }

    /**
     * 
     * @return
     *     The actOvtCost
     */
    public Integer getActOvtCost() {
        return actOvtCost;
    }

    /**
     * 
     * @param actOvtCost
     *     The Act_Ovt_Cost
     */
    public void setActOvtCost(Integer actOvtCost) {
        this.actOvtCost = actOvtCost;
    }

    /**
     * 
     * @return
     *     The standardRate
     */
    public Double getStandardRate() {
        return standardRate;
    }

    /**
     * 
     * @param standardRate
     *     The Standard_Rate
     */
    public void setStandardRate(Double standardRate) {
        this.standardRate = standardRate;
    }

    /**
     * 
     * @return
     *     The actStandardRate
     */
    public Integer getActStandardRate() {
        return actStandardRate;
    }

    /**
     * 
     * @param actStandardRate
     *     The Act_Standard_Rate
     */
    public void setActStandardRate(Integer actStandardRate) {
        this.actStandardRate = actStandardRate;
    }

    /**
     * 
     * @return
     *     The fixedCost
     */
    public Integer getFixedCost() {
        return fixedCost;
    }

    /**
     * 
     * @param fixedCost
     *     The Fixed_Cost
     */
    public void setFixedCost(Integer fixedCost) {
        this.fixedCost = fixedCost;
    }

    /**
     * 
     * @return
     *     The actFixedCost
     */
    public Integer getActFixedCost() {
        return actFixedCost;
    }

    /**
     * 
     * @param actFixedCost
     *     The Act_Fixed_Cost
     */
    public void setActFixedCost(Integer actFixedCost) {
        this.actFixedCost = actFixedCost;
    }

    /**
     * 
     * @return
     *     The totalCost
     */
    public Double getTotalCost() {
        return totalCost;
    }

    /**
     * 
     * @param totalCost
     *     The Total_Cost
     */
    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * 
     * @return
     *     The actTotalCost
     */
    public Integer getActTotalCost() {
        return actTotalCost;
    }

    /**
     * 
     * @param actTotalCost
     *     The Act_Total_Cost
     */
    public void setActTotalCost(Integer actTotalCost) {
        this.actTotalCost = actTotalCost;
    }

    /**
     * 
     * @return
     *     The totalCostProgn
     */
    public Double getTotalCostProgn() {
        return totalCostProgn;
    }

    /**
     * 
     * @param totalCostProgn
     *     The Total_Cost_Progn
     */
    public void setTotalCostProgn(Double totalCostProgn) {
        this.totalCostProgn = totalCostProgn;
    }

    /**
     * 
     * @return
     *     The leaf
     */
    public Boolean getLeaf() {
        return leaf;
    }

    /**
     * 
     * @param leaf
     *     The leaf
     */
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    /**
     * 
     * @return
     *     The children
     */
    public Object getChildren() {
        return children;
    }

    /**
     * 
     * @param children
     *     The children
     */
    public void setChildren(Object children) {
        this.children = children;
    }

    /**
     * 
     * @return
     *     The noticeStatus
     */
    public String getNoticeStatus() {
        return noticeStatus;
    }

    /**
     * 
     * @param noticeStatus
     *     The NoticeStatus
     */
    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    /**
     * 
     * @return
     *     The ordID
     */
    public String getOrdID() {
        return ordID;
    }

    /**
     * 
     * @param ordID
     *     The Ord_ID
     */
    public void setOrdID(String ordID) {
        this.ordID = ordID;
    }

    /**
     * 
     * @return
     *     The proID
     */
    public Object getProID() {
        return proID;
    }

    /**
     * 
     * @param proID
     *     The Pro_ID
     */
    public void setProID(Object proID) {
        this.proID = proID;
    }

    /**
     * 
     * @return
     *     The vorID
     */
    public String getVorID() {
        return vorID;
    }

    /**
     * 
     * @param vorID
     *     The Vor_ID
     */
    public void setVorID(String vorID) {
        this.vorID = vorID;
    }

    /**
     * 
     * @return
     *     The expanded
     */
    public Boolean getExpanded() {
        return expanded;
    }

    /**
     * 
     * @param expanded
     *     The expanded
     */
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * 
     * @return
     *     The number1
     */
    public Integer getNumber1() {
        return number1;
    }

    /**
     * 
     * @param number1
     *     The Number1
     */
    public void setNumber1(Integer number1) {
        this.number1 = number1;
    }

    /**
     * 
     * @return
     *     The number2
     */
    public Integer getNumber2() {
        return number2;
    }

    /**
     * 
     * @param number2
     *     The Number2
     */
    public void setNumber2(Integer number2) {
        this.number2 = number2;
    }

    /**
     * 
     * @return
     *     The number3
     */
    public Integer getNumber3() {
        return number3;
    }

    /**
     * 
     * @param number3
     *     The Number3
     */
    public void setNumber3(Integer number3) {
        this.number3 = number3;
    }

    /**
     * 
     * @return
     *     The number4
     */
    public Integer getNumber4() {
        return number4;
    }

    /**
     * 
     * @param number4
     *     The Number4
     */
    public void setNumber4(Integer number4) {
        this.number4 = number4;
    }

    /**
     * 
     * @return
     *     The number5
     */
    public Integer getNumber5() {
        return number5;
    }

    /**
     * 
     * @param number5
     *     The Number5
     */
    public void setNumber5(Integer number5) {
        this.number5 = number5;
    }

    /**
     * 
     * @return
     *     The number6
     */
    public Integer getNumber6() {
        return number6;
    }

    /**
     * 
     * @param number6
     *     The Number6
     */
    public void setNumber6(Integer number6) {
        this.number6 = number6;
    }

    /**
     * 
     * @return
     *     The number7
     */
    public Integer getNumber7() {
        return number7;
    }

    /**
     * 
     * @param number7
     *     The Number7
     */
    public void setNumber7(Integer number7) {
        this.number7 = number7;
    }

    /**
     * 
     * @return
     *     The number8
     */
    public Integer getNumber8() {
        return number8;
    }

    /**
     * 
     * @param number8
     *     The Number8
     */
    public void setNumber8(Integer number8) {
        this.number8 = number8;
    }

    /**
     * 
     * @return
     *     The number9
     */
    public Integer getNumber9() {
        return number9;
    }

    /**
     * 
     * @param number9
     *     The Number9
     */
    public void setNumber9(Integer number9) {
        this.number9 = number9;
    }

    /**
     * 
     * @return
     *     The number10
     */
    public Integer getNumber10() {
        return number10;
    }

    /**
     * 
     * @param number10
     *     The Number10
     */
    public void setNumber10(Integer number10) {
        this.number10 = number10;
    }

    /**
     * 
     * @return
     *     The text1
     */
    public String getText1() {
        return text1;
    }

    /**
     * 
     * @param text1
     *     The Text1
     */
    public void setText1(String text1) {
        this.text1 = text1;
    }

    /**
     * 
     * @return
     *     The text2
     */
    public Object getText2() {
        return text2;
    }

    /**
     * 
     * @param text2
     *     The Text2
     */
    public void setText2(Object text2) {
        this.text2 = text2;
    }

    /**
     * 
     * @return
     *     The text3
     */
    public Object getText3() {
        return text3;
    }

    /**
     * 
     * @param text3
     *     The Text3
     */
    public void setText3(Object text3) {
        this.text3 = text3;
    }

    /**
     * 
     * @return
     *     The text4
     */
    public Object getText4() {
        return text4;
    }

    /**
     * 
     * @param text4
     *     The Text4
     */
    public void setText4(Object text4) {
        this.text4 = text4;
    }

    /**
     * 
     * @return
     *     The text5
     */
    public Object getText5() {
        return text5;
    }

    /**
     * 
     * @param text5
     *     The Text5
     */
    public void setText5(Object text5) {
        this.text5 = text5;
    }

    /**
     * 
     * @return
     *     The text6
     */
    public Object getText6() {
        return text6;
    }

    /**
     * 
     * @param text6
     *     The Text6
     */
    public void setText6(Object text6) {
        this.text6 = text6;
    }

    /**
     * 
     * @return
     *     The text7
     */
    public Object getText7() {
        return text7;
    }

    /**
     * 
     * @param text7
     *     The Text7
     */
    public void setText7(Object text7) {
        this.text7 = text7;
    }

    /**
     * 
     * @return
     *     The text8
     */
    public Object getText8() {
        return text8;
    }

    /**
     * 
     * @param text8
     *     The Text8
     */
    public void setText8(Object text8) {
        this.text8 = text8;
    }

    /**
     * 
     * @return
     *     The text9
     */
    public Object getText9() {
        return text9;
    }

    /**
     * 
     * @param text9
     *     The Text9
     */
    public void setText9(Object text9) {
        this.text9 = text9;
    }

    /**
     * 
     * @return
     *     The text10
     */
    public Object getText10() {
        return text10;
    }

    /**
     * 
     * @param text10
     *     The Text10
     */
    public void setText10(Object text10) {
        this.text10 = text10;
    }

}